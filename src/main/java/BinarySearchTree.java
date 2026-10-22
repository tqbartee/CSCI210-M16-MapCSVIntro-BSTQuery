import com.zybooks.dsaj.treebook.LinkedBinaryTree;
import com.zybooks.dsaj.treebook.Position;
import com.zybooks.dsaj.util.Entry;
import edu.desu.utilities.UtilityMethodsBinaryTreeBook;
import java.util.ArrayList;

public class BinarySearchTree<K, V> extends LinkedBinaryTree<Entry<K, V>> {

    /**
     * Default constructor that initializes a BinarySearchTree as a LinkedBinaryTree with no nodes.
     */
    public BinarySearchTree() {
        super();
    }

    /**
     * Supporting method
     * Compares two elements.
     *
     * @param a first element
     * @param b second element
     * @return a negative integer, zero, or a positive integer as a is less than, equal to, or greater than b
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected int compare(Object a, Object b) {
        if (a == b) return 0;
        if (a == null) return -1;
        if (b == null) return 1;

        Object keyA = (a instanceof Entry) ? ((Entry<?, ?>) a).getKey() : a;
        Object keyB = (b instanceof Entry) ? ((Entry<?, ?>) b).getKey() : b;

        if (keyA == keyB) return 0;
        if (keyA == null) return -1;
        if (keyB == null) return 1;

        if (keyA instanceof Comparable && keyB instanceof Comparable) {
            try {
                return ((Comparable) keyA).compareTo(keyB);
            } catch (ClassCastException e) {
                // Ignore and fall through to reflection / toString comparison
            }
        }
        try {
            java.lang.reflect.Method m = keyA.getClass().getMethod("compareTo", keyB.getClass());
            return (Integer) m.invoke(keyA, keyB);
        } catch (Exception e) {
            return keyA.toString().compareTo(keyB.toString());
        }
    }

    /**
     * Searches for a target object in the binary search tree.
     * Finds the associated node (position) if it exists, or the insertion point node if it does not.
     *
     * @param target the target object to get for
     * @return the Position containing the target element if found, or the Position of the last node visited, or null if empty
     */
    public Position<Entry<K, V>> get(Entry<K, V> target) {
        return get(root(), target);
    }

    /**
     * Recursively searches the subtree rooted at position p for the target object.
     *
     * @param p the root of the subtree to get
     * @param target the target object to get for
     * @return the Position containing target if found, or the last visited node Position
     */
    public Position<Entry<K, V>> get(Position<Entry<K, V>> p, Entry<K, V> target) {
        if (p == null || target == null) {
            return p;
        }
        int comp = compare(target, p.getElement());
        if (comp == 0) {
            return p;
        } else if (comp < 0) {
            Position<Entry<K, V>> leftChild = left(p);
            if (leftChild == null) {
                return p;
            }
            return get(leftChild, target);
        } else {
            Position<Entry<K, V>> rightChild = right(p);
            if (rightChild == null) {
                return p;
            }
            return get(rightChild, target);
        }
    }

    /**
     * Adds a node to the tree.
     * If the tree does not have a root node, adds the root.
     * If the tree has a root, adds the new node using standard binary tree get methodology.
     *
     * @param e the element to add
     * @return the Position of the added or updated node
     */
    public Position<Entry<K, V>> put(Entry<K, V> e) {
        if (isEmpty()) {
            return addRoot(e);
        }
        Position<Entry<K, V>> p = get(e);
        int comp = compare(e, p.getElement());
        if (comp < 0) {
            return addLeft(p, e);
        } else if (comp > 0) {
            return addRight(p, e);
        } else { // Default condition when comp == 0 - implied
            set(p, e);
            return p;
        }
    }

    /**
     * Removes the node storing element e from the binary search tree, if present.
     *
     * @param e the element to delete
     * @return the element that was removed, or null if not found
     */
    public Entry<K, V> delete(Entry<K, V> e) {
        if (isEmpty() || e == null) {
            return null;
        }
        Position<Entry<K, V>> p = get(e);
        if (p == null || compare(e, p.getElement()) != 0) {
            return null;
        }
        return delete(p);
    }

    /**
     * Removes the node at Position p from the binary search tree while preserving search tree properties.
     *
     * @param p the Position to remove
     * @return the element that was stored at Position p prior to deletion
     * @throws IllegalArgumentException if p is not a valid position
     */
    public Entry<K, V> delete(Position<Entry<K, V>> p) throws IllegalArgumentException {
        if (p == null) {
            return null;
        }
        if (numChildren(p) == 2) {
            Position<Entry<K, V>> successor = right(p);
            while (left(successor) != null) {
                successor = left(successor);
            }
            Entry<K, V> deletedElement = p.getElement();
            set(p, successor.getElement());
            remove(successor);
            return deletedElement;
        } else {
            return remove(p);
        }
    }

    /**
     * Adds an element to the tree by delegating to put.
     *
     * @param e element to add
     * @return Position of added element
     */
    public Position<Entry<K, V>> add(Entry<K, V> e) {
        return put(e);
    }

    /**
     * Rebalances the current binary search tree object using LeetCode 109 logic.
     */
    public void rebalanceTreeL109() {
        ArrayList<Entry<K, V>> sortedList = UtilityMethodsBinaryTreeBook.generateArrayListFromBinaryTree(this);
        ArrayList<Entry<K, V>> balancedArrayList = new ArrayList<>();
        buildBalancedArrayList(sortedList, 0, sortedList.size() - 1, balancedArrayList);

        BinarySearchTree<K, V> newTree = new BinarySearchTree<>();
        for (Entry<K, V> element : balancedArrayList) {
            newTree.add(element);
        }
        this.root = newTree.root;
    }

    private void buildBalancedArrayList(ArrayList<Entry<K, V>> list, int left, int right, ArrayList<Entry<K, V>> balancedList) {
        if (left > right) {
            return;
        }
        int mid = left + (right - left) / 2;
        balancedList.add(list.get(mid));
        buildBalancedArrayList(list, left, mid - 1, balancedList);
        buildBalancedArrayList(list, mid + 1, right, balancedList);
    }
}
