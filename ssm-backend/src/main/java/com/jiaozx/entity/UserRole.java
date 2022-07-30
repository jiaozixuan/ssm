package com.jiaozx.entity;

import java.io.Serializable;

/**
 * 用户和角色关联表(UserRole)实体类
 *
 * @author makejava
 * @since 2022-07-30 18:40:28
 */
public class UserRole implements Serializable {
    private static final long serialVersionUID = 840660354879088176L;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long roleId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}

