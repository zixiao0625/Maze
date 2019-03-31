package datastructures.sorting;

import misc.BaseTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout = 15 * SECOND)
    public void testArrayHeapInverseSorted() {
        int cap = 10000000;
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = cap; i >= 0; i--) {
            heap.insert(i);
        }
        heap.removeMin();
        for (int i = 0; i < cap; i++) {
            heap.peekMin();
            heap.removeMin();
        }
    }
    
    @Test(timeout = 15 * SECOND)
    public void testArrayHeapSorted() {
        int cap = 10000000;
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < cap; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < cap; i++) {
            heap.peekMin();
            heap.removeMin();
        }
    }
    
    @Test(timeout = 10 * SECOND)
    public void testArrayHeapRandom() {
        int cap = 10000000;
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < cap; i++) {
            if (i % 3 == 0) {
                heap.insert(i % 14);
            } else {
                heap.insert(i);
            }
        }
        for (int i = 0; i < cap; i++) {
            heap.peekMin();
            heap.removeMin();
        }
    }
    
    @Test (timeout = 10 * SECOND)
    public void testArrayHeapSame() {
        int cap = 10000000;
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < cap; i++) {
            heap.insert(666);
        }
        for (int i = 0; i < heap.size(); i++) {
            heap.peekMin();
            heap.removeMin();
        }
    }
    
    @Test (timeout = 15 * SECOND)
    public void testArrayHeapMisc() {
        String[] alphabet = {"a", "b", "c", "d", "e", "f", "g"};
        int cap = 100000;
        IPriorityQueue<String> heap = this.makeInstance();
        for (int i = 0; i< cap; i++) {
            heap.insert(alphabet[i % alphabet.length]);
        }
    }
    
    @Test (timeout = 5 * SECOND)
    public void testHeapSortBasic() {
        int cap = 10000000;
        List<Integer> notSorted = new ArrayList<>();
        
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < cap; i++) {
            notSorted.add(i / 2 % 5);
        }
        Collections.sort(notSorted);
        for (int i = 0; i < notSorted.size(); i++) {
            heap.insert(notSorted.get(i));
        }
        int[] tmp = new int[cap];
        for (int i = 0; i < cap; i++) {
            tmp[i] = heap.removeMin();
        }
        for (int i = 0; i < notSorted.size(); i++) {
            assertEquals(notSorted.get(i), tmp[i]);
        }
    }
    
}
