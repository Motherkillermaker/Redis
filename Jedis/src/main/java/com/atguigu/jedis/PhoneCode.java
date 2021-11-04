package com.atguigu.jedis;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * @author CJYong
 * @create 2021-10-11 10:37
 */
public class PhoneCode {
    public static void main(String[] args) {
        //模拟验证码发送
//        verifyCode("17371273547");
        testCode("17371273547","257719");

    }

    //1.生成6位数字的验证码
    public static String getCode(){
        Random random = new Random();
        String code ="";
        for (int i = 0; i < 6; i++) {
            int rand = random.nextInt(10);
            code += rand;
        }
        return code;
    }

    //2.每个手机每天只能发送三次，将验证码放到redis中并设置过期时间120s
    public static void verifyCode(String phone){
        //连接redis
        Jedis jedis = new Jedis("192.168.5.130",6379);
        //拼接Key
        String countKey = phone + ":count";
        String codeKey = phone + ":code";
        //每个手机每天只能发送三次
        String count = jedis.get(countKey);
        if (count == null){
            //没有发送次数，设置发送次数为1
            jedis.setex(countKey,24*60*60,"1");
        }else if (Integer.parseInt(count) <= 2){
            //发送次数 + 1
            jedis.incr(countKey);
        }else if (Integer.parseInt(count) > 2){
            //发送次数为3
            System.out.println("今天发送次数已达三次，无法再次进行发送 ！");
            jedis.close();
            return;
        }

        //将发送的验证码放到redis中
        String vcode = getCode();
        jedis.setex(codeKey,120,vcode);
        jedis.close();
    }

    //3.验证码校验
    public static void testCode(String phone, String code){
        //从redis中获取验证码
        Jedis jedis = new Jedis("192.168.5.130",6379);
        //验证码key
        String codeKey = phone + ":code";
        String redisCode = jedis.get(codeKey);
        //判断
        if (redisCode.equals(code)){
            System.out.println("登录成功");
        }else {
            System.out.println("请重新输入验证码 ！");
        }
        jedis.close();
    }


}
