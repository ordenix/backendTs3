package com.otavi.pl.backend.repositoryUsers

import com.otavi.pl.backend.entityUsers.BaseUsersInfoOnTeamsepak
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BaseUsersInfoOnTeamsepakRepository:JpaRepository<BaseUsersInfoOnTeamsepak, Long> {


    @Query("select b from BaseUsersInfoOnTeamsepak b where b.dbid = ?1")
    fun findByDbid(dbid: Int): BaseUsersInfoOnTeamsepak

}