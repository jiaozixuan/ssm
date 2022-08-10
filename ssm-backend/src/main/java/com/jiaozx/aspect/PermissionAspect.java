package com.jiaozx.aspect;

import com.jiaozx.annotation.HasRole;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

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

    @Before("@annotation(hasRole)")
    public void befoRole(JoinPoint joinPoint, HasRole hasRole) {
        String[] value = hasRole.value();
    }
}
