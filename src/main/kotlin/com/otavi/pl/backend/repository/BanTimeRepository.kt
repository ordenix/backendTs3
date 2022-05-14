package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.BanTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BanTimeRepository: JpaRepository<BanTime, Long> {


    @Query("select b from BanTime b where b.banId = ?1")
    fun findByBanId(banId: Int): List<BanTime>


    @Query("select (count(b) > 0) from BanTime b where b.banId = ?1")
    fun existsByBanId(banId: Int): Boolean


}