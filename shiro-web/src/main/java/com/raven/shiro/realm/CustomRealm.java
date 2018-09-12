/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CustimRealm
 * Author:   YuSong
 * Date:     2018/9/4 22:07
 * Description: 自定义Realm
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.shiro.realm;

import com.raven.dao.UserDao;
import com.raven.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈自定义Realm〉
 *
 * @author YuSong
 * @create 2018/9/4
 * @since 1.0.0
 */
public class CustomRealm extends AuthorizingRealm {

    /*Map<String,String> userMap  = new HashMap<String, String>(16);

    {
        userMap.put("Raven","6635aeec96937ade86a2c1f3a724c8a5");

        super.setName("customRealm");
    }*/

    @Resource
    private UserDao userDao;

    /**
     * 自定义授权的过程
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String userName = (String) principalCollection.getPrimaryPrincipal();
        //从数据库或者缓存中获取角色
        Set<String> roles = getRolesByUserName(userName);
        Set<String> permissions = getPermissionsByUserName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //设置权限
        simpleAuthorizationInfo.setStringPermissions(permissions);
        //设置角色
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionsByUserName(String userName) {

        Set<String> sets = new HashSet<String>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    /**
     *
     * @param userName
     * @return
     */
    private Set<String> getRolesByUserName(String userName) {
       /* sets.add("admin");
        sets.add("user");*/
        System.out.println("从数据库中获取授权数据");
       List<String> list = userDao.queryRoleByUserName(userName);
        Set<String> sets = new HashSet<String>(list);
        return sets;
    }

    /**
     * 自定义认证的过程
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        // 1.从主体传过来的认证信息中，获得用户名
        String userName = (String) authenticationToken.getPrincipal();

        // 2.通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(userName);
        if (password == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo
                (userName,password,"customRealm");

        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));
        return authenticationInfo;
    }

    /**
     * 模拟数据库查询
     * @param userName
     * @return
     */
    private String getPasswordByUserName(String userName) {
        User user = userDao.getUserByUserName(userName);
        if (user != null){
            return user.getPassword();
        }
        return null;
        //        return  userMap.get(userName);
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456","Raven");
        System.out.println(md5Hash.toString());
    }
}

