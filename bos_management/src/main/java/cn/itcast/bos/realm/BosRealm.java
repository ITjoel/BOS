package cn.itcast.bos.realm;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by ${joel} on 2017/10/13 0013.
 */
//@Service("bosRealm")
public class BosRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;


    //权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("权限......");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        //调用业务层查询角色

        List<Role> roles = roleService.findByUser(user);
        for (Role role : roles) {
            simpleAuthorizationInfo.addRole(role.getKeyword());
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                simpleAuthorizationInfo.addStringPermission(permission.getKeyword());
            }
        }
        /*List<Permission> permissions = permissionService.findByUser(user);
        for (Permission permission : permissions) {
            simpleAuthorizationInfo.addStringPermission(permission.getKeyword());
        }*/
        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证......");
        // 转换token
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        // 根据用户名 查询 用户信息
        User user = userService.findByUsername(usernamePasswordToken
                .getUsername());
        if (user == null) {
            // 用户名不存在
            return null;
        } else {
            // 用户名存在
            return new SimpleAuthenticationInfo(user, user.getPassword(),
                    getName());
        }

    }
}
