package com.dealership.car.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class responsible for setting up project-specific security settings.
 * It defines the security filter chain, including CSRF protection, request authorization,
 * form login configurations, and HTTP basic authentication.
 */
@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((request) -> request.requestMatchers("/**").permitAll())
                .formLogin(loginConfigurer -> loginConfigurer.loginPage("/login")
                        .defaultSuccessUrl("/dashboard").failureUrl("/login?error=true").permitAll())
                .httpBasic(Customizer.withDefaults());


        return httpSecurity.build();
    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    @Bean
//    public UserDetailsService users(){
//        UserDetails owner = User.builder()
//                .username("owner")
//                .password("owner")
//                .roles("OWNER")
//                .build();
//        return new InMemoryUserDetailsManager(owner);
//    }
}
