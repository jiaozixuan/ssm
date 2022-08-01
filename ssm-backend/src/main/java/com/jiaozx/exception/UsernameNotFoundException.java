package com.jiaozx.exception;

/**
 * @ClassName UsernameNotFoundException
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/1 18:30
 * @Version 1.0
 */
public class UsernameNotFoundException extends Exception  {

    private static final long serialVersionUID = -6171051906557000400L;
    public UsernameNotFoundException() {
    }
    public UsernameNotFoundException(String message) {
        super(message);// 把参数传递给Throwable的带String参数的构造方法
    }
}
