package com.larry.api.zk.updatenode;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created on 15/12/21
 */
public class UpdateNodeSync implements Watcher {
    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new UpdateNodeSync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(){
        try {
            Stat stat = zookeeper.setData("/node_sync_1","1234".getBytes(),-1);
            System.out.println("stat:"+stat);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (event.getType()== Event.EventType.None && null==event.getPath()){
                doSomething();
            }
        }
    }
}

/*
[zk: 192.168.1.107:2181(CONNECTED) 3] get /node_sync_1
123
cZxid = 0x10000000a
ctime = Sun Dec 20 22:04:27 CST 2015
mZxid = 0x10000000a
mtime = Sun Dec 20 22:04:27 CST 2015
pZxid = 0x10000000a
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 3
numChildren = 0
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

 */
