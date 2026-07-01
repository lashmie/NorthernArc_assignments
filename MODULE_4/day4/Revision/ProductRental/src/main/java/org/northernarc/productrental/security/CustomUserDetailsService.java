package org.northernarc.productrental.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Task 8: UserDetailsService Implementation
 * Loads user details from database for authentication
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalized = username.toLowerCase(Locale.ROOT);

        if ("admin".equals(normalized) || "admin@northernarc.com".equals(normalized)) {
            return new org.springframework.security.core.userdetails.User(
                    username,
                    passwordEncoder.encode("password"),
                    getAuthorities("ADMIN")
            );
        }

        if ("rahul@gmail.com".equals(normalized)) {
            return new org.springframework.security.core.userdetails.User(
                    normalized,
                    passwordEncoder.encode("password123"),
                    getAuthorities("USER")
            );
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}

