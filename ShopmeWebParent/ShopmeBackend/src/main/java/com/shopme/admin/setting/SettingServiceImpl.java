package com.shopme.admin.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

  private SettingRepository settingRepository;

  @Autowired
  public SettingServiceImpl(SettingRepository settingRepository, PasswordEncoder passwordEncoder) {
    this.settingRepository = settingRepository;
  }

  @Override
  public List<Setting> listAllSettings() {
    return (List<Setting>) settingRepository.findAll();
  }

  @Override
  public GeneralSettingBag getGeneralSettings() {
    List<Setting> settings = new ArrayList<>();

    List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
    List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);

    settings.addAll(generalSettings);
    settings.addAll(currencySettings);

    return new GeneralSettingBag(settings);
  }

  @Override
  public void saveAll(Iterable<Setting> settings) {
    settingRepository.saveAll(settings);
  }

  @Override
  public List<Setting> getMailServerSettings() {
    return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
  }

  @Override
  public List<Setting> getMailTemplateSettings() {
    return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
  }

}
