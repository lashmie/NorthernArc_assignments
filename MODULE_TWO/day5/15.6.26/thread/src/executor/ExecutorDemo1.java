package executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorDemo1{

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable task1 = () -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println(
                        Thread.currentThread().getName()
                                + " Task-1 : " + i);
            }
        };

        Runnable task2 = () -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println(
                        Thread.currentThread().getName()
                                + " Task-2 : " + i);
            }
        };

        Runnable task3 = () -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println(
                        Thread.currentThread().getName()
                                + " Task-3 : " + i);
            }
        };

        executor.submit(task1);
        executor.submit(task2);
        executor.submit(task3);

        executor.shutdown();

        System.out.println("Main thread finished.");
    }
}