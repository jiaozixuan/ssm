package com.jiaozx.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jiaozx.entity.DTO.UserLoginDTO;
import com.jiaozx.utils.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Set;

/**
 * @ClassName BaseController
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/10 17:36
 * @Version 1.0
 */

public class BaseController {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 根据登录用户查询权限
     *
     * @param :
     * @return UserLoginDTO
     * @author @jiaozx
     * @description TODO
     * @date 2022/8/10 17:36
     */
    protected UserLoginDTO getLoginUser() {

        //获取请求信息
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String token = request.getHeader("Authorization");
        if (null == token) {
            throw new RuntimeException("当前用户没有登陆");
        }
        Set<String> keys = redisTemplate.keys("*" + token);
        if (null == keys || 0 == keys.size()) {
            throw new RuntimeException("当前用户信息已过期");
        }
        String key = (String) keys.toArray()[0];
        return redisTemplate.getObject(key, new TypeReference<UserLoginDTO>() {
        });
    }
}
