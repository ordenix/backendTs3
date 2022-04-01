package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.PrivilageToRank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PrivilageToRankRepository:JpaRepository<PrivilageToRank, Long> {


    @Query("select p from PrivilageToRank p where p.dbid.dbid = ?1")
    fun findByDbid_Dbid(dbid: Int): PrivilageToRank

}