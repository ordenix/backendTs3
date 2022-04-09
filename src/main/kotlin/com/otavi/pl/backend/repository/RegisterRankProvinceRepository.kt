package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.RegisterRankProvince
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RegisterRankProvinceRepository: JpaRepository<RegisterRankProvince, Long> {


    @Query("select (count(r) > 0) from RegisterRankProvince r where r.groupId = ?1")
    fun existsByGroupId(groupId: Int): Boolean

}