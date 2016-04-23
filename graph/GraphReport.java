package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.sun.javafx.geom.transform.GeneralTransform3D;

//Class to store main parameters calculated on the given graph
public class GraphReport {
	private int NoOfNodes;
	private int NoOfEdges;
	private boolean isSparse;
	private double averageFriendsNo;
	private double mutualFriends;
	private HashMap<Integer, Integer> topFriends;
	private HashMap<Integer, Integer> topFriendOf;
	private int[] feedSizeDistribution;
	private int[] popularityDistribution;
	private double ACC;
	private int diameter;
	
	public GraphReport(){
		NoOfNodes = 0;
		NoOfEdges = 0;
		isSparse = false;
		averageFriendsNo = 0;
		mutualFriends = 0;
		topFriends = new HashMap<>();
		topFriendOf = new HashMap<>();
		feedSizeDistribution = new int[7];
		popularityDistribution = new int[7];	
		ACC = 0;
		diameter = 0;
	}
	
	//Use to print full graph report to console
	public void printFullReport(){
		System.out.println("Number of nodes: " + getNoOfNodes());
		System.out.println("Number of edges: " + getNoOfEdges());
		System.out.print("Is this graph sparse? ");
		System.out.println(isSparse() ? "Yes" : "No");
		System.out.println("Average feed size: " + String.format("%.2f", getAverageFriendsNo()));
		System.out.println("Mutual friendships, % of all friendships: " + String.format("%.2f", getMutualFriends()) + "%");		
		System.out.println("Longest feeds: ");
		printTop20(getTopFriends());
		System.out.println("Feed size distribution: ");
		printDistribution(feedSizeDistribution);
		System.out.println("Most popular users have that many followers: ");
		printTop20(getTopFriendsOf());
		System.out.println("Popular users distribution (followed by): ");
		printDistribution(popularityDistribution);
		System.out.println("Average Clustering Coefficient: " + getACC());
	}
	
	public void printDiameter() {
		System.out.println("Diameter: " + getDiameter());
	}
	
	//helper methods for full report print method, prints top 20 users from the top N list
	private void printTop20(HashMap<Integer, Integer> data) {		
		ArrayList<Integer> result = new ArrayList<>();
		for (Integer i : data.values()) {
			result.add(i);
    	}
		Collections.sort(result);
		Collections.reverse(result);
		if (result.size() >= 20) {
			System.out.println(result.subList(0, 19).toString());
		} else {
			System.out.println(result.toString());
		}
		System.out.println("");
	}
	
	//helper method to print distributions
	private void printDistribution(int[] hist) {
		//Total is the same as NoOfNodes for feed distribution,
		//but smaller than that for popularity distribution, as not all the nodes are friends of other nodes
		int total = 0;
		for (int i = 0; i < hist.length; i++) {
			total += hist[i];
		}
		System.out.println("Nodes counted: " + total);
		System.out.println("Nodes not included (friends of 0): " + (getNoOfNodes() - total) 
						+ ", or " + String.format("%.2f", (getNoOfNodes() - total) / (double) getNoOfNodes() * 100) + "%");
		System.out.println("Range 1-3: " + hist[0] + ", or " + String.format("%.2f", hist[0] / (double) total * 100) + "%");
		System.out.println("Range 4-10: " + hist[1] + ", or " + String.format("%.2f", hist[1] / (double) total * 100) + "%");
		System.out.println("Range 11-25: " + hist[2] + ", or " + String.format("%.2f", hist[2] / (double) total * 100) + "%");
		System.out.println("Range 26-50: " + hist[3] + ", or " + String.format("%.2f", hist[3] / (double) total * 100)+ "%");
		System.out.println("Range 51-100: " + hist[4] + ", or " + String.format("%.2f", hist[4] / (double) total * 100) + "%");
		System.out.println("Range 101-200: " + hist[5] + ", or " + String.format("%.2f", hist[5] / (double) total * 100) + "%");
		System.out.println("Over 200: " + hist[6] + ", or " + String.format("%.2f", hist[6] / (double) total * 100) + "%");
		System.out.println("");
	}
	
	//Getters and setters for private variables
	public int getNoOfNodes() {
		return NoOfNodes;
	}
	public void setNoOfNodes(int noOfNodes) {
		NoOfNodes = noOfNodes;
	}
	public int getNoOfEdges() {
		return NoOfEdges;
	}
	public void setNoOfEdges(int noOfEdges) {
		NoOfEdges = noOfEdges;
	}

	public HashMap<Integer, Integer> getTopFriends() {
		return topFriends;
	}

	public void setTopFriends(HashMap<Integer, Integer> topFriends) {
		this.topFriends = topFriends;
	}
	public HashMap<Integer, Integer> getTopFriendsOf() {
		return topFriendOf;
	}

	public void setTopFriendOf(HashMap<Integer, Integer> topFriendOf) {
		this.topFriendOf = topFriendOf;
	}

	public boolean isSparse() {
		return isSparse;
	}

	public void setSparse(boolean isSparse) {
		this.isSparse = isSparse;
	}

	public double getAverageFriendsNo() {
		return averageFriendsNo;
	}

	public void setAverageFriendsNo(double averageFriendsNo) {
		this.averageFriendsNo = averageFriendsNo;
	}

	public double getMutualFriends() {
		return mutualFriends;
	}

	public void setMutualFriends(double mutualFriends) {
		this.mutualFriends = mutualFriends;
	}

	public int[] getFeedSizeDistribution() {
		return feedSizeDistribution;
	}

	public void setFeedSizeDistribution(int[] feedSizeDistribution) {
		this.feedSizeDistribution = feedSizeDistribution;
	}

	public int[] getPopularityDistribution() {
		return popularityDistribution;
	}

	public void setPopularityDistribution(int[] popularityDistribution) {
		this.popularityDistribution = popularityDistribution;
	}

	public double getACC() {
		return ACC;
	}

	public void setACC(double aCC) {
		ACC = aCC;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}
}
