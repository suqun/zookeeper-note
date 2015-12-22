package com.larry.zk.getchildren;

import com.larry.zk.createnode.CreateNodeSync;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

/**
 * Created on 15/12/20
 */
public class GetChildrenSync implements Watcher {
    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new GetChildrenSync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event) {
        System.out.println("收到事件:" + event);
        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (event.getType() == Event.EventType.None && null == event.getPath()) {
                getChildren();
            } else {
                if (event.getType() == Event.EventType.NodeChildrenChanged) {
                    try {
                        System.out.println(zookeeper.getChildren(event.getPath(), true));
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void getChildren() {
        try {

            List<String> children = zookeeper.getChildren("/", true);
            System.out.println(children);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}


/*
CONNECTING
收到事件:WatchedEvent state:SyncConnected type:None path:null
[node_async, node_sync_1, node_sync, zookeeper]
 */