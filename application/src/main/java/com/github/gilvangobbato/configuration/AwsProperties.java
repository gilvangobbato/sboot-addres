package com.github.gilvangobbato.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "aws")
public record AwsProperties(String endpoint,
                            String region,
                            String accessKey,
                            String secretKey,
                            AwsSqsProperties sqs) {
}
