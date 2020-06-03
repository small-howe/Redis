import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * 限流
 */
public class RateLimiter {

    private Jedis jedis;

    public RateLimiter(final Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 限流方法
     *
     * @param user     操作的用户 相当于 限流的对象
     * @param action   具体 的操作
     * @param period   时间窗  相当于 限流的周期
     * @param maxCount 限流的次数 某一个周期里面 允许多少次访问
     * @return
     */
    public boolean isAllowed(String user, String action, int period, int maxCount) {
        // 1.数据用zset操作
        //生成一个Key
        String key = user + "-" + action;
        // 2.获取当前时间戳
        long nowTime = System.currentTimeMillis();
        // 3.建立管道
        Pipeline pipelined = jedis.pipelined();
        // 开启任务的执行
        pipelined.isInMulti();

        // 4.编写任务 将当前操作 存储下来
        pipelined.zadd(key, nowTime, String.valueOf(nowTime));

        // 5.移除时间窗之外的数据
        pipelined.zremrangeByScore(key, 0, nowTime - period * 1000);

        // 6.统计剩下的key
        Response<Long> response = pipelined.zcard(key);
        //7.将当前key设置过期时间 过期时间就是 时间窗
        pipelined.expire(key,period + 1);

        // 执行
//        pipelined.exec();
        // 关闭
        pipelined.close();

        // 8. 返回 比较时间窗内的操作数
        return response.get()<=maxCount;


    }

    public static void main(String[] args) {
        RedisConnect redis= new RedisConnect();
        redis.executr(s->{
            RateLimiter rateLimiter = new RateLimiter(s);
            for (int i = 0; i <20 ; i++) {
                // 5s之内 只能由三次操作
                System.out.println(rateLimiter.isAllowed("java", "publish", 1, 3));

            }
        });
    }
}
