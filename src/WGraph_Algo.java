package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, java.io.Serializable {

    private weighted_graph g;
    private Queue<node_info> que;                           //isConnected Method
    private HashSet<node_info> checkNodes;                  //isConnected
    private Map<node_info, Boolean> vis;                    //visited
    private Map<node_info, node_info> prev;                 //parents
    private PriorityQueue<node_info> pQue;                  //for shortest path as list


    public WGraph_Algo() {
        this.g = new WGraph_DS();
        this.prev = new HashMap<node_info, node_info>();
        this.vis = new HashMap<node_info, Boolean>();

    }
    //compare distance
    public static class CompareDis implements Comparator<node_info> {

        @Override
        public int compare(node_info node1, node_info node2) {
            return Double.compare(node1.getTag(), node2.getTag());
        }
    }
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }
    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return this.g;
    }
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public weighted_graph copy() {
        weighted_graph copyFrom = this.g;
        weighted_graph copyTo = new WGraph_DS();
        HashSet<node_info> ver = new HashSet<>(g.getV());       //all vertices as set

        while (!ver.isEmpty()) {
            if (ver.iterator().hasNext()) {
                node_info node = ver.iterator().next();
                LinkedList<node_info> que = new LinkedList<>();
                que.add(node);
                while (!que.isEmpty()) {
                    node = que.poll();
                    copyTo.addNode(node.getKey());

                    for (node_info curNei : copyFrom.getV(node.getKey())) {     //iterate on node neighbors
                        double edgeWeight = copyFrom.getEdge(node.getKey(), curNei.getKey());   //edge weight
                        if (ver.contains(curNei)) {
                            copyTo.addNode(curNei.getKey());                    //add node to copied graph
                            que.add(curNei);
                            copyTo.connect(node.getKey(), curNei.getKey(), edgeWeight); // add edge(mark as neighbor)
                            ver.remove(curNei);
                        } else if (!(copyTo.hasEdge(node.getKey(), curNei.getKey()))) {

                            copyTo.connect(node.getKey(), curNei.getKey(), edgeWeight);

                        }
                    }
                    ver.remove(node);
                }
            }
        }
            ((WGraph_DS) copyTo).setMC(copyFrom.getMC());       //set tha same mc
            return copyTo;
        }


    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. NOTE: assume ubdirectional graph.
     * @return
     */
    @Override
    public boolean isConnected() {
        if (g.nodeSize() == 0 || g.nodeSize() == 1)
            return true;

        node_info start = g.getV().stream().iterator().next();
        this.que = new LinkedList<node_info>();
        this.checkNodes = new HashSet<node_info>();

        que.add(start);
        while (!que.isEmpty()) {
            node_info at = que.poll();
            checkNodes.add(at);

            for (node_info cur : g.getV(at.getKey())) {

                if (cur.getTag() == 0) {
                    cur.setTag(1);
                    que.add(cur);
                }
            }
        }
        for (node_info cur : g.getV())
            cur.setTag(0);
        if (checkNodes.size() == g.nodeSize())
            return true;
        return false;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        node_info start = g.getNode(src);                    //int to node
        node_info finish = g.getNode(dest);                    //int to node
        this.prev = new HashMap<node_info, node_info>();    //parents
        this.pQue = new PriorityQueue<>(g.nodeSize(), new CompareDis());                    //priority queue by distance
        Map<node_info, Boolean> vis1 = new HashMap<node_info, Boolean>();                   //visited

        if (src == dest)
            return 0.0;

        if (start == null || finish == null)
            return -1;


        if (isConnected()) {

            for (node_info set : g.getV())
                set.setTag(-1.0);                   //set each node distance "unreachable"

            pQue.add(start);
            vis1.put(start, true);
            start.setTag(0.0);
            while (!pQue.isEmpty()) {
                node_info current = pQue.poll();

                for (node_info node : g.getV(current.getKey())) {        //current node neighbors checking and update
                    if (!vis1.containsKey(node)) {
                        if (node.getTag() == -1.0)                       //if "unreachable" tag it reach but max distance
                            node.setTag(Double.MAX_VALUE);

                        double newDis = current.getTag() + g.getEdge(current.getKey(), node.getKey()); //the new distance
                        if (newDis < node.getTag()) {                    //choose the shortest
                            node.setTag(newDis);
                            prev.put(node, current);
                            pQue.add(node);
                        }
                    }
                }
                vis1.put(current, true);
            }
            return g.getNode(dest).getTag();
        }
        return -1;                                                //if graph is not connected
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        node_info source = g.getNode(src);                                          //int to node
        node_info finish = g.getNode(dest);                                         //int to node
        this.prev = new HashMap<node_info, node_info>();                            //parents
        this.pQue = new PriorityQueue<>(g.nodeSize(), new CompareDis());            //queue
        Map<node_info, Boolean> vis = new HashMap<node_info, Boolean>();
        LinkedList<node_info> directions = new LinkedList<node_info>();

        if (src == dest) {
            directions.add(source);
            return directions;
        }

        if (source == null || finish == null)
            return null;

        if (isConnected()) {

            for (node_info set : g.getV())
                set.setTag(-1.0);                                       //set each node distance "unreachable"

            pQue.add(source);
            vis.put(source, true);
            source.setTag(0.0);
            node_info current = source;
            while (!pQue.isEmpty()) {
                current = pQue.poll();
//                if (current.equals(finish))
//                    break;
                if (current.getKey()==finish.getKey())
                    break;
                for (node_info curNode : g.getV(current.getKey())) {        //current node neighbors checking and update
                    if (!vis.containsKey(curNode)) {
                        if (curNode.getTag() == -1.0)                       //if "unreachable" tag it reach but max distance
                            curNode.setTag(Double.MAX_VALUE);

                        double newDis = current.getTag() + g.getEdge(current.getKey(), curNode.getKey());   //the new distance
                        if (newDis < curNode.getTag()) {                    //choose the shortest
                            curNode.setTag(newDis);
                            prev.put(curNode, current);
                            pQue.add(curNode);
                        }
                    }
                }
                vis.put(current, true);
            }

            if (!current.equals(finish)) {
                return null;
            }
            for (node_info node = finish; node != null; node = prev.get(node)) {    //adding nodes directions in reverse order
                directions.add(node);
            }
            Collections.reverse(directions);
            return directions;

        }
        return null;                                                //if graph is not connected
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param filename - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String filename) {
        System.out.println("starting Serialize to "+filename+"\n");
        try {
            //Saving object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method serialization of object
            out.writeObject(g);
            out.close();
            file.close();

        } catch (IOException ex) {
            System.out.println("IOException is caught");
            return false;
        }
        System.out.println("end of Serialize \n\n");
        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param filename - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String filename) {
        System.out.println("deserialize from "+filename+"\n");
        try {
            FileInputStream fInput = new FileInputStream(filename);
            ObjectInputStream oInput = new ObjectInputStream(fInput);
            fInput.close();
            oInput.close();

        }
        catch (IOException ex) {
            System.out.println("IOException!");
            return  false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        WGraph_Algo that = (WGraph_Algo) o;

        if (g.equals(that.g) && prev.equals(that.prev))
            return true;

        return false;
    }

}

