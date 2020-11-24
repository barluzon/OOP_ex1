package ex1.src;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class WGraph_DS implements weighted_graph {


    static class NodeInfo implements node_info {

        static int key = 0;
        private int id;
        private String info;
        private double tag = 0;

        public NodeInfo() {
            this.tag = 0;
            this.info = " ";
            this.id = key + 1;
        }

        public NodeInfo(int key) {
            this.tag = 0;
            this.id = key;
            this.info = " ";
        }

        /**
         * Return the key (id) associated with this node.
         * Note: each node_data should have a unique key.
         * @return
         */
        @Override
        public int getKey() {
            return this.id;
        }

        /**
         * return the remark (meta data) associated with this node.
         * @return
         */
        @Override
        public String getInfo() {
            return this.info;
        }
        /**
         * Allows changing the remark (meta data) associated with this node.
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }
        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         * @return
         */
        @Override
        public double getTag() {
            return this.tag;
        }
        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        @Override
        //override equals node info
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NodeInfo that = (NodeInfo) o;
            return id == that.getKey();
        }
    }

    private HashMap<Integer, node_info> vertices;
    public HashMap<Integer, HashMap<node_info, Double>> neighbors;
    private int edgeSize = 0;
    private int mc = 0;

    public WGraph_DS() {
        this.vertices = new HashMap<Integer, node_info>();
        this.neighbors = new HashMap<Integer, HashMap<node_info, Double>>();
    }
    /**
     * return the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        return this.vertices.get(key);
    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        node_info n1 = getNode(node1);
        node_info n2 = getNode(node2);
        return neighbors.get(node1).containsKey(n2) && neighbors.get(node2).containsKey(n1);

    }
    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {
        node_info n2 = getNode(node2);
        if (this.hasEdge(node1, node2)) {
            return neighbors.get(node1).get(n2);
        }
        return -1;
    }

    /**
     * add a new node to the graph with the given key.
     * Note: this method should run in O(1) time.
     * Note2: if there is already a node with such a key -> no action should be performed.
     * @param key
     */
    @Override
    public void addNode(int key) {
        if(!this.vertices.containsKey(key)) {
            this.vertices.put(key, new NodeInfo(key));
            this.neighbors.put(key, new HashMap<node_info, Double>());
            NodeInfo.key++;
            this.mc++;
        }
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (!this.vertices.containsKey(node1) || !this.vertices.containsKey(node2) || node1 == node2)
            return;
        if (w >= 0) {
            node_info n1 = getNode(node1);
            node_info n2 = getNode(node2);
            if (!this.hasEdge(node1, node2)) {
                this.neighbors.get(node1).put(n2, w);
                this.neighbors.get(node2).put(n1, w);
                this.edgeSize++;
            }
            if (this.getEdge(node1, node2) != w) {
                this.neighbors.get(node1).put(n2, w);
                this.neighbors.get(node2).put(n1, w);
                this.mc++;
            }
        }
    }

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method should run in O(1) tim
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return this.vertices.values();
    }
    /**
     *
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * Note: this method can run in O(k) time, k - being the degree of node_id.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if (this.neighbors.containsKey(node_id)) {
            return new HashSet<>(this.neighbors.get(node_id).keySet());
        }
        return new HashSet<>();
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     * @param key
     */
    @Override
    public node_info removeNode(int key) {
        if (this.vertices.containsKey(key)) {
            for (node_info cur : this.vertices.values()) {
                this.removeEdge(key, cur.getKey());
            }
            this.mc++;
            return this.vertices.remove(key);
        }
        return null;
    }

    /**
     * Delete the edge from the graph,
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        node_info n1 = getNode(node1);
        node_info n2 = getNode(node2);
        if (this.hasEdge(node1, node2)) {
            this.neighbors.get(node1).remove(n2);
            this.neighbors.get(node2).remove(n1);

            this.edgeSize--;
            this.mc++;
        }
    }
    /** return the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int nodeSize() {
        return this.vertices.size();
    }
    /**
     * return the number of edges (undirectional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int edgeSize() {
        return this.edgeSize;
    }
    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     * @return
     */
    @Override
    public int getMC() {
        return this.mc;
    }

    /**
     * setting the Mode Count - for the deep copy function
     * @param mc
     */
    public void setMC(int mc) {
        this.mc = mc;
    }

    //helping Override equals
    public boolean neighborsEquals(HashMap<node_info, Double> neighbors1 , HashMap<node_info, Double> neighbors2, WGraph_DS ds){
        for(node_info cur : neighbors1.keySet()){
            if(!neighbors2.containsKey(ds.getNode(cur.getKey()))){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WGraph_DS that = (WGraph_DS) o;

        if (mc != that.mc)
            return false;

        if (edgeSize != that.edgeSize)
            return false;

        if (!vertices.equals(that.vertices))
            return false;

        if(!(this.neighbors.size() == that.neighbors.size())){

            return false;
        }
        for (node_info cur : vertices.values()) {
            HashMap<node_info, Double> n1 = neighbors.get(cur.getKey());
            if(!that.neighbors.containsKey(cur.getKey()))
                return false;

            HashMap<node_info, Double> n2 = that.neighbors.get(cur.getKey());
            if(!neighborsEquals(n1,n2,that))
                return false;

        }
        return true;
    }
}