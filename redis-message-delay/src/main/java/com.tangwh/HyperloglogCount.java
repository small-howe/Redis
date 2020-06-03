package com.tangwh;

import com.tangwh.utisRedisConnect.RedisConnect;

/**
 * 统计 不同用户登录的浏览量 user view(去重 )
 */
public class HyperloglogCount {


    public static void main(String[] args) {

        RedisConnect redisConnect = new RedisConnect();

        redisConnect.executr(jedis -> {
            for (int i = 0; i <1000 ; i++) {

                jedis.pfadd("uv", "u"+i,"u"+(i+1));
            }
            long uv = jedis.pfcount("uv");
            System.out.println(uv);
        });
    }
}
