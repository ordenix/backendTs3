package com.otavi.pl.backend

import com.otavi.pl.backend.entity.RankLimit
import com.otavi.pl.backend.repository.GameRankRepository
import com.otavi.pl.backend.repository.GamesRankTableListRepository
import com.otavi.pl.backend.repository.PrivilegeToRankRepository
import com.otavi.pl.backend.repository.RankLimitRepository
import com.otavi.pl.backend.repositoryUsers.BaseUsersServerDataOnTeamspeakRepository

class ManageRankFunction(
    private val rankLimitRepository: RankLimitRepository,
    private val baseUsersServerDataOnTeamspeakRepository: BaseUsersServerDataOnTeamspeakRepository
) {
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