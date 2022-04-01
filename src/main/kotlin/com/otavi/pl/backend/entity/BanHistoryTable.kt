package com.otavi.pl.backend.entity

import com.otavi.pl.backend.entityUsers.BaseUsersOnTeamsepak
import javax.persistence.*

@Entity
@Table(name = "ban_history_table")
open class BanHistoryTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ban_client_dbid", referencedColumnName = "DBID")
    open var banClientDbid: BaseUsersOnTeamsepak? = null

    @Column(name = "ban_id")
    open var banId: Int? = null

    @Column(name = "action_id")
    open var actionId: Int? = null

    @Column(name = "additional_info", length = 1024)
    open var additionalInfo: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_admin_dbid", referencedColumnName = "DBID")
    open var addAdminDbid: BaseUsersOnTeamsepak? = null

    @Column(name = "time_add")
    open var timeAdd: Int? = null

    @Column(name = "time_to")
    open var timeTo: Int? = null

    @Column(name = "active")
    open var active: Boolean? = null

    @Column(name = "time_to_overflow")
    open var timeToOverflow: Int? = null

    @Column(name = "to_commit")
    open var toCommit: Boolean? = null

    @Column(name = "commit")
    open var commit: Boolean? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commit_admin_dbid", referencedColumnName = "DBID")
    open var commitAdminDbid: BaseUsersOnTeamsepak? = null

    @Column(name = "time_commit")
    open var timeCommit: Int? = null

    @Column(name = "auto_ban")
    open var autoBan: Boolean? = null

    @Column(name = "removed")
    open var removed: Boolean? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "removed_dbid", referencedColumnName = "DBID")
    open var removedDbid: BaseUsersOnTeamsepak? = null

    @Column(name = "time_removed")
    open var timeRemoved: Int? = null


}