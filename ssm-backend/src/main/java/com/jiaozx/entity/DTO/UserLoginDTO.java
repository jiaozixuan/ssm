package com.jiaozx.entity.DTO;

import com.jiaozx.entity.PO.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName UserLoginVO
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/1 16:38
 * @Version 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = 1960806386199948985L;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户唯一标识
     */
    private String token;
    /**
     * 登录时间
     */
    private Date loginTime;
    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     * 操作系统
     */
    private String os;
    /**
     * 用户信息
     */
    private User User;
}
