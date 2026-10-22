import com.zybooks.dsaj.util.Entry;
import java.util.Objects;

public class BSTBuilder {

    public static class BSTEntry<K, V> implements Entry<K, V> {
        private final K key;
        private V value;

        public BSTEntry(K key, V value) {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entry)) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.getKey()) &&
                   Objects.equals(value, entry.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    private BSTBuilder() {
        // Private constructor to prevent instantiation of utility class
    }

    public static BinarySearchTree<Integer, Integer>
    buildUnbalancedBST(BinarySearchTree<Integer, Integer> bst) {
        if (bst == null) {
            bst = new BinarySearchTree<>();
        }
        int[] values = {2, 4, 5, 7, 8, 9, 12, 14, 17, 19, 22, 25, 27, 28, 33, 37};
        for (int v : values) {
            bst.put(new BSTEntry<>(v, v));
        }
        return bst;
    }

    public static BinarySearchTree<Integer, Integer> buildUnbalancedBST() {
        return buildUnbalancedBST(new BinarySearchTree<>());
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer, Integer> bst1 = new BinarySearchTree<>();
        buildUnbalancedBST(bst1);
        SwingBinaryTreeDisplay.showInWindow(bst1);
    }
}

