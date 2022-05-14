package com.otavi.pl.backend.entity

import javax.persistence.*

@Entity
@Table(name = "ban_times")
open class BanTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "action_id")
    open var actionId: Int? = null

    @Column(name = "ban_id")
    open var banId: Int? = null

    @Column(name = "times_from")
    open var timesFrom: Int? = null

    @Column(name = "times_from_setting", length = 128)
    open var timesFromSetting: String? = null

    @Column(name = "times_to")
    open var timesTo: Int? = null

    @Column(name = "times_to_setting", length = 128)
    open var timesToSetting: String? = null

    @Column(name = "time_from")
    open var timeFrom: Int? = null

    @Column(name = "time_from_setting", length = 128)
    open var timeFromSetting: String? = null

    @Column(name = "time_to")
    open var timeTo: Int? = null

    @Column(name = "time_to_setting", length = 128)
    open var timeToSetting: String? = null

    @Column(name = "points")
    open var points: Int? = null
}