package com.example.vse_site.Configuration;

import com.example.vse_site.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{
    @Autowired
    private MyUserDetailService userDetailService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/unverified").hasRole("UNVERIFIED");
                    registry.requestMatchers("/home", "/register/**", "/loginform", "/registration/**", "/verify/**", "/facultyCounts", "/scoreboard", "/gridColors", "/about").permitAll();
                    registry.requestMatchers("/banned", "/logout").hasRole("BANNED");
                    registry.requestMatchers("/admin/**", "/logout", "/gridColorsUsers").hasRole("ADMIN");
                    registry.requestMatchers("/user/**", "/logout").hasRole("USER");
                    registry.anyRequest().authenticated();

                })

                //.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .formLogin(form -> form
                        .loginPage("/loginform") // Specify the custom login page
                        .loginProcessingUrl("/login") // Specify the login processing URL
                        .successHandler(successHandler()) // Handle successful login
                        .failureHandler(failureHandler()) // Handle login failure
                        .permitAll() // Allow access to the login form
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return  userDetailService;
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public SimpleUrlAuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            protected void handle(jakarta.servlet.http.HttpServletRequest request,
                                  jakarta.servlet.http.HttpServletResponse response,
                                  org.springframework.security.core.Authentication authentication) throws java.io.IOException {
                //změnit generate token -> claims in token only role nad user id
                String name = authentication.getName();
                String role = authentication.getAuthorities().iterator().next().getAuthority();
//                String token = util.generateToken(name, role);
//                System.out.println(token);
//
//                // Add the token to the response header
//                response.addHeader("Authorization", "Bearer " + token);
//
//                // Optionally, set the token in a cookie
//                jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("JWT", token);
//                cookie.setHttpOnly(true);
//                cookie.setPath("/");
//                response.addCookie(cookie);
                //if verify fun -> user. is enabled is false -> tak user se nedostane na home ale na jinou str
                if (role.equals("ROLE_ADMIN")) {
                    getRedirectStrategy().sendRedirect(request, response, "/admin/home");
                } else if (role.equals("ROLE_UNVERIFIED")) {
                    getRedirectStrategy().sendRedirect(request, response, "/unverified");
                } else if (role.equals("ROLE_BANNED")) {
                    getRedirectStrategy().sendRedirect(request, response, "/banned");
                } else {
                    getRedirectStrategy().sendRedirect(request, response, "/user/home");
                }
            }
        };
    }
    @Bean
    public SimpleUrlAuthenticationFailureHandler failureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
    }

}
