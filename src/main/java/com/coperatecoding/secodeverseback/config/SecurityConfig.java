package com.coperatecoding.secodeverseback.config;

import com.coperatecoding.secodeverseback.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {

    private final String[] whiteList = {
            "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/api/v1/user/login", "api/v1/user/signup", // 원래 이것만
            "api/v1/user/info/my","api/v1/user/logout",
            "/error", "api/v1/s3/*",
            "api/v1/user/username/**", "api/v1/user/id/**",
            "api/v1/board/**", "api/v1/comment/**","/api/v1/likes/**","api/v1/question/**","test/hello",
            "/error", "api/v1/s3/presigned",
            "/api/v1/token/**", "api/v1/user/nickname/**", "api/v1/user/id/**",
            "api/v1/board/**", "api/v1/comment/**","/api/v1/likes/**","api/v1/question/**","test/hello","api/v1/chatbot",
            "api/v1/ctf/**"
            // 이건 다 임의로 넣어둠.
//            "/logout"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final TokenProvider tokenProvider;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final AuthenticationProvider authenticationProvider;
    private final RedisRepository redisService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeHttpRequests()
                .requestMatchers(whiteList).permitAll()
                .requestMatchers( "/api/v1/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .authenticationProvider(authenticationProvider)
         .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
        .apply(new JwtSecurityConfig(tokenProvider, redisService)); // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig class 적용

        return http.build();
    }

    // CORS 허용 적용
    @Bean //--------- (2)
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost");
        configuration.addAllowedOrigin("http://localhost/");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://3.35.101.75");
        configuration.addAllowedOrigin("http://3.35.101.75/");
        configuration.addAllowedOrigin("http://3.35.101.75:8080");
        configuration.addAllowedOrigin("https://lucky-llama-7b0801.netlify.app");
        configuration.addAllowedOrigin("https://lucky-llama-7b0801.netlify.app/");
        configuration.addAllowedOrigin("https://lucky-llama-7b0801.netlify.app:443");
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setMaxAge(3600L);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}