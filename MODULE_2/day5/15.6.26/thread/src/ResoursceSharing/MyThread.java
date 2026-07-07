package ResoursceSharing;

import java.io.*;

public class MyThread extends Thread{
    private static OutputStream destinationwriter;
    private InputStream sourcereader;
    public MyThread(String name,InputStream sourcereader){
        super(name);
        this.sourcereader=sourcereader;
    }
    public static void openDestinationWriter(){
        try {
            MyThread.destinationwriter=new FileOutputStream("Output.log");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static  void closeDestinationWriter(){
        try {
            destinationwriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        synchronized (destinationwriter){
            System.out.println("Thread Name: "+Thread.currentThread().getName());
            int c;
            while(true){
                try {
                    if (!((c=sourcereader.read())!=-1)) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    destinationwriter.write(c);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                finally{
                    try {
                        sourcereader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }

    }
}
