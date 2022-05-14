package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.ActiveModule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface ActiveModuleRepository: JpaRepository<ActiveModule, Long> {


    fun existsByModuleName(moduleName: String): Boolean


    @Query("select a from ActiveModule a where a.moduleName = ?1")
    fun findByModuleName(moduleName: String): ActiveModule


    @Transactional
    @Modifying
    @Query("update ActiveModule a set a.status = ?1 where a.moduleName = ?2")
    fun updateStatusByModuleName(status: Boolean, moduleName: String): Int


}