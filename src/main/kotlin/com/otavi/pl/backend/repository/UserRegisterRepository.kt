package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.UsersRegister
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRegisterRepository: JpaRepository<UsersRegister, Long> {


    @Query("select u from UsersRegister u where u.login = ?1")
    fun findByLogin(login: String): UsersRegister


    fun existsByLogin(login: String): Boolean


    fun existsByDbid(dbid: Int): Boolean


    fun countByRole(role: String): Long

}