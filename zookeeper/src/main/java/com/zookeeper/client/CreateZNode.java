package com.zookeeper.client;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 同步异步创建znode
 * zookeeper不支持递归创建，无法在父类不存在的情况下创建子类节点，如果一个节点已经存在
 * 再创建则会抛出KeeperErrorCode = NodeExists
 *
 */
public class CreateZNode implements Watcher{
	
	 static  CountDownLatch latch = new CountDownLatch(1);
	 static ZooKeeper zkClients ;
	
	public static void main(String[] args)throws Exception {
		
		final ZooKeeper zkClient = new ZooKeeper("localhost:2181", 400, new CreateZNode());
		latch.await();
		zkClients = zkClient;
		
		//同步创建
		//String path1  =zkClient.create("/create-znode", "test".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		//System.out.println(path1);
		
		
		/**
		 * 异步节点创建
		 * path znode 节点
		 * data 数据
		 * acl 权限
		 * 
		 * CreateMode 四种 
		 * 1)PERSISTENT 持久
		 * 2)PERSISTENT_SEQUENTIAL 持久顺序  节点+序号
		 * 3)EPHEMERAL 临时 当客户端断开连接 删除
		 * 4)EPHEMERAL_SEQUENTIAL 临时顺序  当客户端断开连接 删除
		 */
		zkClient.create("/create-znode", "test".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,(int rc, String path, Object ctx, String name)->{
			  //rc 返回码 
			 //ctx  调用时的传入上下文
			System.out.println(rc);
			System.out.println(path);
			//  
			try {
				zkClient.getData(path, true, new Stat());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} , new Object());
		
		Thread.sleep(Integer.MAX_VALUE);
	}

	public void process(WatchedEvent event) {
		System.out.println(event);
		if(KeeperState.SyncConnected == event.getState()){
			if(EventType.NodeDataChanged==event.getType()){
				System.out.println(event.getPath());
				try {
					// 原生的api Watcher 生效一次后会失效 需要重新注册
					zkClients.getData(event.getPath(), true, new Stat());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
			latch.countDown();
			}
			
		}
		
	}

}
