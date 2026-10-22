package com.zybooks.dsaj.priorityqueuebook;

import com.zybooks.dsaj.listsiterators.Entry;

/** Interface for the priority queue ADT. */
public interface PriorityQueue<K,V> {
    int size();
    boolean isEmpty();
    Entry<K,V> insert(K key, V value);
    Entry<K,V> min();
    Entry<K,V> removeMin();
}