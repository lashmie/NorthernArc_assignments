package first;


import java.io.FileWriter;
import java.io.IOException;

public class MyThread1 extends Thread {

    public MyThread1(String name) {
        super(name);
    }

    public void run() {

        try {
            // Each thread writes into SAME file
            FileWriter fw = new FileWriter("output.txt", true); // append mode

            for (int i = 1; i <= 100; i++) {
                String data = i + " " + this.getName() + "\n";

                System.out.print(data);   // console output
                fw.write(data);           // file output
            }

            fw.close();

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}
