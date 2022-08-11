package com.jiaozx.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jiaozx.configuration.CustomObjectMapper;
import com.jiaozx.entity.DTO.UserLoginDTO;
import com.jiaozx.entity.PO.Menu;
import com.jiaozx.entity.PO.Role;
import com.jiaozx.entity.PO.User;
import com.jiaozx.dao.UserDao;
import com.jiaozx.exception.PasswordIncorrectException;
import com.jiaozx.exception.UsernameNotFoundException;
import com.jiaozx.service.UserService;
import com.jiaozx.utils.RedisTemplate;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户信息表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-07-30 18:40:28
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CustomObjectMapper objectMapper;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Long userId) {
        return this.userDao.queryById(userId);
    }

    /**
     * 分页查询
     *
     * @param user        筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<User> queryByPage(User user, PageRequest pageRequest) {
        long total = this.userDao.count(user);
        return new PageImpl<>(this.userDao.queryAllByLimit(user, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User insert(User user) {
        this.userDao.insert(user);
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User update(User user) {
        this.userDao.update(user);
        return this.queryById(user.getUserId());
    }

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long userId) {
        return this.userDao.deleteById(userId) > 0;
    }

    /**
     * 登录
     *
     * @param userName :
     * @param password :
     * @return UserLoginVO
     * @author @jiaozx
     * @description TODO
     * @date 2022/8/1 16:47
     */
    @Override
    public UserLoginDTO login(String userName, String password) throws IOException {

        User user = userDao.queryByUserName(userName);
        if (null == user) throw new UsernameNotFoundException(userName + "--该用户不存在");
        if (!password.equals(user.getPassword())) throw new PasswordIncorrectException(userName + "--用户密码输入错误");

        //获取请求信息
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //获取浏览器信息
        UserAgent userAgent = new UserAgent(request.getHeader("User-Agent"));
        // 通过ip获取其所属的地址
        ResponseEntity<String> result = restTemplate.getForEntity("https://whois.pconline.com.cn/ipJson.jsp?ip=" + request.getRemoteHost() + "&json=true", String.class);
        String body = result.getBody();
        Map<String, String> map = objectMapper.readValue(body, new TypeReference<>() {
        });
        UUID uuid = UUID.randomUUID();
        UserLoginDTO userLoginDTO = UserLoginDTO.builder().token(uuid.toString()).userId(user.getUserId()).ipaddr(request.getRemoteAddr()).loginTime(new Date()).browser(userAgent.getBrowser().getName()).os(userAgent.getOperatingSystem().getName()).loginLocation(map.get("addr") + map.get("pro") + map.get("city") + map.get("region")).User(user).build();

        Set<String> keys = redisTemplate.keys(userName + ":*");
        keys.stream().forEach(key -> redisTemplate.remove(key));
        redisTemplate.setObject(userName + ":" + uuid.toString(), userLoginDTO, 30 * 60L);
        return userLoginDTO;
    }

    /**
     * 退出登录
     *
     * @param :
     * @return void
     * @author @jiaozx
     * @description TODO
     * @date 2022/8/10 17:34
     */
    @Override
    public void logout() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader("Authorization");
        String username = request.getHeader("username");
        redisTemplate.remove(username + ":" + token);
    }

    /**
     * 获取用户权限
     *
     * @param :
     * @return HashMap<String, List < String>>
     * @author @jiaozx
     * @description TODO
     * @date 2022/8/10 17:34
     */
    @Override
    public HashMap<String, List<String>> getInfo() {
        UserLoginDTO loginUser = getLoginUser();
        User info = userDao.getInfo(loginUser.getUserId());
        List<String> roles = info.getRoles().stream().map(Role::getRoleTag).collect(Collectors.toList());
        redisTemplate.setObject("roles:" + loginUser.getToken(), roles, 30 * 60L);
        List<String> perms = new ArrayList<>();
        info.getRoles().stream().map(Role::getMenus).forEach(menus -> {
            perms.addAll(menus.stream().map(Menu::getPerms).collect(Collectors.toList()));
        });
        redisTemplate.setObject("perms:" + loginUser.getToken(), perms, 30 * 60L);

        HashMap<String, List<String>> map = new HashMap<>();
        map.put("roles", roles);
        map.put("perms", perms);
        return map;
    }

    /**
     * 根据登录信息获取权限
     *
     * @param :
     * @return UserLoginDTO
     * @author @jiaozx
     * @description TODO
     * @date 2022/8/10 17:35
     */
    public UserLoginDTO getLoginUser() {

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
