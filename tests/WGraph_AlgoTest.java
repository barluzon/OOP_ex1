package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

    @Test
    void copy() {

         weighted_graph g = new WGraph_DS();
         weighted_graph_algorithms gAlgo = new WGraph_Algo();
        gAlgo.init(g);

        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);

        g.connect(1,2,3.9);
        g.connect(1,3,4.5);
        g.connect(1,4,10.0);

        weighted_graph copy = gAlgo.copy();
        assertNotSame(g,copy);
        assertEquals(g,copy);
        assertEquals(g.edgeSize(),copy.edgeSize());
        g.removeEdge(1,2);
        assertNotEquals(g,copy);
        g.connect(1,2,3.0);
        assertNotEquals(g,copy);
        assertNotEquals(g.getMC(),copy.getMC());
        assertEquals(g.nodeSize(),copy.nodeSize());
        assertEquals(g.edgeSize(),copy.edgeSize());
        assertEquals(g.getV().size(),copy.getV().size());
        assertEquals(g.getV(1).size(),copy.getV(1).size());
        copy.removeEdge(1,2);
        copy.connect(1,2,3.0);
        assertEquals(g,copy);
        copy.connect(1,2,3.9);
        assertNotEquals(g,copy);

        gAlgo.init(copy);
        g = gAlgo.copy();
        assertEquals(g,copy);
        assertNotSame(copy,g);
        assertEquals(g.getNode(1),copy.getNode(1));
        assertEquals(g.getNode(2),copy.getNode(2));
        assertEquals(g.getNode(3),copy.getNode(3));
        assertEquals(g.getEdge(1,2),copy.getEdge(1,2));
        assertNotEquals(g.getEdge(1,2),copy.getEdge(1,4));

    }


    @Test
    void copiedSize () {
        weighted_graph g0 = WGraph_DSTest.graph_creator(100,450,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        weighted_graph copiedGraph = ag0.copy();
        assertEquals(g0.edgeSize(),copiedGraph.edgeSize());
        assertEquals(g0.nodeSize(),copiedGraph.nodeSize());
        assertEquals(g0.getMC(),copiedGraph.getMC());
        assertEquals(g0.getV().size(),copiedGraph.getV().size());

    }
    @Test
    void isConnected() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(0,0,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(1,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertFalse(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2,1,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(10,30,1);
        ag0.init(g0);
        boolean b = ag0.isConnected();
        assertTrue(b);
    }

    @Test
    void shortestPathDist() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());
        double d = ag0.shortestPathDist(0,10);
        assertEquals(d, 5.1);
    }

    @Test
    void shortestPath() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        List<node_info> sp = ag0.shortestPath(0,10);
        //double[] checkTag = {0.0, 1.0, 2.0, 3.1, 5.1};
        int[] checkKey = {0, 1, 5, 7, 10};
        int i = 0;
        for(node_info n: sp) {
            //assertEquals(n.getTag(), checkTag[i]);
            assertEquals(n.getKey(), checkKey[i]);
            i++;
        }
    }

    @Test
    void save_load() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(10,30,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        String str = "g0.obj";
        ag0.save(str);
        weighted_graph g1 = WGraph_DSTest.graph_creator(10,30,1);
        ag0.load(str);

        System.out.println("g0 node size:"+g0.nodeSize()+" g0 edge size:"+g0.edgeSize()+" g0 mc:"+g0.getMC());
        System.out.println("g1 node size:"+g1.nodeSize()+" g1 edge size:"+g1.edgeSize()+" g1 mc:"+g1.getMC());
        assertEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
    }

    private weighted_graph small_graph() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(11,0,1);
        g0.connect(0,1,1);
        g0.connect(0,2,2);
        g0.connect(0,3,3);

        g0.connect(1,4,17);
        g0.connect(1,5,1);
        g0.connect(2,4,1);
        g0.connect(3, 5,10);
        g0.connect(3,6,100);
        g0.connect(5,7,1.1);
        g0.connect(6,7,10);
        g0.connect(7,10,2);
        g0.connect(6,8,30);
        g0.connect(8,10,10);
        g0.connect(4,10,30);
        g0.connect(3,9,10);
        g0.connect(8,10,10);

        return g0;
    }

}
