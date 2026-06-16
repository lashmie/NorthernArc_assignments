package Callable;
import java.util.concurrent.*;
public class ExecutorDemoCall {
    public static void main(String[] args) {
        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        Future<Integer> futureVal =executor.submit(()->{
            Thread.sleep((int)(10000*Math.random()));
            System.out.println("Callable is called by "+Thread.currentThread().getName());
            return (int)(Math.random()*10000);
        });
        System.out.println("Future value will be printed now................");
        try {
            int value = futureVal.get(5, TimeUnit.SECONDS);
            System.out.println("Value: "+value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        executor.shutdown();
    }
}