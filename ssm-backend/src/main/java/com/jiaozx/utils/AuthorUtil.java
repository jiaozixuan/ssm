package com.jiaozx.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jiaozx.constant.ConstantClassField;
import com.jiaozx.entity.DTO.UserLoginDTO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * @ClassName AuthorUtil
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/13 3:14
 * @Version 1.0
 */
public class AuthorUtil {

    public static UserLoginDTO getAuthor(RedisTemplate redisTemplate) {
        //获取请求信息
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader(ConstantClassField.HEAD_AUTHORIZATION);
        if (null == token) {
            throw new RuntimeException("当前用户没有登陆");
        }
        Set<String> keys = redisTemplate.keys("*" + token);
        if (null == keys || 0 == keys.size()) {
            throw new RuntimeException("当前用户信息已过期");
        }
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            if (str.contains("roles") || str.contains("perms")) {
                iter.remove();
            }
        }
        String key = (String) keys.toArray()[0];
        return redisTemplate.getObject(key, new TypeReference<>() {
        });
    }
}
