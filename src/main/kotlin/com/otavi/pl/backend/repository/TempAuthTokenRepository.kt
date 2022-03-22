package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.TempAuthToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface TempAuthTokenRepository: JpaRepository<TempAuthToken, Long> {

    fun existsByDbid(dbid: Int): Boolean


    @Query("select t from TempAuthToken t where t.dbid = ?1")
    fun findByDbid(dbid: Int): TempAuthToken


    @Transactional
    @Modifying
    @Query("delete from TempAuthToken t where t.dbid = ?1")
    fun deleteByDbid(dbid: Int): Int


    @Transactional
    @Modifying
    @Query("update TempAuthToken t set t.token = ?1 where t.dbid = ?2")
    fun updateTokenByDbid(token: String, dbid: Int): Int

}