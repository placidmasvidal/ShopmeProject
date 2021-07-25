package com.shopme.setting;

import com.shopme.common.entity.setting.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
public class SettingFilter implements Filter {

  private SettingService settingService;

  @Autowired
  public SettingFilter(SettingService settingService) {
    this.settingService = settingService;
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    String url = httpServletRequest.getRequestURL().toString();

    if (url.endsWith(".css")
        || url.endsWith(".js")
        || url.endsWith(".png")
        || url.endsWith(".jpg")) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }

    List<Setting> generalSettings = settingService.getGeneralSettings();
    generalSettings.forEach(
        setting -> {
          servletRequest.setAttribute(setting.getKey(), setting.getValue());
        });

    filterChain.doFilter(servletRequest, servletResponse);
  }
}
