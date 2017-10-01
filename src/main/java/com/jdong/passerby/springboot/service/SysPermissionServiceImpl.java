package com.jdong.passerby.springboot.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jdong.passerby.springboot.entity.SysPermission;
import com.jdong.passerby.springboot.mapper.SysPermissionMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jdong
 * @since 2017-09-30
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {
	
}
