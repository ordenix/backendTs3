package com.otavi.pl.backend.controler

import com.otavi.pl.backend.GetUserAuthDetail
import com.otavi.pl.backend.ManageRankFunction
import com.otavi.pl.backend.Ts3
import com.otavi.pl.backend.dataClass.detailError
import com.otavi.pl.backend.entity.GameRank
import com.otavi.pl.backend.entity.GamesRankTableList
import com.otavi.pl.backend.repository.GameRankRepository
import com.otavi.pl.backend.repository.GamesRankTableListRepository
import com.otavi.pl.backend.repository.PrivilegeToRankRepository
import com.otavi.pl.backend.repository.RankLimitRepository
import com.otavi.pl.backend.repositoryUsers.BaseUsersServerDataOnTeamspeakRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game-rank")
class GameRank(val gamesRankTableListRepository: GamesRankTableListRepository,
               val gameRankRepository: GameRankRepository,
               val rankLimitRepository: RankLimitRepository,
               val baseUsersServerDataOnTeamspeakRepository: BaseUsersServerDataOnTeamspeakRepository,
               val privilegeToRankRepository: PrivilegeToRankRepository,
               val gameRankTableListRepository: GamesRankTableListRepository) {

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
        return ManageRankFunction(rankLimitRepository, baseUsersServerDataOnTeamspeakRepository).rankLimit(dbid)
    }

    @GetMapping("/current-rank-array-to-initialize/{group_name}")
    fun getCurrentRankArray(@PathVariable group_name: String): ArrayList<Int> {
        val dbid: Int = SecurityContextHolder.getContext().authentication.name.toInt()
        val currentRank: IntArray = ManageRankFunction(rankLimitRepository, baseUsersServerDataOnTeamspeakRepository).currentRank(dbid)
        val rankGamesByName: List<GameRank> = gameRankRepository.findByGroupNameOrderBySortIdAsc(group_name)
        val rankArray: ArrayList<Int> = ArrayList()
        rankGamesByName.forEach {  element ->
            if (currentRank.contains(element.groupId)) rankArray.add(element.groupId)
        }
        return rankArray
    }

    @PutMapping("/set-rank-game")
    fun setRankGame(@RequestBody rankListToSet: ArrayList<Int>): ResponseEntity<detailError> {
        val dbid: Int = SecurityContextHolder.getContext().authentication.name.toInt()
        if (rankListToSet.size >
            ManageRankFunction(rankLimitRepository, baseUsersServerDataOnTeamspeakRepository).rankLimit(dbid))
            return ResponseEntity(detailError("UNAUTHORIZED OPERATION"), HttpStatus.UNAUTHORIZED)
        val allGameRanks: MutableList<GameRank> = gameRankRepository.findAll()
        rankListToSet.forEach { rankId ->
            if (allGameRanks.find { it.groupId == rankId } ==null) return ResponseEntity(detailError("UNAUTHORIZED OPERATION"), HttpStatus.UNAUTHORIZED)
        }
        val currentRank: IntArray = ManageRankFunction(rankLimitRepository, baseUsersServerDataOnTeamspeakRepository).currentRank(dbid)
        currentRank.forEach { element->
            if (!rankListToSet.contains(element) && (allGameRanks.find { it.groupId == element } != null)) {
                Ts3().deleteRank(dbid, element)
                ManageRankFunction(rankLimitRepository, baseUsersServerDataOnTeamspeakRepository).deleteRankInDb(dbid, element)
            }
        }
        rankListToSet.forEach { element->
            if(!currentRank.contains(element) && (allGameRanks.find { it.groupId == element } != null)) {
                Ts3().setRank(dbid, element)
                ManageRankFunction(rankLimitRepository, baseUsersServerDataOnTeamspeakRepository).addRankInDb(dbid, element)
            }

        }
        return ResponseEntity(detailError("OK"), HttpStatus.OK)
    }

    @PutMapping("modify-rank-game")
    fun modifyRankGame(@RequestBody gameRank: com.otavi.pl.backend.entity.GameRank): ResponseEntity<Any> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToGameRank == true) {
            gameRankRepository.save(gameRank)
            ResponseEntity(detailError("Ok"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @DeleteMapping("delete-rank-game")
    fun deleteRankGame(@RequestBody gameRank: com.otavi.pl.backend.entity.GameRank): ResponseEntity<Any> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToGameRank == true) {
            gameRankRepository.delete(gameRank)
            ResponseEntity(detailError("Ok"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @PutMapping("modify-rank-table-list")
    fun modifyRankTableList(@RequestBody gameRankTableList: GamesRankTableList): ResponseEntity<Any> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToGameRank == true) {
            gameRankTableListRepository.save(gameRankTableList)
            ResponseEntity(detailError("Ok"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @DeleteMapping("delete-rank-table-list")
    fun deleteRankTableList(@RequestBody gameRankTableList: GamesRankTableList): ResponseEntity<Any> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToGameRank == true) {
            gameRankTableListRepository.delete(gameRankTableList)
            ResponseEntity(detailError("Ok"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }


}