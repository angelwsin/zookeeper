package com.zookeeper.task_schedule;

public interface SerializeComponet {
    
    
    
    
    
    
    
       byte[]  enCode(Object obj);
       
       Object  deCode(byte[] data);

}
