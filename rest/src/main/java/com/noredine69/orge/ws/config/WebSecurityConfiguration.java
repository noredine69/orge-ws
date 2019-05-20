package com.noredine69.orge.ws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${orge.ws.http.auth-token-header-name}")
    private String principalRequestHeader;

    @Value("${orge.ws.http.auth-token}")
    private String principalRequestValue;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        final APIKeyAuthFilter filter = new APIKeyAuthFilter(this.principalRequestHeader);
        filter.setAuthenticationManager(authentication -> {
            final String principal = (String) authentication.getPrincipal();
            if (!WebSecurityConfiguration.this.principalRequestValue.equals(principal)) {
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }
            authentication.setAuthenticated(true);
            return authentication;
        });

        //@formatter:off
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "/h2/**","/h2", "/swagger-ui.html", "/webjars/**", "/api-docs", "/swagger-resources", "/swagger-resources/configuration/ui", "/swagger-resources/configuration/security")
            .permitAll()
            .and()
                .antMatcher("/**")
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .antMatcher("/fee").antMatcher("/rule")
                .addFilter(filter).authorizeRequests().anyRequest().authenticated()
            .and()
                .headers().frameOptions().disable()
                .contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline'");
        //@formatter:on
    }
}