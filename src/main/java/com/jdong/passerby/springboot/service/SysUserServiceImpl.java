package com.jdong.passerby.springboot.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jdong.passerby.springboot.entity.FrontPage;
import com.jdong.passerby.springboot.entity.SysUser;
import com.jdong.passerby.springboot.entity.UserOnlineBO;
import com.jdong.passerby.springboot.mapper.SysUserMapper;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jdong
 * @since 2017-09-30
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    RedisSessionDAO redisSessionDAO;

    @Autowired
    SessionManager sessionManager;

    public Page<UserOnlineBO> getPagePlus(FrontPage<UserOnlineBO> frontPage) {
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        Iterator<Session> it = sessions.iterator();
        List<UserOnlineBO> onlineUserList = new ArrayList<>();
        Page<UserOnlineBO> pageList = frontPage.getPagePlus();

        while (it.hasNext()) {
            Session session = it.next();

            if (session.getAttribute("kickout") != null) {
                continue;
            }

            UserOnlineBO onlineUser = getSessionBO(session);

            if (onlineUser != null) {
                onlineUserList.add(onlineUser);
            }
        }

        int page = frontPage.getPage() - 1;
        int rows = frontPage.getRows() - 1;
        int size = onlineUserList.size();
        int startIdx = page * rows;
        int endIdx = Math.min(size, startIdx + rows);
        pageList.setRecords(onlineUserList.subList(startIdx, endIdx));
        pageList.setTotal(size);

        return pageList;
    }

    public void kickout(Serializable sessionId) {
        getSessionById(sessionId).setAttribute("kickout", true);
    }

    private Session getSessionById(Serializable sessionId) {
        return sessionManager.getSession(new DefaultSessionKey(sessionId));
    }

    private UserOnlineBO getSessionBO(Session session) {
        Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);

        if (obj == null) {
            return null;
        }

        if (obj instanceof SimplePrincipalCollection) {
            SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
            obj = spc.getPrimaryPrincipal();

            if (obj != null && obj instanceof SysUser) {
                UserOnlineBO userBo = new UserOnlineBO((SysUser) obj);
                userBo.setLastAccess(session.getLastAccessTime());
                userBo.setHost(session.getHost());
                userBo.setSessionId(session.getId().toString());
                userBo.setLastLoginTime(session.getLastAccessTime());
                userBo.setTimeout(session.getTimeout());
                userBo.setStartTime(session.getStartTimestamp());
                userBo.setSessionStatus(false);

                return userBo;
            }
        }

        return null;
    }
}
