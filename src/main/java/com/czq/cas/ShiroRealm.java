package com.czq.cas;


import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import java.util.Map;

/**
 * 在Shiro中，最终是通过Realm来获取应用程序中的用户、角色及权限信息的
 * 在Realm中会直接从我们的数据源中获取Shiro需要的验证信息。可以说，Realm是专用于安全框架的DAO.
 * @author chengzequn@foxmail.com
 * @since 2020/8/10 14:04
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 授权用户权限 但是这个方法并不用,我们会在 ShiroAuthenticationHandler的 checkSubjectRolesAndPermissions 中单独去验证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //添加角色
        SimpleAuthorizationInfo info= new SimpleAuthorizationInfo();
        return info;
    }

    /**
     * 验证用户身份
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户密码第一种方式
//        String username=(String)authenticationToken.getPrincipal();
//        String password=new String((char[])authenticationToken.getCredentials());

        //获取用户密码 第二种方式
        UsernamePasswordToken usernamePasswordToken= (UsernamePasswordToken) authenticationToken;
        String username=usernamePasswordToken.getUsername();

        Map<String,Object> user=userService.findByUserName(username);

        //可以在这里直接对用户名校验，或者调用CredentialsMatcher校验
        if(user == null){
            throw new UnknownAccountException("用户名或密码错误！");
        }

        //这里将密码对比注销，否则无法锁定，要将密码对比交给密码比较器，这里可以添加自己的密码比较器等
//        if(!password.equals(user.getPassword())){
//            throw new IncorrectCredentialsException("用户名或密码错误！");
//        }
        if("1".equals(user.get("state"))){
            throw new LockedAccountException("账号已被锁定，请联系管理员！");
        }

        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(username,user.get("password"),getName());
        return info;
    }
}
