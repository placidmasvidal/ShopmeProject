package com.shopme.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(MvcConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        exposeDirectory("../category-images", registry);
        exposeDirectory("../brand-logos", registry);
        exposeDirectory("../product-images", registry);

    }

//  Backend ABSOLUTE PATH: /home/placidmasvidal/git/repositories/ShopmeProject/ShopmeWebParent/ShopmeBackend/../category-images

    private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry){
        Path path = Paths.get(pathPattern);
        String absolutePath = path.toFile().getAbsolutePath();
        String logicalPath = pathPattern.replace("../", "") + "/**";

        LOG.info("Frontend PATH: {}", path);
        LOG.info("Frontend ABSOLUTE PATH: {}", absolutePath);
        LOG.info("Frontend LOGICAL PATH: {}", logicalPath);

        registry.addResourceHandler(logicalPath)
                .addResourceLocations("file:" + absolutePath + "/");
    }
}
