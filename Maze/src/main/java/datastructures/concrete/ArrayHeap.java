package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private static final int CAPACITY = 10;
    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int size;
    // Feel free to add more fields and constants.

    public ArrayHeap() {
        size = -1;
        heap = this.makeArrayOfT(CAPACITY);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int sizex) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[sizex]);
    }

    @Override
    public T removeMin() {
        if (size == -1) {
            throw new EmptyContainerException();
        }
        T tmp = heap[0];
        size--;
        if (size > -1) {
            heap[0] = heap[size + 1];
            percolateDown(0);
        }
        return tmp;
    }

    @Override
    public T peekMin() {
        if (size == -1) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        size++;
        heap[size] = item;
        if (size > 0) {
            percolateUp(size);
            if (size == heap.length - 1) {
                expand();
            }
        }
    }

    private void expand() {
        T[] nHeap = this.makeArrayOfT(2 * heap.length);
        for (int i = 0; i < heap.length; i++) {
            nHeap[i] = heap[i];
        }
        heap = nHeap;
    }

    private void percolateUp(int startAt) {
        int root = (startAt - 1) / NUM_CHILDREN;
        if (heap[startAt].compareTo(heap[root]) < 0) {
            swap(startAt, root);
            percolateUp(root);
        }
    }

    private void percolateDown(int rootIndex) {
        int cIndex = findSmallestChild(rootIndex);
        if (size == 0 || cIndex == rootIndex) {
            return;
        }
        swap(rootIndex, cIndex);
        percolateDown(cIndex);
    }
    
    private int findSmallestChild(int rootIndex) {
        int smallIndex = rootIndex;
        for (int i = 1; i <= NUM_CHILDREN; i++) {
            int tmp = (NUM_CHILDREN * rootIndex) + i;
            if (tmp <= size && tmp > -1) {
                 if (heap[tmp].compareTo(heap[smallIndex]) < 0) {
                     smallIndex = tmp;
                 }
            }
        }
        return smallIndex;
    }

    private void swap(int from, int to) {
        if (from > size || to > size) {
            throw new IllegalArgumentException();
        }
        T temp = heap[from];
        heap[from] = heap[to];
        heap[to] = temp;
    }
    
    @Override
    public int size() {
        return size + 1;
    }
}
