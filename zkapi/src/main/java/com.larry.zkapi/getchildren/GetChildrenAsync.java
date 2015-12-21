package com.larry.zkapi.getchildren;

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
/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/bin/java -Didea.launcher.port=7533 "-Didea.launcher.bin.path=/Applications/IntelliJ IDEA 15.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/lib/tools.jar:/Users/larry/IdeaProjects/java/zookeeper-note/zkapi/target/classes:/Users/larry/Downloads/zookeeper/zookeeper-3.4.7/lib/jline-0.9.94.jar:/Users/larry/Downloads/zookeeper/zookeeper-3.4.7/lib/log4j-1.2.16.jar:/Users/larry/Downloads/zookeeper/zookeeper-3.4.7/lib/netty-3.7.0.Final.jar:/Users/larry/Downloads/zookeeper/zookeeper-3.4.7/lib/slf4j-api-1.6.1.jar:/Users/larry/Downloads/zookeeper/zookeeper-3.4.7/lib/slf4j-log4j12-1.6.1.jar:/Users/larry/Downloads/zookeeper/zookeeper-3.4.7/zookeeper-3.4.7.jar:/Applications/IntelliJ IDEA 15.app/Contents/lib/idea_rt.jar" com.intellij.rt.execution.application.AppMain com.larry.zkapi.getchildren.GetChildrenAsync
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
CONNECTING
收到事件:WatchedEvent state:SyncConnected type:None path:null
rc=0
path=/
ctx=ctx
children=[node_async, node_sync_1, node_sync, zookeeper]
stat=0,0,0,0,0,2,0,0,0,4,4294967309
 */