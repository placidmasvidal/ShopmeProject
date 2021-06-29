package com.shopme.admin.setting.controller;

import com.shopme.admin.setting.CurrencyRepository;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
  public String listAll(Model model){
    List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();
    model.addAttribute("listCurrencies", listCurrencies);

    List<Setting> listSettings = settingService.listAllSettings();
    listSettings.forEach(setting -> model.addAttribute(setting.getKey(), setting.getValue()));

    return "settings/settings";
  }


}
