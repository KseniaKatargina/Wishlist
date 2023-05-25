package ru.kpfu.itis.katargina.security;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http
                        .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .and()
                        .authorizeHttpRequests()
                        .requestMatchers( "/login", "/register").permitAll()
                        .requestMatchers("/profile", "/myWishlists", "/logout", "/addProduct/{prodID}/{listID}",
                                "/removeProduct/{prodID}/{listID}", "/products/filter",
                                "/filterProducts/{prodID}/{listID}", "/categories", "/",
                                "/mainPage","/editProfile","/addWishlist","/wishlist",
                                "/changeWishlist","removeWishlist", "/subscribe", "/subscriptions").authenticated()
                        .requestMatchers("/adminPage/**", "/products/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll()
                        .and().formLogin().loginPage("/login").defaultSuccessUrl("/mainPage").usernameParameter("email")
                        .passwordParameter("password")
                        .and().logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .and()
                        .build();
    }

    @Autowired
    public void bindUserDetailsServiceAndPasswordEncoder(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);
    }

}
