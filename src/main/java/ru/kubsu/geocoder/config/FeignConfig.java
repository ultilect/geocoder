package ru.kubsu.geocoder.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author Bogdan Lesin
 */
@EnableFeignClients(basePackages = "ru.kubsu.geocoder")
@Configuration
public class FeignConfig {
}
