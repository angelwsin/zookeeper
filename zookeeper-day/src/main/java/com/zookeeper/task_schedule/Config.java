package com.zookeeper.task_schedule;

import java.util.Objects;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.api.SetDataBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Config {
    
    
    
   public static void main(String[] args) {
       
       jobConfig();
    
  }
   
   
   //job 的加载   zookeeper /job/jobtype znode
   
   
   public static void  jobConfig(){
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(300, 5);
        CuratorFramework fw =   CuratorFrameworkFactory.builder().connectString("localhost:2181")
                               .connectionTimeoutMs(400).sessionTimeoutMs(500)
                               .retryPolicy(retryPolicy).namespace("job").build();
        
        fw.start();
        
        try {
            ExistsBuilder exists = fw.checkExists();
            if(Objects.isNull(exists.forPath("/download"))){
                CreateBuilder create = fw.create();
                create.forPath("/download"); 
            }
            SetDataBuilder setData = fw.setData();
            setData.forPath("/download", "task.jar,com.zookeeper.task_schedule.Task,main".getBytes());
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
   }
   
   
   

}
