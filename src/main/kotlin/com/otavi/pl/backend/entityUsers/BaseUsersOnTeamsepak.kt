package com.otavi.pl.backend.entityUsers

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "base_users_on_teamsepak")
open class BaseUsersOnTeamsepak: Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "DBID")
    open var dbid: Int? = null

    @Column(name = "UID", length = 32)
    open var uid: String? = null

    @Lob
    @Column(name = "IP")
    open var ip: String? = null

    @Lob
    @Column(name = "Nick")
    open var nick: String? = null

}