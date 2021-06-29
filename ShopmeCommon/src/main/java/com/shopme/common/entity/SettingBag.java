package com.shopme.common.entity;

import java.util.List;

public class SettingBag {

    private List<Setting> listSettings;

    public SettingBag(List<Setting> listSettings) {
        this.listSettings = listSettings;
    }

    public Setting get(String key){
        int index = listSettings.indexOf(new Setting(key));
        return (index >= 0) ? listSettings.get(index) : null;
    }

    public String getValue(String key){
        Setting setting = get(key);
        return (setting != null) ? setting.getValue() : null;
    }

    public void update(String key, String value){
        Setting setting = get(key);
        if(setting != null && value != null){
            setting.setValue(value);
        }
    }

    public List<Setting> list(){
        return listSettings;
    }
}
