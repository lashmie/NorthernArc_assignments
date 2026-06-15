import java.io.FileOutputStream;
import java.io.IOException;

public class stream2 {
    public static void main(String[] args) {

        try (FileOutputStream fos = new FileOutputStream("output.txt")) {

            String data = "Hello, this is written using OutputStream";

            fos.write(data.getBytes());

            System.out.println("Data has been written to the file!!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
