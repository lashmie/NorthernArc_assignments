package demo1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoubleString {
    public static void main(String[] args) {
        List<Double> a =new ArrayList<>();
        a.add(1.0);
        a.add(4.0);
        a.add(2.0);
        System.out.println(a);
        a.add(8.5);
        System.out.println(a);
        Collections.sort(a);
        System.out.println(a);
        a.remove(3);
        System.out.println(a);
        System.out.println(a.get(1));
        a.set(0,0.0);
        System.out.println(a);
    }
}
