package com.larry.zkapi.deletenode;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Created on 15/12/21
 */
public class DeleteNodeAsync implements Watcher {
    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new DeleteNodeAsync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(){
        zookeeper.delete("/node_async", -1,new IVoidCallback(),"0000");
    }

    public void process(WatchedEvent event) {
        // TODO Auto-generated method stub

        if (event.getState()== Event.KeeperState.SyncConnected){
            if (event.getType()== Event.EventType.None && null==event.getPath()){
                doSomething();
            }

        }
    }

    private class IVoidCallback implements AsyncCallback.VoidCallback {
        public void processResult(int rc, String path, Object ctx) {
            StringBuilder sb = new StringBuilder();
            sb.append("rc="+rc).append("\n");
            sb.append("path"+path).append("\n");
            sb.append("ctx="+ctx).append("\n");
            System.out.println(sb.toString());
        }
    }
}

/*

[zk: 192.168.1.107:2181(CONNECTED) 15] ls /
[node_async, node_sync, zookeeper, node_sync_acl]


CONNECTING
rc=0
path/node_async
ctx=0000


[zk: 192.168.1.107:2181(CONNECTED) 16] ls /
[node_sync, zookeeper, node_sync_acl]
 */
