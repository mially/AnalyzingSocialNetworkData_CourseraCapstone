package graph;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

//Main class with static methods to calculate graph parameters
public class GraphReportGenerator {
	
	//generates Graph Report object and stores all parameters for further use
	public static GraphReport generateReport(Graph graph) {
		//convert graph data to HashMap 
		HashMap<Integer, HashSet<Integer>> data = graph.exportGraph();	
		//create and fill the graph report
		GraphReport report = new GraphReport();		
		report.setNoOfNodes(countNodes(data));
		report.setNoOfEdges(countEdges(data));
		report.setSparse(checkIfSparse(data));
		report.setAverageFriendsNo(averageFriendsNo(data));
		report.setMutualFriends(mutualFriends(data));
		report.setTopFriends(topFeedSize(data)); 
		report.setTopFriendOf(topFriendOf(data));
		report.setFeedSizeDistribution(DistributionsCounter.feedSizeDistribution(data));
		report.setPopularityDistribution(DistributionsCounter.popularityDistribution(data));
		report.setACC(calculateACC(data));
		return report;
	}
	
	public static void calculateDiameter(Graph graph, GraphReport report) {
		report.setDiameter(calculateDiameter(graph.exportGraph()));
	}
	
    private static int countNodes(HashMap<Integer, HashSet<Integer>> data) {
    	return data.keySet().size();
    }
    
    private static int countEdges(HashMap<Integer, HashSet<Integer>> data) {
    	int result = 0;
    	for (HashSet<Integer> s : data.values()) {
    		result += s.size();
    	}
    	return result;
    }
    
    private static boolean checkIfSparse(HashMap<Integer, HashSet<Integer>> data) {
    	//graph is sparse if edges count (E) is closer to nodes count (N) than to N(N-1) 
    	//(max possible in digraph)
    	int E = countEdges(data);
    	int N = countNodes(data);
    	int maxE = (N*(N - 1));
    	if (maxE - E > E - N) return true;   	
    	return false;
    }
    
    private static double averageFriendsNo(HashMap<Integer, HashSet<Integer>> data) {
    	return countEdges(data)/ (double) countNodes(data);
    }
    
    //calculates mutual friendships % in the network
    private static double mutualFriends(HashMap<Integer, HashSet<Integer>> data) {
    	int counterMutual = 0;   
    	int counterNotMutual = 0;
    	//increment counter if node for set of friends contains main node in friends => mutual friends
    	for (Map.Entry<Integer, HashSet<Integer>> entry : data.entrySet()) {
    		int mainNode = entry.getKey();
    		for (Integer i : entry.getValue()) {
    			if (data.get(i).contains(mainNode)) {
    				counterMutual++;    				
    			} else {
    				counterNotMutual++;
    			}
    		}
    	}    	
    	int mutualFriendships = counterMutual/2; //convert 2 directed edges between nodes to 1 mutual friendship
    	int allFriendships = mutualFriendships + counterNotMutual;   	
    	return mutualFriendships / (double) allFriendships * 100; //how mutual are friendships in the network, %
    }
    
    //calculates and return top N users based on their Feed size
    private static HashMap<Integer, Integer> topFeedSize (HashMap<Integer, HashSet<Integer>> data) {    	
    	HashMap<Integer, Integer> fullSizes = new HashMap<>();    	
    	for (Map.Entry<Integer, HashSet<Integer>> entry : data.entrySet()) {
    		fullSizes.put(entry.getKey(), entry.getValue().size());
    	}    	
    	return heapifyAndReturnTop(fullSizes, 500);
    }

    //calculates and return top N users based on their popularity (added to friends by how many users)
    private static HashMap<Integer, Integer> topFriendOf(HashMap<Integer, HashSet<Integer>> data) {
    	//uses counters for every node to calculate node's popularity
    	HashMap<Integer, AtomicInteger> counts = new HashMap<>();
    	HashSet<Integer> seen = new HashSet<>();
    	for (Iterable<Integer> friends : data.values()) {
    		for (Integer userId : friends) {
    			if (!seen.contains(userId)) {
    				counts.put(userId, new AtomicInteger(1));
    				seen.add(userId);
    			} else {
    				counts.get(userId).incrementAndGet();
    			}
    		}
    	}
    	//fills "popularity" map and passes it to the top users calculator
    	HashMap<Integer, Integer> fullPopularityCounts = new HashMap<>();
    	for (Map.Entry<Integer, AtomicInteger> entry : counts.entrySet()) {
    		fullPopularityCounts.put(entry.getKey(), entry.getValue().intValue());
    	}
    	return heapifyAndReturnTop(fullPopularityCounts, 500);    	
    }
    
