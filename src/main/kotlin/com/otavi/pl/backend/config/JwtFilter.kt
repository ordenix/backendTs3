package com.otavi.pl.backend.config

import com.otavi.pl.backend.dataClass.JwtDataConfig
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtFilter: OncePerRequestFilter() {



    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val requestTokenHeader = request.getHeader("Authorization")
        var username: String? = null
        var jwtToken: String
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7)
            try {
                username = JwtUtil().getUsernameFromToken(jwtToken)
            } catch (e: IllegalArgumentException) {
                println("Unable to get JWT Token")
            } catch (e: ExpiredJwtException) {
                println("JWT Token has expired")
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String")
        }
        // Once we get the token validate it.

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            jwtToken = requestTokenHeader.substring(7)
            val userJwt = JwtUtil().getUserJwtFromToken(jwtToken)
            val context: SecurityContext = SecurityContextHolder.createEmptyContext()
            val simpleGrantedAuthorities: Set<SimpleGrantedAuthority> =
                Collections.singleton(SimpleGrantedAuthority(userJwt.role))
            val authentication: Authentication = UsernamePasswordAuthenticationToken(userJwt.DBID, "password", simpleGrantedAuthorities)
            context.authentication = authentication
            SecurityContextHolder.setContext(context)
        }
        filterChain.doFilter(request, response)
    }
}