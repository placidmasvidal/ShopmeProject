package com.shopme.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

  private SettingRepository settingRepository;

  @Autowired
  public SettingServiceImpl(SettingRepository settingRepository) {
    this.settingRepository = settingRepository;
  }

  @Override
  public List<Setting> getGeneralSettings() {
    return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
  }
}
