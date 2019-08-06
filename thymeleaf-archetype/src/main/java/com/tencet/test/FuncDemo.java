package com.tencet.test;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by pijiang on 2019/8/6.
 * lambda函数式编程 + Guava本地缓存
 */
public class FuncDemo {

    private static ConcurrentHashMap<String, Object> map;
    private static LoadingCache<String, List<ClassDemo.User>> userCache;

    static void init() {
        userCache = CacheBuilder.newBuilder()
                                .maximumSize(100)
                                .expireAfterAccess(60, TimeUnit.SECONDS)
                                .build(new CacheLoader<String, List<ClassDemo.User>>() {
                                    @Override
                                    public List<ClassDemo.User> load(String s) throws Exception {
                                        List<ClassDemo.User> list = new ArrayList<>();
                                        list.add(new ClassDemo.User(201910001, "lisa", '女'));
                                        return list;
                                    }
                                });
    }


    private static <T> T doExecute(Function<String, T> func) throws Exception{
        if (map == null){
            map = new ConcurrentHashMap<>();
            map.put("users", userCache.get("users"));
            return func.apply("users");
        }else{
            return null;
        }
    }

    public static void main(String[] args) throws Exception{
        FuncDemo.init();
        Scanner scanner = new Scanner(System.in);
        System.out.print("please input a string key:");
        String key = scanner.next();
        String rs = FuncDemo.doExecute(k -> {
           if (!k.equals(key)){
               return "error key";
           } else {
               return "this key is ok, get values is " + JSON.toJSONString(map.get(key));
           }
        });
        System.out.println(rs);
    }
}