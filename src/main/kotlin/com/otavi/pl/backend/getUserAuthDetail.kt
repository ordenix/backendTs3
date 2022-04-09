package com.otavi.pl.backend

import com.otavi.pl.backend.entity.PrivilageToRank
import com.otavi.pl.backend.repository.PrivilegeToRankRepository
import org.springframework.security.core.context.SecurityContextHolder

class GetUserAuthDetail(
    var dbid: Int = 0,
    var role: String = "",
    var userPrivilege: PrivilageToRank? = null,
    privilegeToRankRepository: PrivilegeToRankRepository
) {
    init {
        dbid = SecurityContextHolder.getContext().authentication.name.toInt()
        role = SecurityContextHolder.getContext().authentication.authorities.toString()
        userPrivilege = privilegeToRankRepository.findByDbid_Dbid(dbid)
    }


}