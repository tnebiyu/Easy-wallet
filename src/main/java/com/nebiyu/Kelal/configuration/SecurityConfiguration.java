package com.nebiyu.Kelal.configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationProvider; // Ensure the correct import for AuthenticationProvider
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration  {
    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider; // Inject the AuthenticationProvider bean

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {
        http.csrf().disable().authorizeHttpRequests()
                .requestMatchers("/api/v1/**").permitAll()
                .anyRequest().authenticated().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

//    @Bean
//    protected void Configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().authorizeHttpRequests()
//                .requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().permitAll()
//
//                .and()
//                .httpBasic();
//    }
//}
