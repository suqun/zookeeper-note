package com.larry.zk.createsession;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Created on 15/12/20
 */
public class MyWatcher implements Watcher {
    public void process(WatchedEvent event) {
        System.out.println("收到事件:"+event);
    }
}
