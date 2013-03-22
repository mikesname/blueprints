package com.tinkerpop.blueprints.impls.neo4j;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TestSuite;
import com.tinkerpop.blueprints.TransactionalGraphTestSuite;
import com.tinkerpop.blueprints.impls.GraphTest;
import org.junit.Test;
import org.neo4j.kernel.InternalAbstractGraphDatabase;
import org.neo4j.kernel.ha.HighlyAvailableGraphDatabase;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class Neo4jHaGraphTest extends GraphTest {

    public void testHaGraph() throws Exception {
        assertTrue(InternalAbstractGraphDatabase.class.isAssignableFrom(HighlyAvailableGraphDatabase.class));
        Graph graph = generateGraph();
        // TODO: Tests
        graph.shutdown();
    }

    @Override
    public Graph generateGraph() {
        return generateGraph("graph");
    }

    @Override
    public Graph generateGraph(final String graphDirectoryName) {
        String directory = getWorkingDirectory();
        Neo4jHaGraph graph = new Neo4jHaGraph(directory + "/" + graphDirectoryName);
        graph.setCheckElementsInTransaction(true);
        return graph;
    }

    @Override
    public void doTestSuite(final TestSuite testSuite) throws Exception {
        String directory = this.getWorkingDirectory();
        deleteDirectory(new File(directory));
        for (Method method : testSuite.getClass().getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                System.out.println("Testing " + method.getName() + "...");
                method.invoke(testSuite);
                deleteDirectory(new File(directory));
            }
        }
    }

    private String getWorkingDirectory() {
        return this.computeTestDataRoot().getAbsolutePath();
    }
}
