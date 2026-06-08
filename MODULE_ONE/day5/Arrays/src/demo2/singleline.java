package demo2;

public class singleline {
    public static void main(String[] args) {
        //int arr[]= new int[5];
        int[] arr={1,2,3,4,5};
        System.out.println(arr[0]);
        System.out.println(arr[1]);
        arr[0]=100;
        //normal for loop
        System.out.println("");
        for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]);
        }
        //for each
        System.out.println(" ");
        for(int val:arr){
            System.out.print(val);
        }

        short[] arr1= new short[5];
        arr1[0]=1;
        arr1[4]=3;
        System.out.println("The for each loop of short");
        for(short val :arr1){
            System.out.println(val);
        }

    }
}
