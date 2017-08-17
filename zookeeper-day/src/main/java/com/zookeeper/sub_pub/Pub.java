package com.zookeeper.sub_pub;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Pub {
	
	public static void main(String[] args)throws Exception {
		
		CuratorFramework pub = 	CuratorFrameworkFactory.builder().connectString("localhost:2181")
		                       .connectionTimeoutMs(500).sessionTimeoutMs(400)
		                       .retryPolicy(new ExponentialBackoffRetry(300,4))
		                       .build();
		pub.start();
		CreateBuilder builder = pub.create();
		builder.creatingParentsIfNeeded().forPath("/data/db", "db".getBytes());
		
		Thread.sleep(Integer.MAX_VALUE);
	}

}
