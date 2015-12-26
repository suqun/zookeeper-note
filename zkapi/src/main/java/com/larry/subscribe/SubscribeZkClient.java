package com.larry.subscribe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;


public class SubscribeZkClient {
	
	   private static final int  CLIENT_QTY = 5;

	    private static final String  ZOOKEEPER_SERVER = "192.168.1.107:2181";
	    
	    private static final String  CONFIG_PATH = "/config";
	    private static final String  COMMAND_PATH = "/command";
	    private static final String  SERVERS_PATH = "/servers";
	       
	    public static void main(String[] args) throws Exception
	    {

	        List<ZkClient>  clients = new ArrayList<ZkClient>();
	        List<WorkServer>  workServers = new ArrayList<WorkServer>();
	        ManageServer manageServer;

	        try
	        {
	        	ServerConfig initConfig = new ServerConfig();
	        	initConfig.setDbPwd("123456");
	        	initConfig.setDbUrl("jdbc:mysql://localhost:3306/mydb");
	        	initConfig.setDbUser("root");
	        	
	        	ZkClient clientManage = new ZkClient(ZOOKEEPER_SERVER, 5000, 5000, new BytesPushThroughSerializer());
	        	manageServer = new ManageServer(SERVERS_PATH, COMMAND_PATH,CONFIG_PATH,clientManage,initConfig);
	        	manageServer.start();
	        		        	
	            for ( int i = 0; i < CLIENT_QTY; ++i )
	            {
	                ZkClient client = new ZkClient(ZOOKEEPER_SERVER, 5000, 5000, new BytesPushThroughSerializer());
	                clients.add(client);
	                ServerData serverData = new ServerData();
	                serverData.setId(i);
	                serverData.setName("WorkServer#"+i);
	                serverData.setAddress("192.168.1."+i);

	                WorkServer  workServer = new WorkServer(CONFIG_PATH, SERVERS_PATH, serverData, client, initConfig);
	                workServers.add(workServer);
	                workServer.start();	                
	                
	            }	            
	            System.out.println("敲回车键退出！\n");
	            new BufferedReader(new InputStreamReader(System.in)).readLine();
	            
	        }
	        finally
	        {
	            System.out.println("Shutting down...");

	            for ( WorkServer workServer : workServers )
	            {
	            	try {
	            		workServer.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}           	
	            }
	            for ( ZkClient client : clients )
	            {
	            	try {
	            		client.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
	            	
	            }
	        }
	    }	

}

/*

work server start...
work server list changed, new list is
[192.168.1.0]
work server start...
work server list changed, new list is
[192.168.1.1, 192.168.1.0]
work server start...
work server list changed, new list is
[192.168.1.1, 192.168.1.0, 192.168.1.2]
work server start...
work server list changed, new list is
[192.168.1.1, 192.168.1.0, 192.168.1.3, 192.168.1.2]
work server start...
敲回车键退出！

work server list changed, new list is
[192.168.1.1, 192.168.1.0, 192.168.1.3, 192.168.1.2, 192.168.1.4]

//控制台输入
[zk: 192.168.1.107(CONNECTED) 8] create /command list
Created /command
//程序监听打印
cmd:list
[192.168.1.1, 192.168.1.0, 192.168.1.3, 192.168.1.2, 192.168.1.4]

//控制台输入
[zk: 192.168.1.107(CONNECTED) 9] set /command create
cZxid = 0x800003f93
ctime = Sat Dec 26 21:10:18 CST 2015
mZxid = 0x800004066
mtime = Sat Dec 26 21:11:11 CST 2015
pZxid = 0x800003f93
cversion = 0
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 6
numChildren = 0
//程序监听打印
cmd:create
new Work server config is:ServerConfig [dbUrl=jdbc:mysql://localhost:3306/mydb, dbPwd=123456, dbUser=root]
new Work server config is:ServerConfig [dbUrl=jdbc:mysql://localhost:3306/mydb, dbPwd=123456, dbUser=root]
new Work server config is:ServerConfig [dbUrl=jdbc:mysql://localhost:3306/mydb, dbPwd=123456, dbUser=root]
new Work server config is:ServerConfig [dbUrl=jdbc:mysql://localhost:3306/mydb, dbPwd=123456, dbUser=root]
new Work server config is:ServerConfig [dbUrl=jdbc:mysql://localhost:3306/mydb, dbPwd=123456, dbUser=root]

//控制台输入
[zk: 192.168.1.107(CONNECTED) 10] set /command modify
cZxid = 0x800003f93
ctime = Sat Dec 26 21:10:18 CST 2015
mZxid = 0x8000040fb
mtime = Sat Dec 26 21:11:43 CST 2015
pZxid = 0x800003f93
cversion = 0
dataVersion = 2
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 6
numChildren = 0
//程序监听打印
cmd:modify
new Work server config is:ServerConfig [dbUrl=jdbc:mysql://localhost:3306/mydb, dbPwd=123456, dbUser=root_modify]
new Work server config is:ServerConfig [dbUrl=jdbc:mysql://localhost:3306/mydb, dbPwd=123456, dbUser=root_modify]
new Work server config is:ServerConfig [dbUrl=jdbc:mysql://localhost:3306/mydb, dbPwd=123456, dbUser=root_modify]
new Work server config is:ServerConfig [dbUrl=jdbc:mysql://localhost:3306/mydb, dbPwd=123456, dbUser=root_modify]
new Work server config is:ServerConfig [dbUrl=jdbc:mysql://localhost:3306/mydb, dbPwd=123456, dbUser=root_modify]
 */