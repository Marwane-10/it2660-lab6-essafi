
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 4;
    private static final float DEFAULT_MAX_LOAD_FACTOR = 0.75f;

    private int capacity;
    private float loadFactorThreshold;
    private int size = 0;
    private LinkedList<Entry<K, V>>[] table;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_MAX_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactorThreshold) {
        this.capacity = trimToPowerOf2(initialCapacity);
        this.loadFactorThreshold = loadFactorThreshold;
        table = new LinkedList[capacity];
    }

    private int trimToPowerOf2(int initialCapacity) {
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        return capacity;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                table[i].clear();
            }
        }
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (Entry<K, V> entry : table[i]) {
                    if (entry.getValue().equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int bucketIndex = hash(key.hashCode());

        if (table[bucketIndex] != null) {
            for (Entry<K, V> entry : table[bucketIndex]) {
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (get(key) != null) {
            int bucketIndex = hash(key.hashCode());

            for (Entry<K, V> entry : table[bucketIndex]) {
                if (entry.getKey().equals(key)) {
                    V oldValue = entry.getValue();
                    entry.value = value;
                    return oldValue;
                }
            }
        }

        if (size >= capacity * loadFactorThreshold) {
            rehash();
        }

        int bucketIndex = hash(key.hashCode());

        if (table[bucketIndex] == null) {
            table[bucketIndex] = new LinkedList<>();
        }

        table[bucketIndex].add(new Entry<>(key, value));
        size++;

        return value;
    }

    @Override
    public V remove(K key) {
        int bucketIndex = hash(key.hashCode());

        if (table[bucketIndex] != null) {
            for (Entry<K, V> entry : table[bucketIndex]) {
                if (entry.getKey().equals(key)) {
                    V value = entry.getValue();
                    table[bucketIndex].remove(entry);
                    size--;
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();

        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                for (Entry<K, V> entry : table[i]) {
                    set.add(entry.getKey());
                }
            }
        }
        return set;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<>();

        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                set.addAll(table[i]);
            }
        }
        return set;
    }

    public int hash(int hashCode) {
        return (hashCode & 0x7FFFFFFF) % capacity;
    }

    private void rehash() {
        Set<Entry<K, V>> set = entrySet();

        capacity *= 2;
        table = new LinkedList[capacity];
        size = 0;

        for (Entry<K, V> entry : set) {
            put(entry.getKey(), entry.getValue());
        }
    }

    static class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "[" + key + ", " + value + "]";
        }
    }
}
