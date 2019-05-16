package com.noredine69.orge.ws.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/h2-console/**")
            .permitAll()
            .antMatchers("/", "/swagger-ui.html", "/webjars/**", "/continue3rdPartyPayment/**", "/api-docs", "/swagger-resources", "/swagger-resources/configuration/ui", "/swagger-resources/configuration/security")
            .permitAll()
            .and()
                .headers().frameOptions().disable()
                .contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline'");
        //@formatter:on
    }
}