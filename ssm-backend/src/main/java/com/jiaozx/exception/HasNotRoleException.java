package com.jiaozx.exception;

import java.io.Serializable;

/**
 * @ClassName HasNotRoleException
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/11 14:13
 * @Version 1.0
 */
public class HasNotRoleException extends RuntimeException {

    private static final long serialVersionUID = -8625650907802178130L;
    public HasNotRoleException() {

    }

    public HasNotRoleException(String message) {
        super(message);
    }
}
