package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class SCCCalculator {
	
	List<Graph> result = new LinkedList<>();
	Stack<Integer> vertices = new Stack<>();
	Stack<Integer> finished = new Stack<>(); //ordered for the second run

	
	public List<Graph> getSCCList(HashMap<Integer, HashSet<Integer>> graph) {	
		dfs(graph, true); //boolean isFirstRun is true
		HashMap<Integer, HashSet<Integer>> reverted = revert(graph);
		dfs(reverted, false);//boolean isFirstRun is false
		
		return result;
	}
	
	private void dfs(HashMap<Integer, HashSet<Integer>> graph, boolean isFirstRun) {
		//create new Graphs for the result here if true
		//clear, then fill the secondRun Stack if false
		HashSet<Integer> visited = new HashSet<>();
		Graph tempGraph = null;
		result.clear();
		vertices.clear();

		if (isFirstRun) {
			vertices.addAll(graph.keySet());
		} else {
			vertices = finished;
		}
		System.out.println(vertices.toString());

		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			if (!visited.contains(v)) {
				if (!isFirstRun) {
					tempGraph = new CapGraph();
					tempGraph.addVertex(v);
					result.add(tempGraph);
					System.out.println( "Tree started: " + v);
				}
				dfsVisit(graph, v, visited, isFirstRun);
			} else {
				if (!isFirstRun) {
					tempGraph.addVertex(v);
					System.out.print(", " + v);
				}
			}
		}		
	}
	
	private void dfsVisit(HashMap<Integer, HashSet<Integer>> graph, 
							Integer v, HashSet<Integer> visited, boolean isFirstRun) {
		visited.add(v);
		for (int n : graph.get(v)) {
			if (!visited.contains(n)) {
				dfsVisit(graph, n, visited, isFirstRun);
			}
		}
		if (isFirstRun) {
			finished.add(v);
		}
	}
	
	public HashMap<Integer, HashSet<Integer>> revert(HashMap<Integer, HashSet<Integer>> graph) {
		HashMap<Integer, HashSet<Integer>> reverted = new HashMap<>();
		HashSet<Integer> present = new HashSet<>();
		
		for (int node : graph.keySet()) {
			if (!present.contains(node)) {
				present.add(node);
				reverted.put(node, new HashSet<Integer>());
			}
			for (int edge : graph.get(node)) {
				if (!present.contains(edge)) {
					present.add(edge);
					reverted.put(edge, new HashSet<Integer>());
				}
				reverted.get(edge).add(node);
			}
		}		
		return reverted;
	}
}
