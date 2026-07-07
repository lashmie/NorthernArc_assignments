package Custom.Iterables;

import java.util.Iterator;

public class MyRange implements Iterable {
    private int start;
    private int end;
    MyRange(int start,int end){
        this.start=start;
        this.end=end;
    }
    @Override
    public Iterator iterator(){
        return new MyRangeIterator(start,end);
    }
}
