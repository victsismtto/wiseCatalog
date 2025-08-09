//package com.code.elevate.wise.catalog.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class SecurityConfig {
//    private final CustomUserDetailsService userDetailsService;
//
//    public SecurityConfig(CustomUserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()  // para APIs REST, geralmente se desabilita
//                .authorizeHttpRequests()
//                .requestMatchers("/public/**").permitAll()  // endpoints públicos
//                .anyRequest().authenticated()  // o resto precisa de login
//                .and()
//                .userDetailsService(userDetailsService)
//                .httpBasic();  // ou .formLogin() para login via formulário
//
//        return http.build();
//    }
//}
