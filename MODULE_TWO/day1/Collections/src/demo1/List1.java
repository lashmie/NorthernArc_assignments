package demo1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class List1 {
    public static void main(String[] args) {
        List<Integer> a = new ArrayList<Integer>();
        a.add(10);
        a.add(20);
        a.add(30);
        System.out.println(a);

        a.add(1, 15);
        System.out.println(a);

        a.remove(2);
        System.out.println(a);

        a.remove(Integer.valueOf(15));
        System.out.println(a);

        System.out.println(a.get(1));

        a.set(1, 50);
        System.out.println(a);

        System.out.println(a.size());

        System.out.println(a.contains(50));

        for (Integer num : a) {
            System.out.println(num);
        }

        a.clear();
        System.out.println(a);
        a.add(5);
        a.add(3);
        a.add(1);
        Collections.sort(a);
        System.out.println(a);

    }
}
