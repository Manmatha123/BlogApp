package com.manmatha.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.manmatha.server.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/redg","/api/blog","/api/check/username"
                        ,"/api/users/all","/api/login","/api/image","/api/fetch/demo/blog"
                        ).permitAll()
                        // .requestMatchers("/"catagory
                        
                        // ).hasRole("ADMIN")
                        .requestMatchers("/api/blogs/all","/api/blogs/all/titel","/api/blogs/all/catagory","/api/user/comments/get",
                            "/api/user/detail","/api/user/blog/edit","/api/user/update/password","/api/blog/delete","/api/blogs/search","/api/user/prifileimage"
                        ,"/api/user/img","/api","/api/user/update","/api/user/info","/api/like","/api/blog","/api/blog/comment","/api/fetch/username").hasRole("USER")

                        .anyRequest().authenticated())
                        // .httpBasic(withDefaults())
                        // .formLogin(withDefaults())
                        .csrf(AbstractHttpConfigurer::disable);
                // .httpBasic(withDefaults())
                // .formLogin(withDefaults())
                // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();
                http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    // @Bean
    // public UserDetailsService userDetailsService(){
    //     return new OurUserInfoUserDetailsService();
    // }

    // @Bean
    // public AuthenticationProvider authenticationProvider() {
    //     DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    //     daoAuthenticationProvider.setUserDetailsService(userDetailsService());
    //     daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    //     return daoAuthenticationProvider;

    // }
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration  configuration)throws Exception{
    return configuration.getAuthenticationManager();
}
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // @Override
    // public AuthenticationManager authenticationManagerBean() throws Exception {
    //     return super.authenticationManagerBean();
    // }
    
// @Bean   
//  public void configure(AuthenticationManagerBuilder auth) throws Exception {
//         auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//     }

}
