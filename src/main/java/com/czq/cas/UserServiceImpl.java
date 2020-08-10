package com.czq.cas;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chengzequn@foxmail.com
 * @since 2020/8/10 16:09
 */
@Service
public class UserServiceImpl implements UserService{
    @Override
    public Map<String, Object> findByUserName(String username) {
        Map map=new HashMap();
        map.put("uid","111111");
        map.put("","");
        map.put("","");

        return null;
    }
}
