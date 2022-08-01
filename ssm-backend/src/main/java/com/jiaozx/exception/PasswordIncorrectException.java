package com.jiaozx.exception;

/**
 * @ClassName PasswordIncorrectException
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/1 18:31
 * @Version 1.0
 */
public class PasswordIncorrectException extends Exception  {

    private static final long serialVersionUID = -4997208847022642762L;

    public PasswordIncorrectException(){

    }
    public PasswordIncorrectException(String message) {
        super(message);
    }
}
