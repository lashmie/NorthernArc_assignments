package SET;

import java.util.HashSet;
import java.util.Set;

public class maIN {
    public static void main(String[] args) {

        Set<Integer> set = new HashSet<>();
        set.add(10);
        set.add(10);
        set.add(20);
        set.add(30);
        System.out.println(set);//order not maintained but faster
        //sorted -Linked list



    }
}
