/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: RedisCacheManager
 * Author:   YuSong
 * Date:     2018/9/8 13:08
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author YuSong
 * @create 2018/9/8
 * @since 1.0.0
 */
public class RedisCacheManager implements CacheManager {

    @Resource
    private  RedisCache redisCache;

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return redisCache;
    }
}

