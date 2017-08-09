package com.zookeeper.zkclient;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ChildrenAndData {
	
	static CountDownLatch lath = new CountDownLatch(1);
	
	public static void main(String[] args)throws Exception {
		ZooKeeper  zkClient = new ZooKeeper("localhost:2181", 500, (WatchedEvent event)->{
			  System.out.println(event);
			 if(KeeperState.SyncConnected==event.getState()){
				lath.countDown(); 
			 }
		});
		
		lath.await();
		//同步  其中 watch = true 复用上面的watcher
		List<String> chillren = zkClient.getChildren("/", true);
		for(String str:chillren){
			System.out.println(str);
		}
		//异步
		//zkClient.getChildren(path, watch, cb, ctx);
		
		//得到数据
		
		byte[] data = zkClient.getData("/", true, new Stat());
		System.out.println(new String(data));
	}

}
