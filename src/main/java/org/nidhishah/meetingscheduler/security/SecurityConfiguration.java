package org.nidhishah.meetingscheduler.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        (auth) -> auth
                        .requestMatchers("/", "/orgsetup","/orgsetupprocess","/css/**","/js/**","assests/**").permitAll() //this will allow any user to access these two pages
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
