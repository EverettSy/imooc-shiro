/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserDao
 * Author:   YuSong
 * Date:     2018/9/6 22:27
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.dao;

import com.raven.vo.User;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author YuSong
 * @create 2018/9/6
 * @since 1.0.0
 */
public interface UserDao {

    User getUserByUserName(String userName);

    /**
     * 查询角色的方法
     * @param userName
     * @return
     */
    List<String> queryRoleByUserName(String userName);
}

