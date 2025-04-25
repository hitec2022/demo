package com.sparta.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    
    UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            
        // 인가(접근권한) 설정
        http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/user/**").authenticated()
            .requestMatchers("/api/svc/**").authenticated()
            .anyRequest().permitAll()
        ).formLogin(authorize -> authorize
            .loginPage("/api/login")
            .loginProcessingUrl("/api/loginProc")
            .usernameParameter("userEmail")
            .defaultSuccessUrl("/api/loginOk", true)
        ).logout(authorize -> authorize
            .logoutUrl("/api/logout")
            .logoutSuccessUrl("/api/logoutOk")
            .deleteCookies("JSESSIONID")
        ).userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
    	return new BCryptPasswordEncoder();
    } 
}
