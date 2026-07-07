import java.util.Arrays;
public class Stringa {

    public static void main(String[] args) {
        String[] arr = new String[5];

        Arrays.fill(arr," not assigned");
        arr[0]="lav";
        arr[1]="lash";
        System.out.println(arr.length);
        for(int i=0;i<arr.length;i++){
            System.out.println(arr[i]);
        }
    }
}
