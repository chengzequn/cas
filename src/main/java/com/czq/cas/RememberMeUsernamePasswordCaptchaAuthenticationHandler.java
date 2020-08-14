package com.czq.cas;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Set;

/**
 * 创建表单注册器
 * @author chengzequn@foxmail.com
 * @since 2020/8/11 8:53
 */
public class RememberMeUsernamePasswordCaptchaAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public RememberMeUsernamePasswordCaptchaAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        RememberMeUsernamePasswordCaptchaCredential captchaCredential= (RememberMeUsernamePasswordCaptchaCredential) credential;
        String requestCaptcha = captchaCredential.getCaptcha();
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object attribute=attributes.getAttribute("captcha",1);
        String realCaptcha = attribute ==null ? null:attribute.toString();
        if(StringUtils.isBlank(requestCaptcha)||!requestCaptcha.toUpperCase().equals(realCaptcha)){
            throw new FailedLoginException("验证码错误");
        }
        String username = captchaCredential.getUsername();
        Map<String,Object> user= userService.findByUserName(username);

        //可以在这里直接对用户名校验，或者调用CredentialMatcher校验
        if(user == null){
            throw new CustomAccountNotFountException("用戶名不存在！");
        }

        //检测用户是否已被锁定
        if("1".equals(user.get("state"))){
            throw new LockedAccountException("账号已被锁定，请联系管理员！");
        }

        //获取加密后的密码
        String password=captchaCredential.getPassword();
        PasswordEncryption encryption=new PasswordEncryption();
        String realPassword=encryption.encode(password);

        if(!user.get("password").equals(realPassword)){
            throw new UnknownAccountException("用户名或密码错误");
        }

        checkSubjectRolesAndPermissions(String.valueOf(user.get("uid")));

        return createHandlerResult(credential,this.principalFactory.createPrincipal(username,user));
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof  RememberMeUsernamePasswordCaptchaCredential;
    }

    protected void checkSubjectRolesAndPermissions(final String uid) throws FailedLoginException {
        //获取所有的用户角色
        Set<String> allRoles=roleService.findAllRoles();
        //根据id获取用户的角色，这里一个用户只对应一个角色
        String userRole = roleService.findRolesByUserId(uid);
        for(String role : allRoles){
            if(role.equals(userRole)){
                return;
            }
        }
        throw new FailedLoginException();
    }

    protected Subject getCurrentExecutingSubject(){
        return SecurityUtils.getSubject();
    }

}
