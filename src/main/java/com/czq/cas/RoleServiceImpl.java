package com.czq.cas;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chengzequn@foxmail.com
 * @since 2020/8/10 16:09
 */
@Service
public class RoleServiceImpl implements  RoleService{
    @Override
    public Set<String> findAllRoles() {
        //假数据
        Set<String> roles=new HashSet<>();
        roles.add("系统管理员");
        roles.add("测试管理员");
        roles.add("超级管理员");
        return roles;
    }

    @Override
    public String findRolesByUserId(String uid) {
        //假数据
        String role="系统管理员";
        return role;
    }
}
