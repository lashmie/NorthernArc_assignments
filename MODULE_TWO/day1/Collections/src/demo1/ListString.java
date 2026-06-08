package demo1;

import java.util.ArrayList;
import java.util.List;

public class ListString {
    public static void main(String[] args) {
        List<String> a = new ArrayList<String>();
        a.add("lavanya");
        a.add("lashmi");
        a.add("moahana");
        System.out.println("The list length is "+a.size());
        System.out.println(a);

        a.remove(0);
        System.out.println(a);
        System.out.println(a.contains("lavanya"));

        System.out.println(a.isEmpty());
    }
}
