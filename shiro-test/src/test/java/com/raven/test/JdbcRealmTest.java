/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: JdbcRealmTest
 * Author:   YuSong
 * Date:     2018/9/3 18:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author YuSong
 * @create 2018/9/3
 * @since 1.0.0
 */
public class JdbcRealmTest {

    DruidDataSource datasource = new DruidDataSource();

    {
        datasource.setUrl("jdbc:mysql://localhost:3306/test");
        datasource.setUsername("root");
        datasource.setPassword("123456");
    }


    @Test
    public void testAuthentication() {

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(datasource);
        //查询权限开关
        jdbcRealm.setPermissionsLookupEnabled(true);


        String sql ="select password from test_user where user_name = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        String roleSql ="select role_name from test_user_role where user_name = ?";
        jdbcRealm.setUserRolesQuery(roleSql);

        // 1. 构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        // 2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("xiaoming", "123456");
        subject.login(token);

        System.out.println("isAuthenticated:" + subject.isAuthenticated());


       /* subject.checkRole("admin");

        subject.checkRoles("admin","user");

        subject.checkPermission("user:select");*/

        subject.checkRole("user");
    }
}

