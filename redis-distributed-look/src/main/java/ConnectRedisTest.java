import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ConnectRedisTest {

    public void contextLoads() {
        /**
         * 远程连接 配置 redis
         *  打开redis.conf 配置文件
         *   #bind 127.0.0.1  注释这个
         *   打开密码设置密码
         *   ################################## SECURITY ###################################
         *   requirepass 120208
         *  重启redis
         *
         */
        // 构造 Jedis对象  因为 使用的是默认端口  所以不用配置端口
        Jedis jedis = new Jedis("192.168.91.130");

        //2. 密码认证

        jedis.auth("120208");

        //测试 是否连接成功
        String ping = jedis.ping();
        //返回 pong 标是连接成功
        System.out.println(ping);

        // 连接成功后 操作 方法 API 和 命令 高度一直 所以jedis 简明之意


    }

    /**
     * 使用连接池 来 连接Jedis
     */

    public void jedisPoolTest() {


        //快捷键 ctrl+alt+t
        // 构造一个连接池
        JedisPool pool = new JedisPool("192.168.91.130", 6379);

        //从连接池中 获取一个jedis连接
        Jedis   jedis = pool.getResource();
        //2. 密码认证

        jedis.auth("120208");

        try {
            // 操作
            String ping = jedis.ping();
            System.out.println(ping);
            jedis.set("唐", "howes");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                //归还连接
                jedis.close();
            }
        }


    }


    /**
     * 同上  简化操作simpleness
     */


    public void jedisPoolTestSimpleness() {
        //快捷键 ctrl+alt+t
        // 构造一个连接池
        JedisPool pool = new JedisPool("192.168.91.130", 6379);

        //从连接池中 获取一个jedis连接
        // 同上 测试 一直
        try (Jedis jedis = pool.getResource()) {
            //2. 密码认证
            jedis.auth("120208");
            // 操作
            String ping = jedis.ping();
            System.out.println(ping);


        }
    }

    /**
     * 添加约束后 的改变
     */

    public void jedisTest(){

        /**
         * 自己的类
         */
        RedisConnect redis = new RedisConnect();

        redis.executr(jedis -> {
//                 jedis.auth("120208");
            System.out.println(jedis.ping());
        });

    }
}
