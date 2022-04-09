package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.NickHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface NickHistoryRepository: JpaRepository<NickHistory, Long> {


    @Query("select n from NickHistory n where n.dbid = ?1 order by n.id DESC")
    fun findByDbidOrderByIdDesc(dbid: Int): List<NickHistory>


}