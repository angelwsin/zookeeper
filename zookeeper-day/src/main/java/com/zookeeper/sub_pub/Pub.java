package com.zookeeper.sub_pub;

import java.util.Objects;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

public class Pub {
	
	public static void main(String[] args)throws Exception {
		
		CuratorFramework pub = 	CuratorFrameworkFactory.builder().connectString("localhost:2181")
		                       .connectionTimeoutMs(500).sessionTimeoutMs(400)
		                       .retryPolicy(new ExponentialBackoffRetry(300,4))
		                       .build();
		pub.start();
		Stat stat = pub.checkExists().forPath("/data/db");
		if(Objects.isNull(stat)){
		    CreateBuilder builder = pub.create();
		    //默认创建持久结点
	        builder.creatingParentsIfNeeded().forPath("/data/db", "db".getBytes()); 
		}
		Thread.sleep(Integer.MAX_VALUE);
	}

}
