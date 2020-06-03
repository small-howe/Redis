package com.tangwh;

import com.tangwh.utisRedisConnect.RedisConnect;

/**
 * 延迟消息发送 收到 测试
 */
public class DelayMsgTest {

    public static void main(String[] args) {
        RedisConnect redis = new RedisConnect();

        redis.executr(jedis -> {
            //构造一个 消息队列
            DelayMesgQueue queue = new DelayMesgQueue(jedis, "javaboye-delay-queue");
           // 消息生产者
            Thread producer = new Thread(){

                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        queue.queue("wwww.java.org"+i);
                    }
                }
            };

            // 消息消费者
            Thread consumer = new Thread(){

                @Override
                public void run() {
                  queue.loop();
                }
            };

            // 启动
            producer.start();
            consumer.start();
            // 消息7s后 停止程序
            try {
                Thread.sleep(7000);
                consumer.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }

}
