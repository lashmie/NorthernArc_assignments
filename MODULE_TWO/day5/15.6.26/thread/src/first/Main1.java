package first;

public class Main1 {
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
    }

    public static class MyThreadwrite {
    }
}
