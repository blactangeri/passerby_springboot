package com.jdong.passerby.springboot.shiro;

import com.jdong.passerby.springboot.entity.SysPermission;
import com.jdong.passerby.springboot.entity.SysRole;
import com.jdong.passerby.springboot.entity.SysUser;
import com.jdong.passerby.springboot.service.SysPermissionServiceImpl;
import com.jdong.passerby.springboot.service.SysRoleServiceImpl;
import com.jdong.passerby.springboot.service.SysUserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Core class for shiro ID authentication
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private SysPermissionServiceImpl sysPermissionService;

    @Autowired
    private SysRoleServiceImpl sysRoleService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private String SHIRO_LOGIN_COUNT = "shiro_login_count_";

    private String SHIRO_IS_LOCKED = "shiro_is_locked_";

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user.getId());

        Set<String> roleSet = new HashSet<>();
        for (SysRole role : sysRoleService.selectByMap(map)) {
            roleSet.add(role.getType());
        }
        info.setRoles(roleSet);

        Set<String> permissionSet = new HashSet<>();
        for (SysPermission permission : sysPermissionService.selectByMap(map)) {
            permissionSet.add(permission.getName());
        }
        info.setStringPermissions(permissionSet);

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        opsForValue.increment(SHIRO_LOGIN_COUNT + username, 1);

        if (Integer.parseInt(opsForValue.get(SHIRO_LOGIN_COUNT + username)) >= 5) {
            opsForValue.set(SHIRO_IS_LOCKED + username, "LOCK");
            stringRedisTemplate.expire(SHIRO_LOGIN_COUNT + username, 1, TimeUnit.HOURS);
        }

        if ("LOCK".equals(opsForValue.get(SHIRO_IS_LOCKED + username))) {
            throw new DisabledAccountException("Your account is locked due to too many failed password attempts.");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("nickname", username);

        String paw = password + username;
        map.put("pswd", paw);

        List<SysUser> userList = sysUserService.selectByMap(map);
        SysUser user = userList.size() > 0 ? userList.get(0) : null;

        if (user == null) {
            throw new AccountException("Wrong username or password.");
        } else if ("0".equals(user.getStatus())) {
            throw new DisabledAccountException("Your account has been locked.");
        }  else {
            user.setLastLoginTime(new Date());
            sysUserService.updateById(user);
            opsForValue.set(SHIRO_LOGIN_COUNT + username, "0");
        }

        Logger.getLogger(String.valueOf(getClass())).info("Authentication succeeded. Logged in user: " + username);

        return new SimpleAuthenticationInfo(user, password, getName());
    }
}
