package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.RankLimit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RankLimitRepository: JpaRepository<RankLimit, Int>{


    @Query("select r from RankLimit r order by r.limitRegisterRank DESC")
    fun findByOrderByLimitRegisterRankDesc(): List<RankLimit>

}