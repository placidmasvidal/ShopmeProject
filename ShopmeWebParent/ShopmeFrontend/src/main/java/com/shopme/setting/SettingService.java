package com.shopme.setting;

import com.shopme.common.entity.setting.Setting;

import java.util.List;

public interface SettingService {

    List<Setting> getGeneralSettings();

    EmailSettingBag getEmailSettings();

}
