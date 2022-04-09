package com.otavi.pl.backend.repositoryUsers

import com.otavi.pl.backend.entityUsers.BaseUsersMiscOnTeamsepak
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BaseUsersMiscOnTeamsepakRepository:JpaRepository<BaseUsersMiscOnTeamsepak, Long> {


    @Query("select b from BaseUsersMiscOnTeamsepak b where b.dbid = ?1")
    fun findByDbid(dbid: Int): BaseUsersMiscOnTeamsepak

}