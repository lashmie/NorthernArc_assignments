package intro;

public class Main2 {
    public static void main(String[] args) {
        String s= new String("hello");
        String s2="hello";
        System.out.println(s==s2);
        System.out.println(s.intern()==s2);
        System.out.println(s.equals(s2));
    }
}
