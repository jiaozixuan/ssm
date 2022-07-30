package com.jiaozx.service.impl;

import com.jiaozx.service.YdlUserService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 用户信息表(YdlUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-30 18:38:15
 */
@Service("ydlUserService")
public class YdlUserServiceImpl implements YdlUserService {
    @Resource
    private YdlUserDao ydlUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    @Override
    public YdlUser queryById(Long userId) {
        return this.ydlUserDao.queryById(userId);
    }

    /**
     * 分页查询
     *
     * @param ydlUser 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<YdlUser> queryByPage(YdlUser ydlUser, PageRequest pageRequest) {
        long total = this.ydlUserDao.count(ydlUser);
        return new PageImpl<>(this.ydlUserDao.queryAllByLimit(ydlUser, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param ydlUser 实例对象
     * @return 实例对象
     */
    @Override
    public YdlUser insert(YdlUser ydlUser) {
        this.ydlUserDao.insert(ydlUser);
        return ydlUser;
    }

    /**
     * 修改数据
     *
     * @param ydlUser 实例对象
     * @return 实例对象
     */
    @Override
    public YdlUser update(YdlUser ydlUser) {
        this.ydlUserDao.update(ydlUser);
        return this.queryById(ydlUser.getUserId());
    }

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long userId) {
        return this.ydlUserDao.deleteById(userId) > 0;
    }
}
