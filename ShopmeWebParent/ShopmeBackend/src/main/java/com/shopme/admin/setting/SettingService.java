package com.shopme.admin.setting;

import com.shopme.common.entity.Setting;

import java.util.List;

public interface SettingService {

    List<Setting> listAllSettings();

    GeneralSettingBag getGeneralSettings();

    void saveAll(Iterable<Setting> settings);

    List<Setting> getMailServerSettings();

    List<Setting> getMailTemplateSettings();

    List<Setting> getCurrencySettings();
}
