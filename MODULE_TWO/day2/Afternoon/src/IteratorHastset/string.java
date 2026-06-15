package IteratorHastset;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class string {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("lavanya");
        set.add("vinay");
        set.add("vikram");
        System.out.println("for loop");

        for(String val:set){
            System.out.println(val);
        }

        System.out.println("for Iterator");
        Iterator<String> itr =set.iterator();
        while (itr.hasNext()){
            System.out.println(itr.next());
        }
    }
}
