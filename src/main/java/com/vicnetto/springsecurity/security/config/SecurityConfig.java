package com.vicnetto.springsecurity.security.config;

import com.vicnetto.springsecurity.security.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.vicnetto.springsecurity.security.config.UserPermissions.STUDENT_READ;
import static com.vicnetto.springsecurity.security.config.UserPermissions.STUDENT_WRITE;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private static final String MANAGEMENT_ENDPOINT = "/management/api/**";

    public SecurityConfig(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Any request must be authenticated, with HTTP basic.
        http
                .csrf().disable()
                // In this program, all the requisitions are made by the postman, so CSRF could be disabled.
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
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/courses", true)
                .and()
                .rememberMe()
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login")
                    // As the CSRV Token is off, logout can be a GET request (even though logout should be a POST).
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);

        return provider;
    }
}