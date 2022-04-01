package com.github.gilvangobbato.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public record AwsProperties(String endpoint,
                            String region) {
}
