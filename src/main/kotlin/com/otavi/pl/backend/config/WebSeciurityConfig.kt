package com.otavi.pl.backend.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*


@Configuration
@EnableWebSecurity
class WebSeciurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    private var jwtAuthenticationEntryPoint: JwtEntryPoint? = null

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }


    @Override
    override fun configure(http: HttpSecurity) {
        http
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST,"/public/*").permitAll()
            .antMatchers(HttpMethod.GET,"/game-rank/*").authenticated()// to remove/ edit
            .antMatchers(HttpMethod.PUT,"/game-rank/*").authenticated()// to remove/ edit
            .antMatchers(HttpMethod.GET,"/game-rank/rank-games-list/*").permitAll() // to remove/ edit
            .antMatchers(HttpMethod.GET,"/game-rank/current-rank-array-to-initialize/*").permitAll() // to edit
            .antMatchers(HttpMethod.GET,"/test/test2").permitAll() // to remove
            .antMatchers(HttpMethod.GET,"/test/*").hasAuthority("Guest") // to remove
            .antMatchers("/staff/*").hasAuthority("Staff")
            .antMatchers(HttpMethod.GET,"/user/*").hasAuthority("Staff")
            .antMatchers(HttpMethod.GET,"/ban/*").hasAuthority("Staff")
            .antMatchers(HttpMethod.GET,"/others/get-ip").permitAll()
            .antMatchers(HttpMethod.GET,"/others/*").authenticated()
            .antMatchers(HttpMethod.GET,"/public/*").permitAll()
            .antMatchers(HttpMethod.GET,"/sidebar/*").authenticated()
            .antMatchers(HttpMethod.GET,"/rank-register/*").authenticated()
            .antMatchers(HttpMethod.POST,"/public/recovery-password/*").permitAll()
            .antMatchers(HttpMethod.GET,"/public/set-Temp-Token/*").permitAll()
            .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            //.addFilter(JwtFilter(authenticationManager()));
        // Add a filter to validate the tokens with every request
        http.addFilterBefore(JwtFilter(), UsernamePasswordAuthenticationFilter::class.java)
        super.configure(http)

    }

//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource? {
//        val configuration = CorsConfiguration()
//        configuration.allowedOrigins = Arrays.asList("http://localhost:8082")
//        configuration.allowedMethods = Arrays.asList("GET", "POST")
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", configuration)
//        return source
//    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = Arrays.asList("*")
        configuration.allowedMethods = Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        configuration.allowedHeaders = Arrays.asList("authorization", "content-type", "x-auth-token")
        configuration.exposedHeaders = Arrays.asList("x-auth-token")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }




}


