package com.otavi.pl.backend.controler

import com.github.theholywaffle.teamspeak3.api.wrapper.Client
import com.otavi.pl.backend.GetUserAuthDetail
import com.otavi.pl.backend.Ts3
import com.otavi.pl.backend.dataClass.DashboardLastIp
import com.otavi.pl.backend.dataClass.DashboardLastNick
import com.otavi.pl.backend.dataClass.detailError
import com.otavi.pl.backend.dataClass.userListToUserModule
import com.otavi.pl.backend.repository.*
import com.otavi.pl.backend.repositoryUsers.BaseUsersInfoOnTeamsepakRepository
import com.otavi.pl.backend.repositoryUsers.BaseUsersMiscOnTeamsepakRepository
import com.otavi.pl.backend.repositoryUsers.BaseUsersOnTeamsepakRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/user")
@RestController
class UserModule(
    val privilegeToRankRepository: PrivilegeToRankRepository,
    val baseUsersOnTeamsepakRepository: BaseUsersOnTeamsepakRepository,
    val baseUsersInfoOnTeamsepakRepository: BaseUsersInfoOnTeamsepakRepository,
    val baseUsersMiscOnTeamsepakRepository: BaseUsersMiscOnTeamsepakRepository,
    val timingUsersOnTeamspeakRepository: TimingUsersOnTeamspeakRepository,
    val nickHistoryRepository: NickHistoryRepository,
    val ipHistoryRepository: IpHistoryRepository,
    val checkedIpRepository: CheckedIpRepository
) {

    @GetMapping("/all-user-list")
    fun getAllUser(): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToUser == true) {
            val allBaseUsers = baseUsersOnTeamsepakRepository.findAll()
            val allInfoUsers = baseUsersInfoOnTeamsepakRepository.findAll()
            val response = mutableListOf<userListToUserModule>()
            val listOnlineOnTs: MutableList<Client> = Ts3().listOnLine()
            allBaseUsers.forEach{ element ->
                val statusOnline: Boolean = (listOnlineOnTs.find{ it.databaseId == element.dbid } != null)
                val userToAdd: userListToUserModule = userListToUserModule(
                    Nick = element.nick,
                    UID = element.uid,
                    DBID = element.dbid,
                    Last_ip = element.ip,
                    Last_login = allInfoUsers.find { it.dbid == element.dbid }?.lastConnect,
                    Status = "", //TODO Implement Status
                    IsOnline = statusOnline
                )
                response.add(userToAdd)
            }
            response
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-user-dashboard-base-data/{dbid}")
    fun getUserDashboardBaseData(@PathVariable dbid: Int): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToUser == true) {
                baseUsersOnTeamsepakRepository.findByDbid(dbid = dbid)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-user-dashboard-info-data/{dbid}")
    fun getUserDashboardInfoData(@PathVariable dbid: Int): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToUser == true) {
            baseUsersInfoOnTeamsepakRepository.findByDbid(dbid = dbid)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-user-dashboard-misc-data/{dbid}")
    fun getUserDashboardMiscData(@PathVariable dbid: Int): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToUser == true) {
            baseUsersMiscOnTeamsepakRepository.findByDbid(dbid = dbid)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-user-dashboard-timing-data/{dbid}")
    fun getUserDashboardTimingData(@PathVariable dbid: Int): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToUser == true) {
            timingUsersOnTeamspeakRepository.findByDbid(dbid = dbid)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-user-dashboard-last-nick/{dbid}")
    fun getUserDashboardLastNick(@PathVariable dbid: Int): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToUser == true) {
            val response: MutableList<DashboardLastNick> = mutableListOf()
            val lastNicks = nickHistoryRepository.findByDbidOrderByIdDesc(dbid)
            if (lastNicks.isNotEmpty()) {
                for (i in 0 .. 4) {
                    val dashboardLastNick = DashboardLastNick(
                        nick = lastNicks[i].nick,
                        date_change = lastNicks[i].time,
                        status = "nie działa" //TODO Implement Status
                    )
                    response.add(dashboardLastNick)
                }
            }
            response

        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-user-dashboard-last-ip/{dbid}")
    fun getUserDashboardLastIp(@PathVariable dbid: Int): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToUser == true) {
            val response: MutableList<DashboardLastIp> = mutableListOf()
            val lastNicks = ipHistoryRepository.findByDbidOrderByIdDesc(dbid)
            if (lastNicks.isNotEmpty()) {
                for (i in 0 .. 4) {
                    val dashboardLastNick = DashboardLastIp(
                        ip = checkedIpRepository.findById(lastNicks[i].idIp!!.toLong()),
                        date_change = lastNicks[i].time,
                        status = "nie działa" //TODO Implement Status
                    )
                    response.add(dashboardLastNick)
                }
            }
            response

        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

}