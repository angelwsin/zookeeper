package com.zookeeper.client;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;


/**
 * zookeeper 数据类型只支持  二进制
 * @author angel
 *
 */
public class ZkClient implements Watcher{
	
	static CountDownLatch  connetionLath = new CountDownLatch(1);
	
	public static void main(String[] args)throws Exception{
		
		//创建 zookeeper客户端 异步连接
		/*
		 * sendThread = new SendThread();  sendThread线程 负责 连接,心跳,和接收发送数据
          eventThread = new EventThread(); 接收的数据 组织成Event事件 丢到 EventThread线程处理
		 */
		ZooKeeper zkClient  = new ZooKeeper("localhost:2181", 500, new ZkClient());
		System.out.println(zkClient.getState());
		connetionLath.await();
		
		/**
		 * 传入SessionId SessionPasswd 为了复用上面的会话
		 */
		ZooKeeper zkClient1   = new ZooKeeper("localhost:2181", 500,new ZkClient() , zkClient.getSessionId(), zkClient.getSessionPasswd());
		
		Thread.sleep(Integer.MAX_VALUE);
		
		
	}

	public void process(WatchedEvent event) {
		System.out.println(event);
		//等待连接通知
		if(KeeperState.SyncConnected==event.getState()){
			connetionLath.countDown();
			System.out.println("连接上");
		}
	}

}
