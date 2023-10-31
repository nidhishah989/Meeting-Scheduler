package org.nidhishah.meetingscheduler.security;

import org.nidhishah.meetingscheduler.config.OrganizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http    .addFilterBefore(new OrganizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        (auth) -> auth
                        .requestMatchers("/", "/orgsetup","/orgsetupprocess","/login","/css/**","/js/**","assests/**").permitAll() //this will allow any user to access these two pages
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
