package com.zookeeper.curator;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.DeleteBuilder;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.api.SetDataBuilder;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class zkClient {

	
	public static void main(String[] args) throws Exception{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
		CuratorFramework curatorFramework =CuratorFrameworkFactory.newClient("localhost:2181",5000,3000,retryPolicy);
		//连接 zookeeper
		curatorFramework.start();
		
		//Fluent 风格创建客户端
		CuratorFrameworkFactory.builder()
		                       .connectString("localhost:2181")
		                       .sessionTimeoutMs(5000)
		                       .retryPolicy(retryPolicy)
		                       //独立的命名空间 创建  /namespace 节点
		                       .namespace("zkCient")
		                       .build();
		//创建节点
		CreateBuilder create = curatorFramework.create();
		// 递归创建父节点 
		create.creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
		      .forPath("/client", "init".getBytes());
		
		//删除节点
		DeleteBuilder delete = curatorFramework.delete();
		delete.deletingChildrenIfNeeded().forPath("/client");
		// 只要客户端会话有效 强制保证删除
		delete.guaranteed().forPath("/client");
		
		//读取数据
		GetDataBuilder getData = curatorFramework.getData();
		//读取数据 同时获取 节点的 stat
		getData.storingStatIn(new Stat()).forPath("/client");
		
		GetChildrenBuilder children = curatorFramework.getChildren();
		List<String>  childrens = children.forPath("/client");
		
		//更新数据
		SetDataBuilder setData  = curatorFramework.setData();
		setData.forPath("/client", "data".getBytes());
		
		ExistsBuilder exists = curatorFramework.checkExists();
		exists.forPath("/client");
		
		//-------------------------以上为同步调用------------------------------
		
		
		//-------------------------一下为异步调用------------------------------
		create.creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
		      .inBackground((CuratorFramework client, CuratorEvent event)->{
		    	  
		      },new Object()).forPath("/client");
		
		
		//------------事件监听-----------------------------------------
		
		//zookeeper 提供的 watcher 的监听 但是 需要反复注册
		//curator 提供了封装 为 Cache  监听类型：节点节点监听和子节点监听
		
		//Fluent 风格创建客户端
		CuratorFramework cache = 		CuratorFrameworkFactory.builder()
				                       .connectString("localhost:2181")
				                       .sessionTimeoutMs(5000)
				                       .retryPolicy(retryPolicy)
				                       //独立的命名空间 创建  /namespace 节点
				                       .namespace("zkCient")
				                       .build();
		
		//节点监听 NodeCache
		NodeCache  nodeCache  = new NodeCache(cache, "zkCient",false);
		
		cache.start();
		nodeCache.getListenable().addListener(()->{
			System.out.println("nodeChanged:"+new String(nodeCache.getCurrentData().getData()));
		});
		
		//子节点监听
		PathChildrenCache childrenCache = new PathChildrenCache(cache, "zkCient",false);
		childrenCache.getListenable().addListener((CuratorFramework client, PathChildrenCacheEvent event)->{
			
			System.out.println(event.getType());
		});
		
		
       //--------------------master选举--------------------------------
		String leaderPath ="/leaderPath";
		LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, leaderPath, new LeaderSelectorListenerAdapter(){
            
			//master 选举成功回调  当takeLeadership 方法执行完进行新一轮的选举
			@Override
			public void takeLeadership(CuratorFramework client) throws Exception {
				System.out.println(" 成为 master 角色");
			}

			
			
		});
		
		
		//-------------------------分布式锁--------------------------------
		
		InterProcessMutex mutex = new InterProcessMutex(curatorFramework, "/locks");
		mutex.acquire();//请求锁   拿不到锁的线程等待
		mutex.release();//释放锁
		
		
		//-----------------------分布式计数器-------------------
		
		DistributedAtomicInteger count = new DistributedAtomicInteger(curatorFramework, "count", new RetryNTimes(3, 1000));
		count.add(1);
		
		//---------------------分布式Barrier
		//DistributedBarrier
		
	}
}
