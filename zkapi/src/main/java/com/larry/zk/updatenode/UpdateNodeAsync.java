package com.larry.zk.updatenode;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created on 15/12/21
 */
public class UpdateNodeAsync implements Watcher {
    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new UpdateNodeAsync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(){
        zookeeper.setData("/node_sync_1","7899".getBytes(),-1,new IStatCallback(),"上下文");
    }

    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (event.getType()== Event.EventType.None && null==event.getPath()){
                doSomething();
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
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
CONNECTING
rc=0
path=/node_sync_1
ctx=上下文
Stat=4294967306,8589934603,1450620267570,1450703020732,2,0,0,0,4,0,4294967306

[zk: 192.168.1.107:2181(CONNECTED) 4] get /node_sync_1
1234
cZxid = 0x10000000a
ctime = Sun Dec 20 22:04:27 CST 2015
mZxid = 0x200000009
mtime = Mon Dec 21 20:59:11 CST 2015
pZxid = 0x10000000a
cversion = 0
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 4
numChildren = 0
[zk: 192.168.1.107:2181(CONNECTED) 5] get /node_sync_1
7899
cZxid = 0x10000000a
ctime = Sun Dec 20 22:04:27 CST 2015
mZxid = 0x20000000b
mtime = Mon Dec 21 21:03:40 CST 2015
pZxid = 0x10000000a
cversion = 0
dataVersion = 2
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 4

 */
