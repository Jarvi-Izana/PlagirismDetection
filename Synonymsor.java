import java.util.List;
import java.util.Map;

/**
 * Created by GuihaoLiang on 11/3/16.
 * get synonyms for abstract file
 * Object K can have multiple meanings, such as V1, V2, V3...
 */
public interface Synonymsor<K extends Comparable<K>, V extends Comparable<V>> {
    Map<K, List<V>> getSynonyms();
}
