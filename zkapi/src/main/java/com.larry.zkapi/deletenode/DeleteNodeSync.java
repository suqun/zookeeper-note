package com.larry.zkapi.deletenode;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created on 15/12/21
 */
public class DeleteNodeSync implements Watcher {
    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new DeleteNodeSync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(){
        try {
            zookeeper.delete("/node_sync_1", -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void process(WatchedEvent event) {
        // TODO Auto-generated method stub

        if (event.getState()== Event.KeeperState.SyncConnected){
            if (event.getType()== Event.EventType.None && null==event.getPath()){
                doSomething();
            }

        }
    }
}

/*

[zk: 192.168.1.107:2181(CONNECTED) 13] ls /
[node_async, node_sync_1, node_sync, zookeeper, node_sync_acl]


CONNECTING


[zk: 192.168.1.107:2181(CONNECTED) 14] ls /
[node_async, node_sync, zookeeper, node_sync_acl]

 */
