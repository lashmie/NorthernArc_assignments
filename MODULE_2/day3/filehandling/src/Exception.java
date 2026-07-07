import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

//public class Exception {
//    public static void main(String[] args) {
//        try{
//            Writer fw = new FileWriter("lavanya.txt");
//        }catch(IOException e){
//            throw new RuntimeException();
//        }
//        finally {
//            try{
//                fw.close();
//            }catch(IOException e) {
//                throw new RuntimeException();
//
//            }
//        }
//
//
//    }
//}

public class Exception{
    public static void main(String[] args) {
        try(Writer fw = new FileWriter("Myfirstfile",true);){
            fw.write("hello\n");
            fw.write("lava");

        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
