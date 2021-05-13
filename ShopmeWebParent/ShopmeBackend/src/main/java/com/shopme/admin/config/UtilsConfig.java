package com.shopme.admin.config;

import com.shopme.admin.user.UserCsvExporter;
import com.shopme.admin.user.UserExcelExporter;
import com.shopme.admin.user.UserPdfExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfig {

    @Bean
    public UserCsvExporter getUserCsvExporter(){
        return new UserCsvExporter();
    }

    @Bean
    public UserExcelExporter getUserExcelExporter(){
        return new UserExcelExporter();
    }

    @Bean
    public UserPdfExporter getUserPdfExporter(){
        return new UserPdfExporter();
    }
}
