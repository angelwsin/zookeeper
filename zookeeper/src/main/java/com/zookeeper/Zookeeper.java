package com.zookeeper;

public class Zookeeper {
	
	/**
	 * 网络中 的异步性
	 * 
	 * zookeeper 分布式数据一致性解决方案，数据发布/订阅，负载均衡，命名服务，分布式协调/通知，集群管理，Master选举
	 *           分布式锁和分布式队列等
	 *zookeeper  分布式一致性特性
	 *  1)顺序一致性   客户端发起的事务请求，最终会被严格地按照其发起顺序被应用到Zk中
	 *  2)原子性         要么整个集群中的机器成功应用某一事务，要么没有应用
	 *  3)单一视图       客户端无论连接那个zk服务器，看到的服务器数据模型都是一致的
	 *  4)可靠性          一旦服务端应用了一个事务并完成对客户端的反馈，应用事务的状态会被保留
	 *  5)实时性          zookeeper仅仅能保证在一定的时间段内（滑动窗口），客户端最终一定能从服务端读取到最新的数据(写立即读不保证)
	 *     
	 *zookeeper  概念
	 *
	 * 1)集群角色
	 *   传统使用 master/slave 由master写入 异步复制数据给slave
	 *   zookeeper 引入了三种角色
	 *    1.leader  zookeeper 通过选举产生一个 leader,为客户的提供读和写
	 *    2.follower和Observer  只提供读，区别在于 observer不参与选举也不参与写操作的 过半写成功
	 * 2)Session
	 * 3)数据节点 znode
	 *   zookeeper 有两类节点: 1)构成集群的机器 2)数据模型中的数据单元，为数据节点（znode),zk的所有
	 *   数据存放在内存中，数据模型是一棵树由/进行分割的路径 如 /foo/path,数据类型只支持二进制
	 * 4)版本
	 *   zk的每个znode存储数据，每一个znode都会维护一个stat数据结构，stat记录znode三个数据版本
	 * 5)Watcher 
	 *   事件监听器，zk允许在节点上注册watcher,当特定的事件触发服务端会通知监听器
	 * 6)ACL
	 *  ZK采用ACL策略进行权限控制 五种权限
	 *   create,read,write(更新),delete,admin(设置节点权限）
	 * zookeeper  协议  zab（Zookeeper Atomic Broadcast）  之一致性协议
	 *   事务的请求由全局唯一的leader处理器协调处理，leader将客户端的事务请求转换为事务Proposal提议
	 *   并将Proposal分发给集群中的所有follower,leader等待follower的反馈一旦反馈超过半数，leader
	 *   再次向所有follower分发commit消息，提交Proposal事务，自己也提交proposal事务
	 *   1）消息广播   是一个原子广播协议 类似于二阶段提交
	 *   
	 *   2）崩溃恢复
	 *     leader崩溃或 leader失去与过半follower的联系
	 *     
	 *  
	 * 
	 *  ZAB协议主要用于构建一个高可用的分布式数据主备系统,而Paxos算法则用于构建一个分布式的一致性状态机系统
	 *  状态变化 状态是从 1 变成 2 再变成 4, 最后变成 3
	 * 
	 * 
	 * 
	 * 
	 * zab 协议实现
	 *  1）消息广播
	 *   客户端请求到leader  leader把请求转换为事务Proposal请求，leader生成全局唯一的txid
	 *   严格顺序的，为每一个Follower维持一个队列基于FIFO,leader类似于二阶段提交，leader把
	 *   proposal事务发送给所有的Follower 等待folloer的 ack,follower把proposal事务写到日志中，
	 *   反馈给leader 当leader接受到多半的ack 发送给folloer commit消息  同时自身也执行commit
	 *   
	 *   2）崩溃恢复-- leader选举算法
	 *    1.当leader接受到过半的ack 发送出commit leader挂了   要保证提交的事务在所有的follower执行成功
	 *    
	 *    2.当leader提出proposal事务 就挂了  导致其他follower没有收到  要保证丢弃这个事务
	 *    
	 *    leader选举算法：能够保证提交已经被leader提交的事务Proposal,同时丢弃已经被跳过的事务Proposal
	 *     要实现这样的算法被选举的leader则是 txid最大的
	 *     
	 *     
	 *   3)数据同步
	 *   从崩溃恢复 后同步数据  当数据一致后 接受客户端请求
	 *    txid（zxid)64  前32位记录leader选择周期  后32记录事务id
	 *   
	 * 
	 * 
	 * zookeeper的部署模式
	 * 1)集群
	 * 2)伪分布
	 * 3)单机
	 * 
	 * 
	 * zookeeper 操作
	 * 
	 * 1.创建节点
	 *   create [-s][-e] path data acl
	 *   s 顺序，e临时  默认情况 持久节点
	 * 2.读取
	 *   ls path [watch]   列出指定节点下的所有子节点
	 *   
	 *   get path [watch]  指定节点的数据内容和属性
	 *   
	 * 3.更新
	 *  set  path data [version]
	 *  本次更新操作是基于znode 的哪一个数据版本进行的
	 * 4.删除
	 * 
	 *  delete path [version]
	 *  
	 *  
	 * 
	 * zookeeper 组件
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * zookeeper 特点和功能
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 　Quorom 机制保证数据冗余和最终一致性的投票算法
	 * 
	 * 
	 * 
	 */

}
