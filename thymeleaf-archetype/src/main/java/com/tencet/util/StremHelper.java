package com.tencet.util;

import com.tencet.entity.UserEntity;

import java.util.function.Function;

/**
 * Created by pijiang on 2019/6/5.
 */
public class StremHelper {

    //lambda表达式声明接口
    static <T> T doSomething(Function<UserEntity, T> function){
        UserEntity userEntity = new UserEntity().setAge(10).setName("张三");
        System.out.println("function 前置处理 -> id:" + userEntity.getId());
        T t = function.apply(userEntity);
        return t;
    }

    //测试
    public static void main(String[] args) {
        String rs = StremHelper.doSomething(userEntity -> {
            userEntity.setId((short) 1001);
            System.out.println("function 主体实现 -> id:" + userEntity.getId());
            return "OK";
        });
        System.out.println("处理结果：" + rs);
    }
}