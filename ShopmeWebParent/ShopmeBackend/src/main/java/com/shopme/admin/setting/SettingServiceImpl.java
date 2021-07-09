package com.shopme.admin.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

  private SettingRepository settingRepository;

  private PasswordEncoder passwordEncoder;

  @Autowired
  public SettingServiceImpl(SettingRepository settingRepository, PasswordEncoder passwordEncoder) {
    this.settingRepository = settingRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public List<Setting> listAllSettings() {
    return (List<Setting>) settingRepository.findAll();
  }

  @Override
  public GeneralSettingBag getGeneralSettings() {
    List<Setting> settings = new ArrayList<>();
    settings.addAll(settingRepository.findByCategory(SettingCategory.GENERAL));
    settings.addAll(settingRepository.findByCategory(SettingCategory.CURRENCY));

    return new GeneralSettingBag(settings);
  }

  @Override
  public void saveAll(Iterable<Setting> settings) {
    settings.forEach(
        setting -> {
          if (setting.getCategory().equals(SettingCategory.MAIL_SERVER)) {
            encodePassword(setting);
          }
        });
    settingRepository.saveAll(settings);
  }

  private void encodePassword(Setting setting) {
    if (setting.getKey().equals("MAIL_PASSWORD")) {
      String encodedPassword = passwordEncoder.encode(setting.getValue());
      setting.setValue(encodedPassword);
    }
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
