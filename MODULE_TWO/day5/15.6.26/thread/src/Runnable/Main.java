package Runnable;

public class Main {
    public static void main(String[] args) {
//        Thread t1 = new Thread(new myRunnable());
//        Thread t2 = new Thread(new myRunnable());
//        Thread t3 = new Thread( ()-> {
//        for(int i=1;i<=10;i++){
//                System.out.println(i+" "+Thread.currentThread().getName());
//            }
//        });
//        t1.start();
//        t2.start();
        for(int i=1;i<=3;i++){
            new Thread(()->{
                for(int j=1;j<=10;j++){
                    System.out.println(j+" "+Thread.currentThread().getName());
                }
            }).start();
        }



    }
}
