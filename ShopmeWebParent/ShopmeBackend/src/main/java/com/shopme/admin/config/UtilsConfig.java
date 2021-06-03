package com.shopme.admin.config;

import com.shopme.admin.category.export.CategoryCsvExporter;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.admin.util.export.AbstractExporter;
import com.shopme.admin.util.export.ExporterFactory;
import com.shopme.admin.util.export.ExporterTypeEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfig {

  @Bean
  public UserCsvExporter getUserCsvExporter() {
    return (UserCsvExporter) ExporterFactory.createExporter(ExporterTypeEnum.USER_CSV);
  }

  @Bean
  public UserExcelExporter getUserExcelExporter() {
    return (UserExcelExporter) ExporterFactory.createExporter(ExporterTypeEnum.USER_EXCEL);
  }

  @Bean
  public UserPdfExporter getUserPdfExporter() {
    return (UserPdfExporter) ExporterFactory.createExporter(ExporterTypeEnum.USER_PDF);
  }

  @Bean
  public CategoryCsvExporter getCategoryCsvExporter() {
    return (CategoryCsvExporter) ExporterFactory.createExporter(ExporterTypeEnum.CAT_CSV);
  }
}
