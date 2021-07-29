package com.shopme.setting;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

  private SettingRepository settingRepository;
;
  @Autowired
  public SettingServiceImpl(SettingRepository settingRepository) {
    this.settingRepository = settingRepository;
  }

  @Override
  public List<Setting> getGeneralSettings() {
    return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
  }

  @Override
  public EmailSettingBag getEmailSettings() {
    List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));

    return new EmailSettingBag(settings);
  }

    @Override
    public CurrencySettingBag getCurrencySettings() {
      List<Setting> settings = settingRepository.findByCategory(SettingCategory.CURRENCY);
      return new CurrencySettingBag(settings);
    }
}
