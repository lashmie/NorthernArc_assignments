package sorting;

import java.util.Arrays;

public class reverse {
    public static void main(String[] args) {
        int arr[]={1,2,3,4,5};
        int temp=0;
        System.out.println("The array is "+ Arrays.toString(arr));
        for(int i=0;i<arr.length/2;i++){
            int n=arr.length;
            int j=n-(i+1);
            temp = arr[i];
            arr[i]=arr[j];
            arr[j]=temp;

        }
        System.out.println("After sorting : "+Arrays.toString(arr));
    }
}
