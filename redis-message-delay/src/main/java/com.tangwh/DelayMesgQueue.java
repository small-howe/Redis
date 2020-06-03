package com.tangwh;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * 延迟消息队列
 */
public class DelayMesgQueue {

    private Jedis jedis;

    // 队列名称
    private String queue;

    public DelayMesgQueue(Jedis jedis, String queue) {
        this.jedis = jedis;
        this.queue = queue;
    }

    /**
     * 消息入队的方法
     *
     * @param data 要发送的消息
     */
    public void queue(Object data) {
        // 构造一个  JavaboyMessage
        JavaboyMessage message = new JavaboyMessage();
        message.setId(UUID.randomUUID().toString());
        message.setData(data);
        // 序列化

        try {
            String s = new ObjectMapper().writeValueAsString(message);
            System.out.println("msg publish" + new Date());
            // 序列化后的字符串
            //消息发送 core  队列的名字    当前时间 延迟 5s   消息本身
            jedis.zadd(queue, System.currentTimeMillis()+ 5000, s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息出队  消费消息
     */
    public void loop() {
        // 只要当前线程没打断  就一直循环
        while (!Thread.interrupted()) {
            // 消息名字   范围 从0 开始 到当前时间点   每次只读一条消息  偏移量是0  每次只读1个
            // 读取时间在 0 - 当前时间戳之间的消息
            Set<String> zrange = jedis.zrangeByScore(queue, 0, System.currentTimeMillis(), 0, 1);

            // 判断 如果 消息 为空
            if (zrange.isEmpty()) {
                // 如果消息是空的  则 休息 500毫秒 继续

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // 如果异常退出
                    break;
                }
                continue;
            }
            // 如果读取到了消息 则直接加载
            String next = zrange.iterator().next();
            // 消息 如果 移除操作 大于0  说明抢到了
            if (jedis.zrem(queue, next) > 0) {
                //抢到了 接下来 处理业务 确保消息只给一个人消费
                try {
                     JavaboyMessage msg = new ObjectMapper().readValue(next, JavaboyMessage.class);
                     // 打印
                    System.err.println("receive msg:"+new Date()+">>>"+msg);

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }


            }

        }
    }
}
