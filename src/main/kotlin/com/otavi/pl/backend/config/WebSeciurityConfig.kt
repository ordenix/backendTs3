package com.otavi.pl.backend.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class WebSeciurityConfig: WebSecurityConfigurerAdapter() {

    @Override
    protected override fun configure(http: HttpSecurity) {
        http.formLogin().disable()
            .authorizeRequests()
            .antMatchers("/account_login").permitAll();

        super.configure(http)
    }
}