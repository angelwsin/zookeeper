package zookeeper_day.zookeeper_day;

import java.lang.reflect.Method;
import java.net.URL;

import com.zookeeper.task_schedule.JobLoader;

/**
 * Hello world!
 *
 */
public class App 


{
    
   
    public static void main( String[] args )throws Exception
    {
        //String name,int pos,int endPos,URL url,File path
        URL url = new  URL("jar:file:/"+System.getProperty("user.dir")+"/job/task.jar!/");
         JobLoader jobLoader = new JobLoader(new URL[]{url});
         Method method =  jobLoader.loadClass("com.zookeeper.task_schedule.Task").getDeclaredMethod("main", String[].class);
         //可变参数问题
         //编译器会兼容jdk 1.4的语法，即按照1.4的语法进行处理，即把字符串数组打散成为若干个单独的参数，这样就会产生参数个数不匹配的异常
         method.invoke(null, (Object)new String[]{});
         
         App.class.getDeclaredMethod("t", String[].class).invoke(null, new String[]{});
    }
    
    
    public static void t( String[] args){
        System.out.println("t");
        
        
    }
    
    public static void x(String ...arg){
        System.out.println(arg);
    }
}
