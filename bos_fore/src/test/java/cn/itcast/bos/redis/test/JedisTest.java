package cn.itcast.bos.redis.test;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by ${joel} on 2017/9/27 0027.
 */
public class JedisTest {
    @Test
    public void test() {
        Jedis jedis = new Jedis("localhost");
        jedis.setex("company", 10, "黑马程序员");
        System.out.println(jedis.get("company"));
    }
}
