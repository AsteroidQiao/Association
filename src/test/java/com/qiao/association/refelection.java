package com.qiao.association;

import com.qiao.pojo.College;
import com.qiao.pojo.Users;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author AsteroidQiao
 * @Create 2023-03-13
 */
public class refelection {
    //普通方式调用
    public static void test01() {
        College college = new College();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000000000; i++) {
            college.getCname();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("执行10亿次时间：" + (endTime - startTime)+"ms");
    }
    //反射调用
    public static void test02() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        College college = new College();
        Class c1 = Class.forName("com.qiao.pojo.College");
        Method getCname = c1.getDeclaredMethod("getCname", null);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000000000; i++) {
            getCname.invoke(college,null);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("执行10亿次时间：" + (endTime - startTime)+"ms");
    }
    //反射调用 关闭检测
    public static void test03() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        College college = new College();
        Class c1 = Class.forName("com.qiao.pojo.College");
        Method getCname = c1.getDeclaredMethod("getCname", null);
        getCname.setAccessible(true);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000000000; i++) {
            getCname.invoke(college,null);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("执行10亿次时间：" + (endTime - startTime)+"ms");
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        test01();
        test02();
        test03();
    }

}

