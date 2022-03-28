package com.otavi.pl.backend.repositoryUsers

import com.otavi.pl.backend.entityUsers.BaseUsersServerDataOnTeamsepak
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BaseUsersServerDataOnTeamspeakRepository: JpaRepository<BaseUsersServerDataOnTeamsepak, Int> {


    @Query("select b from BaseUsersServerDataOnTeamsepak b where b.dbid = ?1")
    fun findByDbid(dbid: Int): BaseUsersServerDataOnTeamsepak

}