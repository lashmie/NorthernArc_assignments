import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.Exception;

public class WholeLineRead {
    public static void main(String[] args) {
        try(Reader rd = new FileReader("Myfirstfile");
            BufferedReader bfr = new BufferedReader(rd);){
            String line;
            do{
                line= bfr.readLine();
                if(line!=null)
                    System.out.println(line);
            }while(line!=null);
        }
        catch (FileNotFoundException e){
            System.out.println("file not found");
        }
        catch (Exception e){
            System.out.println("something had happened");
        }
    }
}
