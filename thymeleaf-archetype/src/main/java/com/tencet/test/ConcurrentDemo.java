package com.tencet.test;

import java.util.concurrent.*;

/**
 * Created by pijiang on 2019/6/6.
 */
public class ConcurrentDemo {

    /**
     * CountDownLatch 计数器
     *
     * public void await();   //调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行
     * public boolean await(long timeout, TimeUnit unit);  //和await()类似，只不过等待一定的时间后count值还没变为0的话就会继续执行
     * public void countDown();  //将count值减1
     */
    static class Demo1 {
        public static void main(String[] args) throws Exception{
            final  CountDownLatch count = new CountDownLatch(2);

            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "执行完成");
                count.countDown(); //计数器 -1
            }).start();

            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "执行完成");
                count.countDown(); //计数器 -1
            }).start();

            System.out.println("主线程开始阻塞，等待其他子线程结束...");
            count.await(); //主线程阻塞，等待计数器减为 0
            System.out.println("主线程阻塞释放，主线程继续执行...");
        }
    }

    /**
     * CyclicBarrier 可复用计数器
     */
    static class Demo2 {
        public static void main(String[] args){
            ExecutorService exec = Executors.newCachedThreadPool();

            CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
                @Override
                public void run() {
                    System.out.println("3个人都到齐了，开始happy吧");
                }
            });

            for (int i = 0; i < 3; i++){
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(Thread.currentThread().getName() + "到达现场，等待其他朋友");
                            barrier.await();
                            System.out.println(Thread.currentThread().getName() + "开始happy");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            exec.shutdown();
        }
    }


    /**
     * Semaphore 计数信号量
     *
     * Semaphore(int permits):构造方法，创建具有给定许可数的计数信号量并设置为非公平信号量。
     * Semaphore(int permits,boolean fair):构造方法，当fair等于true时，创建具有给定许可数的计数信号量并设置为公平信号量。
     * void acquire():从此信号量获取一个许可前线程将一直阻塞。相当于一辆车占了一个车位。
     * void acquire(int n):从此信号量获取给定数目许可，在提供这些许可前一直将线程阻塞。比如n=2，就相当于一辆车占了两个车位。
     * void release():释放一个许可，将其返回给信号量。就如同车开走返回一个车位。
     * void release(int n):释放n个许可。
     * int availablePermits()：当前可用的许可数。
     */
    static class Demo3 {
        public static void main(String[] args){
            ExecutorService exec = Executors.newCachedThreadPool();
            Semaphore semaphore = new Semaphore(3); //3个车位

            for (int i = 0; i < 5; i++){ //5辆车等待停车
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("当前车位："+semaphore.availablePermits());
                            semaphore.acquire();
                            System.out.println(Thread.currentThread().getName() + "获取车位，开始停车");
                            Thread.sleep(1000);
                            semaphore.release();
                            System.out.println(Thread.currentThread().getName() + "释放车位，开车离开");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            exec.shutdown();
        }
    }


}