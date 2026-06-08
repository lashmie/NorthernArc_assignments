package exercise;

import java.sql.SQLOutput;

public class Main3 {
    public static void main(String[] args) {
        Object o1=new Object();
        Object o2=new Object();
        Object o3=o1;
        System.out.println(o1==o2);
        System.out.println(o3==o1);
        System.out.println(o1.equals(o2));
        System.out.println(o3.equals(o1));
        System.out.println(o1.hashCode());
        System.out.println(o1.hashCode()==o2.hashCode());
        System.out.println(o1.getClass().getSimpleName());//name of the class
        System.out.println(o1.getClass().getName());// name of the package
        System.out.println(o1.toString());
    }
}
