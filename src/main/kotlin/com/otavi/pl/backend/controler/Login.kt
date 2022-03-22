package com.otavi.pl.backend.controler

import com.otavi.pl.backend.Ts3
import com.otavi.pl.backend.config.JwtUtil
import com.otavi.pl.backend.dataClass.*
import com.otavi.pl.backend.entity.TempAuthToken
import com.otavi.pl.backend.repository.TempAuthTokenRepository
import com.otavi.pl.backend.repository.UserRegisterRepository
import com.otavi.pl.backend.service.JwtUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin("*")
@RequestMapping("/public")
class Login(private val userRegisterRepository: UserRegisterRepository,
            private val tempAuthTokenRepository: TempAuthTokenRepository) {

    @Autowired
    private val authenticationManager: AuthenticationManager? = null

    @Autowired
    lateinit var userDetailsService: JwtUserDetailsService

    @CrossOrigin("*")
    @PostMapping("/account_login")
    fun login(@RequestBody userForm: UserForm): ResponseEntity<Any>{
        val status = authenticate(userForm)
        println(status)
        if (userRegisterRepository.existsByLogin(userForm.login)) {
            val user = userRegisterRepository.findByLogin(userForm.login)
            if (status=== "Ok" && !user.isBanned) {
                val test= UserJwt(DBID = user.dbid, UID = user.uid, role = user.role, username = user.login)
                val token = JwtToken(token = JwtUtil().generateToken(test), token_type = "bearer")
                return ResponseEntity(token, HttpStatus.OK)
            }
        }
        val detailError = detailError(detail = "Incorrect username or password")
        return ResponseEntity(detailError,HttpStatus.UNAUTHORIZED)

    }

    @PostMapping("/register")
    fun registerUser(@RequestBody registerUser: RegisterUser): ResponseEntity<Any> {
        return if (userRegisterRepository.existsByLogin(registerUser.login)) {
            val detailError = detailError(detail = "Login already registered")
            ResponseEntity(detailError, HttpStatus.BAD_REQUEST)
        } else if (userRegisterRepository.existsByDbid(registerUser.dbid)){
            val detailError = detailError(detail = "DBID already registered")
            ResponseEntity(detailError, HttpStatus.BAD_REQUEST)
        } else if (!tempAuthTokenRepository.existsByDbid(registerUser.dbid)) {
            val detailError = detailError(detail = "Token was'n created")
            ResponseEntity(detailError, HttpStatus.BAD_REQUEST)
        } else if (tempAuthTokenRepository.findByDbid(registerUser.dbid).token != registerUser.token) {
            val detailError = detailError(detail = "Token is invalid")
            ResponseEntity(detailError, HttpStatus.BAD_REQUEST)
        } else {
            userDetailsService.save(registerUser)
            tempAuthTokenRepository.deleteByDbid(registerUser.dbid)
            ResponseEntity("ok", HttpStatus.OK)
        }
    }

    @GetMapping("/setTempToken/{dbid}")
    fun setTempToken(@PathVariable dbid: Int) {
        val token: String = genToken()
        if(tempAuthTokenRepository.existsByDbid(dbid)) {
            tempAuthTokenRepository.updateTokenByDbid(token = token, dbid = dbid)
        } else {
            val tokenData = TempAuthToken()
            tokenData.dbid = dbid
            tokenData.token = token
            tempAuthTokenRepository.save(tokenData)
        }
        Ts3().sendTokenToUser(token = token, dbid = dbid)
    }

    fun authenticate(userForm: UserForm): String {
        try {
            authenticationManager!!.authenticate(UsernamePasswordAuthenticationToken(userForm.login, userForm.password))
        } catch (e: DisabledException) {
            return "Disabled user"
        } catch (e: BadCredentialsException) {
            return "Incorrect password"
        }
        return "Ok"
    }

    fun genToken(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..8)
            .map { allowedChars.random() }
            .joinToString("")
    }
}