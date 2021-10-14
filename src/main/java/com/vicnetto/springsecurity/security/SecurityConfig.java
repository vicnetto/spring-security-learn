package com.vicnetto.springsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.vicnetto.springsecurity.security.UserPermissions.STUDENT_READ;
import static com.vicnetto.springsecurity.security.UserPermissions.STUDENT_WRITE;
import static com.vicnetto.springsecurity.security.UserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private static final String MANAGEMENT_ENDPOINT = "/management/api/**";

    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Any request must be authenticated, with http basic.
        http
                .csrf().disable()
                // In this program, all the requisitions are made by the postman, so csrf could be disabled.
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                // Adding this authorization on StudentController
                // .antMatchers("/api/**").hasRole(STUDENT.name())
                .antMatchers(HttpMethod.GET, MANAGEMENT_ENDPOINT).hasAuthority(STUDENT_READ.getPermission())
                .antMatchers(HttpMethod.POST, MANAGEMENT_ENDPOINT).hasAuthority(STUDENT_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, MANAGEMENT_ENDPOINT).hasAuthority(STUDENT_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, MANAGEMENT_ENDPOINT).hasAuthority(STUDENT_WRITE.getPermission())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails victor = User.builder()
                .username("vicnetto")
                .password(passwordEncoder.encode("password"))
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails beatriz = User.builder()
                .username("byubia")
                .password(passwordEncoder.encode("password"))
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails eden = User.builder()
                .username("eden40")
                .password(passwordEncoder.encode("password"))
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(victor, beatriz, eden);
    }
}