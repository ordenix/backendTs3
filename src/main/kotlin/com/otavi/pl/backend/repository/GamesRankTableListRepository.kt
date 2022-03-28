package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.GamesRankTableList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface GamesRankTableListRepository: JpaRepository<GamesRankTableList, Int> {


    @Query("select g from GamesRankTableList g order by g.sortId")
    fun findByOrderBySortIdAsc(): List<GamesRankTableList>

}