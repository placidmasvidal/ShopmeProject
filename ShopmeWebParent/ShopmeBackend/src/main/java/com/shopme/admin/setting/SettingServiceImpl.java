package com.shopme.admin.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingServiceImpl implements SettingService{

    private SettingRepository settingRepository;

    @Autowired
    public SettingServiceImpl(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
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
