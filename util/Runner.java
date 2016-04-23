package util;

import graph.CapGraph;
import graph.Graph;
import graph.GraphReport;
import graph.GraphReportGenerator;

import static graph.GraphReportGenerator.generateReport;

import com.sun.javafx.css.CalculatedValue;

public class Runner implements Runnable  {
    public static void main(String[] args) {
        /*Runner go = new Runner();
        Thread thread = new Thread(go);
        thread.start();*/
    	for (char c = '0'; c <= '9'; c++) {
    		System.out.println(char2int(c));
    	}
    }
    
    private static int char2int(char value) {
    	return ((int)value) - 48;
    }
    
    @Override
    public void run() {
    	String file = "livejournal.txt";
        System.out.println("");
        Graph graph = new CapGraph();
        System.out.println("Loading " + file + "...");
        System.out.println("");
        GraphLoader.loadGraph(graph, "data/" + file, 500000); 
        System.out.println("Generating report...");
        GraphReport fullReport = generateReport(graph);
        System.out.println(file.toUpperCase()  + " graph report:");
        System.out.println("");
        fullReport.printFullReport();        
        System.out.println("________________________________________________________");

        System.out.println("Generating report for top most active users (biggest feeds)");
        Graph graphTopFeeds = graph.importFeedsGraph(fullReport.getTopFriends());
        GraphReport fullReportTopFeeds = generateReport(graphTopFeeds);
        fullReportTopFeeds.printFullReport();
        System.out.println("________________________________________________________");

        System.out.println("Generating report for top most popular users");
        Graph graphTopPopular = graph.importPopularGraph(fullReport.getTopFriendsOf());
        GraphReport fullReportTopPopular = generateReport(graphTopPopular);
        fullReportTopPopular.printFullReport();
        
//        System.out.println("Calculating diameter for top most active users");
//        GraphReportGenerator.calculateDiameter(graphTopFeeds, fullReportTopFeeds);
//        fullReportTopFeeds.printDiameter();
//
//        System.out.println("Calculating diameter for top most popular users");
//        GraphReportGenerator.calculateDiameter(graphTopPopular, fullReportTopPopular);
//        fullReportTopPopular.printDiameter();
    }
}
