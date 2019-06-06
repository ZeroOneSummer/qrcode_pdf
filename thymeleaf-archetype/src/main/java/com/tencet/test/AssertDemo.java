package com.tencet.test;

/**
 * Created by pijiang on 2019/6/6.
 */
public class AssertDemo {

    //vm options 添加断言开关 -ea
    public static void main(String[] args) {
        assert false : "断言失败，抛出错误信息xxx";
        System.out.println("断言ok");
    }

}