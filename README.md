##概述

####ZooKeeper是什么

ZooKeeper是源代码开放的**分布式协调服务** ，由雅虎创建，是Google
Chubby的开源实现。ZooKeeper是一个高性能的**分布式数据一致性解决方案** ，它将那些复杂的、容易出错的分布式一致性服务封装起来，构成一个高效可靠的原语集，并提供一系列简单易用的接口给用户使用

Zookeeper 从设计模式角度来看，是一个**基于观察者模式设计的分布式服务管理框架**，它负责存储和管理大家都关心的数据，然后接受观察者的注册，一旦这些数据的状态发生变化，Zookeeper 就将负责通知已经在 Zookeeper 上注册的那些观察者做出相应的反应，从而实现集群中类似 Master/Slave 管理模式

######知识要点
- 源代码开放
- 分布式协调服务，解决分布式数据一致性问题
    * 顺序一致性
    * 原子性
    * 单一视图
    * 可靠性
    * 实时性
- 高性能
- 通过调用ZooKeeper提供的接口来解决一些分布式应用中的实际问题

####Zookeeper的典型应用场景
- 数据发布/订阅

    *数据发布/订阅* 顾名思义就是一方把数据发布出来，另一方通过某种手段可以得到这些数据。通常数据订阅有两种方式：推模式和拉模式；*推模式*一般是服务器主动向客户端推送信息，*拉模式*是客户端主动去服务器获取数据（通常是采用定时轮询的方式）

    ZK采用两种方式相结合

    发布者将数据发布到ZK集群节点上，订阅者通过一定的方法告诉服务器，我对哪个节点的数据感兴趣，那服务器在这些节点的数据发生变化时，就通知客户端，客户端得到通知后可以去服务器获取数据信息

- 分布式协调/通知

    * 心跳检测
    
        在分布式系统中，我们常常需要知道某个机器是否可用，传统的开发中，可以通过Ping某个主机来实现，Ping得通说明对方是可用的，相反是不可用的，ZK 中我们让所有的机其都注册一个临时节点，我们判断一个机器是否可用，我们只需要判断这个节点在ZK中是否存在就可以了，不需要直接去连接需要检查的机器 ，降低系统的复杂度 

- 统一命名服务（Name Service）
    
    分布式应用中，通常需要有一套完整的命名规则，既能够产生唯一的名称又便于人识别和记住，通常情况下用树形的名称结构是一个理想的选择，树形的名称结构是一个有层次的目录结构，既对人友好又不会重复。说到这里你可能想到了 JNDI，没错 Zookeeper 的 Name Service 与 JNDI 能够完成的功能是差不多的，它们都是将有层次的目录结构关联到一定资源上，但是 Zookeeper 的 Name Service 更加是广泛意义上的关联，也许你并不需要将名称关联到特定资源上，你可能只需要一个不会重复名称，就像数据库中产生一个唯一的数字主键一样。

    例如数据库表格ID，一般用得比较多的有两种ID，一种是自动增长的ID，一种是UUID(9291d71a-0354-4d8e-acd8-64f7393c64ae)，两种ID各自都有缺陷，自动增长的ID局限在单库单表中使用，不能在分布式中使用，UUID可以在分布式中使用但是由于ID没有规律难于理解，我们可以借用ZK来生成一个顺序增长的，可以在集群环境下使用的，命名易于理解的ID

    Name Service 已经是 Zookeeper 内置的功能，你只要调用 Zookeeper 的 API 就能实现。如调用 create 接口就可以很容易创建一个目录节点。

