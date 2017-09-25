package com.jdong.passerby.springboot.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.jdong.passerby.springboot.entity.SysUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by James on 9/21/17
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

    private String kickoutUrl;
    private boolean kickoutAfter = false;
    private int maxSession = 1;
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro_redis_cache");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);

        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;
        }

        Session session = subject.getSession();
        SysUser user = (SysUser) subject.getPrincipal();
        String username = user.getNickname();
        Serializable sessionId = session.getId();
        Deque<Serializable> deque = cache.get(username);

        if (deque == null) {
            deque = new LinkedList<>();
        }

        if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.add(sessionId);
            cache.put(username, deque);
        }

        while (deque.size() > maxSession) {
            Serializable kickoutSessionId;
            if (kickoutAfter) {
                kickoutSessionId = deque.removeFirst();
                cache.put(username, deque);
            } else {
                kickoutSessionId = deque.removeLast();
                cache.put(username, deque);
            }

            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));

                if (kickoutSession != null) {
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception ignored) {
            }
        }

        if (session.getAttribute("kickout") != null && (Boolean) session.getAttribute("kickout")) {
            try {
                subject.logout();
            } catch (Exception ignored) {
            }

            saveRequest(request);

            Map<String, String> resultMap = new HashMap<>();

            // check whether this is an ajax request
            if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
                resultMap.put("user_status", "300");
                resultMap.put("message", "You are already logged in.");
                out(response, resultMap);
            } else {
                WebUtils.issueRedirect(request, response, kickoutUrl);
            }

            return false;
        }

        return true;
    }

    private void out(ServletResponse response, Map<String, String> resultMap) {
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            out.println(JSON.toJSONString(resultMap));
            out.flush();
            out.close();
        } catch (Exception e) {
            System.err.println("KickoutSessionFilter.class outputting json exception. Ignored.");
        }
    }
}
