package com.jdong.passerby.springboot.dao;

import com.jdong.passerby.springboot.entity.SysPermissionInit;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author jdong
 * @since 2017-09-30
 */
public interface SysPermissionInitMapper extends BaseMapper<SysPermissionInit> {

    List<SysPermissionInit> selectAll();
}