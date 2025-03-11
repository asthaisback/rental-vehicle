package com.vehiclerental.vehicle_rental_system.config;
import com.vehiclerental.vehicle_rental_system.dto.VehicleDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    // RedisConnectionFactory: redis server se connection banata h
    public RedisTemplate<String, List<VehicleDTO>> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, List<VehicleDTO>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());// To serialize objects properly
                                        //serializer converts Java objects to JSON format
                                       //When retrieving data, automatically converts JSON back to // Java objects
        return redisTemplate;
    }
}
