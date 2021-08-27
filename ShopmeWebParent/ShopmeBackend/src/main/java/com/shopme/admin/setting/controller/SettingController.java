package com.shopme.admin.setting.controller;

import com.shopme.admin.setting.CurrencyRepository;
import com.shopme.admin.setting.GeneralSettingBag;
import com.shopme.admin.setting.SettingService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class SettingController {

  private SettingService settingService;
  private CurrencyRepository currencyRepository;

  @Autowired
  public SettingController(SettingService settingService, CurrencyRepository currencyRepository) {
    this.settingService = settingService;
    this.currencyRepository = currencyRepository;
  }

  @GetMapping("/settings")
  public String listAll(Model model) {
    List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();
    model.addAttribute("listCurrencies", listCurrencies);

    List<Setting> listSettings = settingService.listAllSettings();
    listSettings.forEach(setting -> model.addAttribute(setting.getKey(), setting.getValue()));

    return "settings/settings";
  }

  @PostMapping("/settings/save_general")
  public String saveGeneralSettings(
      @RequestParam("fileImage") MultipartFile multipartFile,
      HttpServletRequest request,
      RedirectAttributes redirectAttributes)
      throws IOException {

    GeneralSettingBag settingBag = settingService.getGeneralSettings();

    saveSiteLogo(multipartFile, settingBag);
    saveCurrencySymbol(request, settingBag);

    updateSettingValuesFromForm(request, settingBag.list());

    redirectAttributes.addFlashAttribute("message", "General Settings have been saved.");

    return "redirect:/settings";
  }

  private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag)
      throws IOException {
    if (!multipartFile.isEmpty()) {
      String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
      String value = "/site-logo/" + fileName;
      settingBag.updateSiteLogo(value);
      String uploadDir = "../site-logo/";
      FileUploadUtil.cleanDir(uploadDir);
      FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }
  }

  private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
    Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
    Optional<Currency> findByIdResult = currencyRepository.findById(currencyId);

    if (findByIdResult.isPresent()) {
      Currency currency = findByIdResult.get();
      settingBag.updateCurrencySymbol(currency.getSymbol());
    }
  }

  private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
    listSettings.forEach(
        setting -> {
          String value = request.getParameter(setting.getKey());
          if (value != null) setting.setValue(value);
        });

    settingService.saveAll(listSettings);
  }

  @PostMapping("/settings/save_mail_server")
  public String saveMailServerSettings(
      HttpServletRequest request, RedirectAttributes redirectAttributes) {

    List<Setting> mailServerSettings = settingService.getMailServerSettings();
    updateSettingValuesFromForm(request, mailServerSettings);

    redirectAttributes.addFlashAttribute("message", "Mail server settings have been saved");

    return "redirect:/settings#mailServer";
  }

  @PostMapping("/settings/save_mail_templates")
  public String saveMailTemplateSettings(
          HttpServletRequest request, RedirectAttributes redirectAttributes) {

    List<Setting> mailTemplateSettings = settingService.getMailTemplateSettings();
    updateSettingValuesFromForm(request, mailTemplateSettings);

    redirectAttributes.addFlashAttribute("message", "Mail template settings have been saved");

    return "redirect:/settings#mailTemplates";
  }

  @PostMapping("/settings/save_payment")
  public String savePaymentSettings(HttpServletRequest servletRequest, RedirectAttributes redirectAttributes) {
    List<Setting> paymentSettings = settingService.getPaymentSettings();
    updateSettingValuesFromForm(servletRequest, paymentSettings);

    redirectAttributes.addFlashAttribute("message", "Payment settings have been saved");

    return "redirect:/settings#payment";
  }
}
