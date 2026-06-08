public class IntMain {
    public static void main(String[] args) {
        int value=10;
//        Integer i = new Integer(5);
        Integer i = Integer.valueOf(value);//boxing
        System.out.println(i);



        //Unboxing
        int unboxed = i.intValue();

        Integer j=value;//Autoboxing
        System.out.println(j);


    }
}
