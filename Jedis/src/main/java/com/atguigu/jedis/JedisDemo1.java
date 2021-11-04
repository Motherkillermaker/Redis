package com.atguigu.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * @author CJYong
 * @create 2021-10-11 9:31
 */
public class JedisDemo1 {
    public static void main(String[] args) {
        //创建Jedis对象
        Jedis jedis = new Jedis("192.168.5.130",6379);

        //测试
        String ping = jedis.ping();
        System.out.println(ping);
    }

    @Test
    public void test1(){
        //创建Jedis对象
        Jedis jedis = new Jedis("192.168.5.130",6379);
        //添加
        jedis.set("key1","v1");
        jedis.set("key2","v2");
        jedis.set("key3","v3");
        //设置多个Key - value
        jedis.mset("one","1","two","2");
        List<String> mget = jedis.mget("one", "two");
        System.out.println(mget);

        //获取
        String key2 = jedis.get("key2");
        System.out.println(key2);

        Set<String> keys = jedis.keys("*");
        for (String key :keys) {
            System.out.println(key);
        }

        jedis.close();

    }

}
