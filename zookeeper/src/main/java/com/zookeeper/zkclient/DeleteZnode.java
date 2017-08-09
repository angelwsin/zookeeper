package com.zookeeper.zkclient;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;




public class DeleteZnode {
	static CountDownLatch lath = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception{
		
		ZooKeeper  zkClient = new ZooKeeper("localhost:2181", 500, (WatchedEvent event)->{
			  System.out.println(event);
			 if(KeeperState.SyncConnected==event.getState()){
				lath.countDown(); 
			 }
		});
		
		lath.await();
		
		zkClient.delete("/delate-znode", -1);
		
	}
	
	
	

}
