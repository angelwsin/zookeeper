package com.zookeeper.task_schedule;

import java.io.Serializable;

public class JobConf implements Serializable{
    
    
    
     /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String  bootClass;
     
     private String   executeMethod;
     
     private String   jobPath;
     
     private String   jobNode;
     
     private String   monitorNode;

    public String getBootClass() {
        return bootClass;
    }

    public void setBootClass(String bootClass) {
        this.bootClass = bootClass;
    }

    public String getExecuteMethod() {
        return executeMethod;
    }

    public void setExecuteMethod(String executeMethod) {
        this.executeMethod = executeMethod;
    }

    public String getJobPath() {
        return jobPath;
    }

    public void setJobPath(String jobPath) {
        this.jobPath = jobPath;
    }

    public String getJobNode() {
        return jobNode;
    }

    public void setJobNode(String jobNode) {
        this.jobNode = jobNode;
    }

    public String getMonitorNode() {
        return monitorNode;
    }

    public void setMonitorNode(String monitorNode) {
        this.monitorNode = monitorNode;
    }
     
     
     
    

}
