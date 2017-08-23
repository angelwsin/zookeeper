package com.zookeeper.task_schedule;

public class TaskEvent<T> {
    
    EventTask event;
    T     data;
    public TaskEvent(EventTask event, T data) {
        this.event = event;
        this.data = data;
    }

}
