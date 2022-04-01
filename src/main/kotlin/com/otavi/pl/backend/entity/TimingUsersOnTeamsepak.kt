package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "timing_users_on_teamsepak")
open class TimingUsersOnTeamsepak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "DBID")
    open var dbid: Int? = null

    @Column(name = "TIME_TOTAL")
    open var timeTotal: Int? = null

    @Column(name = "TIME_ONLINE")
    open var timeOnline: Int? = null

    @Column(name = "TIME_AWAY")
    open var timeAway: Int? = null

    @Column(name = "TIME_IDLE")
    open var timeIdle: Int? = null

    @Column(name = "TIME_MIC_DISABLED")
    open var timeMicDisabled: Int? = null
}