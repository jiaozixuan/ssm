package com.jiaozx.aspect;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jiaozx.annotation.HasPermission;
import com.jiaozx.annotation.HasRole;
import com.jiaozx.constant.ConstantClassField;
import com.jiaozx.exception.HasNotPermissionException;
import com.jiaozx.exception.HasNotRoleException;
import com.jiaozx.utils.RedisTemplate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName PermissionAspect
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/10 17:46
 * @Version 1.0
 */

@Component
@Aspect
public class PermissionAspect {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 通过@HasRole注解 校验用户访问接口所需要的角色权限
     *
     * @param joinPoint:
     * @param hasRole:
     * @return void
     * @author @jiaozx
     * @description TODO
     * @date 2022/8/11 14:17
     */
    @Before("@annotation(hasRole)")
    public void befoRole(JoinPoint joinPoint, HasRole hasRole) {
        String[] value = hasRole.value();
        //获取请求信息
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader(ConstantClassField.HEAD_AUTHORIZATION);
        List<String> rolesList = redisTemplate.getObject("roles:" + token, new TypeReference<>() {
        });
        boolean flag = false;
        flag = Arrays.asList(value).contains(rolesList);
        if (!flag) throw new HasNotRoleException("当前用户没有角色权限");
    }

    @Before("@annotation(hasPermission)")
    public void befoPremission(JoinPoint joinPoint, HasPermission hasPermission) {
        String[] value = hasPermission.value();
        //获取请求信息
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader(ConstantClassField.HEAD_AUTHORIZATION);
        List<String> permsList = redisTemplate.getObject("perms:" + token, new TypeReference<>() {
        });
        boolean flag = false;
        flag = Arrays.asList(value).contains(permsList);
        if (!flag) throw new HasNotPermissionException("当前用户没有角色权限");
    }
}
