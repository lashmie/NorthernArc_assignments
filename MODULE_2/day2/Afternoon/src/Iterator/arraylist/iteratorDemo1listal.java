package Iterator.arraylist;

import java.util.ArrayList;
import java.util.Iterator;

public class iteratorDemo1listal {
    public static void main(String[] args) {
        ArrayList<String> arr = new ArrayList<>();
        arr.add("lavanya");
        arr.add("Renji");
        arr.add("yuva");
        arr.add("kamya");

        System.out.println("for each loop");
        for(String val:arr){
            System.out.println(val);
        }

        System.out.println("iterator");
        Iterator<String> itr = arr.listIterator();

        System.out.println(itr.getClass().getName());
        System.out.println();
        while (itr.hasNext()){
            System.out.println(itr.next());
        }

    }
}
