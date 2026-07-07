package homeassignment;

import homeassignment.Childclass;
import java.util.Comparator;

public class bynamedesc implements Comparator<Childclass> {
    @Override
    public int compare(Childclass o1, Childclass o2) {
        return o2.getFname().compareTo(o1.getFname());
    }
}
