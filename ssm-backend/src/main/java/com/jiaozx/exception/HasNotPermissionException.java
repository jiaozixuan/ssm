package com.jiaozx.exception;

/**
 * @ClassName HasNotPremissionException
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/11 14:23
 * @Version 1.0
 */
public class HasNotPermissionException extends RuntimeException{

    private static final long serialVersionUID = 5520900806712128246L;
    public HasNotPermissionException() {

    }

    public HasNotPermissionException(String message) {
        super(message);
    }
}
