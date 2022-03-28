package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.GameRank
import org.springframework.data.jpa.repository.JpaRepository

interface GameRankRepository:JpaRepository<GameRank, Int> {


    fun findByGroupNameOrderBySortIdAsc(groupName: String): List<GameRank>

}