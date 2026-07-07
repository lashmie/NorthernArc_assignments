public class ShortMain {
    public static void main(String[] args) {
        Short sh =10;
        Short i =Short.valueOf(sh);//boxing
        System.out.println(i);


        int j = i.shortValue();//unboxing;
        System.out.println(j);

        Integer val = j;//autoboxing
        System.out.println(val);




    }
}
