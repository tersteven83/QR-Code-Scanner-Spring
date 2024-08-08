package com.pcop.qrcode_scanner;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/qr_codes/**")
                .addResourceLocations("file:./src/main/resources/static/qr_codes/");
        registry.addResourceHandler("/static/uploads/**")
                .addResourceLocations("file:./src/main/resources/static/uploads/");
    }
}
