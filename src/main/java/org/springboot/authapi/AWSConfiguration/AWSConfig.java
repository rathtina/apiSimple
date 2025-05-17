package org.springboot.authapi.AWSConfiguration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @PostConstruct
    public void test() {
        System.out.println("✅ Access Key: " + accessKey);
        System.out.println("✅ Secret Key: " + (secretKey != null ? "LOADED" : "MISSING"));
        System.out.println("✅ Region: " + region);
    }

    @Bean
    public S3Client s3client(){
      return S3Client.builder()
              .region(Region.of(region))
              .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey,secretKey)))
              .build();
    }
}

//S3Client class is used to interact with  Amazon S3
