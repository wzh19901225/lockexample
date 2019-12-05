package com.lk.zk.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DistributedLockDemo {

    /** session超时时间 */
    static final int SESSION_OUTTIME = 5000;//ms

    static int count = 10;
    public static void genarNo(){
        try {
            count--;
            System.out.println(count);
        } finally {

        }
    }

    public static void main(String[] args) throws Exception {

        //1 重试策略：初次间隔时间为1s 重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        //2 通过工厂创建连接
        CuratorFramework cf = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(SESSION_OUTTIME)
                .retryPolicy(retryPolicy)
//					.namespace("super")
                .build();
        //3 开启连接
        cf.start();

        //4 分布式锁
        final InterProcessMutex lock = new InterProcessMutex(cf, "/super");
        final CountDownLatch countdown = new CountDownLatch(1);

        for(int i = 0; i < 10; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        countdown.await();
                        //加锁
                        lock.acquire();
                        System.out.println(Thread.currentThread().getName()+" 开始执行业务逻辑...");

                        TimeUnit.SECONDS.sleep(2);//业务处理中...

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {

                            System.out.println(Thread.currentThread().getName()+" 结束\n");
                            //释放
                            lock.release();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            },"t" + i).start();
        }
        Thread.sleep(100);
        countdown.countDown();

    }
}
