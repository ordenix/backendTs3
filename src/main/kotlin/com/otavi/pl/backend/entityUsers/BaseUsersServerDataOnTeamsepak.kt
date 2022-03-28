package com.otavi.pl.backend.entityUsers

import javax.persistence.*

@Entity
@Table(name = "base_users_server_data_on_teamsepak")
open class BaseUsersServerDataOnTeamsepak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "DBID")
    open var dbid: Int? = null

    @Column(name = "server_groups", length = 128)
    open var serverGroups: String? = null

    @Column(name = "is_register")
    open var isRegister: Boolean? = null

    @Column(name = "check_rules")
    open var checkRules: Boolean? = null
}