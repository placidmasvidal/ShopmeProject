package com.shopme.security;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private CustomerOAuth2UserService oAuth2UserService;

  @Autowired
  public WebSecurityConfig(CustomerOAuth2UserService oAuth2UserService) {
    this.oAuth2UserService = oAuth2UserService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/customer")
        .authenticated()
        .anyRequest()
        .permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .usernameParameter("email")
        .permitAll()
        .and()
        .oauth2Login()
        .loginPage("/login")
        .userInfoEndpoint()
        .userService(oAuth2UserService)
        .and()
        .and()
        .logout()
        .permitAll()
        .and()
        .rememberMe()
        .key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
        .tokenValiditySeconds(14 * 24 * 60 * 60);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomerUserDetailsService();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());

    return authenticationProvider;
  }
}
