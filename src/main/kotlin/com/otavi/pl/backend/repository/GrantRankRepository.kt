package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.GrantRank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface GrantRankRepository:JpaRepository<GrantRank, Long> {


    @Query("select g from GrantRank g where g.id = ?1")
    fun findById(id: Int): GrantRank



}