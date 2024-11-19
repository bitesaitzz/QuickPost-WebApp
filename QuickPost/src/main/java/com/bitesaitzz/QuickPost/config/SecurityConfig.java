package com.bitesaitzz.QuickPost.config;


import com.bitesaitzz.QuickPost.security.SessionCheckFilter;
import com.bitesaitzz.QuickPost.services.PersonDetailsService;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.OAuth2ClientDsl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig implements WebMvcConfigurer {

    private final PersonDetailsService personDetailsService;
    private final DataSource dataSource;

    public SecurityConfig(PersonDetailsService personDetailsService, DataSource dataSource) {
        this.personDetailsService = personDetailsService;
        this.dataSource = dataSource;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                //addFilterBefore(new SessionCheckFilter(), UsernamePasswordAuthenticationFilter.class).
                authorizeRequests(authorize -> authorize
                        .requestMatchers("/admin").hasAnyRole("ADMIN")
                        .requestMatchers("/auth/login", "/error", "/auth/registration").permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN", "MODERATOR")).
                formLogin(form -> form.
                        loginPage("/auth/login").
                        defaultSuccessUrl("/messages", true)).
                logout(logout -> logout.
                        logoutUrl("/auth/logout").
                        logoutSuccessUrl("/auth/login")).
                rememberMe(rememberMe -> rememberMe
                                .tokenRepository(persistentTokenRepository())
                                .tokenValiditySeconds(3600) // 1 hour
                                .userDetailsService(personDetailsService)
                        );
        return http.build();

    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(personDetailsService);
        provider.setPasswordEncoder(getPasswordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
