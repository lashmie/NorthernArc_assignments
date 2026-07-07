package first;
import java.util.InputMismatchException;

public class Main2 {
    public static void main(String[] args) {//we can provide throws here also ..
        Thread t1 = new MyThread("lavanya");
        Thread t2 = new MyThread("lashmi");
        t1.start();
        t2.start();
        //joining
        try{
            t1.join();
            t2.join();
        }
        catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
        System.out.println("how many threads are active : "+Thread.activeCount());
        for(int i=0;i<=100;i++){
            System.out.println(i+ " "+Thread.currentThread().getName());
        }
    }
}
