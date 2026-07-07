import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.Exception;

public class Reading {
    public static void main(String[] args) {
        try(Reader rd = new FileReader("Myfirstfile");){
            int value;
            do{
                value=rd.read();
                if(value !=-1)
                System.out.print((char)value);
            }
            while(value!=-1);
        }
        catch (FileNotFoundException e){
            System.out.println("file not found");
        }
        catch (Exception e){
            System.out.println("something had happened");
        }
    }
}
