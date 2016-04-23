package graph;

import java.util.Comparator;
import java.util.Map;

//Helper class to compare graph nodes for PriorityQueue operations
public class FriendsComparator implements Comparator<Map.Entry<Integer, Integer>>{
	@Override
	public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
		int size1 = o1.getValue();
		int size2 = o2.getValue();
		
		return Integer.compare(size1, size2);
	}
}
