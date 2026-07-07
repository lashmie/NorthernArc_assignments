import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
public class classassignments {

    public static void main(String[] args) {
//        Queue<String> q = new LinkedList<>();
//        q.add("lavanya");
//        q.add("lashmi");
//        System.out.println(q);
//        q.remove();
//        System.out.println(q);

        Queue<String > q = new PriorityQueue<>();
        q.add("ram");
        q.add("lakshman");
        q.add("abi");
        System.out.println(q);
        q.remove();
        System.out.println(q);

    }
}
