package com.shopme.admin.config;

import com.shopme.admin.util.CsvExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfig {

    @Bean
    public CsvExporter getCsvExporter(){
        return new CsvExporter();
    }
}
