package misc.graphs;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    
    private class ListNode {
        public int index;
        public V vertex;
        public E edge;
        public ListNode(int index, V v, E e) {
            this.index = index;
            this.vertex = v;
            this.edge = e;
        }
    }
    
    private IList<V> rawVertices;
    private IList<E> rawEdges;
    
    private IDictionary<V, Integer> mapper;
    //private DoubleLinkedList<V> adjList[];
    private IDictionary<Integer, DoubleLinkedList<ListNode>> adjList;

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        int curID = 0;
        rawVertices = vertices;
        rawEdges = edges;
        mapper = new ChainedHashDictionary<>();
        adjList = new ChainedHashDictionary<>();
        for (int i = 0; i < rawVertices.size(); i++) {
            mapper.put(rawVertices.get(i), curID);
            adjList.put(i, new DoubleLinkedList<>());
            curID++;
        }
        for (int j = 0; j < rawEdges.size(); j++) {
            V vertexOne = rawEdges.get(j).getVertex1();
            V vertexTwo = rawEdges.get(j).getVertex2();
            double weight = rawEdges.get(j).getWeight();
            int indexOne;
            int indexTwo;
            if (!mapper.containsKey(vertexOne) || !mapper.containsKey(vertexTwo) || weight < 0) {
                throw new IllegalArgumentException();
            }
            indexOne = mapper.get(vertexOne);
            adjList.get(indexOne).add(new ListNode(mapper.get(vertexTwo), vertexTwo, rawEdges.get(j)));
            indexTwo = mapper.get(vertexTwo);
            adjList.get(indexTwo).add(new ListNode(mapper.get(vertexOne), vertexOne, rawEdges.get(j)));
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return this.rawVertices.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return this.rawEdges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        int convertedEdges = 0;
        ArrayDisjointSet<V> forest = new ArrayDisjointSet<>();
        ISet<E> result = new ChainedHashSet<>();
        IPriorityQueue<E> edgeHeap = new ArrayHeap<>();
        for (V vertex : rawVertices) {
            forest.makeSet(vertex);
        }
        for (E edge : rawEdges) {
            edgeHeap.insert(edge);
        }
        while (!edgeHeap.isEmpty()) {
            E edge = edgeHeap.removeMin();
            if (forest.findSet(edge.getVertex1()) != forest.findSet(edge.getVertex2())) {
                forest.union(edge.getVertex1(), edge.getVertex2());
                result.add(edge);
                convertedEdges++;
                if (convertedEdges >= rawVertices.size() - 1) {
                    break;
                }
            }
        }
        return result;
    }
    
    private class PathNode {
        public double distance;
        public V predecessor;
        public PathNode(double distance) {
            this.distance = distance;
            this.predecessor = null;
        }
    }
    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        IList<V> unprocessed = new DoubleLinkedList<>();
        IList<V> processed = new DoubleLinkedList<>();
        IDictionary<Integer, PathNode> pathy = new ChainedHashDictionary<>();
        for (V vertex : rawVertices) {
            int index = mapper.get(vertex);
            if (vertex.equals(start)) {
                pathy.put(index, new PathNode(0.0));
            } else {
                pathy.put(index, new PathNode(Double.POSITIVE_INFINITY));
            }
        }
        unprocessed.add(start);
        while (!unprocessed.isEmpty()) {
            V smallV = removeLowestDistanceVertex(unprocessed, pathy);
            int index = mapper.get(smallV);
            PathNode u = pathy.get(index);
            for (ListNode e : adjList.get(index)) {
                processed.add(smallV);
                if (!processed.contains(e.vertex)) {
                    unprocessed.add(e.vertex);
                    PathNode v = pathy.get(e.index);
                    E edgeLeaving = e.edge;
                    double tentativity = u.distance + edgeLeaving.getWeight();
                    if (tentativity < v.distance) {
                        v.distance = tentativity;
                        v.predecessor = smallV;
                    }
                }
            }
        }
        if (pathy.get(mapper.get(end)).distance == Double.POSITIVE_INFINITY) {
            throw new NoPathExistsException();
        }
        return generateTrace(start, end, pathy);
    }
    
    
    private IList<E> generateTrace(V s, V e, IDictionary<Integer, PathNode> list) {
        IList<E> path = new DoubleLinkedList<>();
        IList<E> dup = new DoubleLinkedList<>();
        int pid = mapper.get(e);
        while (list.get(pid).predecessor != null) {
            int master = mapper.get(list.get(pid).predecessor);
            IList<ListNode> pList = adjList.get(master);
            for (int i = 0; i < pList.size(); i++) {
                if (pList.get(i).index == pid) {
                    //path.insert(0, pList.get(i).edge); For some reason, this will cause error
                    path.add(pList.get(i).edge);
                    break;
                }
            }
            pid = master;
        }
        
        for (int i = 0; i < path.size(); i++) {
            dup.add(path.get(path.size() - i - 1));
        }
        return dup;
    }
    
    private V removeLowestDistanceVertex(IList<V> list, IDictionary<Integer, PathNode> pathObj) {
        double minDistance = Double.POSITIVE_INFINITY;
        int removeMe = -1;
        for (int i = 0; i < list.size(); i++) {
            int index = mapper.get(list.get(i));
            double d = pathObj.get(index).distance;
            if (d < minDistance) {
                minDistance = d;
                removeMe = i;
            }
        }
        return list.delete(removeMe);
    }
}
