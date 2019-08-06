package com.tencet.test;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by pijiang on 2019/6/14.
 * java 反射机制获取实体属性
 */
@Slf4j
public class ClassDemo {

    @Data
    static class User{
        private int stuNo;
        private String name;
        private char age;

        public User(int stuNo, String name, char age) {
            this.stuNo = stuNo;
            this.name = name;
            this.age = age;
        }
    }

    public static <T> T ClassInfo(Class<T> clazz){
        T t = null;

        try {
            t = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for(Field f : fields){
                //1.获取set名称
                char[] chars = f.getName().toCharArray();
                chars[0]-=32; //ASCII码转大写
                String setName = "set".concat(String.valueOf(chars));
                //2.获取set方法
                Method method = clazz.getDeclaredMethod(setName, f.getType());
                //3.给对象赋值
                switch (f.getGenericType().toString()){
                    case "int":
                    case "class java.lang.Integer":
                        method.invoke(t, 1001);
                        break;
                    case "char":
                    case "class java.lang.Char":
                        method.invoke(t, '男');
                        break;
                    case "class java.lang.String":
                        method.invoke(t, "李海");
                        break;
                    default: break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    public static void main(String[] args){
        User user = ClassDemo.ClassInfo(User.class);
        System.out.println(JSON.toJSONString(user));
    }

}