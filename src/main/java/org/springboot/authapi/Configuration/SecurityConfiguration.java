package org.springboot.authapi.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired private AuthenticationProvider authenticationProvider;

    @Autowired private  JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
                    auth.requestMatchers("/auth/**").permitAll();
                    auth.requestMatchers("/api/v1/**").permitAll();
                    auth.requestMatchers("/swagger-ui.html").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration=new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}

//The authorizeHttpRequests() method in Spring Security is used to configure access control for different
//HTTP endpoints in your application. It determines which requests are allowed or denied based on authentication
//and authorization rules.

//----------------------------------------------sessionManagement-----------------------------------
//1-sessionCreationPolicy(SessionCreationPolicy.STATELESS):No session is created. Good for REST APIs.(Recommended for REST APIs)
//2-sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED):Creates a session only if required (default behavior).
//3-sessionCreationPolicy(SessionCreationPolicy.ALWAYS):Always creates a session, even if not required.
//4-sessionCreationPolicy(SessionCreationPolicy.NEVER):Spring Security will never create a session, but it will use one if it exists.
//5-invalidSessionUrl("/session-expired"):Redirects the user when an invalid session is detected.
//6-maximumSessions(1).maxSessionsPreventsLogin(true):Limits the number of concurrent sessions per user.
//-------------------------------------Purpose of this class------------------------------------------
//The custom authentication is ready, and the remaining thing is to define what criteria an incoming request
//must match before being forwarded to application middleware. We want the following criteria:

//------------------setAllowedOrigins(List.of("http://localhost:8080"))
//1-This means only http://localhost:8080 can access your API.
//2-If your frontend runs on a different port (e.g., http://localhost:3000 for Nuxt.js), this will block it.
//3-Solution: configuration.setAllowedOrigins(List.of("http://localhost:3000")); or configuration.setAllowedOrigins(List.of("*"));
//-------------------If CORS still does not work, add this to application.properties
//spring.web.cors.allowed-origins=http://localhost:3000

//registerCorsConfiguration(String path, CorsConfiguration config) is used to apply the configuration to specific endpoints.
//UrlBasedCorsConfigurationSource is used to map CORS rules to specific API endpoints
//registerCorsConfiguration("/**", config); applies CORS rules globally.

//In Spring Boot, CORS is enforced on all endpoints when we register a CorsConfiguration with the pattern /**.
// This means that every API route in the application will follow the defined CORS rules.

//--------------------------------------------------------6---------------------------------------------