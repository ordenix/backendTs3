package com.otavi.pl.backend.repository

import com.otavi.pl.backend.entity.UsersRegister
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface UserRegisterRepository: JpaRepository<UsersRegister, Long> {


    @Query("select u from UsersRegister u where u.login = ?1")
    fun findByLogin(login: String): UsersRegister


    fun existsByLogin(login: String): Boolean


    fun existsByDbid(dbid: Int): Boolean


    fun countByRole(role: String): Long


    @Query("select u from UsersRegister u where u.role = ?1")
    fun findByRole(role: String): List<UsersRegister>


    @Transactional
    @Modifying
    @Query("update UsersRegister u set u.role = ?1 where u.dbid = ?2")
    fun updateRoleByDbid(role: String, dbid: Int): Int

}