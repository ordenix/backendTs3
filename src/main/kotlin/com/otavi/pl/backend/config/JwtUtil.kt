package com.otavi.pl.backend.config

import com.otavi.pl.backend.dataClass.JwtDataConfig
import com.otavi.pl.backend.dataClass.UserJwt
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil() {
    val secret = JwtDataConfig().secret
    val time = JwtDataConfig().time
    fun getUsernameFromToken(token: String): String {
        return parseToken(token).subject
    }

    fun getUserJwtFromToken(token: String): UserJwt {
        val userJwt = UserJwt()
        userJwt.UID = parseToken(token)["UID"].toString()
        userJwt.DBID = parseToken(token)["DBID"].toString().toInt()
        userJwt.role = parseToken(token)["role"].toString()
        userJwt.username = getUsernameFromToken(token)
        return userJwt
    }

    fun parseToken(token: String): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
    }

    fun generateToken(userJwt: UserJwt): String{
        val currentTimeInMilli: Long = System.currentTimeMillis()
        return Jwts.builder()
            .setSubject(userJwt.username)
            .claim("UID", userJwt.UID)
            .claim("DBID", userJwt.DBID)
            .claim("role", userJwt.role)
            .setIssuedAt(Date(currentTimeInMilli))
            .setExpiration(Date(currentTimeInMilli + time))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()

    }
}