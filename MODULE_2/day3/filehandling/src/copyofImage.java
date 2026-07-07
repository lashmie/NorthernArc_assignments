import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class copyofImage {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("input.png");
             FileOutputStream fos = new FileOutputStream("output.png")) {
            int data;
            while ((data = fis.read()) != -1) {
                fos.write(data);
            }
            System.out.println("file copied successfully");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
