package com.example.albumui.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;


@Configuration(proxyBeanMethods = false)
@EnableRedisHttpSession 
public class Config {

	@Bean
	public LettuceConnectionFactory connectionFactory() {
		System.out.println("########## ConnectionFactory");
		//return new LettuceConnectionFactory(); 
		// application.yaml に設定しても hostname が認識されいないので、ここで設定
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration("redis", 6379));

	}

}