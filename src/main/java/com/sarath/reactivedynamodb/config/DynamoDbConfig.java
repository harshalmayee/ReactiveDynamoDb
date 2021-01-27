package com.sarath.reactivedynamodb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;

import java.net.URI;

@Configuration
public class DynamoDbConfig {
    private final String dynamoDbEndPointUrl;
    private final String accessKey;
    private final String secretKey;

    public DynamoDbConfig(@Value("${aws.dynamodb.endpoint}")
                          String dynamoDbEndPointUrl,
                          @Value("${aws.accessKey}")
                          String accessKey,
                          @Value("${aws.secretKey}")
                          String secretKey) {
        this.dynamoDbEndPointUrl = dynamoDbEndPointUrl;
        this.accessKey=accessKey;
        this.secretKey=secretKey;
    }

    @Bean
    AwsBasicCredentials awsBasicCredentials(){
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient(AwsBasicCredentials awsBasicCredentials){
        DynamoDbAsyncClientBuilder clientBuilder = DynamoDbAsyncClient.builder();
        clientBuilder
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials));
        if(!dynamoDbEndPointUrl.isEmpty()){
            clientBuilder.endpointOverride(URI.create(dynamoDbEndPointUrl));
        }
        return clientBuilder.build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient() {
        return DynamoDbEnhancedAsyncClient.builder()
                                          .dynamoDbClient(dynamoDbAsyncClient(awsBasicCredentials()))
                                          .build();
    }

}
