package com.larry.api.zk.getchildren;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * Created on 15/12/20
 */
public class GetChildrenAsync implements Watcher {
    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new GetChildrenAsync());
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
                    zookeeper.getChildren(event.getPath(), true, new IChildren2Callback(), null);
                }
            }
        }
    }

    private void getChildren() {
        try {

            zookeeper.getChildren("/",true, new IChildren2Callback(),"ctx");

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    static class IChildren2Callback implements AsyncCallback.Children2Callback {

        public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
            StringBuilder sb = new StringBuilder();
            sb.append("rc="+rc).append("\n");
            sb.append("path="+path).append("\n");
            sb.append("ctx="+ctx).append("\n");
            sb.append("children="+children).append("\n");
            sb.append("stat="+stat).append("\n");
            System.out.println(sb.toString());
        }
    }
}



/*
CONNECTING
收到事件:WatchedEvent state:SyncConnected type:None path:null
rc=0
path=/
ctx=ctx
children=[node_async, node_sync_1, node_sync, zookeeper]
stat=0,0,0,0,0,2,0,0,0,4,4294967309
 */