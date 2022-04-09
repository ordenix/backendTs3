package com.otavi.pl.backend.controler

import com.otavi.pl.backend.GetUserAuthDetail
import com.otavi.pl.backend.dataClass.PutBan
import com.otavi.pl.backend.dataClass.detailError
import com.otavi.pl.backend.entity.BanHistoryTable
import com.otavi.pl.backend.entity.BanPermission
import com.otavi.pl.backend.repository.BanHistoryTableRepository
import com.otavi.pl.backend.repository.BanPermissionRepository
import com.otavi.pl.backend.repository.PrivilegeToRankRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ban")
class Ban(
    val privilegeToRankRepository: PrivilegeToRankRepository,
    val banHistoryTableRepository: BanHistoryTableRepository,
    val banPermissionRepository: BanPermissionRepository
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

            ResponseEntity(detailError("OK"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }

    }

}