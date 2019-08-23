package com.tencet.test;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static void countNum() {
        List<String> collect = Stream.of(
                Arrays.asList(1),
                Arrays.asList(1, 2),
                Arrays.asList(1, 2, 3)
        )
                .flatMap(m -> m.stream())
                .map(m -> String.valueOf(m))
                .distinct()
                .collect(Collectors.toList());

        System.out.println(collect);
        System.out.println(collect.stream().count());
    }

    //将byte[]转为16进制
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String hex = null;
        for (int i=0;i<bytes.length;i++){
            hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length()==1){
                //得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(hex);
        }
        return stringBuffer.toString();
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