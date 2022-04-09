package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.PrivilageToRank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PrivilegeToRankRepository:JpaRepository<PrivilageToRank, Long> {


    @Query("select p from PrivilageToRank p where p.dbid.dbid = ?1")
    fun findByDbid_Dbid(dbid: Int): PrivilageToRank


    @Query("select (count(p) > 0) from PrivilageToRank p where p.dbid.dbid = ?1")
    fun existsByDbid_Dbid(dbid: Int): Boolean



}