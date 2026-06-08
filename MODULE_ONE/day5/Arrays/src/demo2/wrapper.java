package demo2;
import java.util.Arrays;
public class wrapper {
    public static void main(String[] args) {
        Integer[] arr =new Integer[5];
        Arrays.fill(arr,10);
        for(int val:arr){
            System.out.println(val);
        }
    }
}
