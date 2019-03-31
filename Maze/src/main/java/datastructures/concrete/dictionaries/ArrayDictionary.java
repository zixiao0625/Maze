package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;

    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        this.pairs = makeArrayOfPairs(10);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }
    
    @Override
    public V get(K key) {
        int index = keyIndex(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        }
        return this.pairs[index].value;
    }

    @Override
    public void put(K key, V value) {
        if (this.size >= this.pairs.length) {
            Pair<K, V>[] newPairs = this.makeArrayOfPairs(2 * this.size);
            for (int i = 0; i < this.size; i++) {
                newPairs[i] = this.pairs[i];
            }
            this.pairs = newPairs;
        }
        int index = keyIndex(key);
        if (index != -1) {
            this.pairs[index].value = value;
        } else {
            this.pairs[size] =  new Pair<K, V>(key, value);
            this.size++;
        }   
    }

    @Override
    public V remove(K key) {
        V remove = null;
        int index = keyIndex(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        }
        remove = this.pairs[index].value;
        this.pairs[index] = null;
        this.pairs[index] = this.pairs[this.size - 1];
        this.pairs[this.size - 1] = null;
        this.size--;
        return remove;
    }

    @Override
    public boolean containsKey(K key) {
        return keyIndex(key) != -1;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<K, V>(pairs, this.size());
    }

    private int keyIndex(K key) {
        for (int i = 0; i < this.size; i++) {
            if (equalKey(i, key)) {
                return i;
            }
        }
        return -1;
    }
    
    private boolean equalKey(int i, K key) {
        return java.util.Objects.equals(this.pairs[i].key, key);
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private int nextIndex;
        private Pair<K, V>[] pairs;
        private int size;
        
        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            this.size = size;
            this.nextIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Pair<K, V> current = pairs[nextIndex];
            nextIndex++;
            return new KVPair<K, V>(current.key, current.value);
        }
    }
}

