package com.jiaozx.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName RestConfiguration
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/1 18:37
 * @Version 1.0
 */
@Configuration
public class RestConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
