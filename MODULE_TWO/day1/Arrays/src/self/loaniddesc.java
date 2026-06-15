package self;

import java.util.Comparator;

public class loaniddesc implements Comparator<nbfc> {
    @Override
    public int compare(nbfc o1, nbfc o2) {
        return o2.getLoanid().compareTo(o1.getLoanid());
    }
}
