package com.jdong.passerby.springboot.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by James on 9/22/17
 */
@TableName("sys_user")
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nickname;
    private String email;
    private String pswd;

    @TableField("last_login_time")
    private Date lastLoginTime;

    private String status;

    @TableField(value = "last_update_name_id", strategy = FieldStrategy.IGNORED)
    private String lastUpdateNameId;

    @TableField(value = "create_name_id")
    private String createNameId;

    @TableField(value = "last_update_time", strategy = FieldStrategy.IGNORED)
    private Date lastUpdateTime;

    @TableField(value = "create_time")
    private Date createTime;

    public SysUser() {
    }

    public SysUser(SysUser user) {
        super();
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.pswd = user.getPswd();
        this.createTime = user.getCreateTime();
        this.lastLoginTime = user.getLastLoginTime();
        this.status = user.getStatus();
        this.createNameId = user.getCreateNameId();
        this.lastUpdateNameId = user.getLastUpdateNameId();
        this.lastUpdateTime = user.getLastUpdateTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastUpdateNameId() {
        return lastUpdateNameId;
    }

    public void setLastUpdateNameId(String lastUpdateNameId) {
        this.lastUpdateNameId = lastUpdateNameId;
    }

    public String getCreateNameId() {
        return createNameId;
    }

    public void setCreateNameId(String createNameId) {
        this.createNameId = createNameId;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
