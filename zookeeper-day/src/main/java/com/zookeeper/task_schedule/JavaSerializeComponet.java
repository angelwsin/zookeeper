package com.zookeeper.task_schedule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaSerializeComponet implements SerializeComponet{

    @Override
    public byte[] enCode(Object obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    @Override
    public Object deCode(byte[] data) {
        Object obj = null;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ObjectInputStream input = new ObjectInputStream(in);
            obj =  input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return obj;
    }

}
