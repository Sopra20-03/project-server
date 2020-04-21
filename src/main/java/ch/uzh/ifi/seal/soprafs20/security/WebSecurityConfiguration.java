package ch.uzh.ifi.seal.soprafs20.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Web Security Configuration Class for Spring Security
 * Enable Security for authentication later.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthSuccessHandler authSuccessHandler;

    @Autowired
    LogoutHandler logoutHandler;

    @Autowired
    AuthFailureHandler authFailureHandler;

    /**
     * This method configures Spring security to allow unauthorized REST calls.
     * @param http SpringSecurity
     * @throws Exception
     */
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
                .antMatchers(HttpMethod.POST,"/**").permitAll()
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
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
*/
}
