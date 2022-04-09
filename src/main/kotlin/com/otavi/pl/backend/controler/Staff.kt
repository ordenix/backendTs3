package com.otavi.pl.backend.controler

import com.otavi.pl.backend.GetUserAuthDetail
import com.otavi.pl.backend.dataClass.Dbid
import com.otavi.pl.backend.dataClass.RoleToStaff
import com.otavi.pl.backend.dataClass.detailError
import com.otavi.pl.backend.entity.GrantRank
import com.otavi.pl.backend.entity.PrivilageToRank
import com.otavi.pl.backend.repository.GrantRankRepository
import com.otavi.pl.backend.repository.PrivilegeToRankRepository
import com.otavi.pl.backend.repository.UserRegisterRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/staff")
class Staff(
    val userRegisterRepository: UserRegisterRepository,
    val privilegeToRankRepository: PrivilegeToRankRepository,
    val grantRankRepository: GrantRankRepository
) {

    @GetMapping("/get-staff-list")
    fun getStaffList(): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accesToStaffUser == true) {
            userRegisterRepository.findByRole("Staff")
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @PutMapping("/set-user-to-staff-by-dbid")
    fun setUserToStaffBydbid(@RequestBody dbid: Dbid): ResponseEntity<detailError> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accesToStaffUser == true) {
            userRegisterRepository.updateRoleByDbid(role = "Staff", dbid = dbid.dbid)
            ResponseEntity(detailError("Ok"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-user-list")
    fun getUserList(): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accesToStaffUser == true) {
            userRegisterRepository.findByRole("Register")
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/get-staff-rank-list")
    fun getStaffRankList(): Any {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accesToGrantRank == true) {
            grantRankRepository.findAll()
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @PutMapping("/modify-staff-rank")
    fun modifyStaffRank(@RequestBody grantRank: GrantRank): ResponseEntity<detailError> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accesToGrantRank == true) {
            if (grantRank.id == 0) {
                val grantRanks = grantRankRepository.findAll()
                if (grantRanks.size == 0) {
                    grantRank.rankId = 1
                } else {
                    val lastGrantRank: Optional<GrantRank> = grantRankRepository.findById(grantRanks.size.toLong())
                    lastGrantRank.ifPresent { element -> grantRank.rankId = element.id?.plus(1) }
                }
            }
            grantRankRepository.save(grantRank)
            ResponseEntity(detailError("Ok"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @PutMapping("/modify-role-to-staff-user")
    fun modifyRoleToStaffUser(@RequestBody roleToStaff: RoleToStaff): ResponseEntity<detailError> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accesToGrantRank == true) {
            var privilegeToRank = PrivilageToRank()
            if (privilegeToRankRepository.existsByDbid_Dbid(roleToStaff.dbid)) {
                privilegeToRank = privilegeToRankRepository.findByDbid_Dbid(roleToStaff.dbid)
                privilegeToRank.rank_id?.rankId = roleToStaff.rank_id
            } else {
                privilegeToRank.dbid?.dbid = roleToStaff.dbid
                privilegeToRank.rank_id?.rankId = roleToStaff.rank_id
            }
            privilegeToRankRepository.save(privilegeToRank)
            ResponseEntity(detailError("OK"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }

    @DeleteMapping("/delete-role-to-staff-user")
    fun deleteRoleToStaffUser(@RequestBody roleToStaff: RoleToStaff): ResponseEntity<detailError> {
        val userAuthDetail = GetUserAuthDetail(privilegeToRankRepository = privilegeToRankRepository)
        return if (userAuthDetail.role == "[Staff]" && userAuthDetail.userPrivilege?.rank_id?.accesToGrantRank == true) {
            var privilegeToRank: PrivilageToRank = privilegeToRankRepository.findByDbid_Dbid(roleToStaff.dbid)
            privilegeToRankRepository.delete(privilegeToRank)
            ResponseEntity(detailError("OK"), HttpStatus.OK)
        } else {
            ResponseEntity(detailError("Could not validate credentials"), HttpStatus.UNAUTHORIZED)
        }
    }


}