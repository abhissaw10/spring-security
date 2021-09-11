package com.example.securitydemo.security;

import com.example.securitydemo.ApplicationPermission;
import com.example.securitydemo.ApplicationRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

import static com.example.securitydemo.ApplicationPermission.*;
import static com.example.securitydemo.ApplicationRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder encoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().
                authorizeRequests()
                .antMatchers("/","/index.html").permitAll()
                .antMatchers("/students/**").hasRole(STUDENT.name())
                .antMatchers(HttpMethod.GET,"/management/**").hasAnyAuthority(COURSE_READ.getPermission(), COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.POST,"/management/**").hasAnyAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.GET,"/management/**").hasAnyRole(ADMIN.name(),NEW_ADMIN.name())
                .anyRequest()
                .authenticated()
                .and().httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
       return new InMemoryUserDetailsManager(
               Arrays.asList(
                       User.builder().username("harish").password(encoder.encode("password")).roles(STUDENT.name()).authorities(STUDENT.getAuthorities()).build(),
                       User.builder().username("vishal").password(encoder.encode("password123")).roles(NEW_ADMIN.name()).authorities(NEW_ADMIN.getAuthorities()).build(),
                       User.builder().username("kartik").password(encoder.encode("password12")).roles(STUDENT.name(), ADMIN.name()).authorities(ADMIN.getAuthorities()).build()));
    }

}
