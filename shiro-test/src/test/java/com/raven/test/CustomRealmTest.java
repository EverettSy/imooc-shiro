/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CustomRealmTest
 * Author:   YuSong
 * Date:     2018/9/4 22:23
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.test;

import com.raven.shiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author YuSong
 * @create 2018/9/4
 * @since 1.0.0
 */
public class CustomRealmTest {

    @Test
    public void testAuthentication() {

        CustomRealm customRealm = new CustomRealm();

        // 1. 构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //加密方式
        matcher.setHashAlgorithmName("md5");
        //加密次数
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);

        // 2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("Raven", "123456");
        subject.login(token);

        System.out.println("isAuthenticated:" + subject.isAuthenticated());

       /* subject.checkRole("admin");

        subject.checkPermission("user:update");*/

      /* subject.checkRole("admin");

       subject.checkPermissions("user:add","user:delete");*/
    }
}

