package com.lk.zk.util;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class ZookeeperProSync implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws  Exception{
        //zookeeper 配置数据存放路径
        String path = "/username";
        //连接zookeeper并且注册一个默认的监听器
        zooKeeper = new ZooKeeper("ip:port",5000,new ZookeeperProSync());
        //等待zk连接成功的通知
        countDownLatch.await();
        //获取path目录节点的配置数据，并注册默认的监听器
        System.out.println(new String(zooKeeper.getData(path,true,stat)));

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()){//zk连接成功通知事件
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()){
                countDownLatch.countDown();
            }else if (Event.EventType.NodeDataChanged==watchedEvent.getType()){
                try {
                    System.out.println("配置已修改：新值为："+new String(zooKeeper.getData(watchedEvent.getPath(),true,stat)));
                }catch (Exception e){

                }
            }
        }
    }
}
