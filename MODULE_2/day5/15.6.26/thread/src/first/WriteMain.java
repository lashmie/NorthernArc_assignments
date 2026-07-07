package first;

public class WriteMain {
    public static void main(String[] args) {

        Thread t1 = new MyThread("lavanya");
        Thread t2 = new MyThread("laxmi");

        t1.start();
        t2.start();

        // join = wait until both threads finish writing to file
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("All threads finished writing file.");

        System.out.println("Active threads: " + Thread.activeCount());

        for (int i = 0; i <= 100; i++) {
            System.out.println(i + " Main thread");
        }
    }
}