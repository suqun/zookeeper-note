package com.larry.zkapi.createnode;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Created on 15/12/20
 * 异步创建节点
 */
public class CreateNodeAsync implements Watcher {
    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new CreateNodeAsync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event) {
        System.out.println("收到事件:" + event);
        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (event.getType() == Event.EventType.None && null == event.getPath()) {
                //创建节点
                createNode();
            }
        }
    }

    private void createNode() {
        zookeeper.create("/node_async", "456".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new IStringCallback(), "999");
        System.out.println("create node async");
    }

    private class IStringCallback implements AsyncCallback.StringCallback {
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println("rc:" + rc);
            System.out.println("path:" + path);
            System.out.println("ctx:" + ctx.toString());
            System.out.println("name:" + name);
        }
    }
}

/*
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
CONNECTING
收到事件:WatchedEvent state:SyncConnected type:None path:null
create node async
rc:0
path:/node_async
ctx:999
name:/node_async
 */
