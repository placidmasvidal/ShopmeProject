package com.shopme.security;

import com.shopme.customer.CustomerService;
import com.shopme.customer.CustomerServiceImpl;
import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;
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

  private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

  private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;

  @Autowired
  public WebSecurityConfig(
      CustomerOAuth2UserService oAuth2UserService,
      OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
      DatabaseLoginSuccessHandler databaseLoginSuccessHandler) {
    this.oAuth2UserService = oAuth2UserService;
    this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    this.databaseLoginSuccessHandler = databaseLoginSuccessHandler;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/account_details", "/update_account_details", "/cart", "/address_book/**")
        .authenticated()
        .anyRequest()
        .permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .usernameParameter("email")
        .successHandler(databaseLoginSuccessHandler)
        .permitAll()
        .and()
        .oauth2Login()
        .loginPage("/login")
        .userInfoEndpoint()
        .userService(oAuth2UserService)
        .and()
        .successHandler(oAuth2LoginSuccessHandler)
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
}
