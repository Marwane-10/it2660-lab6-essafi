
import java.util.Set;

public interface MyMap<K, V> {
    int size();

    boolean isEmpty();

    boolean containsKey(K key);

    boolean containsValue(V value);

    V get(K key);

    V put(K key, V value);

    V remove(K key);

    void clear();

    Set<K> keySet();

    Set<MyHashMap.Entry<K, V>> entrySet();
}
