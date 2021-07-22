package com.shopme.customer.controller;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;
import com.shopme.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

  private CustomerService customerService;
  private SettingService settingService;

  @Autowired
  public ForgotPasswordController(CustomerService customerService, SettingService settingService) {
    this.customerService = customerService;
    this.settingService = settingService;
  }

  @GetMapping("/forgot_password")
  public String showRequestForm() {
    return "customer/forgot_password_form";
  }

  @PostMapping("/forgot_password")
  public String processRequestForm(HttpServletRequest servletRequest, Model model) {
    String email = servletRequest.getParameter("email");
    try {
      String token = customerService.updateResetPasswordToken(email);
      String link = Utility.getSiteURL(servletRequest) + "/reset_password?token=" + token;
      sendEmail(link, email);

      model.addAttribute(
          "message", "We have sent a reset password link to your email." + "\nPlease check it.");
    } catch (CustomerNotFoundException ex) {
      model.addAttribute("error", ex.getMessage());
    } catch (UnsupportedEncodingException | MessagingException ex) {
      model.addAttribute("error", "Could not send email.");
    }

    return "customer/forgot_password_form";
  }

  private void sendEmail(String link, String email)
      throws UnsupportedEncodingException, MessagingException {
    EmailSettingBag emailSettings = settingService.getEmailSettings();
    JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

    String toAddress = email;
    String subject = "Here is the link to reset your password";

    String content =
        "<p>Hello,</p>"
            + "<p>You have requested to reset your password.</p>"
            + "<p>Click the link below to change your password</p>"
            + "<p><a href=\""
            + link
            + "\">Change my password</a></p>"
            + "<br/>"
            + "<p>Ignore this email if you do remember your password, or you have not made the request.</p>";

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
    helper.setTo(toAddress);
    helper.setSubject(subject);

    helper.setText(content, true);

    mailSender.send(message);
  }

  @GetMapping("/reset_password")
  public String showResetForm(@Param("token") String token, Model model) {
    Customer customer = customerService.getByResetPasswordToken(token);
    if (customer != null) {
      model.addAttribute("token", token);
    } else {
      model.addAttribute("message", "Invalid Token");
      model.addAttribute("pageTitle", "Invalid Token");
      return "message";
    }
    return "customer/reset_password_form";
  }

  @PostMapping("/reset_password")
  public String processResetForm(HttpServletRequest servletRequest, Model model) {
    String token = servletRequest.getParameter("token");
    String password = servletRequest.getParameter("password");

    try {
      customerService.updatePassword(token, password);
      model.addAttribute("pageTitle", "Reset your password");
      model.addAttribute("message", "You have successfully changed your password.");
      model.addAttribute("title", "Reset your password");
      return "message";
    } catch (CustomerNotFoundException ex) {
      model.addAttribute("message", ex.getMessage());
      model.addAttribute("pageTitle", "Invalid Token");
      return "message";
    }
  }
}