- 配置管理（Configuration Management）

    配置的管理在分布式应用环境中很常见，例如同一个应用系统需要多台 PC Server 运行，但是它们运行的应用系统的某些配置项是相同的，如果要修改这些相同的配置项，那么就必须同时修改每台运行这个应用系统的 PC Server，这样非常麻烦而且容易出错。
    像这样的配置信息完全可以交给 Zookeeper 来管理，将配置信息保存在 Zookeeper 的某个目录节点中，然后将所有需要修改的应用机器监控配置信息的状态，一旦配置信息发生变化，每台应用机器就会收到 Zookeeper 的通知，然后从 Zookeeper 获取新的配置信息应用到系统中。

    ![配置管理结构图](https://github.com/suqun/zookeeper-note/blob/master/etc/configuration.gif "配置管理结构图")

- 集群管理（Group Membership）

    Zookeeper 能够很容易的实现集群管理的功能，如有多台 Server 组成一个服务集群，那么必须要一个“总管”知道当前集群中每台机器的服务状态，一旦有机器不能提供服务，集群中其它集群必须知道，从而做出调整重新分配服务策略。同样当增加集群的服务能力时，就会增加一台或多台 Server，同样也必须让“总管”知道。

    Zookeeper 不仅能够帮你维护当前的集群中机器的服务状态，而且能够帮你选出一个“总管”，让这个总管来管理集群，这就是 Zookeeper 的另一个功能 Leader Election。

- 分布式锁

    共享锁在同一个进程中很容易实现，但是在跨进程或者在不同 Server之间就不好实现了。Zookeeper 却很容易实现这个功能

- 队列管理
 
    Zookeeper 可以处理两种类型的队列：

    1. 当一个队列的成员都聚齐时，这个队列才可用，否则一直等待所有成员到达，这种是同步队列。
    2. 队列按照 FIFO 方式进行入队和出队操作，例如实现生产者和消费者模型。


##基本概念

####集群角色

Leader，Follower，Observer

Leader服务器是整个Zookeeper集群工作机制中的核心 

Follower服务器是Zookeeper集群状态的跟随者

Observer服务器充当一个观察者的角色

####会话

会话是指客户端和ZooKeeper服务器的连接，ZooKeeper中的会话叫Session，客户端靠与服务器建立一个TCP的长连接

来维持一个Session,客户端在启动的时候首先会与服务器建立一个TCP连接，通过这个连接，客户端能够通过心跳检测与服务器保持有效的会话，也能向ZK服务器发送请求并获得响应

####数据模型

Zookeeper 会维护一个具有层次关系的数据结构，它非常类似于一个标准的文件系统

![数据模型](https://github.com/suqun/zookeeper-note/blob/master/etc/znode.gif "数据模型")

Zookeeper 这种数据结构有如下这些特点：

1. 每个子目录项如 NameService 都被称作为 znode，这个 znode 是被它所在的路径唯一标识，如 Server1 这个 znode 的标识为 /NameService/Server1
2. znode 可以有子节点目录，并且每个 znode 可以存储数据，注意 EPHEMERAL 类型的目录节点不能有子节点目录
3. znode 是有版本的，每个 znode 中存储的数据可以有多个版本，也就是一个访问路径中可以存储多份数据
4. znode 可以是临时节点，一旦创建这个 znode 的客户端与服务器失去联系，这个 znode 也将自动删除，Zookeeper 的客户端和服务器通信采用长连接方式，每个客户端和服务器通过心跳来保持连接，这个连接状态称为 session，如果 znode 是临时节点，这个 session 失效，znode 也就删除了
5. znode 的目录名可以自动编号，如 App1 已经存在，再创建的话，将会自动命名为 App2
6. znode 可以被监控，包括这个目录节点中存储的数据的修改，子节点目录的变化等，一旦变化可以通知设置监控的客户端，这个是 Zookeeper 的核心特性，Zookeeper 的很多功能都是基于这个特性实现的，后面在典型的应用场景中会有实例介绍

####版本

version 当前数据节点数据内容的版本号

cversion 当前数据节点子节点的版本号

aversion 当前数据节点ACL变更版本号

####ACL权限控制

ACL是Access Control Lists 的简写， ZooKeeper采用ACL策略来进行权限控制，有以下权限：

CREATE:创建子节点的权限

READ:获取节点数据和子节点列表的权限

WRITE:更新节点数据的权限

DELETE:删除子节点的权限

ADMIN:设置节点ACL的权限

##ZooKeeper 环境搭建

####集群模式安装
- 设置3台CentOS虚拟机192.168.1.105/106/107 网络模式桥接
- 安装zookeeper到opt目录中
    * cd /opt
    * wget zookeeperURL
    * tar xzvf zookeeper_3.**.gz
- 安装JDK环境
- 配置
    * cd zookeeper/conf/
    * cp zoo_sample.cfg zoo.cfg
    * vim zoo.cfg
        - dataDir=/var/zookeeper
        - clientPort=2181
        - 配置服务器 server.id=host:port:port
        - server.1=192.168.1.105:2888:3888
        - server.2=192.168.1.106:2888:3888
        - server.3=192.168.1.107:2888:3888
        - 保存退出
- 将zoo.cfg 负责到其他2台服务器上
    * scp zoo.cfg root@192.168.1.106:/opt/zookeeper/conf
    * scp zoo.cfg root@192.168.1.107:/opt/zookeeper/conf
- 在var目录下创建zookeeper目录
    * cd /var && mkdir zookeeper
- 在var/zookeeper目录下创建myid文件,内容为server.id的id值
    * vim myid
    * 1
    * wq!
    * 其他2台服务器响应创建写入2，3
- 启动zookeeper
    * cd /opt/zookeeper
    * ./zkserver.sh start
    * 测试连接 telnet 192.168.105 2181 (yum install telnet)
    * stat (显示当前服务器不能对外提供服务，需要其他其他2台)

####伪集群模式安装
- 配置
    - cd zookeeper/conf/
    - cp zoo_sample.cfg zk1.cfg
    - vim zk1.cfg
        - 
        - dataDir=/var/zookeeper/zk1
        - clientPort=2181
        - server.1=192.168.1.105:2888:3888
        - server.2=192.168.1.105:2889:3889
        - server.3=192.168.1.105:2890:3890
    - cp zk1.cfg zk2.cfg
        - dataDir=/var/zookeeper/zk2
        - clientPort=2182
        - server.1=192.168.1.105:2888:3888
        - server.2=192.168.1.105:2889:3889
        - server.3=192.168.1.105:2890:3890
    - cp zk1.cfg zk3.cfg
        - dataDir=/var/zookeeper/zk3
        - clientPort=2183
        - server.1=192.168.1.105:2888:3888
        - server.2=192.168.1.105:2889:3889
        - server.3=192.168.1.105:2890:3890
    - 在var/zookeeper/zk1(zk2,zk3)目录下创建myid文件,内容为server.id的id值
        - vim myid
        - 1
        - wq!
        
- 启动zookeeper
    * cd /opt/zookeeper
    * ./zkserver.sh start zk1.cfg
    * ./zkserver.sh start zk2.cfg
    * ./zkserver.sh start zk3.cfg
    * 查看节点状态
        - ./zkServer.sh status zk1.cfg
        - ./zkServer.sh status zk2.cfg 
        - ./zkServer.sh status zk3.cfg 
        
####单机模式安装
- 保留一台服务器，其他集群安装配置
    - server.1=192.168.1.105:2888:3888

##客户端使用

####zkCli.sh
- 连接zookeeper：./zkCli.sh -server ip:port

    bin目录下输入命令`./zkCli.sh -server 192.168.1.101:2181`

####zookeeper客户端命令

- **help**

```
    ZooKeeper -server host:port cmd args
    stat path [watch]
    set path data [version]
    ls path [watch]
    delquota [-n|-b] path
    ls2 path [watch]
    setAcl path acl
    setquota -n|-b val path
    history 
    redo cmdno
    printwatches on|off
    delete path [version]
    sync path
    listquota path
    rmr path
    get path [watch]
    create [-s] [-e] path data acl
    addauth scheme auth
    quit 
    getAcl path
    close 
    connect host:port
```

- **ls path [watch]** 列出当前节点的子节点

```
[zk: 192.168.1.101:2181(CONNECTED) 5] ls /zookeeper
[quota]
```

- **stat path [watch]** 获取节点状态信息

```
[zk: 192.168.1.101:2181(CONNECTED) 6] stat /zookeeper
cZxid = 0x0                             //该节点被创建时的事务ID
ctime = Thu Jan 01 08:00:00 CST 1970    //创建时间
mZxid = 0x0                             //最后一次更新的事务ID   
mtime = Thu Jan 01 08:00:00 CST 1970    //最后一次更新时间
pZxid = 0x0                             //该节点的子节点列表最后一次被更新（创建或删除节点）的事务ID
cversion = -1                           //子节点的版本号
dataVersion = 0                         //子节点的数据版本号
aclVersion = 0                          //权限版本号
ephemeralOwner = 0x0                    //创建该临时节点的事务Id
dataLength = 0                          //当前节点存放的数据长度
numChildren = 1                         //当前节点所拥有的子节点的个数
```

- **get path [watch]** 获取节点存储的内容
```
[zk: 192.168.1.101:2181(CONNECTED) 14] get /node_test
larry                             //节点存储的内容
cZxid = 0x400000002
ctime = Tue Dec 15 22:04:44 CST 2015
mZxid = 0x400000002
mtime = Tue Dec 15 22:04:44 CST 2015
pZxid = 0x400000002
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 5
numChildren = 0
```

- **ls2 path [watch]** 列出当前节点的子节点并查看子节点的状态信息，相当于ls和stat
```
[zk: 192.168.1.101:2181(CONNECTED) 9] ls /zookeeper
[quota]
[zk: 192.168.1.101:2181(CONNECTED) 10] ls2 /zookeeper
[quota]
cZxid = 0x0
ctime = Thu Jan 01 08:00:00 CST 1970
mZxid = 0x0
mtime = Thu Jan 01 08:00:00 CST 1970
pZxid = 0x0
cversion = -1
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 0
numChildren = 1
```

- **create [-s] [-e] path data acl** 创建指令
    - -s 表示创建的是顺序节点
    - -e 表示创建的是临时节点
    - path 创建节点的路径
    - data 创建节点的内容
    - acl 创建节点的acl权限
```
//创建普通节点
[zk: 192.168.1.101:2181(CONNECTED) 13] create /node_test larry 
Created /node_test
[zk: 192.168.1.101:2181(CONNECTED) 14] get /node_test
larry
cZxid = 0x400000002
ctime = Tue Dec 15 22:04:44 CST 2015
mZxid = 0x400000002
mtime = Tue Dec 15 22:04:44 CST 2015
pZxid = 0x400000002
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 5
numChildren = 0

//创建临时节点,退出重新登录
[zk: 192.168.1.101:2181(CONNECTED) 1] ls /node_test
[]
[zk: 192.168.1.101:2181(CONNECTED) 2] create -e /node_test/node_test_1 123456
Created /node_test/node_test_1
[zk: 192.168.1.101:2181(CONNECTED) 3] ls /node_test
[node_test_1]
[zk: 192.168.1.101:2181(CONNECTED) 4] quit
Quitting...
2015-12-15 22:13:13,291 [myid:] - INFO  [main:ZooKeeper@684] - Session: 0x151a5bedd2c0001 closed
2015-12-15 22:13:13,293 [myid:] - INFO  [main-EventThread:ClientCnxn$EventThread@519] - EventThread shut down for session: 0x151a5bedd2c0001
➜  bin  sudo ./zkCli.sh -server 192.168.1.101:2181  //重新登录
[zk: 192.168.1.101:2181(CONNECTED) 2] ls /node_test
[]

//创建顺序节点
[zk: 192.168.1.101:2181(CONNECTED) 3] create -s /node_test/note_test_1 123456 
Created /node_test/note_test_10000000001
[zk: 192.168.1.101:2181(CONNECTED) 4] create -s /node_test/note_test_1 123456 
Created /node_test/note_test_10000000002
```

- **set path data [version]** 修改指令
    - path 修改节点路径为path
    - data 修改节点的值为data
    - [version] 为节点内容设置版本号
```
[zk: 192.168.1.101:2181(CONNECTED) 4] get /node_test
larry
cZxid = 0x400000002
ctime = Tue Dec 15 22:04:44 CST 2015
mZxid = 0x400000002
mtime = Tue Dec 15 22:04:44 CST 2015
pZxid = 0x40000000d
cversion = 6
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 5
numChildren = 2
[zk: 192.168.1.101:2181(CONNECTED) 5] set /node_test 999999
cZxid = 0x400000002
ctime = Tue Dec 15 22:04:44 CST 2015
mZxid = 0x40000000f
mtime = Tue Dec 15 22:24:57 CST 2015
pZxid = 0x40000000d
cversion = 6
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 6
numChildren = 2
[zk: 192.168.1.101:2181(CONNECTED) 6] get /node_test 
999999
cZxid = 0x400000002
ctime = Tue Dec 15 22:04:44 CST 2015
mZxid = 0x40000000f
mtime = Tue Dec 15 22:24:57 CST 2015
pZxid = 0x40000000d
cversion = 6
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 6
numChildren = 2
[zk: 192.168.1.101:2181(CONNECTED) 7] set /node_test 999999
cZxid = 0x400000002
ctime = Tue Dec 15 22:04:44 CST 2015
mZxid = 0x400000010
mtime = Tue Dec 15 22:31:27 CST 2015
pZxid = 0x40000000d
cversion = 6
dataVersion = 2  //此处增加
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 6
numChildren = 2
[zk: 192.168.1.101:2181(CONNECTED) 8] set /node_test 999999 2
cZxid = 0x400000002
ctime = Tue Dec 15 22:04:44 CST 2015
mZxid = 0x400000011
mtime = Tue Dec 15 22:31:31 CST 2015
pZxid = 0x40000000d
cversion = 6
dataVersion = 3  //修改的值相同，此处扔增加
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 6
numChildren = 2
[zk: 192.168.1.101:2181(CONNECTED) 9] set /node_test 999999 4    //版本号必须同上一个的版本号相同才不会报错，即上面为3，填写版本号为3才行
version No is not valid : /node_test
[zk: 192.168.1.101:2181(CONNECTED) 10] set /node_test 999999 3
cZxid = 0x400000002
ctime = Tue Dec 15 22:04:44 CST 2015
mZxid = 0x400000013
mtime = Tue Dec 15 22:31:42 CST 2015
pZxid = 0x40000000d
cversion = 6
dataVersion = 4 // 版本号增加
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 6
numChildren = 2
```

- **delete path [version]** 删除指定路径的节点
```
[zk: 192.168.1.101:2181(CONNECTED) 12] ls /node_test
[note_test_10000000002, note_test_10000000001]
[zk: 192.168.1.101:2181(CONNECTED) 13] delete /node_test
Node not empty: /node_test //只能删除没有子节点的节点
[zk: 192.168.1.101:2181(CONNECTED) 14] delete /node_test/note_test_10000000001
[zk: 192.168.1.101:2181(CONNECTED) 15] 
```

- **rmr path** 循环删除路径下的节点
```
[zk: 192.168.1.101:2181(CONNECTED) 16] rmr /node_test
[zk: 192.168.1.101:2181(CONNECTED) 17] ls /
[zookeeper]
[zk: 192.168.1.101:2181(CONNECTED) 18] 
```

- **setquota -n|-b val path** 限制节点的值的长度或其子节点的个数
    - -n 限制节点(包含当前节点)的个数为val
    - -b 限制节点值的长度为val
    - path 要限制的节点路径
```
[zk: 192.168.1.101:2181(CONNECTED) 18] create node_test 123
Command failed: java.lang.IllegalArgumentException: Path must start with / character
[zk: 192.168.1.101:2181(CONNECTED) 19] create /node_test 123
Created /node_test
[zk: 192.168.1.101:2181(CONNECTED) 20] setquota -n 2 /node_test
Comment: the parts are option -n val 2 path /node_test
[zk: 192.168.1.101:2181(CONNECTED) 21] create /node_test/node_test_1 234
Created /node_test/node_test_1
[zk: 192.168.1.101:2181(CONNECTED) 22] create /node_test/node_test_2 345
Created /node_test/node_test_2
[zk: 192.168.1.101:2181(CONNECTED) 23] create /node_test/node_test_3 456
Created /node_test/node_test_3 //超额，不会抛出异常，只是在日志/opt/zookeeper/bin/zookeeper.out中记录信息
[zk: 192.168.1.101:2181(CONNECTED) 24] ls /node_test
[node_test_2, node_test_3, node_test_1]

zookeeper.out
2015-12-15 22:49:01,139 [myid:1] - WARN  [CommitProcessor:1:DataTree@389] - Quota exceeded: /node_test count=3 limit=2
2015-12-15 22:49:06,021 [myid:1] - WARN  [CommitProcessor:1:DataTree@389] - Quota exceeded: /node_test count=4 limit=2
```

- **listquota path** 查看节点配额情况
```
[zk: 192.168.1.101:2181(CONNECTED) 27] listquota /node_test
absolute path is /zookeeper/quota/node_test/zookeeper_limits
Output quota for /node_test count=2,bytes=-1   //配额信息 -1没有限制
Output stat for /node_test count=4,bytes=12    //当前节点状态信息，当前节点4个，限额2个，字节总长度12
[zk: 192.168.1.101:2181(CONNECTED) 28]
```

- **delquota [-n|-b] path** 删除配额信息
```
[zk: 192.168.1.101:2181(CONNECTED) 28] delquota -n /node_test
[zk: 192.168.1.101:2181(CONNECTED) 29] listquota /node_test
absolute path is /zookeeper/quota/node_test/zookeeper_limits
Output quota for /node_test count=-1,bytes=-1
Output stat for /node_test count=4,bytes=12
[zk: 192.168.1.101:2181(CONNECTED) 30] 
```

- **history** 列出已操作的指令记录
```
[zk: 192.168.1.101:2181(CONNECTED) 30] history
20 - setquota -n 2 /node_test
21 - create /node_test/node_test_1 234
22 - create /node_test/node_test_2 345
23 - create /node_test/node_test_3 456
24 - ls /node_test
25 - ls
26 - h
27 - listquota /node_test
28 - delquota -n /node_test
29 - listquota /node_test
30 - history
[zk: 192.168.1.101:2181(CONNECTED) 31] 
```

- **redo cmdno** 重新执行history中的命令
```
[zk: 192.168.1.101:2181(CONNECTED) 31] redo 24
[node_test_2, node_test_3, node_test_1]
[zk: 192.168.1.101:2181(CONNECTED) 32] 
```

- **connect host:port** 连接其他服务器
- **close** 关闭connect连接的服务器
- **quit** 退出zookeeper客户端连接


##权限

- 权限模式(scheme)
    - ip
    - digest
- 授权对象(ID)
    - ip权限模式:具体的ip地址
    - digest权限模式:username:Base64(SHA-1(username:password))
- 权限(permission)
    - create(C)
    - delete(D)
    - READ(R)
    - WRITE(W)
    - ADMIN(A)
- 权限组合:scheme + ID + permission
   


[笔记来自于极客学院zookeeper视频](http://www.jikexueyuan.com/course/1813.html)

[分布式服务框架 Zookeeper -- 管理分布式环境中的数据](http://www.ibm.com/developerworks/cn/opensource/os-cn-zookeeper/)
