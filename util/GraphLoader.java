/**
 * @author UCSD MOOC development team
 * 
 * Utility class to add vertices and edges to a graph
 *
 */
package util;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GraphLoader {
    /**
     * Loads graph with data from a file.
     * The file should consist of lines with 2 integers each, corresponding
     * to a "from" vertex and a "to" vertex.
     */ 
    public static void loadGraph(graph.Graph g, String filename, int subGraphSize) {
        Set<Integer> seen = new HashSet<Integer>();
        Set<Integer> printed = new HashSet<>();

        Scanner sc;
        try {
            sc = new Scanner(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // Iterate over the lines in the file, adding new
        // vertices as they are found and connecting them with edges.
        while (sc.hasNextInt()) {
            Integer v1 = Integer.valueOf(sc.nextInt());
            Integer v2 = Integer.valueOf(sc.nextInt());

            
            if (v1 <= subGraphSize && v2 <= subGraphSize) {
	            if (!seen.contains(v1)) {
	                g.addVertex(v1);
	                seen.add(v1);
	            }
	            if (!seen.contains(v2)) {
	                g.addVertex(v2);
	                seen.add(v2);
	            }
	            g.addEdge(v1, v2);
	            
	            //print progress
	            if(seen.size() % 100000 == 0 && !printed.contains(seen.size())) {
	            	System.out.println(seen.size() + " nodes are loaded.");
	            	printed.add(seen.size());
	            }
	            
	            //abort after size limit
	            if (seen.size() % subGraphSize == 0) { 
	            	System.out.println("");
	            	break;
	            }  
            }
        }              
        sc.close();
    }
    
    public static void loadGraph(graph.Graph g, String filename) {
    	loadGraph(g, filename, Integer.MAX_VALUE);
    }
}
