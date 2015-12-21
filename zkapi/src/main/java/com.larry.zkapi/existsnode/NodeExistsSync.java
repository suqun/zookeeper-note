package com.larry.zkapi.existsnode;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created on 15/12/21
 */
public class NodeExistsSync implements Watcher {
    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new NodeExistsSync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(){
        zookeeper.exists("/node_sync",true,new IStatCallback(),"context");
    }

    public void process(WatchedEvent event) {
        if (event.getState()== Event.KeeperState.SyncConnected){
            if (event.getType()== Event.EventType.None && null==event.getPath()){
                doSomething();
            }else{
                try {
                    if (event.getType()== Event.EventType.NodeCreated){
                        System.out.println(event.getPath()+" created");
                        zookeeper.exists(event.getPath(), true,new IStatCallback(),"context");
                    }
                    else if (event.getType()== Event.EventType.NodeDataChanged){
                        System.out.println(event.getPath()+" updated");
                        zookeeper.exists(event.getPath(), true,new IStatCallback(),"context");
                    }
                    else if (event.getType()== Event.EventType.NodeDeleted){
                        System.out.println(event.getPath()+" deleted");
                        zookeeper.exists(event.getPath(), true,new IStatCallback(),"context");
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

        }
    }

    private class IStatCallback implements AsyncCallback.StatCallback {
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            StringBuilder sb = new StringBuilder();
            sb.append("rc="+rc).append("\n");
            sb.append("path="+path).append("\n");
            sb.append("ctx="+ctx).append("\n");
            sb.append("Stat="+stat).append("\n");
            System.out.println(sb.toString());
        }
    }
}


/*
CONNECTING
rc=0
path=/node_sync
ctx=context
Stat=4294967302,8589934618,1450620202716,1450703589836,2,0,0,0,4,0,4294967302



[zk: 192.168.1.107:2181(CONNECTED) 10] set /node_sync 88890 -1
cZxid = 0x100000006
ctime = Sun Dec 20 22:03:22 CST 2015
mZxid = 0x20000001d
mtime = Mon Dec 21 21:20:15 CST 2015
pZxid = 0x100000006
cversion = 0
dataVersion = 3
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 5
numChildren = 0


/node_sync updated
rc=0
path=/node_sync
ctx=context
Stat=4294967302,8589934621,1450620202716,1450704015602,3,0,0,0,5,0,4294967302
 */