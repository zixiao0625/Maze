package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private int size;
    public ChainedHashDictionary() {
        this.size = 0;
        this.chains = makeArrayOfChains(10);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int sizes) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[sizes];
    }

    @Override
    public V get(K key) {
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        
        return chains[hash(key)].get(key);  
    }

    @Override
    public void put(K key, V value) {
        if ((size+0.0)/chains.length > 3) {
            this.expand();
        }
        if (!this.containsKey(key)) {
           size++;
        }
        int index = hash(key);
        if (chains[index] == null) {
            chains[index] = new ArrayDictionary<K, V>();
        }
        chains[index].put(key, value);
    }

    @Override
    public V remove(K key) {
        int index = hash(key);
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        size--;
        return chains[index].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        return chains[hash(key)] != null && chains[hash(key)].containsKey(key);
    }

    @Override
    public int size() {
        return size;
    }
    private void expand() {
        IDictionary<K, V>[] originalChain = this.chains;
        this.chains = this.makeArrayOfChains(originalChain.length * 4);
        this.size = 0;
        for (int i = 0; i < originalChain.length; i++) {
            if (originalChain[i] != null) {
                for (KVPair<K, V> pair : originalChain[i]) {
                    this.put(pair.getKey(), pair.getValue());
                }
            }
        }
    }
    
    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % this.chains.length);
    }
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }
    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        int currentIndex;
        Iterator<KVPair<K, V>> iterator;
    
        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.currentIndex = 0;
            if (this.chains[currentIndex] != null) {
                iterator = this.chains[currentIndex].iterator();
            }
        } 

        @Override
        public boolean hasNext() {
            if (iterator != null && iterator.hasNext()) {
                return true;
            }
            if (currentIndex >= chains.length-1) {
                return false;
            }
            currentIndex++;
            if (chains[currentIndex] != null){
                this.iterator = chains[currentIndex].iterator();
            } else {
                this.iterator = null;
            }
            return this.hasNext();
        }

        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                return this.iterator.next();
            }
        }
    }
}
   
