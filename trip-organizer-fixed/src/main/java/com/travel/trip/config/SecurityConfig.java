package com.travel.trip.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration.
 *
 * NOTE: Spring Security has been intentionally removed from this project.
 * The application uses its own HttpSession-based authentication (see AuthController).
 * H2 console access is enabled via application.properties (spring.h2.console.enabled=true).
 *
 * If you later add spring-boot-starter-security back, replace this class with:
 *
 *   @Configuration
 *   @EnableWebSecurity
 *   public class SecurityConfig {
 *       @Bean
 *       public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 *           http
 *               .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
 *               .csrf(AbstractHttpConfigurer::disable)
 *               .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
 *           return http.build();
 *       }
 *   }
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    // No Spring Security — session auth is handled in AuthController via HttpSession.
}
