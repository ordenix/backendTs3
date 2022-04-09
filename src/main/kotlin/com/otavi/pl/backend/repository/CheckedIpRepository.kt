package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.CheckedIp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface CheckedIpRepository:JpaRepository<CheckedIp, Long> {

}