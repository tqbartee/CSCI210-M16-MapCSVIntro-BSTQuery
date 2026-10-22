import com.zybooks.dsaj.treebook.Position;
import com.zybooks.dsaj.util.Entry;

public class SimpleMapBST<K, V> implements SimpleMap<K, V> {

    protected static class MapEntry<K, V> implements Entry<K, V> {
        private final K key;
        private V value;

        public MapEntry(K key, V value) {
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

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private final BinarySearchTree<K, V> tree;

    public SimpleMapBST() {
        this.tree = new BinarySearchTree<>();
    }

    public SimpleMapBST(BinarySearchTree<K, V> tree) {
        this.tree = (tree != null) ? tree : new BinarySearchTree<>();
    }

    public BinarySearchTree<K, V> getTree() {
        return tree;
    }

    @Override
    public V get(K key) {
        if (key == null || tree.isEmpty()) {
            return null;
        }
        Entry<K, V> query = new MapEntry<>(key, null);
        Position<Entry<K, V>> p = tree.get(query);
        if (p == null || p.getElement() == null) {
            return null;
        }
        if (tree.compare(query, p.getElement()) == 0) {
            return p.getElement().getValue();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            return null;
        }
        Entry<K, V> newEntry = new MapEntry<>(key, value);
        Position<Entry<K, V>> p = tree.get(newEntry);
        V oldValue = null;
        if (p != null && p.getElement() != null && tree.compare(newEntry, p.getElement()) == 0) {
            oldValue = p.getElement().getValue();
        }
        tree.put(newEntry);
        return oldValue;
    }

    @Override
    public V remove(K key) {
        if (key == null || tree.isEmpty()) {
            return null;
        }
        Entry<K, V> query = new MapEntry<>(key, null);
        Position<Entry<K, V>> p = tree.get(query);
        if (p != null && p.getElement() != null && tree.compare(query, p.getElement()) == 0) {
            V oldValue = p.getElement().getValue();
            tree.delete(p);
            return oldValue;
        }
        return null;
    }
}
