package com.pravin.Spring_Security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    UserDetailsService userDetailsService;

    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return provider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http.csrf(customizer->customizer.disable());
        http.authorizeHttpRequests(request->request.anyRequest().authenticated());
        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));




        /*
          Customizer<CsrfConfigurer<HttpSecurity>> custCsrf = new
          Customizer<CsrfConfigurer<HttpSecurity>>() {

          @Override public void customize(CsrfConfigurer<HttpSecurity> configurer) {

          configurer.disable(); } };

          Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.
          AuthorizationManagerRequestMatcherRegistry> custHttp = new
          Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.
          AuthorizationManagerRequestMatcherRegistry>() {

          @Override public void customize(
          AuthorizeHttpRequestsConfigurer<HttpSecurity>.
          AuthorizationManagerRequestMatcherRegistry registry) {
          registry.anyRequest().authenticated();
         } };

         http.authorizeHttpRequests(custHttp); http.csrf(custCsrf);

         */

        return http.build();
    }

    /*
    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user = User
                            .withDefaultPasswordEncoder()
                            .username("pravin")
                            .password("123")
                            .roles("USER")
                            .build();


        UserDetails admin = User
                .withDefaultPasswordEncoder()
                .username("admin")
                .password("1223")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user,admin);
    }

     */

}
