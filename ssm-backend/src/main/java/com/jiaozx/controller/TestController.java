package com.jiaozx.controller;

import com.jiaozx.utils.RedisTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/7/28 17:16
 * @Version 1.0
 */

@Controller
public class TestController {

    @Autowired
    RedisTemplate template;
    @GetMapping("user")
    @ResponseBody
    public User test() {
        List<Integer> integers = List.of(1, 2);
        template.setObject("list",integers,-1L);
        return new User("tom",12);
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

