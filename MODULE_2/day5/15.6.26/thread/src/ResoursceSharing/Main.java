package ResoursceSharing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            Thread t1=new MyThread("Sachin",new FileInputStream("C:\\Guvi_Learning\\MODULE_TWO\\day5\\15.6.26\\thread\\src\\ResoursceSharing\\sachin.txt"));
            Thread t2=new MyThread("Saurav",new FileInputStream("C:\\Guvi_Learning\\MODULE_TWO\\day5\\15.6.26\\thread\\src\\ResoursceSharing\\Saurav.txt"));
            MyThread.openDestinationWriter();
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            MyThread.closeDestinationWriter();
            System.out.println("Exiting main thread!!!");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
