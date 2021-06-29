package com.shopme.admin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  private static final Logger LOG = LoggerFactory.getLogger(MvcConfig.class);

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    exposeDirectory("user-photos", registry);
    exposeDirectory("../category-images", registry);
    exposeDirectory("../brand-logos", registry);
    exposeDirectory("../product-images", registry);
    exposeDirectory("../site-logo", registry);

  }

  private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry){
    Path path = Paths.get(pathPattern);
    String absolutePath = path.toFile().getAbsolutePath();
    String logicalPath = pathPattern.replace("../", "") + "/**";

    LOG.info("Backend PATH: {}", path);
    LOG.info("Backend ABSOLUTE PATH: {}", absolutePath);
    LOG.info("Backend LOGICAL PATH: {}", logicalPath);

    registry.addResourceHandler(logicalPath)
    .addResourceLocations("file:" + absolutePath + "/");
  }
}
