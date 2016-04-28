# Analyzing Social Network Data
## Capstone project for the Coursera specialization

This report in Google docs: https://docs.google.com/document/d/1Z_efCpAT8wtSBvKOZhDkLToyZGUDVf7vjn2lW8sxBXs

##Overview

The goal of this project was to calculate a set of social metrics and statistics based on large social network dataset from popular Livejournal blogging system using graphs and CS algorithms.

##Data

LiveJournal (LJ) is a social networking service started in 1999 in San Francisco, California. Every LJ user has a blog, can add other blogs to the list of friends, and can be added as a friend by others. It has great popularity - 39,663,771 accounts existed on LiveJournal by 2012. The Livejournal dataset available on SNAP is a directed LiveJournal friendship social network, gathered, according to earliest citations, before 2006:

https://snap.stanford.edu/data/soc-LiveJournal1.html (1 GB of data in unzipped txt file)

This dataset contains 4847571 nodes and 68993773 edges in the form of “node i -> friend of node i” pairs. Data is sorted by nodes ids from 0 to max. Due to a large size of data and long processing time, the subset of 500 000 nodes was used for the majority of calculations. Graph of this size was considered big enough for significant calculations as most popular users was calculated to be added to friends by more than 5000 people, which is close to real world data. A subgraph of top 100 most active and popular users was derived, analyzed and compared with the basic graph.

HashMap<Integer, HashSet<Integer>> data structure was used to store the graph nodes, where HashSet holded friends list (node ids that are added to friends by the current node).

##Questions:

###Easy:
- Limit graph size: as ids in the dataset are sorted and named from 0 to max, it was possible to add a “# of nodes to process” parameter to the graph loader (e.g. first 500 000 nodes only); 
- Build the methods to get top 100 most popular (by # of followers) and most active (with largest feeds) users to compare top subnetworks with the full network data;
- Calculate and print a graph report with basic statistics:

  a) # of nodes
  b) # of edges
  c) Is the network sparse? (# of edges closer to N than to N^2)
  d) Average # of friends
  e) Mutual friends percentage
  f) List of # of followers for top 100 popular users
  g) List of # of users, added to friends by top 100 active users (feed sizes)
  h) Calculate a distribution diagrams for user’s popularity and feed size in the initial graph.

###Complex:
- Calculate the average clustering coefficient (ACC)
- Create the subgraphs for the top 100 popular and active users and calculate all parameters above for those subgraphs, compare with initial graph statistics
- Calculate the diameter (the greatest distance value) of the network. In social networks, the diameter could be an indication of how quickly information reaches everyone in the network in the worst-case. 

##Algorithms and Data Structures

Data structures used:
- HashMap<Integer, HashSet<Integer>> to hold the graph data
- PriorityQueue to hold top 100 users for most popular and most active users calculations
- HashMap<Integer, Integer> to hold user id + calculation result for “top” calculations, to be able to return a subgraph of most active users later
- Additional classes for ReportGenerator and GraphReport POJO to store the results of calculations

Algorithms used:

- Binary search - manually :) to determine the right number of nodes needed to be processed to reach the goal of 5000 followers for most popular users
- Minimum heap algorithm, implemented inside PriorityQueue in Java, to hold top 100 users
- All Pairs Shortest Path (APSP) algorithm to calculate the network diameter


##Answers: 

###Large graph statistics: 

- Number of nodes: 500000
- Number of edges: 12585472
- Is this graph sparse? Yes
- Mutual friendships, % of all friendships: 60.31%
- Average feed size: 25.17
- Longest feeds: [4695, 4505, 2330, 2321, 1961, 1766, 1571, 1531, 1360, 1135, 1088, 1072, 1065, 1026, 995, 986, 971, 950, 910]
- Most popular users are in feeds of: [7140, 6734, 3918, 3515, 3356, 3091, 2810, 2674, 2605, 2408, 2328, 2227, 2200, 2181, 2028, 2012, 1959, 1955, 1908] other users
- Average Clustering Coefficient: 0.2657 - very close to 0.2742 listed on dataset source page for the full 5M graph - proves we can approximate the smaller graph of 500 000 statistics to the full graph (and that the calculations were correct :)

###Top 100 active users subgraph statistics:

- Number of nodes: 39410 (all connected nodes and their relationships included). 
- Number of edges: 1964760
- Is this graph sparse? Yes
- Mutual friendships, % of all friendships: 45.61% 
- Average feed size: 49.85 - predictably higher, as we've selected the subgraph nodes based on this parameter.
- Average Clustering Coefficient: 0.24038

###Top 100 popular users subgraph statistics:

- Number of nodes: 79324 (all connected nodes and their relationships included). 
- Number of edges: 2450431
- Is this graph sparse? Yes
- Mutual friendships, % of all friendships: 48.08% 
- Average feed size: 30.89
- Average Clustering Coefficient: 0.21665

