/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CapGraph implements Graph {

	public HashMap<Integer, HashSet<Integer>> graph;
	
	public CapGraph() {
		this.graph = new HashMap<>();
	}
    
	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(Integer num) {
		graph.put(num, new HashSet<>());
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(Integer from, Integer to) {
		HashSet<Integer> e = graph.get(from);
		e.add(to);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		Graph egoGraph = new CapGraph();
		HashSet<Integer> present = new HashSet<>();
		
		egoGraph.addVertex(center);
		present.add(center);
		
		for (Integer i : graph.get(center)) {			
			egoGraph.addVertex(i);
			present.add(i);
			egoGraph.addEdge(center, i);			
		}
		
		for (int vertice : present) {
			if (vertice != center) {
				HashSet<Integer> edges = graph.get(vertice);
				for (Integer edge : edges) {
					if (present.contains(edge)) {
						egoGraph.addEdge(vertice, edge);
					}
				}
			}
		}	
		System.out.println("Egonet for node " + center + " is calculated");
		return egoGraph;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		return new SCCCalculator().getSCCList(graph);
	}	
	
	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		return graph;
	}
	
	//generates graph of biggest feeds (ids + sizes)
	@Override
	public Graph importFeedsGraph(HashMap<Integer, Integer> data) {
		Graph g = new CapGraph(){};
		HashSet<Integer> friends = new HashSet<>();
		//adds all top users and their friends as nodes, adds directed friendship edges (node -> friend)
		for (Integer i : data.keySet()) {
			g.addVertex(i);
			friends.add(i);
			for (Integer j : graph.get(i)) {
				g.addVertex(j);
				friends.add(j);
				g.addEdge(i, j);
			}
		}
		//adds all other edges in the subset, skips edges outside the subset
		for (Integer i : friends) {
			for (Integer j : graph.get(i)) {
				if (friends.contains(j)) {
					g.addEdge(i, j);
				}
			}
		}		
		return g;
	}
	
	//generates graph of top popular users (ids + sizes)
	@Override
	public Graph importPopularGraph(HashMap<Integer, Integer> data) {
		Graph g = new CapGraph(){};
		HashSet<Integer> friends = new HashSet<>();
		//adds all top users and their followers as nodes, adds directed friendship edges (follower -> node)
		for (Integer i : data.keySet()) {
			g.addVertex(i);
			friends.add(i);
			for (Integer j : graph.keySet()) {
				if (graph.get(j).contains(i)) {
					g.addVertex(j);
					friends.add(j);
					g.addEdge(j, i);
				}
			}
		}
		//adds all other edges in the subset, skips edges outside the subset
		for (Integer i : friends) {
			for (Integer j : graph.get(i)) {
				if (friends.contains(j)) {
					g.addEdge(i, j);
				}
			}
		}		
		return g;
	}
	
	//print graph
	@Override
	public String toString() {
		if (graph.isEmpty()) {
			return "Graph is empty";
		}
		
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<Integer, HashSet<Integer>>> it = graph.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<Integer, HashSet<Integer>> entry = (Map.Entry<Integer, HashSet<Integer>>) it.next();
			sb.append(entry.getKey() + ": " + entry.getValue().toString());
		}		
		return sb.toString();
	}

}
