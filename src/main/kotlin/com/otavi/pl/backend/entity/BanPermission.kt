package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "ban_permission")
open class BanPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "rank_grant_id")
    open var rankGrantId: Int? = null

    @Column(name = "action_ban_id")
    open var actionBanId: Int? = null

    @Column(name = "max_time_to_action")
    open var maxTimeToAction: Int? = null

    @Column(name = "two_factor_auth")
    open var twoFactorAuth: Boolean? = null

    @Column(name = "`add`")
    open var add: Boolean? = null

    @Column(name = "limit_per_10m")
    open var limitPer10m: Int? = null

    @Column(name = "limit_per_1d")
    open var limitPer1d: Int? = null

    @Column(name = "overflow")
    open var overflow: Boolean? = null

    @Column(name = "commit")
    open var commit: Boolean? = null

    @Column(name = "commit_auto")
    open var commitAuto: Boolean? = null

    @Column(name = "`delete`")
    open var delete: Boolean? = null
}