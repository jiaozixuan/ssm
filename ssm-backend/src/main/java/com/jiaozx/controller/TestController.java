package com.jiaozx.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/7/28 17:16
 * @Version 1.0
 */

@Controller
public class TestController {

    @GetMapping("user")
    @ResponseBody
    public String test() {
        return "index";
    }
}

@NoArgsConstructor
@Data
class User {

    String username;
    Integer age;

    public User(String username, Integer age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

