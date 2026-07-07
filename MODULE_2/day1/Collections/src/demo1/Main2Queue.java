package demo1;

import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main2Queue {
    public static void main(String[] args) {
//        Queue<Intdeger> q = new LinkedList<Integer>();
//        q.add(1);
//        q.add(2);
//        q.remove();
//        System.out.println(q);//removing in fifo

        Queue<Integer> a = new PriorityQueue<Integer>(Collections.reverseOrder());
        a.add(1);
        a.add(2);
        a.add(300);
        System.out.println(a);
        a.remove();
        System.out.println(a);
        System.out.println(a);
    }
}
