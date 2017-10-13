package com.zookeeper.task_schedule;

import java.util.UUID;

public class TaskMasterSalve extends Thread{
    
    
    public static void main(String[] args) {
        UUID uid = UUID.randomUUID();
        System.out.println(uid.toString()); 
    }
    
    @Override
    public void run() {
        
        UUID uid = UUID.randomUUID();
        System.out.println(uid.toString());
        super.run();
    }

}
