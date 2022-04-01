package com.otavi.pl.backend.controler

import com.otavi.pl.backend.Ts3
import com.otavi.pl.backend.dataClass.detailError
import com.otavi.pl.backend.entity.GameRank
import com.otavi.pl.backend.entity.GamesRankTableList
import com.otavi.pl.backend.entity.RankLimit
import com.otavi.pl.backend.repository.GameRankRepository
import com.otavi.pl.backend.repository.GamesRankTableListRepository
import com.otavi.pl.backend.repository.RankLimitRepository
import com.otavi.pl.backend.repositoryUsers.BaseUsersServerDataOnTeamspeakRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game-rank")
class GameRank(val gamesRankTableListRepository: GamesRankTableListRepository,
               val gameRankRepository: GameRankRepository,
               val rankLimitRepository: RankLimitRepository,
               val baseUsersServerDataOnTeamspeakRepository: BaseUsersServerDataOnTeamspeakRepository ) {

    @GetMapping("/rank-games-list/{group_name}")
    fun getRankGamesListByGroupName(@PathVariable group_name: String): List<GameRank> {
        return gameRankRepository.findByGroupNameOrderBySortIdAsc(group_name)
    }

    @GetMapping("/game-rank-group-list")
    fun getGameRankGroupList(): List<GamesRankTableList> {
        return gamesRankTableListRepository.findByOrderBySortIdAsc()
    }

    @GetMapping("/current-rank-limit")
    fun getCurrentRankLimit(): Int {
        val dbid: Int = SecurityContextHolder.getContext().authentication.name.toInt()
        //val dbid = 2
        return rankLimit(dbid)
    }

    @GetMapping("/current-rank-array-to-initialize/{group_name}")
    fun getCurrentRankArray(@PathVariable group_name: String): ArrayList<Int> {
        val dbid: Int = SecurityContextHolder.getContext().authentication.name.toInt()
        val currentRank: IntArray = currentRank(dbid)
        val rankGamesByName: List<GameRank> = gameRankRepository.findByGroupNameOrderBySortIdAsc(group_name)
        val rankArray: ArrayList<Int> = ArrayList()
        rankGamesByName.forEach {  element ->
            if (currentRank.contains(element.groupId)) rankArray.add(element.id)
        }
        return rankArray
    }

    @GetMapping("/set-rank-game")
    fun name(@RequestBody rankListToSet: ArrayList<Int>): ResponseEntity<detailError> {
        val dbid: Int = SecurityContextHolder.getContext().authentication.name.toInt()
        if (rankListToSet.size > rankLimit(dbid)) return ResponseEntity(detailError("UNAUTHORIZED OPERATION"), HttpStatus.UNAUTHORIZED)
        val allGameRanks: MutableList<GameRank> = gameRankRepository.findAll()
        rankListToSet.forEach { rankId ->
            if (allGameRanks.find { it.groupId == rankId } ==null) return ResponseEntity(detailError("UNAUTHORIZED OPERATION"), HttpStatus.UNAUTHORIZED)
        }
        val currentRank: IntArray = currentRank(dbid)
        currentRank.forEach { element->
            if (!rankListToSet.contains(element) && (allGameRanks.find { it.groupId == element } != null)) {
                Ts3().deleteRank(dbid, element)
                deleteRankInDb(dbid, element)
            }
        }
        rankListToSet.forEach { element->
            if(!currentRank.contains(element) && (allGameRanks.find { it.groupId == element } != null)) {
                Ts3().setRank(dbid, element)
                addRankInDb(dbid, element)
            }

        }
        return ResponseEntity(detailError("OK"), HttpStatus.OK)
    }

    fun currentRank(dbid: Int):IntArray {
        var currentRank: IntArray? = Ts3().getCurrentRank(dbid)
        if (currentRank == null) {
            val userData = baseUsersServerDataOnTeamspeakRepository.findByDbid(dbid)
            currentRank = userData.serverGroups!!.split(",").map { it.toInt() }.toIntArray()
        }
        return currentRank
    }
    fun rankLimit(dbid: Int): Int {
        val currentRank = currentRank(dbid)
        val limitRank:List<RankLimit> = rankLimitRepository.findByOrderByLimitRegisterRankDesc()
        var currentLimit = 0
        limitRank.forEach { element ->
            if (currentRank!!.contains(element.rankId) && currentLimit == 0) currentLimit = element.limitRegisterRank
        }
        return currentLimit
    }

    fun deleteRankInDb(dbid: Int, rankId: Int) {
        val userData = baseUsersServerDataOnTeamspeakRepository.findByDbid(dbid)
        var currentRank: MutableList<Int> = userData.serverGroups!!.split(",").map { it.toInt() }.toMutableList()
        try {
            currentRank.removeAt(currentRank.indexOf(rankId))
        } catch (e: Exception) {
            println(e.message)
        }
        userData.serverGroups = currentRank.joinToString(",")
        baseUsersServerDataOnTeamspeakRepository.save(userData)
    }

    fun addRankInDb(dbid: Int, rankId: Int) {
        val userData = baseUsersServerDataOnTeamspeakRepository.findByDbid(dbid)
        var currentRank: IntArray = userData.serverGroups!!.split(",").map { it.toInt() }.toIntArray()
        currentRank = currentRank.plus(rankId)
        userData.serverGroups = currentRank.joinToString(",")
        baseUsersServerDataOnTeamspeakRepository.save(userData)
    }
}