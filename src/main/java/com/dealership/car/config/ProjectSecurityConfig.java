package com.dealership.car.config;

import com.dealership.car.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/about").permitAll()
                        .requestMatchers("/services").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/public/**","/public//forgotPassword").permitAll()
                        .requestMatchers("/contact", "contact/saveMsg").permitAll()
                        .requestMatchers("/deleteAdmin/**","turnIntoOperator/**","/updateAdmin/**").hasRole(Constants.OWNER_ROLE)
                        .requestMatchers("/analytics/**").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE, Constants.OPERATOR_ROLE)
                        .requestMatchers("/contact/closeMsg", "contact/showMessages").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE, Constants.OPERATOR_ROLE)
                        .requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/user/displayOrders").authenticated()
                        .requestMatchers("/custom/**").hasAnyRole(Constants.OWNER_ROLE, Constants.ADMIN_ROLE, Constants.OPERATOR_ROLE)
                        .requestMatchers("/dynamic-fields/**").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE)
                        .requestMatchers("/deleteOperator","/updateOperator").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE)
                        .requestMatchers("/turnIntoAdmin").hasRole(Constants.OWNER_ROLE)
                        .requestMatchers("/orders/**").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE, Constants.OPERATOR_ROLE)
                        .requestMatchers("/showPwd").authenticated()
                        .requestMatchers("/product/**").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE, Constants.OPERATOR_ROLE)
                        .requestMatchers("/admins/**","/staff/admins").hasRole(Constants.OWNER_ROLE)
                        .requestMatchers("/staff/operators").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE)
                        .requestMatchers("/staff/users","staff/findById").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE,Constants.OPERATOR_ROLE)
                        .requestMatchers("/supplier/supplierForm", "supplier/addSupplier","/supplier/updateSupplier").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE)
                        .requestMatchers("/supplier/supplierOrderForm","supplier/addSupplierToOrder",
                                "/supplier/listSuppliersSoon","supplier/setDelay").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE,Constants.OPERATOR_ROLE)

                        .requestMatchers("/technicalData/**").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE, Constants.OPERATOR_ROLE)
                        .requestMatchers("/deleteUser","/turnUserIntoOperator","/updateUser").hasAnyRole(Constants.OWNER_ROLE,Constants.ADMIN_ROLE)
                        .requestMatchers("/css/**", "/js/**", "/images/**","/fonts/**").permitAll()



                )
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
