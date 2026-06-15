import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.Exception;

public class fileWrite {
    public static void main(String[] args) {
        try(Writer fw = new FileWriter("firstfile.txt");
                BufferedWriter bfr = new BufferedWriter(fw);){
            for(int i=0;i<10;i++){
            bfr.write("lavanya");}
        }

        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
