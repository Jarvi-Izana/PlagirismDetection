import java.util.Iterator;

/**
 * Created by GuihaoLiang on 11/3/16.
 * the interface
 */

public interface Indexable<E extends Comparable<E>> {
    E get(int i);
    int size();
    int currentIdx();
    int resetIdx();
}
