import java.io.FileInputStream;
import java.io.IOException;

public class stream1 {
    public static void main(String[] args) {
        try(FileInputStream op = new FileInputStream("OutputStream.java");){
            int val;
            while ((val =op.read())!=-1){
                System.out.println("data has been written to the file!!");}
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}