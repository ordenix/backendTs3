package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.BanTable
import org.springframework.data.jpa.repository.JpaRepository

interface BanTableRepository:JpaRepository<BanTable, Long> {
}