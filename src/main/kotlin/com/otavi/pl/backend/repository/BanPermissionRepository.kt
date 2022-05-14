package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.BanPermission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BanPermissionRepository:JpaRepository<BanPermission, Long> {


    @Query("select b from BanPermission b where b.rankGrantId = ?1 and b.actionBanId = ?2")
    fun findByRankGrantIdAndActionBanId(rankGrantId: Int, actionBanId: Int): BanPermission


    @Query("select b from BanPermission b where b.rankGrantId = ?1")
    fun findByRankGrantId(rankGrantId: Int): MutableList<BanPermission>

}