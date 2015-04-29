package rayutil;

import java.util.Map;

final public class RayEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public RayEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V pre = this.value;
        this.value = value;
        return pre;
    }
}