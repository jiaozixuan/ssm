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
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @ClassName 日志
 * @Description 操作日志切面处理类，用于记录用户操作日志
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

    // 使用线程池异步保存日志，提高性能
    @Resource(name = ThreadPoolConfiguration.DEFAULT_TASK_EXECUTOR_NAME)
    private Executor executor;

    @Pointcut("@annotation(com.jiaozx.annotation.CustomLog)")
    public void logPointcut() {
    }

    @AfterReturning(pointcut = "logPointcut()", returning = "customLog")
    public void afterRunning(JoinPoint joinPoint, CustomLog customLog) {
        logAndSave(joinPoint, customLog, null);
    }

    @AfterThrowing(value = "@annotation(customLog)", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, CustomLog customLog, Exception exception) {
        logAndSave(joinPoint, customLog, exception);
    }

    /**
     * 异步记录日志信息
     */
    private void logAndSave(JoinPoint joinPoint, CustomLog customLog, Exception exception) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                    RequestContextHolder.getRequestAttributes())).getRequest();
            
            // 异步执行日志记录
            executor.execute(() -> {
                try {
                    OperLog operLog = createOperLog(request, joinPoint, customLog, exception);
                    operLogService.insert(operLog);
                    
                    // 根据是否有异常记录不同级别的日志
                    if (exception == null) {
                        log.info("{} ,执行了【{}】操作", operLog.getOperName(), operLog.getTitle());
                    } else {
                        log.error("{} ,执行了【{}】操作", operLog.getOperName(), operLog.getTitle(), exception);
                    }
                } catch (Exception e) {
                    log.error("记录操作日志失败", e);
                }
            });
        } catch (Exception e) {
            log.error("获取请求上下文失败", e);
        }
    }

    private OperLog createOperLog(HttpServletRequest request, JoinPoint joinPoint, CustomLog customLog, Exception exception) {
        OperLog operLog = new OperLog();
        
        try {
            // 设置日志基本信息
            operLog.setTitle(customLog.title());
            operLog.setBusinessType(customLog.type());
            operLog.setRequestMethod(request.getMethod());
            operLog.setOperUrl(request.getRequestURI());
            operLog.setOpertime(new Date());
            operLog.setMethod(joinPoint.getSignature().getName());
            
            // 获取客户端IP
            String clientIp = getClientIP(request);
            operLog.setOperIp(clientIp);
            
            // 设置操作人信息
            operLog.setOperName(AuthorUtil.getAuthor(redisTemplate).getUser().getUserName());
            
            // 处理异常信息
            if (exception != null) {
                String errorMsg = exception.getMessage();
                if (errorMsg != null && errorMsg.length() > 1000) {
                    errorMsg = errorMsg.substring(0, 997) + "...";
                }
                operLog.setErrormsg(errorMsg);
                operLog.setStatus(500); // 异常状态码设为500
            } else {
                operLog.setStatus(200); // 正常状态码设为200
            }
        } catch (Exception e) {
            log.warn("构建操作日志异常", e);
            // 即使部分信息构建失败，也要保证基本日志可用
            operLog.setTitle("未知操作");
            operLog.setOperIp("unknown");
            operLog.setOperName("anonymous");
            operLog.setErrormsg("构建日志时发生异常：" + e.getMessage());
            operLog.setStatus(500);
        }
        
        return operLog;
    }

    /**
     * 获取客户端真实IP地址（考虑代理情况）
     */
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
