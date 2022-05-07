package com.github.gilvangobbato.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public record AwsSqsProperties(String endpoint) {
}
