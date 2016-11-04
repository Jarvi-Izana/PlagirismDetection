/**
 * Created by GuihaoLiang on 11/3/16.
 * the interface
 */

public interface Indexable<T extends Comparable<T>> {
    T get(int i);
    int size();
    int currentIdx();
    int resetIdx();
}
