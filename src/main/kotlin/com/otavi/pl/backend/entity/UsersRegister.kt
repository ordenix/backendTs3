package com.otavi.pl.backend.entity
import javax.persistence.*

@Entity
@Table(name = "users_register")
open class UsersRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int = 0

    @Column(name = "login", length = 32)
    open var login: String = ""

    @Column(name = "password", length = 64)
    open var password: String = ""

    @Column(name = "is_banned")
    open var isBanned: Boolean = false

    @Column(name = "date_created")
    open var dateCreated: Int = 0

    @Column(name = "last_login")
    open var lastLogin: Int = 0

    @Column(name = "uid", length = 32)
    open var uid: String = ""

    @Column(name = "dbid")
    open var dbid: Int = 0

    @Column(name = "role", length = 32)
    open var role: String = ""
}