!!! Top subgraphs cover the very significant amount of network, with ~8% for top 100 active and ~16% for top 100 popular users, which can be used in data flow calculations for the network. Top 250 gives us numbers of 12,5 and 20% accordingly, with the gain not that impressive for more that doubling the top size (see https://goo.gl/photos/xwsEwQhLj4RgCgfn8 for a detail graph). The friend nodes are predictably less connected to each other (lower mutual friendship % and ACC compared with basic graph), as popular users not necessarily connected between each other and covers different areas of the network.

###Distributions:

Feed size distribution (by # of users added to the feed, nodes counted: 500000):
- Range 1-3: 88122, or 17.62%
- Range 4-10: 124949, or 24.99%
- Range 11-25: 141648, or 28.33%
- Range 26-50: 84276, or 16.86%
- Range 51-100: 41895, or 8.38%
- Range 101-200: 14225, or 2.84%
- Over 200: 4885, or 0.98%

Popular users distribution (followed by # users, nodes counted: 488543, nodes not included (friends of 0): 11457, or 2.29%)
- Range 1-3: 81348, or 16.65%
- Range 4-10: 132873, or 27.20%
- Range 11-25: 137325, or 28.11%
- Range 26-50: 78370, or 16.04%
- Range 51-100: 39876, or 8.16%
- Range 101-200: 13656, or 2.80%
- Over 200: 5095, or 1.04%

Both distributions are very close and are almost identical to the top active/popular subgraphs distributions. Over 50% of users has feeds and are friends of 4-25 other users, and around 1% are very active with over 200 users in feeds and as friends of. 

Diameter was calculated on the subgraphs (Top 100 users of the 100 000 initial graph) due to time complexity and were 9 for top active and 11 for top popular users. (Diameter of all network in online resources for this dataset is 16).


##Algorithm Analysis

**Finding top active users** (biggest feeds) using the Min Heap (PriorityQueue in Java) is linear (O(N)) for graph G<N(odes), E(dges)> with a heap size of K. As all friends of the node are stored in a HashSet, we need to:
get .size() (O(1) * N => N),
compare it to the minimum from the min heap (O(1) * N => N)
swap the heap minimum if compared size is bigger (O(log(K)) to resort the heap on insertion) * L insertions (should be way less than N)
=> total of N + N + L * log(K). Worst case: L == N and the algorithm will be of N * log(K) complexity. In our case K= 100, so we can still think about this complexity as linear.

To **find top popular users** we need to count how many times each node is present in other node’s HashSet of friends. Bruteforce solution would be for every node:
- .cointains(node) every HashSet of friends in other nodes (1 * N * N)
- Increment the count (might be close to E)
- Compare the final count for the node to the min heap (O(1)*N) and
- insert to the heap (O(log(K) * L insertions)
This adds up to N * N + E + N + log(K) * L => O(n^2) complexity.

This algorithm though does not take the advantage of the fact that HashSet for each node is much smaller than the graph size (an average node will be a friend of only ~50-100 nodes, so a great number of .contains(node) will be negative, if we test each node on each set (“N - 50” negative queries for each node; imagine N is 1000000, and the algorithm is quadratic!). More optimal approach was used with a counter for each node, going through all HashSets node by node and incrementing the counter for every node found inside. For graph G<N, E> this would make E increments (as E is the number of edges, stored as HashSets), but will take additional space to hold the counters for N nodes. To find top user we’ll need either to sort (N * logN) the final counters and return top K nodes, or heapify in to the K-sized PriorityQueue (N + log(K) * L). Final calculations: E + N + L * log(K) with a PQ vs E + N * logN with sorting.

**Mutual friends count**: Create the counter, and for every node n in graph:
- Take each node m from a HashSet of friends -> m.contains(n) (S - size of Set)
- Increment counter by 1 (one mutual friendship) (L << S)

=> N(S + L), where S and L in sparse graph are so low, that the complexity is closer to a linear.
Finding the average clustering coefficient (ACC) will require to compute the clustering coefficient for each node, store it in some array and then calculate the average.

**The clustering coefficient** (CC) is the ratio of the number of actual edges there are between neighbors  to the number of potential edges there are between neighbors. The set of neighbours for the node is a HashSet of friends (say size S, and average size of a network should be around 50-100, up to 5000 for the top users); the No of potential (maximum) edges of directed graph can be calculated as N(N-1). The No of actual edges we need to determine first: create one counter to hold all edges, and for every node  in neighbors:  
- .contains(node) on other sets (S*N)
- increment a counter on every positive query (L*N)

=> N*(S + L). As N is 500000, and average S is 25, and L < S, the algorithm is better than quadratic for the sparse graph we have, but will be closer to quadratic for the highly connected and smaller graphs.
The need to calculate CC on every node will make the ACC N^2(S + L), which might be still possible on the sparse graphs but very heavy on the highly connected graphs.


##Correctness verification

Different approaches were used to test the correctness of the calculations:

a) Small artificial graphs were created, all parameters manually calculated and compared with the programmatic results;
b) Already available numbers for the dataset (available online) were compared to the final calculations and were really close;
c) Sanity check and comparison with real life experience (I'm a heavy user of LJ since 2006 and have experience based expectations)


##Reflection

Main goals from the initial planning documents were achieved. The only thing dropped due to the time constrains was Diameter optimization using BoundingDiameters algorithm, described by Frank W. Takes and Walter A. Kosters in the “Determining the Diameter of Small World Networks” article. 

##Code structure: 

Class name: Graph
- initial Interface with main Graph methods (from the warm-up assignment)

Class name: CapGraph
- concrete implementation of Graph interface, which holds Graph nodes collection in a HashMap <Node id, Set of friends> format. Supports adding Nodes and friendships (vertices and edges) for Graph building, calculating egonets and exporting / printing.

Class name: GraphReport
- stores parameters calculated on the given graph, allows printing to console and getters / setters for all parameters

Class name: GraphReportGenerator
- main class with static methods to calculate graph parameters. Creates and fills GraphReport.

Class name: FriendsComparator
- helper class to compare intermediate data structures for PriorityQueue operations

Class Name: DistributionsCounter
- helper class to calculate nodes distributions by custom ranges

Class name: GraphLoader
- converts txt data to the Graph with ability to set the size limit

Class name: Runner
- contains main method to run the analysis

