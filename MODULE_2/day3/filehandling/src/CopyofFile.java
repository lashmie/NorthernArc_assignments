import java.io.*;

public class CopyofFile {
    public static void main(String[] args) {
        try(Writer fw = new FileWriter("copyfile.txt"); Reader rd =new FileReader("firstfile.txt")){
            int val;
            while ((val=rd.read())!=-1){
                fw.write(val);}


        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
