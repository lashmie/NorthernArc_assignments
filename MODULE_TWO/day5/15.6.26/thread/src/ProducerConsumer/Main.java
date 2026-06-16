package ProducerConsumer;
import ProducerConsumer.Inventory;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Inventory inventory = new Inventory();
        Thread producer = new Thread(()->{
            try {
                int i = 1;
                while (i <= 10000) {
                    synchronized (inventory) {
                        while (inventory.size() >= 10) {
                            System.out.println("Inventory is full , producer going to sleep.");
                            inventory.wait();
                        }
                        System.out.println("Producer produced: " + inventory.add("item_" + i));
                        i++;
                        inventory.notify();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread consumer = new Thread(()->{
            try {
                int i = 1;
                while (i <= 100) {
                    synchronized (inventory) {
                        while (inventory.size() <= 0) {
                            System.out.println("Inventory is empty , consumer going to sleep.");
                            inventory.wait();
                        }
                        System.out.println("Consumer is consumed :" + inventory.remove());
                        i++;

                        inventory.notify();
                    }
                } } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        producer.start();
        Thread.sleep(100);
        consumer.start();
    }
}