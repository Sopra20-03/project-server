package ch.uzh.ifi.seal.soprafs20.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Web Security Configuration Class for Spring Security
 * Enalble Security for authentication later.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/h2","/h2/**").permitAll()
                .and()
                .csrf().disable()
                .headers().frameOptions().sameOrigin();
    }

    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/users","/users/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/h2","/h2/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll().successHandler(authSuccessHandler).failureHandler(authFailureHandler)
                .and()
                .csrf().disable()
                .headers().frameOptions().sameOrigin()
                .and()
                .logout().logoutSuccessHandler(logoutHandler)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
     */
}
