package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

//Helper class to calculate nodes distributions by ranges
public class DistributionsCounter {

	public static int[] feedSizeDistribution(HashMap<Integer, HashSet<Integer>> data) {
    	ArrayList<Integer> feedSizes = new ArrayList<>();
    	for (HashSet<Integer> s : data.values()) {
    		feedSizes.add(s.size());
    	}
		return calculateDistribution(feedSizes);
    }
	
	public static int[] popularityDistribution(HashMap<Integer, HashSet<Integer>> data) {
    	return calculateDistribution(calculatePopularity(data));
    }
	
	//distribute nodes to 7 ranges
	private static int[] calculateDistribution(ArrayList<Integer> data) {
    	int[] result = new int[7];

    	for (Integer size : data) {
    		if (size <= 3) {
    			result[0]++;
    			continue;
    		} else if (size <= 10) {
    			result[1]++;
    			continue;
    		} else if (size <= 25) {
    			result[2]++;
    			continue;
    		} else if (size <= 50) {
    			result[3]++;
    			continue;
    		} else if (size <= 100) {
    			result[4]++;
    			continue;
    		} else if (size <= 200) {
    			result[5]++;
    			continue;
    		} else if (size > 200) {
    			result[6]++;
    			continue;
    		} 
    	}   	  	
    	return result;
	}
	
	//Cycles over friends sets to check how many times node is in others friend lists
    private static ArrayList<Integer> calculatePopularity(HashMap<Integer, HashSet<Integer>> data) {
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
    	ArrayList<Integer> result = new ArrayList<>();
    	for (AtomicInteger i : counts.values()) {
    		result.add(i.intValue());
    	}
    	Collections.sort(result);
    	Collections.reverse(result);
    	return result;
    }
}
