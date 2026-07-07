package intro;

import java.sql.SQLOutput;

public class Main1 {
    public static void main(String[] args) {
        String s1="Hello";
        String s2 ="Hello";
        System.out.println(s1==s2);
        s2=s2+"world";
        System.out.println(s1==s2);
        System.out.println("s1="+s1);
        System.out.println("s2="+s2);
    }
}
