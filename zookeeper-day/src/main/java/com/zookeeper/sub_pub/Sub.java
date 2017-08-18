package com.zookeeper.sub_pub;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Sub {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception{
		ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(100, 4);
		CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", 500, 400, retryPolicy);
		client.start();
		NodeCache listener = new NodeCache(client, "/data/db");
		try {
			listener.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		listener.getListenable().addListener(() -> {
			System.out.println(new String(listener.getCurrentData().getData()));
		});
		Thread.sleep(Integer.MAX_VALUE);         
	}

}
