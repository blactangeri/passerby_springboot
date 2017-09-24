package com.jdong.passerby.springboot.entity;

import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;

/**
 * Created by James on 9/22/17
 */
public class SysPermission extends Model<SysPermission> {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * URL address
     */
    private String url;
    /**
     * URL description
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
