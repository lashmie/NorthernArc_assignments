package Custom.Iterables;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        MyRange range1= new MyRange(10,20);
        Iterator itr = range1.iterator();

        while(itr.hasNext()){
            System.out.println(itr.next());
        }

        System.out.println("==========================");
        for(Object data: range1){
            System.out.println((Integer)data);
        }
    }
}
