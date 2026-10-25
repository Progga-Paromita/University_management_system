package com.kuet.university.config;

import com.kuet.university.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository repository) {

        return username -> repository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/admins", "/admins/**",
                                "/teachers", "/teachers/**",
                                "/students", "/students/**"
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/error", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        .requestMatchers("/admins", "/admins/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/teachers", "/teachers/**")
                        .hasAnyRole("ADMIN", "TEACHER")

                        .requestMatchers("/students", "/students/**")
                        .hasAnyRole("ADMIN", "STUDENT")

                        .requestMatchers("/ui/admins", "/ui/admins/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/ui/teachers", "/ui/teachers/**")
                        .hasAnyRole("ADMIN", "TEACHER")

                        .requestMatchers("/ui/students", "/ui/students/**")
                        .hasAnyRole("ADMIN", "STUDENT")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
