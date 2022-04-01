package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.TimingUsersOnTeamsepak
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TimingUsersOnTeamspeakRepository:JpaRepository<TimingUsersOnTeamsepak, Long> {


    @Query("select t from TimingUsersOnTeamsepak t where t.dbid = ?1")
    fun findByDbid(dbid: Int): TimingUsersOnTeamsepak


    fun existsByDbid(dbid: Int): Boolean


}