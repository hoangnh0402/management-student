package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().configurationSource(request -> corsConfiguration())
            .and().csrf().disable()
            .authorizeRequests()
            .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Cho phép truy cập Swagger UI và API docs
            .antMatchers(HttpMethod.GET, "/public/**").permitAll() // Ví dụ: cho phép GET requests đến /public/**
            .anyRequest().authenticated() // Chỉ cho phép các yêu cầu còn lại được xác thực
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }


  CorsConfiguration corsConfiguration() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://your-frontend-domain.com"));
    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
    corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    return corsConfiguration;
  }

  @Bean
  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

}