package com.dhiraj.canteen.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration{

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager= new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("select email, password, active from users where email=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select email, role from roles where email=?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests(configurer->
                        configurer
                                .requestMatchers("/","/shop/**","/register","/forgotpassword").permitAll()
                                .requestMatchers("/admin","/admin/**").hasRole("Admin")
                                .anyRequest().authenticated()
                )
                .formLogin(form-> form.loginPage("/login").loginProcessingUrl("/authenticate").permitAll()
                        .failureUrl("/login?error=true")
                  //      .defaultSuccessUrl("/")
                        .usernameParameter("email")
                        .passwordParameter("password")
                )

                .logout(logout->
                        logout
                                .permitAll()
                                .logoutSuccessUrl("/")
                )
                .logout(logout->
                        logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                )

                .exceptionHandling(configurer-> configurer.accessDeniedPage("/access-denied"))

        ;

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**","/templates/**", "/static/**","/images/**","/productImages/**","/css/**","/js/**");
    }

}
