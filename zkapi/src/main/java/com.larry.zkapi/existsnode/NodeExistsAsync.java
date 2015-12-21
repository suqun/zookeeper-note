package com.larry.zkapi.existsnode;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created on 15/12/21
 */
public class NodeExistsAsync implements Watcher {
    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new NodeExistsAsync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(){
        try {
            zookeeper.exists("/node_sync",false);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void process(WatchedEvent event) {
        if (event.getState()== Event.KeeperState.SyncConnected){
            if (event.getType()== Event.EventType.None && null==event.getPath()){
                doSomething();
            }else{
                try {
                    if (event.getType()== Event.EventType.NodeCreated){
                        System.out.println(event.getPath()+" created");
                        System.out.println(zookeeper.exists(event.getPath(), true));
                    }
                    else if (event.getType()== Event.EventType.NodeDataChanged){
                        System.out.println(event.getPath()+" updated");
                        System.out.println(zookeeper.exists(event.getPath(), true));
                    }
                    else if (event.getType()== Event.EventType.NodeDeleted){
                        System.out.println(event.getPath()+" deleted");
                        System.out.println(zookeeper.exists(event.getPath(), true));
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

        }
    }
}
