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
                                // Syllabus, Learning Objective
                                .antMatchers(HttpMethod.POST, "/syllabus/**", "/objective/**")
                                .hasAnyAuthority("SYLLABUS_FULL_ACCESS", "SYLLABUS_CREATE")
                                .antMatchers(HttpMethod.PUT, "/syllabus/**", "/objective/**")
                                .hasAnyAuthority("SYLLABUS_FULL_ACCESS", "SYLLABUS_CREATE", "SYLLABUS_MODIFY")
                                .antMatchers(HttpMethod.GET, "/syllabus/**", "/objective/**")
                                .hasAnyAuthority("SYLLABUS_FULL_ACCESS", "SYLLABUS_VIEW",
                                        "SYLLABUS_MODIFY", "SYLLABUS_CREATE")
                                // Training Program
                                .antMatchers(HttpMethod.POST, "/program/**")
                                .hasAnyAuthority("TRAININGPROGRAM_FULL_ACCESS", "TRAININGPROGRAM_CREATE")
                                .antMatchers(HttpMethod.PUT, "/program/**")
                                .hasAnyAuthority("TRAININGPROGRAM_FULL_ACCESS", "TRAININGPROGRAM_CREATE", "TRAININGPROGRAM_MODIFY")
                                .antMatchers(HttpMethod.GET, "/program/**")
                                .hasAnyAuthority("TRAININGPROGRAM_FULL_ACCESS", "TRAININGPROGRAM_CREATE",
                                        "TRAININGPROGRAM_MODIFY", "TRAININGPROGRAM_VIEW")
                                // Training Material
                                .antMatchers(HttpMethod.POST, "/material/**")
                                .hasAnyAuthority("LEARNINGMATERIAL_FULL_ACCESS", "LEARNINGMATERIAL_CREATE")
                                .antMatchers(HttpMethod.DELETE, "/material/**")
                                .hasAnyAuthority("LEARNINGMATERIAL_FULL_ACCESS")
                                // User Management
                                .antMatchers(HttpMethod.PUT, "/user/**")
                                .hasAnyAuthority("USERMANAGEMENT_FULL_ACCESS", "USERMANAGEMENT_CREATE", "USERMANAGEMENT_MODIFY")
                                .antMatchers(HttpMethod.GET, "/user/**")
                                .hasAnyAuthority("USERMANAGEMENT_FULL_ACCESS", "USERMANAGEMENT_CREATE",
                                        "USERMANAGEMENT_MODIFY", "USERMANAGEMENT_VIEW")
                                // User Permission
                                .antMatchers(HttpMethod.PUT, "/permission/**")
                                .hasAnyAuthority("USERMANAGEMENT_FULL_ACCESS", "USERMANAGEMENT_CREATE",
                                        "USERMANAGEMENT_MODIFY")
                                .antMatchers(HttpMethod.GET, "/permission/**")
                                .hasAnyAuthority("USERMANAGEMENT_FULL_ACCESS", "USERMANAGEMENT_CREATE",
                                        "USERMANAGEMENT_MODIFY", "USERMANAGEMENT_VIEW")
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
