package com.jiaozx.interceptor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jiaozx.configuration.CustomObjectMapper;
import com.jiaozx.entity.DTO.UserLoginDTO;
import com.jiaozx.utils.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName LoginInterceptor
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/1 20:49
 * @Version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CustomObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断有没有Authorization这个请求头，拿到首部信息的Authorization的值
        ResponseEntity<String> res = ResponseEntity.status(401).body("Bad Credentials!");
        String token = request.getHeader("Authorization");
        String tokenKey = request.getHeader("username")+":"+token;

        if (token == null) {
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(res));
            return false;
        }
        Optional<UserLoginDTO> userLoginDTO = redisTemplate.getObject(token, new TypeReference<>() {
        });
        if (userLoginDTO == null) {
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(res));
            return false;
        }
        redisTemplate.expire(tokenKey,30 * 60L);

     /*   Set<String> keys = redisTemplate.keys(token);
        if (keys== null || keys.size() == 0){
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(res));
            return false;
        }
        String tokenKey = (String)keys.toArray()[0];
        // 3、使用token去redis中查看，有没有对应的loginUser
        Optional<UserLoginDTO> userLoginDTO = redisTemplate.getObject(tokenKey, new TypeReference<>() {
        });

        // 给token续命
        */

        return true;
    }
}
