package com.example.securitydemo.security;

import com.example.securitydemo.ApplicationUserDetailsService;
import com.example.securitydemo.JWTAuthenticationFilter;
import com.example.securitydemo.JWTTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.securitydemo.ApplicationPermission.STUDENT_READ;
import static com.example.securitydemo.ApplicationRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ApplicationUserDetailsService applicationUserDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilterAfter(new JWTTokenVerifier(),JWTAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/","/index.html").permitAll()
                .antMatchers("/students/**").hasRole(STUDENT.name())
                .antMatchers(HttpMethod.GET,"/students/**").hasAuthority(STUDENT_READ.name())
            //    .antMatchers(HttpMethod.GET,"/management/**").hasAnyAuthority(COURSE_WRITE.name(),COURSE_READ.name())
               // .antMatchers(HttpMethod.POST,"/management/**").hasAuthority(COURSE_WRITE.name())
                //.antMatchers(HttpMethod.GET,"/management/**").hasAnyRole(ADMIN.name(), NEW_ADMIN.name())
                .anyRequest()
                .authenticated();
    }

    /*@Override
    @Bean
    protected UserDetailsService userDetailsService() {
       return new InMemoryUserDetailsManager(
               Arrays.asList(
                       User.builder().username("harish").password(encoder.encode("password")).roles(STUDENT.name()).authorities(STUDENT.getAuthorities()).build(),
                       User.builder().username("vishal").password(encoder.encode("password123")).roles(NEW_ADMIN.name()).authorities(NEW_ADMIN.getAuthorities()).build(),
                       User.builder().username("kartik").password(encoder.encode("password12")).roles(STUDENT.name(), ADMIN.name()).authorities(ADMIN.getAuthorities()).build()));
    }*/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(applicationUserDetailsService);
        return provider;
    }


}
