package com.caoyong.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    private Jedis jedis;

    @Bean(name = "jedis.pool")
    @Autowired
    public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config,
                               @Value("${jedis.pool.host}") String host, @Value("${jedis.pool.port}") int port,
                               @Value("${jedis.pool.timeout}") int timeout) {
        JedisPool jedisPool = new JedisPool(config, host, port, timeout);
        jedis = jedisPool.getResource();
        return jedisPool;
    }

    @Bean(name = "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig(@Value("${jedis.pool.config.maxTotal}") int maxTotal,
                                           @Value("${jedis.pool.config.maxIdle}") int maxIdle,
                                           @Value("${jedis.pool.config.maxWaitMillis}") int maxWaitMillis,
                                           @Value("${jedis.pool.config.minIdle}") int minIdle,
                                           @Value("${jedis.pool.config.timeBetweenEvictionRunsMillis}") int timeBetweenEvictionRunsMillis,
                                           @Value("${jedis.pool.config.numTestsPerEvictionRun}") int numTestsPerEvictionRun,
                                           @Value("${jedis.pool.config.minEvictableIdleTimeMillis}") int minEvictableIdleTimeMillis) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        //Idle时进行连接扫描
        config.setTestWhileIdle(true);
        //表示idle object evitor两次扫描之间要sleep的毫秒数
        config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        //表示idle object evitor每次扫描的最多的对象数
        config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        return config;
    }

    @Bean
    @DependsOn("jedis.pool")
    public Jedis jedis() {
        return jedis;
    }
}
