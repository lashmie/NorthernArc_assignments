package Deadlock;

public class DeadLockDemo {
    public static void main(String[] args) throws InterruptedException {
        Object chopstick1=new Object();
        Object chopstick2=new Object();
        Thread philosopher1=new Thread(()->{
            System.out.println("Philosopher 1 is occupying chopstick 1");
            synchronized (chopstick1){
                System.out.println("Philosopher 1 occupied chopstick 1,going for chopstick 2");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (chopstick2){
                    System.out.println("Philosopher 1 is eating");
                }
            }
        });

        Thread philosopher2=new Thread(()->{
            System.out.println("Philosopher 2 is occupying chopstick 2");
            synchronized (chopstick2){
                System.out.println("Philosopher 2 occupied chopstick 2,going for chopstick 1");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (chopstick1){
                    System.out.println("Philosopher 2 is eating");
                }
            }
        });

        philosopher1.start();
        philosopher2.start();
    }
}
