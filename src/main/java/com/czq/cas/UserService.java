package com.czq.cas;

import java.util.Map;

/**
 * @author chengzequn@foxmail.com
 * @since 2020/8/10 15:57
 */
public interface UserService {

    Map<String,Object> findByUserName(String username);
}
