package com.devactivity.config;

import com.devactivity.user.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOauth2UserService customOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable();

        http
                .authorizeRequests()
                .mvcMatchers("/", "/h2-console/*").permitAll()
                .mvcMatchers("/user").hasAnyAuthority("ROLE_USER")
                .mvcMatchers("/admin").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
        ;

        http
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOauth2UserService);

        http
                .logout()
                .logoutSuccessUrl("/");
    }
}
