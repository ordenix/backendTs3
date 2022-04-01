package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.ModulesSetting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ModulesSettingRepository:JpaRepository<ModulesSetting, Long>{


    @Query("select m from ModulesSetting m where m.setting = ?1")
    fun findBySetting(setting: String): ModulesSetting

}