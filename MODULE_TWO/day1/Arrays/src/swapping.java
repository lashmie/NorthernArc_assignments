import java.util.Arrays;

public class swapping {//swapping
    public static void main(String[] args) {

        String arr1[]={"lava","mohi","lash"};
//        int n=arr1.length;
        System.out.println(Arrays.toString(arr1));
//        String arr2[]= new String[n];
            String temp=null;
           temp=arr1[0];
           arr1[0]=arr1[arr1.length-1];
           arr1[arr1.length-1]=temp;



        System.out.println(Arrays.toString(arr1));

    }
}
