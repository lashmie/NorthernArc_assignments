package ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;

//public class Inventory {
//    private Queue<String> stock = new LinkedList<>();
//    public String add(String item)throws InterruptedException{
//        stock.add(item);
//        return item;
//    }
//
//    public String remove( )throws InterruptedException{
//            wait();
//        return stock.remove();
//    }
//}
import java.util.LinkedList;
import java.util.Queue;
public class Inventory {
    private Queue<String> stock = new LinkedList<>();
    //add method will be called by producer
    public String add(String item) throws InterruptedException {
        stock.add(item);
        return item;
    }
    //remove method will be called by consumer
    public String remove() throws InterruptedException {
        return stock.remove();
    }
    public int size(){
        return stock.size();
    }
}
