package com.otavi.pl.backend.entityUsers

import javax.persistence.*

@Entity
@Table(name = "base_users_misc_on_teamsepak")
open class BaseUsersMiscOnTeamsepak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "DBID")
    open var dbid: Int? = null

    @Column(name = "client_badges", length = 128)
    open var clientBadges: String? = null

    @Column(name = "client_country", length = 128)
    open var clientCountry: String? = null

    @Column(name = "client_version", length = 128)
    open var clientVersion: String? = null

    @Column(name = "platform", length = 128)
    open var platform: String? = null
}