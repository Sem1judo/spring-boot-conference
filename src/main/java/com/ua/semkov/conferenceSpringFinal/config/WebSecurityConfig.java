package com.ua.semkov.conferenceSpringFinal.config;

import com.ua.semkov.conferenceSpringFinal.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * @author Semkov.S
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                //Доступ разрешен всем пользователей
                .antMatchers("/create/**","/moderator/**").hasRole("MODERATOR")
                .antMatchers("createTopic/**","/update/**").hasAnyRole("MODERATOR", "SPEAKER")
                .antMatchers("/sign-up/**", "/sign-in/**")
                .permitAll()
                .antMatchers("/css/**", "/js/**", "/images/**")
                .permitAll()
                //Все остальные страницы требуют аутентификации
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/sign-in")
                .defaultSuccessUrl("/events")
                .failureUrl("/login.html?error=true")
                //.failureHandler(authenticationFailureHandler())
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("client@gmail.com").password(bCryptPasswordEncoder.encode("12345")).roles("CLIENT")
                .and()
                .withUser("email@mail.com").password(bCryptPasswordEncoder.encode("12345")).roles("MODERATOR")
                .and()
                .withUser("speaker@gmail.com").password(bCryptPasswordEncoder.encode("12345")).roles("SPEAKER");

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

}
