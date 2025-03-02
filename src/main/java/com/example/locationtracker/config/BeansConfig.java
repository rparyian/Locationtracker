package com.example.locationtracker.config;

import com.example.locationtracker.model.Location;
import com.squareup.moshi.Moshi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.inject.Provider;
import java.util.Random;

@Configuration
public class BeansConfig {
    @Bean
    public RedisTemplate<String, String> redisTemplate(org.springframework.data.redis.connection.RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public Moshi moshi() {
        return new Moshi.Builder().build();
    }

    @Bean
    public Random random() {
        return new Random();
    }
    @Bean
    public Provider<Location> locationProvider(){
        return () -> new Location();
    }
}
