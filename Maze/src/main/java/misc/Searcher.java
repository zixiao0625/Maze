package misc;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

public class Searcher {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "biggest".
     *
     * If the input list contains fewer then 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        if (k < 0) {
            throw new IllegalArgumentException();
        }
        IList<T> result = new DoubleLinkedList<>();
        IPriorityQueue<T> heap = new ArrayHeap<>();
        if (k > 0 && input != null) {
            for (T data : input) {
                if (heap.size() < k) {
                    heap.insert(data);
                } else {
                    if (data.compareTo(heap.peekMin()) > 0) {
                        heap.removeMin();
                        heap.insert(data);
                    }
                }
            }
            int oHeapSize = heap.size();
            for (int i = 0; i < oHeapSize; i++) {
                result.add(heap.removeMin());
            }
        }
        return result;
    }
}
