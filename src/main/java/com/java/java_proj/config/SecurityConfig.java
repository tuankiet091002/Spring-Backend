package com.java.java_proj.config;

import com.java.java_proj.config.filter.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final String[] swaggerAntPatterns = {"/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
            "/configuration/security", "/swagger-ui.html", "/webjars/**"};


    @Bean
    public JwtTokenFilter authenticationJwtTokenFilter() {
        return new JwtTokenFilter();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                        configurer -> configurer
                                .antMatchers(HttpMethod.POST, "/user/login", "user/register").permitAll()
                                // User Management
                                .antMatchers(HttpMethod.PUT, "/user/**")
                                .hasAnyAuthority("USER_FULL_ACCESS", "USER_CREATE", "USER_MODIFY")
                                .antMatchers(HttpMethod.GET, "/user/**")
                                .hasAnyAuthority("USER_FULL_ACCESS", "USER_CREATE",
                                        "USER_MODIFY", "USER_VIEW")
                                // User Permission
                                .antMatchers(HttpMethod.PUT, "/permission/**")
                                .hasAnyAuthority("USER_FULL_ACCESS", "USER_CREATE",
                                        "USER_MODIFY")
                                .antMatchers(HttpMethod.GET, "/permission/**")
                                .hasAnyAuthority("USER_FULL_ACCESS", "USER_CREATE",
                                        "USER_MODIFY", "USER_VIEW")
                                .anyRequest().permitAll()
                        // 25
                )
                .csrf().disable()
                .httpBasic()
                .and()
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
