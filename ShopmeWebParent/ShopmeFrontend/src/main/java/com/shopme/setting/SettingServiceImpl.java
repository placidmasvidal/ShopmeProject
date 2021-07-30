package com.shopme.setting;

import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

  private SettingRepository settingRepository;
  private CurrencyRepository currencyRepository;

  @Autowired
  public SettingServiceImpl(SettingRepository settingRepository, CurrencyRepository currencyRepository) {
    this.settingRepository = settingRepository;
    this.currencyRepository = currencyRepository;
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

    @Override
    public PaymentSettingBag getPaymentSettings() {
      List<Setting> settings = settingRepository.findByCategory(SettingCategory.PAYMENT);
        return new PaymentSettingBag(settings);
    }

  @Override
  public String getCurrencyCode() {
    Setting setting = settingRepository.findByKey("CURRENCY_ID");
    Currency currency = currencyRepository.findById(Integer.valueOf(setting.getValue())).get();
    return currency.getCode();
  }
}
