package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.BanHistoryTable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BanHistoryTableRepository:JpaRepository<BanHistoryTable, Long> {


    @Query("select b from BanHistoryTable b where b.banClientDbid.dbid = ?1")
    fun findByBanClientDbid_Dbid(dbid: Int): List<BanHistoryTable>


    @Query("select b from BanHistoryTable b where b.banClientDbid.dbid = ?1 and b.timeAdd > ?2")
    fun findByBanClientDbid_DbidAndTimeAddIsGreaterThan(dbid: Int, timeAdd: Int): List<BanHistoryTable>


    @Query("select count(b) from BanHistoryTable b where b.addAdminDbid.dbid = ?1 and b.actionId = ?2 and b.timeAdd > ?3")
    fun countByAddAdminDbid_DbidAndActionIdAndTimeAddIsGreaterThan(dbid: Int, actionId: Int, timeAdd: Int): Long


    @Query("select count(b) from BanHistoryTable b where b.banClientDbid.dbid = ?1")
    fun countByBanClientDbid_Dbid(dbid: Int): Long


    @Query("select count(b) from BanHistoryTable b where b.banClientDbid.dbid = ?1 and b.banId = ?2")
    fun countByBanClientDbid_DbidAndBanId(dbid: Int, banId: Int): Long


}