    //calculates and returns top N users using PriorityQueue and custom comparator
    //"Size" variable controls the size of resulting HashMap (node id + comparable parameter)
    private static HashMap<Integer, Integer> heapifyAndReturnTop(HashMap<Integer, Integer> data, int size) {
    	Comparator<Map.Entry<Integer, Integer>> comparator = new FriendsComparator();
    	PriorityQueue<Map.Entry<Integer, Integer>>  pq = new PriorityQueue<>(comparator);
    	HashMap<Integer, Integer> result = new HashMap<>();
    	
    	for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
    		//fill first N nodes 
    		Map.Entry<Integer, Integer> newEntry = new AbstractMap.SimpleEntry<>(entry.getKey(), 
    												entry.getValue());
    		if (pq.size() < size) {
    			pq.add(newEntry);
    		} 
    		//change the minimum value in PQ (always on top) with the bigger value when needed
    		if (pq.size() >= size && entry.getValue() > pq.peek().getValue()) {  		
				pq.poll();
				pq.add(newEntry); 
			}   		
    	}   	  
    	//restore top N nodes with data in the Map and return
    	for (Map.Entry<Integer, Integer> entry : pq) {
    		result.put(entry.getKey(), entry.getValue());
    	}  	
    	return result;
    }
    
    //Calculates Average Clustering Coefficient
    private static double calculateACC(HashMap<Integer, HashSet<Integer>> data) {
    	double total = 0;
    	ArrayList<Double> CCs = new ArrayList<>();
    	for (Integer i : data.keySet()) {
    		//skip undefined CCs for nodes with 0- or 1-sized networks
    		if (data.get(i).size() != 0 && data.get(i).size() != 1) {
    			double res = calculateCC(i, data);
    			CCs.add(res); 
    		}
    	}   
    	for (Double i : CCs) {
    		total = total + i;
    	}
    	double res = total / (double) data.keySet().size();
    	return res;
    }
    
    //helper method, calculating Clustering coefficient for a single node
    private static double calculateCC(Integer id, HashMap<Integer, HashSet<Integer>> data) {
    	int nodes = data.get(id).size(); //all friends (network)
    	int actualEdges = 0;
    	for (Integer i : data.get(id)) {
    		for (Integer j : data.get(id)) {
    			if (data.get(j).contains(i)) {
    				actualEdges++;
    			}
    		}
    	}
    	int possibleEdges = nodes*(nodes - 1);
    	double res = actualEdges / (double) possibleEdges;
    	return res;
    }
    
    //Calculated Diameter (greatest distance between nodes in the network) 
    private static int calculateDiameter(HashMap<Integer, HashSet<Integer>> data) {
    	int diameter = 0;    	
    	for (Integer i : data.keySet()) {
    		int nodeDiameter = bfs(i, Integer.MAX_VALUE, data);
    		if (nodeDiameter > diameter) {
    			diameter = nodeDiameter;
    		}
    	}   	
    	return diameter;
    }
    
    //helper method to find the shortest path between two nodes using BFS
    private static int bfs(Integer mainNode, Integer toSearch, HashMap<Integer, HashSet<Integer>> data) {
    	int shortestPath = -1;
    	Queue<Integer> queue = new LinkedList<>();
    	HashSet<Integer> visited = new HashSet<>();
    	HashMap<Integer, Integer> parent = new HashMap<>();
    	
    	queue.add(mainNode);
    	visited.add(mainNode);
    	
    	while (!queue.isEmpty()) {
    		Integer curr = queue.poll();
    		if (curr == toSearch || queue.isEmpty()) {
    			if (parent.isEmpty()) {
    				shortestPath = 0;
    			} else {
    				Integer back = curr;
    				while (back != mainNode) {
    					back = parent.get(back);
    					shortestPath++;
    				}
    				return shortestPath;
    			}   			
    		}
    		for (int k : data.get(curr)) {
    			if (!visited.contains(k)) {
	    			queue.add(k);
	    			parent.put(k, curr);
	    			visited.add(k);
    			}
    		}
    	}    	
    	return shortestPath; 
    }
}

