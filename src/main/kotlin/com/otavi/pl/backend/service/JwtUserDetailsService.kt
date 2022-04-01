package com.otavi.pl.backend.service

import com.otavi.pl.backend.dataClass.RegisterUser
import com.otavi.pl.backend.entity.UsersRegister
import com.otavi.pl.backend.repository.UserRegisterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(private val UserRegisterRepository: UserRegisterRepository) : UserDetailsService {

    @Autowired
    private val bcryptEncoder: PasswordEncoder? = null


    @Override
    override fun loadUserByUsername(username: String): UserDetails {
        println(username)
        //val user: UsersRegister = userDao?.findByUsername(username:)

        if (UserRegisterRepository.existsByLogin(username)) {
            val user: UsersRegister = UserRegisterRepository.findByLogin(username)
            return User(user.login, user.password, ArrayList<GrantedAuthority>())
        } else {
            throw UsernameNotFoundException("User not found with username: $username")
        }
    }

    fun save(user: RegisterUser) {
        val newUser = UsersRegister()
        val currentTimeInMilli: Long = System.currentTimeMillis() / 1000L
        if (UserRegisterRepository.countByRole("Staff").toInt() == 0) {
            newUser.role = "Staff"
        } else newUser.role = "Register"
        newUser.dbid = user.dbid
        newUser.password = bcryptEncoder!!.encode(user.password)
        newUser.uid = user.uid
        newUser.dateCreated = currentTimeInMilli.toInt()
        newUser.login = user.login
        UserRegisterRepository.save(newUser)
        return
    }

    fun update(user: UsersRegister) {
        user.password = bcryptEncoder!!.encode(user.password)
        UserRegisterRepository.save(user)
        return
    }

    //@Throws(UsernameNotFoundException::class)
   // override fun loadUserByUsername(username: String): UserDetails {
       // val user: DAOUser = userDao.findByUsername(username)
        //    ?: throw UsernameNotFoundException("User not found with username: $username")
       // return User(
         //   user.getUsername(), user.getPassword(),
         //   ArrayList<GrantedAuthority>()
        //)
   // }

   // fun save(user: UserDTO): DAOUser {
     //   val newUser = DAOUser()
    //    newUser.setUsername(user.getUsername())
    //    newUser.setPassword(bcryptEncoder!!.encode(user.getPassword()))
      //  return userDao.save(newUser)
    //}
}