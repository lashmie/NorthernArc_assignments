package demo1;

import java.util.LinkedList;
import java.util.List;

public class withLinkedList {
    public static void main(String[] args) {
        List<Integer> list = new LinkedList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println(list);
        list.remove(2);
        System.out.println(list);
        System.out.println(list.contains(1));
    }
}
