package com.jiaozx.controller;

import com.jiaozx.annotation.CustomLog;
import com.jiaozx.annotation.HasPermission;
import com.jiaozx.annotation.HasRole;
import com.jiaozx.entity.DTO.PageDTO;
import com.jiaozx.entity.DTO.UserLoginDTO;
import com.jiaozx.entity.PO.User;
import com.jiaozx.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 用户信息表(User)表控制层
 *
 * @author makejava
 * @since 2022-07-30 18:53:08
 */
@RestController
@RequestMapping("user")
@Api(value = "用户的操作类", tags = "用户")
@Slf4j
public class UserController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 分页查询
     *
     * @param user    筛选条件
     * @param pageDTO 分页对象
     * @return 查询结果
     */
    @GetMapping
    @CustomLog(type = "用户",title = "分页查询")
    public ResponseEntity<Page<User>> queryByPage(User user, PageDTO pageDTO) {
        return ResponseEntity.ok(this.userService.queryByPage(user, PageRequest.of(pageDTO.getPage(), pageDTO.getSize())));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @HasPermission("system:user")
    @HasRole("")
    public ResponseEntity<User> queryById(@PathVariable("id") Long id) {
        UserLoginDTO loginUser = getLoginUser();
        return ResponseEntity.ok(this.userService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param user 实体
     * @return 新增结果
     */
    @PostMapping
    @HasRole("admin")
    @CustomLog(title = "用户添加" , type = "用户")

    public ResponseEntity<User> add(User user) {
        return ResponseEntity.ok(this.userService.insert(user));
    }

    /**
     * 编辑数据
     *
     * @param user 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<User> edit(User user) {
        return ResponseEntity.ok(this.userService.update(user));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.userService.deleteById(id));
    }


    /**
     * 查询用户权限并且把权限放到redis中
     *
     * @param :
     * @return null
     * @author @jiaozx
     * @description TODO
     * @date 2022/8/10 17:32
     */
    @RequestMapping(value = "getInfo",method = RequestMethod.GET,produces="application/json; charset=UTF-8")
    @ApiOperation(value = "查询用户权限",notes = "test")
//    @ApiImplicitParam(paramType = "path",name = "id",value = "用户主键",required = true,dataType = "int")
    public ResponseEntity<HashMap<String, List<String>>> getInfo() {

        return ResponseEntity.ok(this.userService.getInfo());
    }
}

