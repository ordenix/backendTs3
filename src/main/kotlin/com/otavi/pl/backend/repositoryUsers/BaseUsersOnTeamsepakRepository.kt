package com.otavi.pl.backend.repositoryUsers

import com.otavi.pl.backend.entityUsers.BaseUsersOnTeamsepak
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BaseUsersOnTeamsepakRepository:JpaRepository<BaseUsersOnTeamsepak, Long> {


    @Query("select b from BaseUsersOnTeamsepak b where b.dbid = ?1")
    fun findByDbid(dbid: Int): BaseUsersOnTeamsepak

}