package com.otavi.pl.backend.dataClass

import com.otavi.pl.backend.entity.ActionBanType
import com.otavi.pl.backend.entity.GrantRank


data class BanPermissionsExtended(
     var id: Int? = null,
     var grant_rank: GrantRank? = null,
     var action_ban_type: ActionBanType? = null,
     var maxTimeToAction: Int? = null,
     var twoFactorAuth: Boolean? = null,
     var add: Boolean? = null,
     var limitPer10m: Int? = null,
     var limitPer1d: Int? = null,
     var overflow: Boolean? = null,
     var commit: Boolean? = null,
     var commitAuto: Boolean? = null,
     var delete: Boolean? = null,
)
