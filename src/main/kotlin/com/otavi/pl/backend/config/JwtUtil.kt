package com.otavi.pl.backend.config

import com.otavi.pl.backend.dao.UserJwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import java.util.*

class JwtUtil {

    fun generateToken(userJwt: UserJwt, secret: String?): String{
        val currentTimeInMilli: Long = System.currentTimeMillis()
        return Jwts.builder()
            .claim("UID", userJwt.UID)
            .claim("DBID", userJwt.DBID)
            .claim("role", userJwt.role)
            .setIssuedAt(Date(currentTimeInMilli))
            .setExpiration(Date(currentTimeInMilli + 1000 * 60 * 60))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()

    }
}