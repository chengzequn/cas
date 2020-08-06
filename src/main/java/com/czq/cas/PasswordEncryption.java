package com.czq.cas;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author czq
 * @date 2020/6/4 16:09
 */
public class PasswordEncryption implements PasswordEncoder{
    @Override
    public String encode(CharSequence charSequence) {

        try {
            //给数据进行md5加密,第一个参数是算法，第二个参数是密码，第三个参数是盐，第四个参数是要循环加密几次
            SimpleHash hash = new SimpleHash("MD5", charSequence, "qwe", 1);
            return hash.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {

        if(charSequence==null){
            return false;
        }
        //通过md5加密后的密码
        String password=this.encode(charSequence.toString());
        //比较密码是否相等
        return password.equals(s);
    }
}