package com.jiaozx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jiaozx.entity.DTO.UserLoginDTO;
import com.jiaozx.entity.PO.User;
import com.jiaozx.exception.PasswordIncorrectException;
import com.jiaozx.exception.UsernameNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.List;

/**
 * 用户信息表(User)表服务接口
 *
 * @author makejava
 * @since 2022-07-30 18:40:28
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    User queryById(Long userId);

    /**
     * 分页查询
     *
     * @param user        筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<User> queryByPage(User user, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User update(User user);

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    boolean deleteById(Long userId);

    /**
     * 用户登录
     *
     * @param userName:
     * @param password:
     * @return UserLoginVO
     * @author @jiaozx
     * @description TODO
     * @date 2022/8/1 16:47
     */
    UserLoginDTO login(String userName, String password) throws JsonProcessingException, UsernameNotFoundException, PasswordIncorrectException;

    /**
     * 退出登录
     *
     * @param :
     * @return void
     * @author @jiaozx
     * @description TODO
     * @date 2022/8/10 17:34
     */
    void logout();

    /**
     * 查询用户权限
     *
     * @param :
     * @return null
     * @author @jiaozx
     * @description TODO
     * @date 2022/8/10 17:32
     */
    HashMap<String, List<String>> getInfo();
}
