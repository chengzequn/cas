package com.czq.cas;

import javax.security.auth.login.AccountException;

/**
 * @author chengzequn@foxmail.com
 * @since 2020/8/12 15:17
 */
public class CustomAccountNotFountException extends AccountException {
    public CustomAccountNotFountException() {
    }

    public CustomAccountNotFountException(String msg) {
        super(msg);
    }
}
