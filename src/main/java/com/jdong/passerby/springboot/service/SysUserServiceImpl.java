package com.jdong.passerby.springboot.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jdong.passerby.springboot.entity.SysUser;
import com.jdong.passerby.springboot.mapper.SysUserMapper;
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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
