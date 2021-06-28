package com.shopme.admin.setting;

import com.shopme.common.entity.Setting;
import org.springframework.data.repository.CrudRepository;

public interface SettingRepository extends CrudRepository<Setting, String> {

}
