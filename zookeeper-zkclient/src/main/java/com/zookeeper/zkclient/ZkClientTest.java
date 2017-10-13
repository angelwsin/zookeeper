package com.zookeeper.zkclient;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * zkClient 可以自定义 序列化器
 * 默认使用 java的序列化
 * @author angel
 *
 */
public class ZkClientTest {
	
	public static void main(String[] args) {
	
		 ZkClient zkClient  = new ZkClient("localhost:2181", 500);
		 System.out.println(zkClient);
		 //zkClient 不同于zookeeper api 的wather  zkclient  IZkChildListener注册一次一直生效 
		 zkClient.subscribeChildChanges("/zkhello", (String parentPath, List<String> currentChilds)->{
			 
			 System.out.println(parentPath+":"+currentChilds);
		 });
		 
		
		 
		 //创建节点
		 
		 zkClient.create("/zkclient", "hello zk", CreateMode.EPHEMERAL);
		 
		 //zookeeper 不支持递归创建znode，创建时必须检测父类是否存在
		 //zkClient封装了 创建父类节点
		 zkClient.createPersistent("/zkhello/zk", true);
		 
		 //删除节点
		 zkClient.delete("/zkClient");
		 //zookeeper删除是若有子节点在 不给于删除  此接口给于封装可以删除
		 zkClient.deleteRecursive("/zkhello");
		 
		 //读取数据
		 zkClient.getChildren("/zkhello");
		 zkClient.readData("/zkhello");
		 
		 //更新数据
		 zkClient.writeData("/zkhello", "hello");
		 
		 
		 //节点检测
		 zkClient.exists("/zkhello");
		 
	}

}
