package com.otavi.pl.backend.dao.entity
import org.springframework.boot.jackson.JsonComponent
import javax.persistence.*

@Entity
@Table(name = "users_register")
open class UsersRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "login", length = 32)
    open var login: String? = null

    @Column(name = "password", length = 64)
    open var password: String? = null

    @Column(name = "is_banned")
    open var isBanned: Boolean? = null

    @Column(name = "date_created")
    open var dateCreated: Int? = null

    @Column(name = "last_login")
    open var lastLogin: Int? = null

    @Column(name = "uid", length = 32)
    open var uid: String? = null

    @Column(name = "dbid")
    open var dbid: Int? = null

    @Column(name = "role", length = 32)
    open var role: String? = null
}