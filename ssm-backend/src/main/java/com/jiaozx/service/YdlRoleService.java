package com.jiaozx.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 角色信息表(YdlRole)表服务接口
 *
 * @author makejava
 * @since 2022-07-30 18:38:15
 */
public interface YdlRoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    YdlRole queryById(Long roleId);

    /**
     * 分页查询
     *
     * @param ydlRole 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<YdlRole> queryByPage(YdlRole ydlRole, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param ydlRole 实例对象
     * @return 实例对象
     */
    YdlRole insert(YdlRole ydlRole);

    /**
     * 修改数据
     *
     * @param ydlRole 实例对象
     * @return 实例对象
     */
    YdlRole update(YdlRole ydlRole);

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 是否成功
     */
    boolean deleteById(Long roleId);

}
