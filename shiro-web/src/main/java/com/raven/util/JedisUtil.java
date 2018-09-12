/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: JedisUtil
 * Author:   YuSong
 * Date:     2018/9/7 17:21
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author YuSong
 * @create 2018/9/7
 * @since 1.0.0
 */
@Component
public class JedisUtil {

    @Resource
    private JedisPool jedisPool;

    /**
     * 连接的方法
     *
     * @return
     */
    private Jedis getResource() {
        return jedisPool.getResource();
    }

    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getResource();

        try {
            jedis.set(key, value);
            return value;
        } finally {
            //关闭连接
            jedis.close();
        }
    }

    public void expire(byte[] key, int i) {
        Jedis jedis = getResource();
        try {
            jedis.expire(key, i);
        } finally {
            //关闭连接
            jedis.close();
        }
    }

    public byte[] get(byte[] key) {
        Jedis jedis = getResource();
        try {
           return jedis.get(key);
        } finally {
            //关闭连接
            jedis.close();
        }
    }

    public void del(byte[] key) {
        Jedis jedis = getResource();
        try {
            jedis.del(key);
        } finally {
            //关闭连接
            jedis.close();
        }
    }

    public Set<byte[]> keys(String shiro_session_prefix) {
        Jedis jedis = getResource();
        try {
            return jedis.keys((shiro_session_prefix +"*").getBytes());
        } finally {
            //关闭连接
            jedis.close();
        }
    }
}

