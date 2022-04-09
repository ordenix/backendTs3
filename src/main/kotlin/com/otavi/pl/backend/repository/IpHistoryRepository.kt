package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.IpHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface IpHistoryRepository:JpaRepository<IpHistory, Long> {


    @Query("select i from IpHistory i where i.dbid = ?1 order by i.id DESC")
    fun findByDbidOrderByIdDesc(dbid: Int): List<IpHistory>


}