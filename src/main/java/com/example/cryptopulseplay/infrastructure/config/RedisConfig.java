package com.example.cryptopulseplay.infrastructure.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

@Configuration
public class RedisConfig {

    private @Value("${spring.data.redis.host}") String redisHost;
    private @Value("${spring.data.redis.port}") int redisPort;

    @Bean
    public RedisConnectionFactory connectionFactory() {

        LettucePoolingClientConfiguration config = LettucePoolingClientConfiguration.builder()
                .poolConfig(new GenericObjectPoolConfig()).build();

        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort), config);

    }

}
