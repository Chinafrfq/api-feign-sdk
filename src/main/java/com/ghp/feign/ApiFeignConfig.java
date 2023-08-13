package com.ghp.feign;

import com.ghp.feign.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ghp
 * @title
 * @description
 */
@Configuration
@ConfigurationProperties("api.feign")
@ComponentScan // 需要扫描当前项目中其他的的类，因为这里没有使用SpringApplication注解
@Data
public class ApiFeignConfig {
    private String accessKey;

    private String secretKey;

    /**
     * 将ApiFeignConfig对象注入IOC容器
     *
     * @return
     */
    @Bean
    public ApiClient apiClient() {
        return new ApiClient(accessKey, secretKey);
    }
}
