package com.czq.cas;

import java.util.Set;

/**
 * @author chengzequn@foxmail.com
 * @since 2020/8/10 15:57
 */
public interface RoleService {
    Set<String> findAllRoles();

    String findRolesByUserId(String uid);
}
