package com.larry.zk.getdata;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created on 15/12/21
 */
public class GetDataSync implements Watcher {
    private static ZooKeeper zookeeper;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new GetDataSync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething() {
        //添加权限
        zookeeper.addAuthInfo("digest", "larry:123456".getBytes());
        try {
            System.out.println(new String(zookeeper.getData("/node_sync_acl", true, stat)));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void process(WatchedEvent event) {
        // TODO Auto-generated method stub

        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (event.getType() == Event.EventType.None && null == event.getPath()) {
                doSomething();
            } else {
                if (event.getType() == Event.EventType.NodeDataChanged) {
                    try {
                        System.out.println(new String(zookeeper.getData(event.getPath(), true, stat)));
                        System.out.println("stat:" + stat);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
    }
}

/*
 CONNECTING
 aclValue
 */