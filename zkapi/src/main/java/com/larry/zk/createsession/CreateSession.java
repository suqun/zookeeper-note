package com.larry.zk.createsession;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created on 15/12/20
 */
public class CreateSession {
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        //异步设置连接
        zooKeeper = new ZooKeeper("192.168.1.107:2181",3000,new MyWatcher());
        System.out.println(zooKeeper.getState());

        //main函数等待watcher监听ß
        Thread.sleep(Integer.MAX_VALUE);
    }
}

/*
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
CONNECTING
收到事件:WatchedEvent state:SyncConnected type:None path:null
 */