package com.jdong.passerby.springboot.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jdong.passerby.springboot.entity.SysRole;
import com.jdong.passerby.springboot.mapper.SysRoleMapper;
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
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
	
}
