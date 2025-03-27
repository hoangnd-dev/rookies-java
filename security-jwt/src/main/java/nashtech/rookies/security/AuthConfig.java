package nashtech.rookies.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import nashtech.rookies.security.jwt.JwtAuthenticationFilter;
import nashtech.rookies.security.jwt.TokenProvider;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthConfig {

    @Bean
    JwtAuthenticationFilter authFilter (TokenProvider tokenProvider, UserDetailsService userService) {
        return new JwtAuthenticationFilter(tokenProvider, userService);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider (UserDetailsService userService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder());
        authProvider.setUserDetailsService(userService);
        return authProvider;
    }

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement()
                                 .addList(securitySchemeName))
            .components(new Components()
                            .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity,
                                             JwtAuthenticationFilter authFilter) throws Exception {
        return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/swagger-ui.html"
                    ,"/swagger-ui/**"
                    ,"/api-docs/**"
                ).permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/auth/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/admin").hasRole("ADMIN")
                .anyRequest().authenticated())
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    AuthenticationManager authenticationManagerJwt (AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
}