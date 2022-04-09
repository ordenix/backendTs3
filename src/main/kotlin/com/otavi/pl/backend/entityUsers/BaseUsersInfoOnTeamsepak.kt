package com.otavi.pl.backend.entityUsers

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "base_users_info_on_teamsepak")
open class BaseUsersInfoOnTeamsepak: Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "DBID")
    open var dbid: Int? = null

    @Column(name = "total_connections")
    open var totalConnections: Int? = null

    @Column(name = "real_total_connections")
    open var realTotalConnections: Int? = null

    @Column(name = "created")
    open var created: Int? = null

    @Column(name = "last_connect")
    open var lastConnect: Int? = null

    @Column(name = "myteamspeak_id", length = 128)
    open var myteamspeakId: String? = null

    @Lob
    @Column(name = "description")
    open var description: String? = null
}