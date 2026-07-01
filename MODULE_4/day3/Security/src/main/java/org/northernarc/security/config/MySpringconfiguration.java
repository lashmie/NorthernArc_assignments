package org.northernarc.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MySpringconfiguration {
@Autowired
private PasswordEncoder passwordEncoder;
private
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user1 = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();
        UserDetails admin1 = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("wowjava"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, admin1);
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/hello1").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                // .formLogin(Customizer.withDefaults())
                // .logout(Customizer.withDefaults())
                .build();
    }
}
