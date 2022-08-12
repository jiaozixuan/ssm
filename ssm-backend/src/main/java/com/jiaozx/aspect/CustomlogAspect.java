package com.jiaozx.aspect;

import com.jiaozx.annotation.CustomLog;
import com.jiaozx.configuration.ThreadPoolConfiguration;
import com.jiaozx.entity.PO.OperLog;
import com.jiaozx.service.OperLogService;
import com.jiaozx.utils.AuthorUtil;
import com.jiaozx.utils.RedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @ClassName CustomlogAspect
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/12 16:22
 * @Version 1.0
 */

@Component
@Aspect
@Slf4j
public class CustomlogAspect {

    @Resource
    private OperLogService operLogService;

    @Resource
    private RedisTemplate redisTemplate;


    @AfterReturning("@annotation(customLog)")
    public void afterRunning(JoinPoint joinPoint, CustomLog customLog) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        OperLog operLog = createOperLog(request, joinPoint, customLog, null);
        operLogService.insert(operLog);
        log.info("{} ,执行了【{}】 操作", operLog.getOperName(), operLog.getTitle());
    }

    @AfterThrowing(value = "@annotation(customLog)", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, CustomLog customLog, Exception exception) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        OperLog operLog = createOperLog(request, joinPoint, customLog, exception);
        operLogService.insert(operLog);
        log.error("{} ,执行了【{}】 操作", operLog.getOperName(), operLog.getTitle(), exception);
    }


    private OperLog createOperLog(HttpServletRequest request, JoinPoint joinPoint, CustomLog customLog, Exception exception) {

        OperLog operLog = new OperLog();
        operLog.setTitle(customLog.title());
        operLog.setBusinessType(customLog.type());
        operLog.setStatus(200);
        if (null != exception) {
            operLog.setErrormsg(exception.getMessage().length() > 1000 ? exception.getMessage().substring(0, 500) : exception.getMessage());
            operLog.setStatus(200);
        }
        operLog.setOperIp(request.getRemoteAddr());
        operLog.setRequestMethod(request.getMethod());
        operLog.setOperUrl(request.getRequestURI());
        operLog.setOpertime(new Date());
        operLog.setMethod(joinPoint.getSignature().getName());
        operLog.setOperName(AuthorUtil.getAuthor(redisTemplate).getUser().getUserName());
        return operLog;
    }


}
