# Map-Navigator
Graph2.java cointains all the major code and heap.java contains heap implementation.


Project Summary: Metro Map Navigator

Developed a Metro Map Navigator using Java, focusing on data structures and algorithms.
Implemented custom classes to represent metro stations, connections, and travel times.
Utilized Dijkstra's algorithm to compute the shortest time and distance by time between stations.

Data Structures:

--Station Representation:
Created classes to model metro stations, incorporating attributes like station names and connections.
Connection Representation:
Modeled connections as weighted edges with travel time as weights.

--Graph Structure:
Constructed a graph with stations as nodes and connections as edges.
Used adjacency lists to efficiently represent station connections.

--Dijkstra's Algorithm:
Implemented Dijkstra's algorithm for optimal pathfinding between stations.
Employed a priority queue (Min Heap) to efficiently select stations with the shortest time.
Calculated and updated shortest time to reach each station.

--Functionality:

Supported user input of source and destination stations.
Applied Dijkstra's algorithm to determine the shortest time and distance by time.
Displayed the optimal route and corresponding travel time.

--Advantages and Impact:

Demonstrated efficient metro navigation with Dijkstra's algorithm.
Tailored for both shortest time and shortest distance by time calculations.
Showcased the practical significance of data structures and algorithms in real-world applications.

--Why only djikstra's algo?
-Non-negative Weights: Dijkstra's algorithm is ideal when dealing with non-negative edge weights. It guarantees correctness and optimality for such cases.

-Single-Source Shortest Path: If you're calculating the shortest path from a single source to all other nodes in a graph, Dijkstra's algorithm is particularly efficient. Its time complexity can be reduced using data structures like priority queues (e.g., min heap).

-Efficient on Dense Graphs: Dijkstra's algorithm tends to perform well on dense graphs with many edges.

-Primarily for Shortest Path: Dijkstra's algorithm is specifically designed for solving the single-source shortest path problem. It's a straightforward algorithm that's easy to implement and understand.
    In summary, while Dijkstra's algorithm is a versatile and effective solution for various scenarios, it's important to assess the specific characteristics of your problem, including edge weights, graph density, and desired outcomes, to determine whether it's the best choice or if other algorithms would better meet your needs.
