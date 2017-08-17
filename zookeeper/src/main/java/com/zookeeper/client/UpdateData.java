package com.zookeeper.client;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class UpdateData {

	
static CountDownLatch lath = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception{
		
		ZooKeeper  zkClient = new ZooKeeper("localhost:2181", 500, (WatchedEvent event)->{
			  System.out.println(event);
			 if(KeeperState.SyncConnected==event.getState()){
				lath.countDown(); 
			 }
		});
		
		lath.await();
		
		//znode  path data  version 
		//version 可以用于并发更新  cas
		zkClient.setData("/update-znode", "data".getBytes(), 0);
		
	}
}
