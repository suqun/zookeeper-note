package com.larry.api.zk.createnode;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created on 15/12/20
 * 同步创建有权限的节点
 */
public class CreateNodeSyncAuth implements Watcher {
    private static ZooKeeper zookeeper;
    private static boolean somethingDone = false;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper("192.168.1.107:2181", 5000, new CreateNodeSyncAuth());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent event) {
        System.out.println("收到事件:" + event);
        if (event.getState() == Event.KeeperState.SyncConnected) {
            if (!somethingDone && event.getType() == Event.EventType.None && null == event.getPath()) {
                doSomething();
            }
        }
    }

    /**
     * - 权限模式(scheme)
     * - ip
     * - digest
     * - 授权对象(ID)
     * - ip权限模式:具体的ip地址
     * - digest权限模式:username:Base64(SHA-1(username:password))
     * - 权限(permission)
     * - create(C)
     * - delete(D)
     * - READ(R)
     * - WRITE(W)
     * - ADMIN(A)
     * - 权限组合:scheme + ID + permission
     */
    private void doSomething() {
        try {
            ACL aclIp = new ACL(ZooDefs.Perms.READ, new Id("ip", "192.168.1.107"));
            ACL aclDigest = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE, new Id("digest", DigestAuthenticationProvider.generateDigest("larry:123456")));
            ArrayList<ACL> acls = new ArrayList<ACL>();
            acls.add(aclDigest);
            acls.add(aclIp);

            //zookeeper.addAuthInfo("digest", "larry:123456".getBytes());
            String path = zookeeper.create("/node_sync_acl", "aclValue".getBytes(), acls, CreateMode.PERSISTENT);

            System.out.println("return path:" + path);

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}

/*
CONNECTING
收到事件:WatchedEvent state:SyncConnected type:None path:null
return path:/node_sync_acl


[zk: 192.168.1.107:2181(CONNECTED) 11] set /node_sync_acl 99999
Authentication is not valid : /node_sync_acl
[zk: 192.168.1.107:2181(CONNECTED) 12] getAcl /node_sync_acl
'digest,'larry:I58ckPdiPA8jAlxGeEnF02Q0W54=
: rw
'ip,'192.168.1.107
: r
 */
