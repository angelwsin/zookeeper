package com.zookeeper.zkclient;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ExistsAndACL {
	
static CountDownLatch lath = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception{
		
		ZooKeeper  zkClient = new ZooKeeper("localhost:2181", 500, (WatchedEvent event)->{
			  System.out.println(event);
			 if(KeeperState.SyncConnected==event.getState()){
				lath.countDown(); 
			 }
		});
		
		lath.await();
		
		//注意 子节点的变化 不会通知父节点
		zkClient.exists("/zookeeper", (WatchedEvent event)->{
			
		});
		
		//权限控制
		//scheme
		//"world", "anyone","auth","digest","ip","super"
		//"angel:wsin" 类似  用户名:密码
		zkClient.addAuthInfo("digest", "angel:wsin".getBytes());
		
	}

}
