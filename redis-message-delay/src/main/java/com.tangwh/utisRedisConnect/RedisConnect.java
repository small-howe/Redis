package com.tangwh.utisRedisConnect;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * reids 连接 工具类
 */
public class RedisConnect {

    private JedisPool pool;


    public RedisConnect(){

        // 连接池 配置
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        // 连接池最大空闲数
        config.setMaxIdle(300);
        // 连接池最大连接数
        config.setMaxTotal(1000);
        // 连接最大等待时间 如果是-1 标是没有限制
        config.setMaxWaitMillis(3000);
        //在空闲时检查有效性
        config.setTestOnBorrow(true);
        //参数    ip            端口      超时时间            密码
        pool = new JedisPool(config, "192.168.91.130",6379,3000,"120208");
    }


    public void executr(CallWithJedis callWithJedis){
        try(Jedis jedis = pool.getResource()) {
            callWithJedis.call(jedis);
        }
    }
}
