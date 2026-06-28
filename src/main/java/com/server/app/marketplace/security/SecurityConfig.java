package com.server.app.marketplace.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/shipping-methods/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/featured-promotions/active").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/api/products/*/approve").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/products/*/reject").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyRole("ADMIN", "SELLER")

                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("SELLER")
                        .requestMatchers(HttpMethod.PATCH, "/api/products/*/price").hasRole("SELLER")

                        .requestMatchers("/api/cart/**").hasRole("BUYER")
                        .requestMatchers("/api/orders/**").hasRole("BUYER")
                        .requestMatchers("/api/payments/**").hasRole("BUYER")

                        .requestMatchers(HttpMethod.POST, "/api/reviews").hasRole("BUYER")
                        .requestMatchers(HttpMethod.POST, "/api/questions").hasRole("BUYER")
                        .requestMatchers(HttpMethod.PATCH, "/api/questions/**").hasRole("SELLER")

                        .requestMatchers("/api/conversations/**").hasAnyRole("BUYER", "SELLER")
                        .requestMatchers("/api/messages/**").hasAnyRole("BUYER", "SELLER")

                        .requestMatchers(HttpMethod.POST, "/api/coupons").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.PATCH, "/api/coupons/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.GET, "/api/coupons/**").hasAnyRole("BUYER", "SELLER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/shipping-methods").hasRole("ADMIN")
                        .requestMatchers("/api/shipments/**").hasAnyRole("BUYER", "SELLER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/returns").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/returns/**").hasAnyRole("BUYER", "SELLER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/returns/**").hasAnyRole("BUYER", "SELLER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/disputes").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/disputes/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/disputes/**").hasAnyRole("BUYER", "SELLER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/disputes/*/respond").hasRole("SELLER")
                        .requestMatchers(HttpMethod.PATCH, "/api/disputes/*/resolve").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/featured-promotions").hasRole("SELLER")
                        .requestMatchers(HttpMethod.GET, "/api/featured-promotions/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/featured-promotions/**").hasAnyRole("SELLER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/featured-promotions/**").hasAnyRole("SELLER", "ADMIN")

                        .requestMatchers("/api/price-notifications/**").hasRole("BUYER")

                        .requestMatchers("/api/reports/**").hasAnyRole("ADMIN", "SELLER")

                        .requestMatchers("/api/sellers/profile/**").hasAnyRole("SELLER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}