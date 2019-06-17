package com.tencet.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by pijiang on 2019/6/17.
 */
public class OtherDemo {

    //获取系统参数
    static void test1(){
        Properties properties = System.getProperties();
        properties.stringPropertyNames()
                .stream()
                .forEach(key -> System.out.println(key + ":" + properties.get(key)));
    }

    //获取文件路径
    static void test2(){
        Path path = Paths.get("./download", "a.txt");
        if (Files.exists(path, new LinkOption[0])){
            String p = path.toFile().getPath();
            System.out.println(p);
        }
        System.out.println("this path is null .");
    }

    //list转数组
    static void test3(){
        ArrayList list = new ArrayList();
        list.add("a");
        list.add("b");
        String[] strs = (String[]) list.toArray(new String[0]);
        System.out.println(Arrays.toString(strs));
    }

    //String常用方法
    static void test4(){
        String str = "welcome to Beijing";
        System.out.println(str.concat(" my friend."));
        System.out.println(str.contains("to"));
        System.out.println(str.startsWith("wel"));
        System.out.println(str.intern());   //String.intern()方法主要适用于程序中需要保存有限个会被反复使用的值的场景,减少内存消耗
    }

    //类加载
    static void test5() throws Exception{
        Class<?> streamDemo = OtherDemo.class.getClassLoader().loadClass("AppWeb");
        System.out.println(streamDemo.getName());
        Class<?> appWeb = Class.forName("AppWeb");
        System.out.println(appWeb.getName());
    }

    //代理接口begin
    static void test6() throws Exception{
        Animal dog = new Dog();
        Animal animal = (Animal)Proxy.newProxyInstance(dog.getClass().getClassLoader(), Animal.class.getInterfaces(), new AnimalInvocationHandler(dog));
        animal.eat("骨头");
    }

    interface Animal{
        void eat(String food);
    }

    static class Dog implements Animal{
        @Override
        public void eat(String food) {
            System.out.println("狗在吃"+food);
        }
    }

    static class AnimalInvocationHandler implements InvocationHandler {
        private final Animal animal;

        public AnimalInvocationHandler(Animal animal) {
            this.animal = animal;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object invoke = method.invoke(animal, args);
            return invoke;
        }
    }
    //代理接口end

    public static void main(String[] args) throws Exception{
        OtherDemo.test6();
    }

}