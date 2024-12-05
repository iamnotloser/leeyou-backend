package com.leeyoubackend.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson 配置
 */
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedissonConfig {
    private String host;
    private String port;

    @Bean
    public RedissonClient redissonClient(){
        //1.创建配置
        String Address = "redis://127.0.0.1:6379";
        Config config = new Config();

        config.useSingleServer().setAddress(Address).setDatabase(3);
        config.setTransportMode(org.redisson.config.TransportMode.NIO);
                // use "rediss://" for SSL connection

        //2.创建RedissonClient实例
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
