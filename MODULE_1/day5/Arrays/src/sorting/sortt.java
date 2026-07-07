package sorting;

import java.util.Arrays;

public class sortt {
    public static void main(String[] args) {
        Integer arr[]={1,2,3,9,4};
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));

        System.out.println("-------------------");


        String str[]={"lava","lash","vani"};
        System.out.println(Arrays.toString(str));
        Arrays.sort(str);
        System.out.println("After sorting: "+Arrays.toString(str));
    }
}
