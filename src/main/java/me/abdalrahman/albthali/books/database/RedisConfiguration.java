package me.abdalrahman.albthali.books.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;
@Configuration
public class RedisConfiguration {
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.host}")
    private String host;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        System.out.println("password:"+password);
        System.out.println("host:"+host);
        jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(6379);

        return jedisConnectionFactory;
    }
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
