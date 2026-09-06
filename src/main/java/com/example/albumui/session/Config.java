package com.example.albumui.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;


@Configuration(proxyBeanMethods = false)
@EnableRedisHttpSession 
public class Config {

    @Value("${redis_hostname:localhost}")
    private String redisHostname;

	@Bean
	public LettuceConnectionFactory connectionFactory() {
		System.out.println("########## ConnectionFactory" + "   " + redisHostname);
		//return new LettuceConnectionFactory(); 
		// application.yaml に設定しても hostname が認識されいないので、ここで設定

		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHostname, 6379));

	}

}