package com.jiaozx.entity.PO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 操作日志(OperLog)实体类
 *
 * @author makejava
 * @since 2022-07-30 18:40:27
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperLog implements Serializable {
    private static final long serialVersionUID = -46951753264295408L;
    /**
     * 日志主键
     */
    private Integer operId;
    /**
     * 操作模块
     */
    private String title;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * api方法
     */
    private String method;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 操作人员
     */
    private String operName;
    /**
     * 请求url
     */
    private String operUrl;
    /**
     * 操作地址
     */
    private String operIp;
    /**
     * 操作状态
     */
    private Integer status;
    /**
     * 错误消息
     */
    private String errormsg;
    /**
     * 操作时间
     */
    private Date opertime;


}

