package DaemonThread;

public class Main {
    public static void main(String[] args) {
        Thread t1 = new MyThread("abi",500);
        Thread t2= new MyThread("bala",1000);
        Thread t3 =new MyThread("camii",1500);
        ///all are running ..that is completed fullu... 10 times running
        t3.setDaemon(true);//cami ran 6 times only but the main thread ends ...
        t1.start();
        t2.start();
        t3.start();

    }
}
