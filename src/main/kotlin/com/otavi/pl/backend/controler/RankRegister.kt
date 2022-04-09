package com.otavi.pl.backend.controler

import com.otavi.pl.backend.GetUserAuthDetail
import com.otavi.pl.backend.ManageRankFunction
import com.otavi.pl.backend.Ts3
import com.otavi.pl.backend.dataClass.RankRegisterForm
import com.otavi.pl.backend.dataClass.UserStatusRule
import com.otavi.pl.backend.dataClass.detailError
import com.otavi.pl.backend.entity.RegisterRankGender
import com.otavi.pl.backend.entity.RegisterRankProvince
import com.otavi.pl.backend.entity.TimingUsersOnTeamsepak
import com.otavi.pl.backend.entityUsers.BaseUsersServerDataOnTeamsepak
import com.otavi.pl.backend.repository.*
import com.otavi.pl.backend.repositoryUsers.BaseUsersInfoOnTeamsepakRepository
import com.otavi.pl.backend.repositoryUsers.BaseUsersServerDataOnTeamspeakRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rank-register")
class RankRegister(val registerRankGenderRepository: RegisterRankGenderRepository,
                   val registerRankProvinceRepository: RegisterRankProvinceRepository,
                   val baseUsersServerDataOnTeamspeakRepository: BaseUsersServerDataOnTeamspeakRepository,
                   val timingUsersOnTeamspeakRepository: TimingUsersOnTeamspeakRepository,
                   val modulesSettingRepository: ModulesSettingRepository,
                   val baseUsersInfoOnTeamsepakRepository: BaseUsersInfoOnTeamsepakRepository,
                   val banHistoryTableRepository: BanHistoryTableRepository,
                   val privilegeToRankRepository: PrivilegeToRankRepository,
                   val rankLimitRepository: RankLimitRepository,
) {

    @GetMapping("/rank-province")
    fun getRankProvince(): MutableList<RegisterRankProvince> {
        return registerRankProvinceRepository.findAll()
    }

    @GetMapping("/rank-gender")
    fun getRankGender(): MutableList<RegisterRankGender> {
        return registerRankGenderRepository.findAll()
    }

    @GetMapping("/user-status-rule")
    fun getUserStatusRule(): UserStatusRule {
        return checkUserRules()
    }

    @PutMapping("/register-user-on-ts3")
    fun registerUserOnTs3(@RequestBody rankRegisterForm: RankRegisterForm): ResponseEntity<detailError> {
        val userStatusRule: UserStatusRule = checkUserRules()
        if (userStatusRule.ban && userStatusRule.status_register && userStatusRule.rules && userStatusRule.time
            && userStatusRule.connect) {
            // register user
            if (registerRankGenderRepository.existsByGroupId(rankRegisterForm.gender_id)
                && registerRankProvinceRepository.existsByGroupId(rankRegisterForm.province_id)) {
                val dbid: Int = SecurityContextHolder.getContext().authentication.name.toInt()
                val currentRank: IntArray = ManageRankFunction(rankLimitRepository,baseUsersServerDataOnTeamspeakRepository).currentRank(dbid)
                val allRegisterProvinceRank: MutableList<RegisterRankProvince> = registerRankProvinceRepository.findAll()
                val allRegisterGenderRank: MutableList<RegisterRankGender> = registerRankGenderRepository.findAll()
                currentRank.forEach { element ->
                    if (element != rankRegisterForm.gender_id && (allRegisterGenderRank.find { it.groupId == element } != null)
                        || element != rankRegisterForm.province_id && (allRegisterProvinceRank.find { it.groupId == element } != null))  {
                        // Delete rank
                        ManageRankFunction(rankLimitRepository,baseUsersServerDataOnTeamspeakRepository).deleteRankInDb(dbid, element)
                        Ts3().deleteRank(dbid, element)
                    }
                }
                if ((currentRank.find { it == rankRegisterForm.gender_id } == null)) {
                    // add rank
                    Ts3().setRank(dbid, rankRegisterForm.gender_id)
                    ManageRankFunction(rankLimitRepository,baseUsersServerDataOnTeamspeakRepository).addRankInDb(dbid, rankRegisterForm.gender_id)
                }
                if ((currentRank.find { it == rankRegisterForm.province_id } == null)) {
                    // add rank
                    ManageRankFunction(rankLimitRepository,baseUsersServerDataOnTeamspeakRepository).addRankInDb(dbid, rankRegisterForm.province_id)
                    Ts3().setRank(dbid, rankRegisterForm.province_id)
                }
                return ResponseEntity(detailError("OK"), HttpStatus.OK)
            } else return ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)

        } else {
            return ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)

        }
    }

    // manage section

    @PutMapping("/modify-rank-register-gender")
    fun modifyRankRegisterGender(@RequestBody registerRankGender: RegisterRankGender):ResponseEntity<Any> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accesToRegister == true) {
            registerRankGenderRepository.save(registerRankGender)
            ResponseEntity(detailError("Ok"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @DeleteMapping("/delete-rank-register-gender")
    fun deleteRankRegisterGender(@RequestBody registerRankGender: RegisterRankGender): ResponseEntity<Any> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accesToRegister == true) {
            registerRankGenderRepository.delete(registerRankGender)
            ResponseEntity(detailError("Ok"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @PutMapping("/modify-rank-register-province")
    fun modifyRankRegisterProvince(@RequestBody registerRankProvince: RegisterRankProvince):ResponseEntity<Any> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accesToRegister == true) {
            registerRankProvinceRepository.save(registerRankProvince)
            ResponseEntity(detailError("Ok"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @DeleteMapping("/delete-rank-register-province")
    fun deleteRankRegisterProvince(@RequestBody registerRankProvince: RegisterRankProvince): ResponseEntity<Any> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accesToRegister == true) {
            registerRankProvinceRepository.delete(registerRankProvince)
            ResponseEntity(detailError("Ok"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    fun checkUserRules(): UserStatusRule {
        val userStatusRule: UserStatusRule = UserStatusRule()
        val dbid: Int = SecurityContextHolder.getContext().authentication.name.toInt()
        val userData: BaseUsersServerDataOnTeamsepak = baseUsersServerDataOnTeamspeakRepository.findByDbid(dbid)
        val rankGender: MutableList<RegisterRankGender> = registerRankGenderRepository.findAll()
        if (!userData.isRegister!!) {
            Ts3().getCurrentRank(dbid)?.forEach { rankId ->
                if (rankGender.find { it.id == rankId } != null) {
                    // user is register on ts but not register on db
                    userData.isRegister = true
                    baseUsersServerDataOnTeamspeakRepository.save(userData)
                    userStatusRule.rules = true
                    userStatusRule.status_register = true
                    userStatusRule.ban = true
                    userStatusRule.connect = true
                    userStatusRule.time = true
                    return userStatusRule
                }
            }
            if (timingUsersOnTeamspeakRepository.existsByDbid(dbid)) {
                val timeOnTs3: TimingUsersOnTeamsepak = timingUsersOnTeamspeakRepository.findByDbid(dbid)
                val minimumConnect: Int =
                    modulesSettingRepository.findBySetting("CRR_minimum_connect").options?.toInt() ?: 0
                val minimumTimeOnline: Int =
                    modulesSettingRepository.findBySetting("CRR_minimum_time_online").options?.toInt() ?: 0
                val minimumRatio: Int =
                    modulesSettingRepository.findBySetting("CRR_minimum_connect_ratio").options?.toInt() ?: 0
                val hourNoBan: Int =
                    modulesSettingRepository.findBySetting("CRR_hour_no_ban").options?.toInt() ?: 0
                if (timeOnTs3.timeOnline!! < minimumTimeOnline) {
                    return userStatusRule
                } else {
                    val totalAfkTime: Int = timeOnTs3.timeAway!! + timeOnTs3.timeMicDisabled!! + timeOnTs3.timeIdle!!
                    val ratio: Int = totalAfkTime / timeOnTs3.timeTotal!!
                    if (ratio > (1 - (minimumRatio * 0.01))) {
                        return userStatusRule
                    } else {
                        if (baseUsersInfoOnTeamsepakRepository.findByDbid(dbid).realTotalConnections!! > minimumConnect) {
                            userStatusRule.time = true
                        } else return userStatusRule
                    }
                }
                val currentTimeInSecond: Long = System.currentTimeMillis() / 1000L
                val banHistory = banHistoryTableRepository.findByBanClientDbid_DbidAndTimeAddIsGreaterThan(dbid = dbid, timeAdd = (currentTimeInSecond - (hourNoBan * 3600)).toInt())
                if (banHistory.isEmpty()) {
                    userStatusRule.ban = true
                }

                if (userData.checkRules == true) userStatusRule.rules = true

                // calc ban time etc
                return userStatusRule
            } else return userStatusRule
            // other calculations etc
        } else {
            userStatusRule.rules = true
            userStatusRule.status_register = true
            userStatusRule.ban = true
            userStatusRule.connect = true
            userStatusRule.time = true
            return userStatusRule
        }
        return userStatusRule
    }



}