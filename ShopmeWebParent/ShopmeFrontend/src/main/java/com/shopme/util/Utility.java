package com.shopme.util;

import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

public class Utility {

  public static String getSiteURL(HttpServletRequest request) {
    String siteURL = request.getRequestURL().toString();
    return siteURL.replace(request.getServletPath(), "");
  }

  public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(settings.getHost());
    mailSender.setPort(settings.getPort());
    mailSender.setUsername(settings.getUsername());
    mailSender.setPassword(settings.getPassword());

    Properties mailProperties = new Properties();

    mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
    mailProperties.setProperty("mail.smtp.ssl.enable", settings.getSmtpSecured());

    mailSender.setJavaMailProperties(mailProperties);

    return mailSender;
  }

  public static String getEmailOfAuthenticatedCustomer(HttpServletRequest servletRequest) {
    Principal principal = servletRequest.getUserPrincipal();

    if (principal == null) {
      return null;
    }

    String customerEmail = null;

    if (principal instanceof UsernamePasswordAuthenticationToken
        || principal instanceof RememberMeAuthenticationToken) {
      customerEmail = servletRequest.getUserPrincipal().getName();
    } else if (principal instanceof OAuth2AuthenticationToken) {
      OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
      CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
      customerEmail = oAuth2User.getEmail();
    }

    return customerEmail;
  }

  public static String formatCurrency(float amount, CurrencySettingBag currencySettings) {
    String symbol = currencySettings.getSymbol();
    String symbolPosition = currencySettings.getSymbolPosition();
    int decimalDigits = currencySettings.getDecimalDigits();

    StringBuilder sb = new StringBuilder();
    sb.append(symbolPosition.equals("Before price") ? symbol : "");
    sb.append("###,###");
    if (decimalDigits > 0) {
      sb.append(".");
      for (int count = 1; count <= decimalDigits; count++) sb.append("#");
    }
    sb.append(symbolPosition.equals("After price") ? symbol : "");

    String pattern = sb.toString();

    DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
    decimalFormatSymbols.setDecimalSeparator(
        (currencySettings.getDecimalPointType().equals("POINT")) ? '.' : ',');
    decimalFormatSymbols.setGroupingSeparator(
        (currencySettings.getThousandPointType().equals("POINT")) ? '.' : ',');

    return new DecimalFormat(pattern, decimalFormatSymbols).format(amount);
  }
}
