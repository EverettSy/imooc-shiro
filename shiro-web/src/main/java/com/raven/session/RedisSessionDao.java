/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: RedisSessionDao
 * Author:   YuSong
 * Date:     2018/9/7 16:40
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.session;

import com.raven.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author YuSong
 * @create 2018/9/7
 * @since 1.0.0
 */
public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtil jedisutil;

    private final String SHIRO_SESSION_PREFIX = "raven-session";

    /**
     * 生成session前缀的方法
     *
     * @param key
     * @return
     */
    private byte[] getKey(String key) {
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }

    /**
     * 保存session的方法
     *
     * @param session
     */
    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            //序列化为byte[]
            byte[] value = SerializationUtils.serialize(session);
            //存储到缓存中
            jedisutil.set(key, value);
            //设置一个操作时间
            jedisutil.expire(key, 600);
        }
    }

    /**
     * 创建session
     *
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        //生成的sessionId与session捆绑
        assignSessionId(session,sessionId);
        //传入到Redis中
        saveSession(session);
        return sessionId;
    }

    /**
     * 获得session
     *
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("read session");
        if (sessionId == null) {
            return null;
        }
        //获取key
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisutil.get(key);
        //反序列化成session对象
        return (Session) SerializationUtils.deserialize(value);
    }


    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    /**
     * 删除session
     *
     * @param session
     */
    @Override
    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            return;
        }
        //获取指定的key
        byte[] key = getKey(session.getId().toString());
        //通过jedis删除key
        jedisutil.del(key);
    }

    /**
     * 获得到存活的指定的session
     *
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        //先获得到我们Redis中所有的key值
        Set<byte[]> keys = jedisutil.keys(SHIRO_SESSION_PREFIX);
        //创建一个session的集合
        Set<Session> sessions = new HashSet<Session>();
        if (CollectionUtils.isEmpty(keys)) {
            return sessions;
        }
        //遍历所有的key值
        for (byte[] key : keys) {
            //反序列化为session对象
            Session session = (Session) SerializationUtils.deserialize(jedisutil.get(key));
            //存储到集合当中
            sessions.add(session);
        }
        return sessions;
    }
}

