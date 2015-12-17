package com.larry.api;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Created on 15/12/17
 */
public class CreateNodeSync implements Watcher{

    private static ZooKeeper zookeeper;
    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.101:2181",5000,new CreateNodeSync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(){
        try {
            String path = zookeeper.create("/node_4", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("return path:"+path);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("do something");
    }

    public void process(WatchedEvent event) {
        // TODO Auto-generated method stub
        System.out.println("ÊÕµ½ÊÂ¼þ£º"+event);
        if (event.getState()== Watcher.Event.KeeperState.SyncConnected){
            if (event.getType()== Watcher.Event.EventType.None && null==event.getPath()){
                doSomething();
            }
        }
    }
}
