package demo1;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Personll {
    public static void main(String[] args) {
        Queue<Person> q = new LinkedList<>();
        q.add(new Person("lavanya",21));
        q.add(new Person("veera",23));
        System.out.println(q);
        q.remove();
        System.out.println(q);

//        Queue<Person> q1 = new PriorityQueue<>(new  );
//        q1.add(new Person("karishma",21));


    }
}
