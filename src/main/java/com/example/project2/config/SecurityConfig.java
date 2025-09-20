package com.example.project2.config;

import com.example.project2.model.User;
import com.example.project2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private UserRepository userRepository;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<User> user = userRepository.findByUsername(username);
                if (user.isPresent()) {
                    User u = user.get();
                    return org.springframework.security.core.userdetails.User.builder()
                            .username(u.getUsername())
                            .password(u.getPassword())
                            .roles(u.getRole().name())
                            .build();
                } else {
                    throw new UsernameNotFoundException("Пользователь не найден: " + username);
                }
            }
        };
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Публичные страницы
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                // Swagger UI и API документация
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api/users/**").permitAll() // Временно разрешаем доступ к API
                // Просмотр страниц доступен всем аутентифицированным пользователям
                .requestMatchers("/students", "/groups", "/courses", "/teachers", "/addresses").authenticated()
                .requestMatchers("/students/view/**", "/groups/view/**", "/courses/view/**", "/teachers/view/**", "/addresses/view/**").authenticated()
                // CRUD операции только для администраторов
                .requestMatchers("/students/new", "/students/edit/**", "/students/delete/**", "/students/restore/**").hasRole("ADMIN")
                .requestMatchers("/groups/new", "/groups/edit/**", "/groups/delete/**", "/groups/restore/**").hasRole("ADMIN")
                .requestMatchers("/courses/new", "/courses/edit/**", "/courses/delete/**", "/courses/restore/**").hasRole("ADMIN")
                .requestMatchers("/teachers/new", "/teachers/edit/**", "/teachers/delete/**", "/teachers/restore/**").hasRole("ADMIN")
                .requestMatchers("/addresses/new", "/addresses/edit/**", "/addresses/delete/**", "/addresses/restore/**").hasRole("ADMIN")
                // Административные функции только для ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Остальные запросы требуют аутентификации
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        
        return http.build();
    }
}
