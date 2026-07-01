package org.northernarc.jpauserexplanation.service;

import jakarta.annotation.PostConstruct;
import org.northernarc.jpauserexplanation.model.JpaUser;
import org.northernarc.jpauserexplanation.repo.JpaUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    @Autowired
    private JpaUserRepo jpaUserRepo;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JpaUser user1 = jpaUserRepo.findByUsername(username);
        return User.builder()
                .username(user1.getUsername())
                .password(user1.getPassword())
                .roles(user1.getRole())
                .build();
    }
//    //RUN THIS ONLY ONCE
//    @PostConstruct
//    public void init() {
//        JpaUser jpaUser = new JpaUser();
//        jpaUser.setUsername("user");
//        jpaUser.setPassword(passwordEncoder.encode("123"));
//        jpaUser.setRole("USER");
//
//        JpaUser jpaUser1 = new JpaUser();
//        jpaUser1.setUsername("admin");
//        jpaUser1.setPassword(passwordEncoder.encode("123"));
//        jpaUser1.setRole("ADMIN");
//
//        jpaUserRepo.save(jpaUser);
//        jpaUserRepo.save(jpaUser1);
//
//    }
}
