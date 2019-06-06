package com.tencet.test;

import com.tencet.entity.UserEntity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by pijiang on 2019/6/5.
 */
public class LockDemo {

    //lock之wait、notify
    static class Demo1{
        static void show(){
            final UserEntity user = new UserEntity();

            new Thread(() -> {
                synchronized (user){
                    try {
                        System.out.println("thread 2 get lock");
                        user.wait();
                        Thread.sleep(2000);
                        System.out.println("thread 2 release lock");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                synchronized (user){
                    try {
                        System.out.println("thread 3 get lock");
                        user.wait();
                        Thread.sleep(2000);
                        System.out.println("thread 3 release lock");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                synchronized (user){
                    try {
                        System.out.println("thread 1 get lock");
                        user.notifyAll();
                        Thread.sleep(2000);
                        System.out.println("thread 1 release lock");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        public static void main(String[] args) {
            Demo1.show();
        }
    }


    //lock之lock、unlock
    static class Demo2 implements Runnable{
        int tickets = 10;
        Lock lock = new ReentrantLock();

        @Override
        public void run() {
            String tName = Thread.currentThread().getName();
            try {
                System.out.println(tName + "开始售票...");
                lock.lock(); //上锁
                do {
                    tickets--;
                    try {
                        Thread.sleep(500);
                        System.out.println(String.format(tName + "已经售出第[%d]张车票，还剩[%d]张车票", (10-tickets), tickets));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (tickets > 0);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock(); //解锁
                System.out.println(tName + "票已售罄...");
            }
        }

        public static void main(String[] args) {
            new Thread(new Demo2(), "【窗口1】").start();
            new Thread(new Demo2(), "【窗口2】").start();
        }
    }

}