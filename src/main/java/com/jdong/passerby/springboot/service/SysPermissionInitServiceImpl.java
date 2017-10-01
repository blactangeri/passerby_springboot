package com.jdong.passerby.springboot.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jdong.passerby.springboot.entity.SysPermissionInit;
import com.jdong.passerby.springboot.dao.SysPermissionInitMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jdong
 * @since 2017-09-30
 */
@Service
public class SysPermissionInitServiceImpl extends ServiceImpl<SysPermissionInitMapper, SysPermissionInit> {

    public List<SysPermissionInit> selectAll() {
        return baseMapper.selectAll();
    }
}
