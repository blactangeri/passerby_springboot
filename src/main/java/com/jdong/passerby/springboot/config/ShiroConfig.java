package com.jdong.passerby.springboot.config;

import com.jdong.passerby.springboot.entity.SysPermissionInit;
import com.jdong.passerby.springboot.service.SysPermissionInitServiceImpl;
import com.jdong.passerby.springboot.shiro.MyShiroRealm;
import com.jdong.passerby.springboot.shiro.filter.KickoutSessionControlFilter;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Created by James on 9/21/17
 */
@Configuration
public class ShiroConfig {

    @Autowired(required = false)
    SysPermissionInitServiceImpl sysPermissionInitService;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout")
    private int timeout;

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("kickout", kickoutSessionControlFilter());

        shiroFilterFactoryBean.setFilters(filterMap);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        for (SysPermissionInit sysPermissionInit : sysPermissionInitService.selectAll()) {
            filterChainDefinitionMap.put(sysPermissionInit.getUrl(), sysPermissionInit.getPermissionInit());
        }

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public MyShiroRealm myShiroRealm() {
        return new MyShiroRealm();
    }

    private RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setExpire(1800);
        redisManager.setTimeout(timeout);
        return redisManager;
    }

    private RedisCacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager());
        return cacheManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    private SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // Effective 30 days
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    private CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.getDecoder().decode("3AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    private KickoutSessionControlFilter kickoutSessionControlFilter() {
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        kickoutSessionControlFilter.setCacheManager(cacheManager());
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        kickoutSessionControlFilter.setKickoutAfter(false);
        kickoutSessionControlFilter.setMaxSession(1);
        kickoutSessionControlFilter.setKickoutUrl("/kickout");
        return kickoutSessionControlFilter;
    }
}
