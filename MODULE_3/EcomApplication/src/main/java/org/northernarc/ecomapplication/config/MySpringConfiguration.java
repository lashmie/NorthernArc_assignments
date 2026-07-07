package org.northernarc.ecomapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
public class MySpringConfiguration {
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user1 = User.builder()
                .username("customer")
                .password(passwordEncoder.encode("password"))
                .roles("CUSTOMER")
                .build();
        UserDetails admin1 = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("wowjava"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, admin1);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/products").permitAll()
                        //.requestMatchers("/admin/**").hasRole("ADMIN")
                        //.requestMatchers("/customer/**").hasRole("CUSTOMER")

                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}