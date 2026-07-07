package Deadlock;

public class Solution {

    static Object chopstick1 = new Object();
    static Object chopstick2 = new Object();

    static class Philosopher implements Runnable {

        private String name;

        public Philosopher(String name) {
            this.name = name;
        }

        @Override
        public void run() {

            System.out.println(name + " is occupying chopstick 1");

            synchronized (chopstick1) {

                System.out.println(name +
                        " occupied chopstick 1, going for chopstick 2");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (chopstick2) {
                    System.out.println(name + " is eating");
                }
            }
        }
    }

    public static void main(String[] args) {

        Thread philosopher1 =
                new Thread(new Philosopher("Philosopher1"));

        Thread philosopher2 =
                new Thread(new Philosopher("Philosopher2"));

        philosopher1.start();
        philosopher2.start();
    }
}