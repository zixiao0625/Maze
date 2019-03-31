package datastructures.concrete;

import datastructures.interfaces.IList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import misc.exceptions.EmptyContainerException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
	private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (front == null) {
            addFront(item);
        } else {
            addBack(item);
        }
        size++;
    }

    /*
     * Insert the given item in front of the list
     */
    private void addFront(T item) {
        //System.out.println("ADD Front " + item);
        Node<T> nNode = new Node<T>(null, item, front);
        front = nNode;
        if (back == null) {
            back = front;
        }
    }
    
    /*
     * Insert the given item at end of the list
     */
    private void addBack(T item) {
        //System.out.println("ADD BACK " + item);
        Node<T> nNode = new Node<T>(back, item, null);
        back.next = nNode;
        back = nNode;
    }
    
    @Override
    public T remove() {
        if (back != null) {
            Node<T> fetchNode;
            fetchNode = back;
            if (back.prev != null) {
                back.prev.next = null;
                back = back.prev;
            } else {
                front = null;
                back = front;
            }
            size--;
            return fetchNode.data;
        }
        throw new EmptyContainerException();
    }

    @Override
    public T get(int index) {
    	if (index < 0 || index >= size) {
    	    throw new IndexOutOfBoundsException();
    	}
    	return getNode(index).data;
    }

    /**
     * Return the specific node object at the given index
     * 
     * @throws IndexOutOfBoundsException if the given index
     * is less than 0 or greater than or equal to list size
     */
    private Node<T> getNode(int index) {
    	if (index < 0 || index >= size) {
    	    throw new IndexOutOfBoundsException();
    	}
    	int c = size - 1;
    	if (index == 0) {
    	    return front;
    	} else if (index == c) {
    	    return back;
    	}
    	Node<T> cur = front;
    	int magicNum = c - index; //Distance to back of list
    	if (magicNum < index) {
    	    cur = back;
    	    while (magicNum > 0) {
    	        cur = cur.prev;
    	        magicNum--;
    	    }
    	} else {
    	    while (index > 0) {
    	        cur = cur.next;
    	        index--;
    	    }
    	}
    	return cur;
    }
    
    @Override
    public void set(int index, T item) {
    	if (index < 0 || index >= size) {
    	    throw new IndexOutOfBoundsException();
    	}
    	int c = size - 1;
    	if (index == 0) {
    	    front = new Node<T>(null, item, front.next);
    	} else if (index == c) {
    	    Node<T> bNode = new Node<T>(back.prev, item, null); 
    	    back.prev.next = bNode;
    	    back = bNode;
    	} else {
    	    if (index > c - index) {
    	        Node<T> node = getNode(index + 1);
    		    Node<T> nNode = new Node<T>(node.prev.prev, item, node);
    		    node.prev.prev.next = nNode;
    		    node.prev = nNode;
    		} else {
    		    Node<T> node = getNode(index - 1);
    		    Node<T> nNode = new Node<T>(node, item, node.next.next);
    		    node.next = nNode;
    		    node.next.next.prev = nNode; 
    		}
    	}
    }
    
    @Override
    public void insert(int index, T item) {
    	if (index < 0 || index > size) {
    		throw new IndexOutOfBoundsException();
    	}
    	if (index == 0) {
    		addFront(item);
    	} else if (index == size) {
    		addBack(item);
    	} else {
    	    int c = size - 1;
    	    int distance = (c - index);
    	    Node<T> node;
    	    Node<T> nNode;
    	    if (distance == 0) {
    	        node = getNode(c);
    	        nNode = new Node<T>(node.prev, item, node);
    	        node.prev.next = nNode;
    	        node.prev = nNode;
    	    } else if (index > distance) {
    	        node = getNode(index + 1);
    	        nNode = new Node<T>(node.prev.prev, item, node.prev);
    	        node.prev.prev.next = nNode;
    	        node.prev.prev = nNode;
    	    } else {
    	        node = getNode(index - 1);
    	        nNode = new Node<T>(node, item, node.next);
    	        node.next.prev = nNode;
    	        node.next = nNode;
    	    }
    	}
    	size++;
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T data = null;
        int c = size - 1;
        if (index == 0) {
            data = front.data;
            front = front.next;
            if (front != null) {
                front.prev = null;
            }
        } else if (index == c) {
            data = back.data;
            back = back.prev;
            if (back != null) {
                back.next = null;
            }
        } else if (index > c - index) {
            Node<T> nxNode = getNode(index + 1);
            data = nxNode.prev.data;
            nxNode.prev.prev.next = nxNode;
            nxNode.prev = nxNode.prev.prev;
        } else {
            Node<T> fsNode = getNode(index - 1);
            data = fsNode.next.data;
            fsNode.next.next.prev = fsNode;
            fsNode.next = fsNode.next.next;
        }
        size--;
        if (size == 0) {
            front = null;
            back = front;
        }
        return data;
    }
    
    @Override
    public int indexOf(T item) {
        Node<T> cur = front;
        int index = 0;
        while (cur != null) {
            if (item == cur.data || (item != null && item.equals(cur.data))) {
                return index;
            }
            /*if (item == null) {
                System.out.println("This is null");
                if (cur.data == null) {
                    return index;
                }
                System.out.println("Cur data is not null");
                System.out.println(cur.data);
            } else {
                System.out.println("Item is not null");
                System.out.println("fuck: " + item);
            }*/
            index++;
            cur = cur.next;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        return indexOf(other) > -1;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }
}
