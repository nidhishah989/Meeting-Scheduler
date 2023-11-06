package org.nidhishah.meetingscheduler.security;

import org.nidhishah.meetingscheduler.config.OrganizationFilter;
import org.nidhishah.meetingscheduler.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserServiceImpl userDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService); //set the custom user details service
        auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
        return auth;
    }

    //beans
    //bcrypt bean definition
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http    .addFilterBefore(new OrganizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        (auth) -> auth
                        .requestMatchers("/", "/orgsetup","/orgsetupprocess","/login","/signup","/css/**","/js/**","assests/**").permitAll() //this will allow any user to access these two pages
                                .requestMatchers("/setorgdetail/**","/process-orgdetailsetup","/adm_dashboard","/addteammember","/addclient").hasAuthority("admin")
                        .anyRequest().authenticated() //this will make sure to authenticate user for other pages

                )

                .formLogin((form) -> form
                        .loginPage("/login") //login page accessible to all
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll()); //logout page accessible to all

        return http.build();
    }



}
