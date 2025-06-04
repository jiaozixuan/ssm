package main;

import com.jiaozx.entity.PO.OperLog;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @ClassName Test
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/11 21:02
 * @Version 1.0
 */
public class Test {

    public static void main(String[] args) {

            //定义一个数组
            int[] currentData = {1,2,3,4};
            AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(currentData);
            System.out.println("获取数组下标为1的值：" + atomicIntegerArray.get(1));
            System.out.println("获取数组下标为2的值：" + atomicIntegerArray.incrementAndGet(2));
            //比较和交换
            System.out.println(atomicIntegerArray.compareAndSet(3, 4, 5));
            System.out.println(atomicIntegerArray.get(3));
            //先累加，再将值放回
            System.out.println(atomicIntegerArray.addAndGet(1, 10));
            //在多线程中不会立即刷新缓存，不能保证可见性
            atomicIntegerArray.lazySet(2,10);
            System.out.println(atomicIntegerArray.get(2));


    }

    private static String sb() {
       return 1+2+"3";
    }


}
