/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CustomSessionManager
 * Author:   YuSong
 * Date:     2018/9/8 0:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author YuSong
 * @create 2018/9/8
 * @since 1.0.0
 */
public class CustomSessionManager extends DefaultWebSessionManager {

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey){
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request !=null && sessionId != null){
            Session session = (Session) request.getAttribute(sessionId.toString());
            if (session != null){
                return session;
            }
        }
        //从Redis中取出来
        Session session = super.retrieveSession(sessionKey);
        if (request != null && sessionId !=null){
           //将取出来的对象设置到request中
            request.setAttribute(sessionId.toString(),session);
        }
        return session;
    }
}

