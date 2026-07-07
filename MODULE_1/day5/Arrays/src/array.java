public class array {
    public static void main(String[] args) {
        //arrays are collection of values of same type
        //stored in contiguous memory locations

        int[] arr = new int[5];
//        arr[0]=10;
//        arr[1]=20;
//        arr[2]=30;
//        arr[3]=40;
//        arr[4]=50;
        System.out.println("The length of the array is"+arr.length);
        for(int i=0;i<arr.length;i++){
            System.out.println(arr[i]);
        }
    }
}
