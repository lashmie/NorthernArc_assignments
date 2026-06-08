public class bytee {
    public static void main(String[] args) {
        Byte val =10;
        Byte b = Byte.valueOf(val);
        System.out.println(b);//boxing

        //unboxing
        byte a = b.byteValue();
        System.out.println(a);

        Byte c =b;


            }
}
