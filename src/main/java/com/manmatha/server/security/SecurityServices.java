package com.manmatha.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.manmatha.server.entity.MyUser;
import com.manmatha.server.repository.UserRepo;



@Component
public class SecurityServices implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myuser = userRepo.findByEmail(username);

        if (myuser != null) {
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(myuser.getEmail())
                    .password(myuser.getPassword())
                    .roles(myuser.getRole())
                    .build();
            return userDetails;
        }
        throw new UsernameNotFoundException("user not found " + username);

    }

}

// @Autowired
// private UserRepo userRepo;

// @Bean
// public UserDetailsService userDetailsService(String name) {

// UserDetails user = User.builder()
// .username()
// .password("$2a$12$YpxMhfKa3eilNz.3wQGm8eKuTutHict1ZExfIHitPyI4KZvX5LHA6")
// .roles("USER")
// .build();
// return new InMemoryUserDetailsManager(user);

// }
