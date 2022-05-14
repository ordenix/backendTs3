package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.ActionBanType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ActionBanTypeRepository:JpaRepository<ActionBanType, Long> {


    @Query("select a from ActionBanType a where a.id = ?1")
    fun findByIds(id: Long): ActionBanType


}