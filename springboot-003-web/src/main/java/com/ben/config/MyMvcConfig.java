package com.ben.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

// extends springmvc
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Bean
    public ViewResolver myViewResolver() {
        return new MyViewResolver();
    }
    // 自定义了一个自己的视图解析器
    public static class MyViewResolver implements ViewResolver {

        @Override
        public View resolveViewName(String s, Locale locale) throws Exception {

            return null;
        }
    }
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/liang").setViewName("test");
    }
}
