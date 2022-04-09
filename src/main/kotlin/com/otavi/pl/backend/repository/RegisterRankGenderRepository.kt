package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.RegisterRankGender
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RegisterRankGenderRepository:JpaRepository<RegisterRankGender, Long> {


    @Query("select (count(r) > 0) from RegisterRankGender r where r.groupId = ?1")
    fun existsByGroupId(groupId: Int): Boolean

}