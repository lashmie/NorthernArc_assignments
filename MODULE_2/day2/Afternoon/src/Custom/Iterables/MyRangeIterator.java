package Custom.Iterables;
import java.util.*;
public class MyRangeIterator implements Iterator{
    private int start;
    private int end;
    public MyRangeIterator(int start,int end){
        this.start=start;
        this.end=end;
    }

    public boolean hasNext(){
        return start<=end;
    }
    public Object next(){
//        start+=1;
//        return start;
        int temp =start;
        start+=1;
        return temp;//10 to 20 is printed
    }
}
