package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private static int initialSize = 10;
    private static int self = -1;
    private static int nullIndex = -1;
    
    private int[] pointers;
    private IDictionary<T, Integer> indexSet;
    
    private int nextUniqueID;
    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        indexSet = new ChainedHashDictionary<T, Integer>();
        pointers = new int[initialSize];
        nextUniqueID = -1;
    }

    private int getUniqueID(T item) {
        if (indexSet.containsKey(item)) {
            return indexSet.get(item);
        } else {
            return nullIndex;
        }
    }
    
    private int applyUniqueID(T item) {
        nextUniqueID++;
        indexSet.put(item, nextUniqueID);
        return nextUniqueID;
    }
    
    @Override
    public void makeSet(T item) {
        int index = getUniqueID(item);
        if (index != -1) {
            throw new IllegalArgumentException();
        }
        index = applyUniqueID(item);
        if (index >= pointers.length) {
            expand();
        }
        pointers[index] = self;
    }

    private void expand() {
        int[] newPointers = new int[2 * pointers.length];
        for (int i = 0; i < pointers.length; i++) {
            newPointers[i] = pointers[i];
        }
        pointers = newPointers;
    }
    
    @Override
    public int findSet(T item) {
        int index = getUniqueID(item);
        if (index == -1) {
            throw new IllegalArgumentException();
        }
        return findSetUtil(index);
    }

    private int findSetUtil(int ptr) {
        if (pointers[ptr] <= self) {
            return ptr;
        } else {
            int root = findSetUtil(pointers[ptr]);
            pointers[ptr] = root;
            return root;
        }
    }

    @Override
    public void union(T item1, T item2) {
        int rootA = findSet(item1);
        int rootB = findSet(item2);
        int rankA = Math.abs(pointers[rootA]) + self;
        int rankB = Math.abs(pointers[rootB]) + self;
        if (rootA != rootB) {
            if (rankA == rankB) {
                unionUtil(rootA, rootB, rankA + 1);
            } else if (rankA < rankB) {
                unionUtil(rootB, rootA, rankB);
            } else {
                unionUtil(rootA, rootB, rankA);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void unionUtil(int root, int child, int newRank) {
        pointers[child] = root;
        pointers[root] = -newRank + self;
    }
}
