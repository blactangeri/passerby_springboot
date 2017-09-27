package com.jdong.passerby.springboot.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Session + User Object
 * Created by James on 9/27/17
 */
public class UserOnlineBO extends SysUser implements Serializable {

    private static final long serialVersion = 1L;

    // Session Id
    private String sessionId;
    // Session Host
    private String host;
    // Session creation time
    private Date startTime;
    // Session last interacted
    private Date lastAccess;
    // Session timeout
    private long timeout;
    // Session kicked out or not
    private boolean sessionStatus = Boolean.TRUE;

    public UserOnlineBO() {
    }

    public UserOnlineBO(SysUser user) {
        super(user);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(boolean sessionStatus) {
        this.sessionStatus = sessionStatus;
    }
}
