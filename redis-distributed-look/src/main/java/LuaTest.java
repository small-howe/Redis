import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.UUID;

public class LuaTest {
    public static void main(String[] args) {
        for (int i =0;i<2;i++) {
            RedisConnect redis = new RedisConnect();
            redis.executr(jedis -> {

                //1. 获取一个 随机字符串
                String value = UUID.randomUUID().toString();
                // 获取锁
                String k1 = jedis.set("k1", value, new SetParams().nx().ex(5));
                // 判断是否 拿到锁

                if (k1 != null && "OK".equals(k1)) {

                    //获取到锁
                    jedis.set("site", "www.howes.r");
                    String site = jedis.get("site");
                    System.out.println(site);

                    //释放锁 调用 lua 脚本的SHA1  已经在 Reids中编译过了
                    /**
                     *
                     if redis.call("get",KEYS[1])==ARGV[1] then
                     return redis.call("del",KEYS[1])
                     else
                     return 0
                     end
                     */
                    jedis.evalsha("260a5af2707ee8198943616380cccaefe46ba3ec", Arrays.asList("k1"), Arrays.asList(value));

                }else {

                    System.out.println("没拿到锁");
                    //没拿到锁
                }

            });
        }
    }

}
