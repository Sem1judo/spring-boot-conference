package com.ua.semkov.conferenceSpringFinal.config;

import com.ua.semkov.conferenceSpringFinal.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;


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
                .antMatchers("/sign-up/**", "/sign-in/**")
                .permitAll()
                .antMatchers("/css/**", "/js/**", "/images/**")
                .permitAll()
                //Все остальные страницы требуют аутентификации
                .anyRequest()
                .authenticated()
                .and()
                //Настройка для входа в систему
                .formLogin()
                .loginPage("/sign-in")
                //Перенарпавление на главную страницу после успешного входа
                .defaultSuccessUrl("/events")
                .failureUrl("/login.html?error=true")
                //.failureHandler(authenticationFailureHandler())
                // .and()
                // .logout()
                //.logoutUrl("/perform_logout")
                //.deleteCookies("JSESSIONID")
                //.logoutSuccessHandler(logoutSuccessHandler());
                .permitAll();
    }

    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("judo1stss1@gmail.com").password(bCryptPasswordEncoder.encode("12345")).roles("CLIENT")
                .and()
                .withUser("email@mail.com").password(bCryptPasswordEncoder.encode("12345")).roles("MODERATOR");

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

}
