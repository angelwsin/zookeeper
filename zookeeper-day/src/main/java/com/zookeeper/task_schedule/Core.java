package com.zookeeper.task_schedule;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * job
 * @author Administrator
 *
 */
public class Core {
    
   static  Logger  log = LoggerFactory.getLogger(Core.class);
   static  Thread process1;
    static{
         process1 = new Core().new ProcessTask();
        process1.setDaemon(true);
        process1.start();
    }
    
    
        //job/jobTask
    public static void main(String[] args)throws Exception {
        ExponentialBackoffRetry retryPolicy   = new ExponentialBackoffRetry(300, 4);
        CuratorFramework fw =  CuratorFrameworkFactory.builder().connectString("localhost:2181")
                               .connectionTimeoutMs(400).sessionTimeoutMs(500).retryPolicy(retryPolicy)
                               .build();
        
        fw.start();
        
        
        PathChildrenCache  jobConfig = new PathChildrenCache(fw, "/job", false);
        jobConfig.start();
        jobConfig.getListenable().addListener((CuratorFramework client, PathChildrenCacheEvent event)->{
            System.out.println(event);
           switch (event.getType()) {
            case CHILD_ADDED:{
                ChildData data = event.getData();
                data.getPath();
                break;

            }  
            case CHILD_UPDATED:{
               ChildData data = event.getData();
               GetDataBuilder get = fw.getData();
               byte[] b = get.forPath(data.getPath());
               que.put(new TaskEvent<byte[]>(EventTask.start,b));
               break;
            }
            default:
                break;
            }
        });
        
        Thread.sleep(Integer.MAX_VALUE);
    }
    
    static  int process   = Runtime.getRuntime().availableProcessors();
    static  Thread[] threads = new Thread[process];
    
    static  ArrayBlockingQueue<TaskEvent<byte[]>> que = new ArrayBlockingQueue<>(1000);
    
    
    
    public static void  execute(byte[] data){
        
         try {
             String ds = new  String(data);
             System.out.println(ds);
             String[] ps = ds.split(",");
             if(ps==null||ps.length<3) return ;
             URL url = new  URL("jar:file:/"+System.getProperty("user.dir")+"/job/"+ps[0]+"!/");
             JobLoader jobLoader = new JobLoader(new URL[]{url});
             Method method =  jobLoader.loadClass(ps[1]).getDeclaredMethod(ps[2], String[].class);
             //可变参数问题
             //编译器会兼容jdk 1.4的语法，即按照1.4的语法进行处理，即把字符串数组打散成为若干个单独的参数，这样就会产生参数个数不匹配的异常
             method.invoke(null, (Object)new String[]{});  
        } catch (Exception e) {
            // TODO: handle exception
        }
         
         
    }
    
    
    class ProcessTask extends Thread {

        @Override
        public void run() {
            for (;;) {
                try {
                    TaskEvent<byte[]> event = que.take();
                    if (event != null) {
                      switch (event.event) {
                    case start:{
                        execute(event.data);
                        break;
                    }
                    default:
                        break;
                    }
                    }
                } catch (Exception e) {
                }

            }
        }

    }

    
}
