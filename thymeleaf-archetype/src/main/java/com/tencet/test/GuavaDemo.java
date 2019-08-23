package com.tencet.test;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.Arrays;

/**
 * Created by pijiang on 2019/8/8.
 */
public class GuavaDemo {

    //string连接
    public static void test1(){
        String str = Joiner.on("_").skipNulls().join(Arrays.asList("12", null, " ", "345", " 678 "));
        System.out.println(str);
    }

    //string拆分
    public static void test2(){
        String str = ",a,,b,";
        Iterable<String> itera = Splitter.on(',').trimResults().omitEmptyStrings().split(str);
        itera.forEach(System.out::println);
    }

    //string大小写转换
    public static void test3(){
        CaseFormat upperCamel = CaseFormat.UPPER_CAMEL;
        CaseFormat upperUnderscore = CaseFormat.UPPER_UNDERSCORE;
        String str = "USER_INTO_REVLATION";
        String s = upperUnderscore.to(upperCamel, str);
        System.out.println(s);
    }

    public static void main(String[] args) {
//        GuavaDemo.test1();
//        GuavaDemo.test2();
        GuavaDemo.test3();
    }
}