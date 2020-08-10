package com.czq.cas;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apereo.cas.authentication.*;
import org.apereo.cas.authentication.AuthenticationException;
import org.apereo.cas.authentication.exceptions.AccountDisabledException;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Set;

/**
 * 自定义验证器
 * @author chengzequn@foxmail.com
 * @since 2020/8/6 14:53
 */
public class UsernamePasswordAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

    private static final Logger logger= LoggerFactory.getLogger(UsernamePasswordAuthenticationHandler.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public UsernamePasswordAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential, String originalPassword) throws GeneralSecurityException, PreventedException {
        try {
        UsernamePasswordToken token=new UsernamePasswordToken(credential.getUsername(),credential.getPassword());
        if(credential instanceof RememberMeUsernamePasswordCredential){
            token.setRememberMe(RememberMeUsernamePasswordCredential.class.cast(credential).isRememberMe());
        }
        Subject currentUser=getCurrentExecutingSubject();
        currentUser.login(token);
        checkSubjectRolesAndPermissions(currentUser);
        return createAuthenticatedSubjectResult(credential, currentUser);
        }catch (final UnknownAccountException uae) {
            throw new AccountNotFoundException(uae.getMessage());
        } catch (final IncorrectCredentialsException ice) {
            throw new FailedLoginException(ice.getMessage());
        } catch (final LockedAccountException | ExcessiveAttemptsException lae) {
            throw new AccountLockedException(lae.getMessage());
        } catch (final ExpiredCredentialsException eae) {
            throw new CredentialExpiredException(eae.getMessage());
        } catch (final DisabledAccountException eae) {
            throw new AccountDisabledException(eae.getMessage());
        } catch (final AuthenticationException e) {
            throw new FailedLoginException(e.getMessage());
        }
    }

    protected void checkSubjectRolesAndPermissions(final Subject currentUser) throws FailedLoginException {
        //查询用户id，也可以在登录成功后，将id放到session中，从session中获取，这里直接查库
        Map<String,Object> user=userService.findByUserName(String.valueOf(currentUser.getPrincipal()));
        //获取所有的用户角色
        Set<String> allRoles=roleService.findAllRoles();
        //根据id获取用户的角色，这里一个用户只对应一个角色
        String userRole=roleService.findRolesByUserId(String.valueOf(user.get("uid")));
        for(String role : allRoles){
            if(role.equals(userRole)){
                return;
            }
        }
        //否则抛出异常，也可以自定义异常，返回不同提示
        throw new FailedLoginException();
    }

    protected AuthenticationHandlerExecutionResult createAuthenticatedSubjectResult(final Credential credential,final Subject currentUser){
        final String username=currentUser.getPrincipal().toString();
        return createHandlerResult(credential, this.principalFactory.createPrincipal(username));
    }

    /**
     *获取当前执行的主题
     * @return
     */
    protected Subject getCurrentExecutingSubject(){
        return SecurityUtils.getSubject();
    }

}
