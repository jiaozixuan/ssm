package com.jiaozx.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jiaozx.entity.DTO.UserLoginDTO;
import com.jiaozx.entity.PO.User;
import com.jiaozx.service.UserService;
import com.jiaozx.utils.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName LogginController
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/1 16:27
 * @Version 1.0
 */
@Slf4j
@RestController
public class LoginController {
    @Resource
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody @Validated User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            allErrors.forEach(error -> log.error("用户登录失败 login()方法 :{} ", error.getDefaultMessage()));
            return ResponseEntity.status(500).build();
        }

        UserLoginDTO userLoginVO = null;
        try {
            userLoginVO = userService.login(user.getUserName(), user.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.ok().body(userLoginVO);
    }

    @GetMapping("logout")
    public JSONResult logout(){

        userService.logout();

        return JSONResult.success("success");
    }
}
