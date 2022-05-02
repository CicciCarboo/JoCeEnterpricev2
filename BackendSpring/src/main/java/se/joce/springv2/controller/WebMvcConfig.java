package se.joce.springv2.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequestMapping("/")
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //this will map uri to jsp view directly without a controller
        registry.addViewController("/about").setViewName("about");
//        registry.addViewController("/login").setViewName("login");
    }

}
