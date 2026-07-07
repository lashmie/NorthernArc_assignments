public class booleanMain {
    public static void main(String[] args) {
        Boolean b = true;
        Boolean boxing =Boolean.valueOf(b);
        System.out.println(b);//boxed value

        boolean unboxed =boxing.booleanValue();
        System.out.println(unboxed);//unboxing

        Boolean a =false;//Autoboxing

    }
}
