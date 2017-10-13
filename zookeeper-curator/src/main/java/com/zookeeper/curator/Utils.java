package com.zookeeper.curator;

import org.apache.curator.utils.ZKPaths;

public class Utils {
	
	
	public static void main(String[] args) {
		
		//ZKPaths znode路径，递归创建和删除
		ZKPaths.makePath("parent", "/child");
		
		
		//EnsurePath  节点存在 无操作，节点不存在创建
		
		
		
	}

}
