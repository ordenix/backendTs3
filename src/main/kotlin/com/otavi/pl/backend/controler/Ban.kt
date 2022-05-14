package com.otavi.pl.backend.controler

import com.github.theholywaffle.teamspeak3.api.wrapper.Client
import com.otavi.pl.backend.GetUserAuthDetail
import com.otavi.pl.backend.ManageRankFunction
import com.otavi.pl.backend.Ts3
import com.otavi.pl.backend.dataClass.*
import com.otavi.pl.backend.entity.*
import com.otavi.pl.backend.repository.*
import com.otavi.pl.backend.repositoryUsers.BaseUsersServerDataOnTeamspeakRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/ban")
class Ban(
    val privilegeToRankRepository: PrivilegeToRankRepository,
    val banHistoryTableRepository: BanHistoryTableRepository,
    val banPermissionRepository: BanPermissionRepository,
    val actionBanTypeRepository: ActionBanTypeRepository,
    val activeModuleRepository: ActiveModuleRepository,
    val grantRankRepository: GrantRankRepository,
    val banTableRepository: BanTableRepository,
    val banTimeRepository: BanTimeRepository,
    val modulesSettingRepository: ModulesSettingRepository,
    val baseUsersServerDataOnTeamspeakRepository: BaseUsersServerDataOnTeamspeakRepository,
    val rankLimitRepository: RankLimitRepository
) {

    @PutMapping("/add-ban")
    fun addBan(@RequestBody putBan: PutBan): ResponseEntity<detailError> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            val currentTimeInSecond: Long = System.currentTimeMillis() / 1000L
            val rankGrantId: Int? = userAuthDetail.userPrivilege?.rank_id?.rankId
            val banPermissions: BanPermission = banPermissionRepository.findByRankGrantIdAndActionBanId(rankGrantId = rankGrantId!!, actionBanId = putBan.action_id)

            val timesHistoryCreatorLast10m: Long = banHistoryTableRepository.countByAddAdminDbid_DbidAndActionIdAndTimeAddIsGreaterThan(
                dbid = userAuthDetail.dbid, actionId = putBan.action_id, timeAdd = (currentTimeInSecond - 10 * 60).toInt())
            val timesHistoryCreatorLastDay: Long = banHistoryTableRepository.countByAddAdminDbid_DbidAndActionIdAndTimeAddIsGreaterThan(
                dbid = userAuthDetail.dbid, actionId = putBan.action_id, timeAdd = (currentTimeInSecond - 1 * 24 * 60 * 60).toInt())

            if (timesHistoryCreatorLast10m > banPermissions.limitPer10m!! && banPermissions.limitPer10m != -1)
                ResponseEntity(detailError("Reached 10m limit"), HttpStatus.CONFLICT)
            if (timesHistoryCreatorLastDay > banPermissions.limitPer1d!! && banPermissions.limitPer1d != -1)
                ResponseEntity(detailError("Reached day limit"), HttpStatus.CONFLICT)

            val banToAdd: BanHistoryTable = BanHistoryTable()
            banToAdd.banClientDbid?.dbid = putBan.ban_client_dbid
            banToAdd.banId = putBan.ban_id
            banToAdd.actionId = putBan.action_id
            banToAdd.additionalInfo = putBan.additional_info
            banToAdd.addAdminDbid?.dbid = userAuthDetail.dbid
            banToAdd.timeAdd = currentTimeInSecond.toInt()

            var banTs3Time: Int = 0

            if (putBan.time == 0 ) {
                banToAdd.timeTo = 0
                banToAdd.removed = true
                banToAdd.active = false
            } else {
                banPermissions.maxTimeToAction = 0
                if (putBan.time > banPermissions.maxTimeToAction!! && banPermissions.maxTimeToAction!! != -1
                    || (putBan.time == -1 && banPermissions.maxTimeToAction!! != -1)
                ) {
                    // overflow check grants
                    if (banPermissions.overflow!!){
                        banToAdd.timeTo = currentTimeInSecond.toInt() + banPermissions.maxTimeToAction!! * 60
                        banTs3Time = banPermissions.maxTimeToAction!! * 60
                        banToAdd.toCommit = true
                        banToAdd.active = true

                        if (putBan.time == -1) {
                            banToAdd.timeToOverflow = -1
                        } else banToAdd.timeToOverflow = (putBan.time - banPermissions.maxTimeToAction!!) *60
                    } else ResponseEntity(detailError("Overflow error"), HttpStatus.CONFLICT)
                } else {
                    // non overflow ok
                    if (putBan.time == -1) {
                        banToAdd.timeTo = -1
                        banTs3Time = -1
                    }
                    else {
                        banToAdd.timeTo = currentTimeInSecond.toInt() + (putBan.time * 60)
                        banTs3Time = putBan.time * 60
                    }

                    banToAdd.active = true
                }
            }
            val listOnline: MutableList<Client> = Ts3().listOnLine()
            if ( listOnline.find { it.databaseId == putBan.ban_client_dbid } != null ) {
                // user is on-line
            } else {
                // user is off-line
            }
            ResponseEntity(detailError("OK"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }

    }

    @PutMapping("/put-ban-action-type")
    fun putBanActionType(@RequestBody banActionType: ActionBanType): ResponseEntity<detailError> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            actionBanTypeRepository.save(banActionType)
            ResponseEntity(detailError("OK"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @DeleteMapping("/delete-ban-action-type")
    fun deleteBanActionType(@RequestBody banActionType: ActionBanType): ResponseEntity<detailError> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            actionBanTypeRepository.delete(banActionType)
            ResponseEntity(detailError("OK"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-all-ban-action-type")
    fun getAllBanActionType(): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            actionBanTypeRepository.findAll()
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-settings")
    fun getSettings(): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            val response: SettingsBanModule = SettingsBanModule()
            if (activeModuleRepository.existsByModuleName("ban_active")) {
                response.active = activeModuleRepository.findByModuleName("ban_active").status!!
            }
            if (activeModuleRepository.existsByModuleName("ban_auto_ban")) {
                response.auto_ban = activeModuleRepository.findByModuleName("ban_auto_ban").status!!
            }
            if (activeModuleRepository.existsByModuleName("ban_two_factor")) {
                response.two_factor = activeModuleRepository.findByModuleName("ban_two_factor").status!!
            }
            response
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @PutMapping("/update_settings")
    fun updateSettings(@RequestBody settingsBanModule: SettingsBanModule): ResponseEntity<detailError> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            if (activeModuleRepository.existsByModuleName("ban_active")) {
                activeModuleRepository.updateStatusByModuleName(status = settingsBanModule.active, moduleName = "ban_active")
            } else {
                val activeModule: ActiveModule = ActiveModule()
                activeModule.moduleName = "ban_active"
                activeModule.status = settingsBanModule.active
                activeModuleRepository.save(activeModule)
            }
            if (activeModuleRepository.existsByModuleName("ban_auto_ban")) {
                activeModuleRepository.updateStatusByModuleName(status = settingsBanModule.auto_ban, moduleName = "ban_auto_ban")
            } else {
                val activeModule: ActiveModule = ActiveModule()
                activeModule.moduleName = "ban_auto_ban"
                activeModule.status = settingsBanModule.auto_ban
                activeModuleRepository.save(activeModule)
            }
            if (activeModuleRepository.existsByModuleName("ban_two_factor")) {
                activeModuleRepository.updateStatusByModuleName(status = settingsBanModule.two_factor, moduleName = "ban_two_factor")
            } else {
                val activeModule: ActiveModule = ActiveModule()
                activeModule.moduleName = "ban_two_factor"
                activeModule.status = settingsBanModule.two_factor
                activeModuleRepository.save(activeModule)
            }
            ResponseEntity(detailError("OK"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @PutMapping("/update-permissions")
    fun updatePermissions(@RequestBody banPermissions: BanPermission): ResponseEntity<detailError> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            banPermissionRepository.save(banPermissions)
            ResponseEntity(detailError("OK"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-permissions/{rankId}")
    fun getPermissionsByRankId(@PathVariable rankId: Int): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            val grantRanks: MutableList<GrantRank> = grantRankRepository.findAll()
            val banActionTypes: MutableList<ActionBanType> = actionBanTypeRepository.findAll()
            var banPermissions: MutableList<BanPermission> = banPermissionRepository.findAll()
            val crossJoin: MutableList<BanPermission> = mutableListOf()
            grantRanks.forEach { elementGrantRank ->
                banActionTypes.forEach { elementActionType ->
                    val banPermission: BanPermission = BanPermission()
                    banPermission.rankGrantId = elementGrantRank.rankId
                    banPermission.actionBanId = elementActionType.id
                    banPermission.maxTimeToAction = 0
                    banPermission.twoFactorAuth = false
                    banPermission.add = false
                    banPermission.limitPer10m = 0
                    banPermission.limitPer1d = 0
                    banPermission.overflow = false
                    banPermission.commit = false
                    banPermission.commitAuto = false
                    banPermission.delete = false
                    crossJoin.add(banPermission)
                }
            }
            crossJoin.forEach { crossElement ->
                if (banPermissions.find { it.rankGrantId == crossElement.rankGrantId &&
                    it.actionBanId == crossElement.actionBanId } == null) {
                    banPermissionRepository.save(crossElement)
                }
            }
            banPermissions.forEach { banPermission ->
                if (crossJoin.find { it.rankGrantId == banPermission.rankGrantId &&
                    it.actionBanId == banPermission.actionBanId } == null) {
                    banPermissionRepository.delete(banPermission)
                }
            }
            banPermissions = banPermissionRepository.findByRankGrantId(rankId)
            val response: MutableList<BanPermissionsExtended> = mutableListOf()
            banPermissions.forEach { elementBanPermission ->
                val banPermissionsExtended = BanPermissionsExtended()
                banPermissionsExtended.id = elementBanPermission.id
                banPermissionsExtended.grant_rank = grantRanks.find { it.rankId == elementBanPermission.rankGrantId }
                banPermissionsExtended.action_ban_type = banActionTypes.find { it.id == elementBanPermission.actionBanId }
                banPermissionsExtended.maxTimeToAction = elementBanPermission.maxTimeToAction
                banPermissionsExtended.twoFactorAuth = elementBanPermission.twoFactorAuth
                banPermissionsExtended.add = elementBanPermission.add
                banPermissionsExtended.limitPer10m = elementBanPermission.limitPer10m
                banPermissionsExtended.limitPer1d = elementBanPermission.limitPer1d
                banPermissionsExtended.overflow = elementBanPermission.overflow
                banPermissionsExtended.commit = elementBanPermission.commit
                banPermissionsExtended.commitAuto = elementBanPermission.commitAuto
                banPermissionsExtended.delete = elementBanPermission.delete
                response.add(banPermissionsExtended)
            }
            response
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-ban")
    fun getAllBan(): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            banTableRepository.findAll()
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @PutMapping("/put-ban")
    fun putAllBan(@RequestBody banTable: BanTable): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            banTableRepository.save(banTable)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @DeleteMapping("/delete-ban")
    fun deleteAllBan(@RequestBody banTable: BanTable): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            banTableRepository.delete(banTable)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-ban-times-by-ban-id/{banId}")
    fun getBanTimesByBanId(@PathVariable("banId") banId: Int): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            banTimeRepository.findByBanId(banId)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @PutMapping("/put-ban-times")
    fun putBanTimes(@RequestBody banTimes: BanTime): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            banTimeRepository.save(banTimes)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @DeleteMapping("/delete-ban-times")
    fun deleteBanTimes(@RequestBody banTimes: BanTime): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            banTimeRepository.delete(banTimes)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-all-ban-from-history")
    fun getBanHistory(): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            banHistoryTableRepository.findAll()
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @PutMapping("/get-ban-parameters-to-add")
    fun getBanParametersToAdd(@RequestBody putBan: PutBan): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            val banParametersToAdd = BanParametersToAdd()
            banParametersToAdd.times_all =
                banHistoryTableRepository.countByBanClientDbid_Dbid(dbid = putBan.ban_client_dbid).toInt()

            banParametersToAdd.times_this =
                banHistoryTableRepository.countByBanClientDbid_DbidAndBanId(
                    dbid = putBan.ban_client_dbid,
                    banId = putBan.ban_id).toInt()
            val grantRanks: MutableList<GrantRank> = grantRankRepository.findAll()
            val ignoreBansGroupId: MutableList<Int> = modulesSettingRepository.findBySetting("IGNORE_BAN_GROUPS").
                options!!.split(",").map { it.toInt() }.toMutableList()
            grantRanks.forEach{ element ->
                ignoreBansGroupId.add(element.groupId!!)
            }
            val currentRank: IntArray = ManageRankFunction(rankLimitRepository,baseUsersServerDataOnTeamspeakRepository).currentRank(putBan.ban_client_dbid)
            currentRank.forEach { element ->
                if (ignoreBansGroupId.find { it == element } != null) {
                    banParametersToAdd.ignore_ban = true
                }
            }
            if (banTimeRepository.existsByBanId(putBan.ban_id)) {
                val banActionTimes = banTimeRepository.findByBanId(putBan.ban_id)
                banActionTimes.forEach { element ->
                    if (element.timesFrom!! <= banParametersToAdd.times_this && element.timesTo!! >= banParametersToAdd.times_this) {
                        banParametersToAdd.action_id = element.actionId!!
                        banParametersToAdd.time_from = element.timeFrom!!
                        banParametersToAdd.time_to_setting = element.timeToSetting!!
                        banParametersToAdd.time_to = element.timeTo!!
                        banParametersToAdd.time_to_setting = element.timeToSetting!!
                    }
                    if (element.timesFrom!! <= banParametersToAdd.times_this && element.timesTo!! == -1) {
                        banParametersToAdd.action_id = element.actionId!!
                        banParametersToAdd.time_from = element.timeFrom!!
                        banParametersToAdd.time_to_setting = element.timeToSetting!!
                        banParametersToAdd.time_to = element.timeTo!!
                        banParametersToAdd.time_to_setting = element.timeToSetting!!
                    }
                    banParametersToAdd.action_name = actionBanTypeRepository.findByIds(banParametersToAdd.action_id.toLong()).name!!
                    banParametersToAdd.timing = actionBanTypeRepository.findByIds(banParametersToAdd.action_id.toLong()).time!!
                }
            }
            banParametersToAdd
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-action-permission/{actionId}")
    fun getActionPermission(@PathVariable actionId: Int) : Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accessToBan == true) {
            val ranGrantId: Int? = privilegeToRankRepository.findByDbid_Dbid(userAuthDetail.dbid).rank_id?.rankId
            banPermissionRepository.findByRankGrantIdAndActionBanId(rankGrantId = ranGrantId!!, actionBanId = actionId)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }
}