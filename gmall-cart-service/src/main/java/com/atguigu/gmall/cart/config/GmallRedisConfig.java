package com.atguigu.gmall.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/10/16 0016
 */
@Configuration
public class GmallRedisConfig {

    /**
     * JedisConnectionFactory factory 这个参数是从容器中自动获取的
     * @param factory
     * @return 将我们做好的 JedisPool 放在容器中，每次只需要autowired获取即可
     */
    @Bean
    public JedisPool jedisPoolConfig(JedisConnectionFactory factory) {
        //连接工厂中所有信息都有
        JedisPoolConfig config = factory.getPoolConfig();
        JedisPool jedisPool = new JedisPool(config, factory.getHostName(), factory.getPort(), factory.getTimeout());
        return jedisPool;
    }

}
