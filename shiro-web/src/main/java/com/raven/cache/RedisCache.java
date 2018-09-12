/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: RedisCache
 * Author:   YuSong
 * Date:     2018/9/8 13:11
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.cache;

import com.raven.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author YuSong
 * @create 2018/9/8
 * @since 1.0.0
 */
@Component
public class RedisCache<K,V> implements Cache<K,V> {

    @Resource
    private JedisUtil jedisUtil;

    //写一个前缀
    private final String cache_prefix = "raven-cache:";

    private byte[] getKey(K k){
        if (k instanceof String){
            return (cache_prefix +k).getBytes();
        }
        //序列化之后的数组
        return SerializationUtils.serialize(k);
    }

    @Override
    public V get(K k) throws CacheException {
        System.out.println("从Redis中获取权限数据");
        byte[] value = jedisUtil.get(getKey(k));
        if (value != null){
            //反序列化成对象
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(key);
        jedisUtil.set(key,value);
        jedisUtil.expire(key,600);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(key);
        jedisUtil.del(key);
        if (value != null){
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {
        //
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}

