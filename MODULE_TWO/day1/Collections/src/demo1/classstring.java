package demo1;

import java.util.ArrayList;
import java.util.List;

public class classstring {
    public static void main(String[] args) {
        List<Person> a = new ArrayList<Person>();
        a.add(new Person("laa",21));
        a.add(new Person("mohi",18));
        System.out.println(a);
        a.remove(1);
        System.out.println(a);
        a.set(0,new Person("selvi",65));
        System.out.println(a);
    }
}
