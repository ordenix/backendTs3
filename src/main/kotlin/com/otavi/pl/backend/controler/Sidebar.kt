package com.otavi.pl.backend.controler

import com.otavi.pl.backend.config.NavElements
import com.otavi.pl.backend.dataClass.NavElement
import com.otavi.pl.backend.entityUsers.BaseUsersServerDataOnTeamsepak
import com.otavi.pl.backend.repository.PrivilegeToRankRepository
import com.otavi.pl.backend.repositoryUsers.BaseUsersServerDataOnTeamspeakRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sidebar")
class Sidebar(val baseUsersServerDataOnTeamspeakRepository: BaseUsersServerDataOnTeamspeakRepository,
              val privilageToRankRepository: PrivilegeToRankRepository) {

    @GetMapping("/account")
    fun getSidebarAccount(): MutableList<NavElement> {
        val dbid: Int = SecurityContextHolder.getContext().authentication.name.toInt()
        val role: String = SecurityContextHolder.getContext().authentication.authorities.toString()
        val userData: BaseUsersServerDataOnTeamsepak = baseUsersServerDataOnTeamspeakRepository.findByDbid(dbid)
        val navList = mutableListOf<NavElement>()
        if (role == "[Staff]") {
            navList.add(NavElements().moduleRegisterUserStatusRegister)
            navList.add(NavElements().moduleRankGameUser)
        } else {
            if (userData.isRegister == true) {
                navList.add(NavElements().moduleRegisterUserStatusRegister)
            } else {
                navList.add(NavElements().moduleRegisterUserStatusNotRegister)
            }
            navList.add(NavElements().moduleRankGameUser)
        }
        return navList
    }

    @GetMapping("/server")
    fun getSidebarServer(): MutableList<NavElement> {
        val dbid: Int = SecurityContextHolder.getContext().authentication.name.toInt()
        val role: String = SecurityContextHolder.getContext().authentication.authorities.toString()
        val userData: BaseUsersServerDataOnTeamsepak = baseUsersServerDataOnTeamspeakRepository.findByDbid(dbid)
        val navList = mutableListOf<NavElement>()
        val userPrivilege = privilageToRankRepository.findByDbid_Dbid(dbid)
        if (role == "[Staff]") {
            if (userPrivilege.rank_id?.accesToRegister == true) {
                navList.add(NavElements().moduleRegister)
            }
            if (userPrivilege.rank_id?.accessToGameRank == true) {
                navList.add(NavElements().moduleRangGame)
            }
            if (userPrivilege.rank_id?.accesToGrantRank == true) {
                navList.add(NavElements().moduleAdminRank)
            }
            if (userPrivilege.rank_id?.accesToStaffUser == true) {
                navList.add(NavElements().moduleAdminList)
            }
            // TODO: add more privilages
            navList.add(NavElements().moduleUserList)
        }
        return navList
    }
}