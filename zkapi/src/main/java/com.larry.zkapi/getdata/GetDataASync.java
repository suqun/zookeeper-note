package com.larry.zkapi.getdata;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created on 15/12/21
 */
public class GetDataASync implements Watcher {


    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new GetDataASync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(ZooKeeper zookeeper){


        zookeeper.getData("/node_sync_acl", true, new IDataCallback(), null);

    }

    public void process(WatchedEvent event) {
        if (event.getState()== Event.KeeperState.SyncConnected){
            if (event.getType()== Event.EventType.None && null==event.getPath()){
                doSomething(zookeeper);
            }else{
                if (event.getType()== Event.EventType.NodeDataChanged){
                    try {
                        zookeeper.getData(event.getPath(), true, new IDataCallback(), null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    static class IDataCallback implements AsyncCallback.DataCallback{

        public void processResult(int rc, String path, Object ctx, byte[] data,
                                  Stat stat) {
            try {
                System.out.println(new String(zookeeper.getData(path, true, stat)));
                System.out.println("stat:"+stat);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }

}

/*
CONNECTING
aclValue
stat:8589934623,8589934623,1450704846599,1450704846599,0,0,0,0,8,0,8589934623
 */