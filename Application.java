package main;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

/*
 * 
 * Name: Kensal Ramos
 * 
 * Description: Implement Dijkstras Algorithm on a graph supporting simple integer weights.  Input will consist 
 * 				of a number of vertices and a list of doubly-connected edges along with their costs; your output 
 * 				will consist of a simple representation of the Dijkstra spanning tree.
 * 
 * INPUT FILE:
 * 6           # number of vertices
 * 5           # source vertex
 * 9           # number of edges
 * 1 2 4
 * 1 5 3
 * 2 3 2
 * 2 4 9
 * 2 5 8
 * 3 4 1
 * 4 5 5
 * 4 6 3
 * 5 6 7
 * vertex1 | vertex2 | edge weight
 * 
 * 
 * 
 * Decided to do priority queue. This is because at first I was thinking of doing a linked list
 * with distance data variables. That led me to thinking about making a queue ordered by their 
 * distances. Surprise surprise thats a priority queue
 * 
 */

public class Application {
	
	static class Vertex  {
		int value;
		int parent;
		boolean visited;
		int distance;
		ArrayList<ArrayList<Integer>> edges; // If we have edge from vertex 1 to vertex 2 this arraylist will have: 1, 2
	}
	
	static Vertex createVertex (int value, ArrayList<ArrayList<Integer>> edges) {
		Vertex temp = new Vertex();
		temp.value = value;
		if (edges != null) 
			temp.edges = new ArrayList<ArrayList<Integer>>(edges);
		else 
			temp.edges = new ArrayList<ArrayList<Integer>>();
		temp.parent = -1;
		temp.visited = false;
		temp.distance = -1;
		return temp;
	}
	
	static ArrayList<Vertex> getAdjacent(ArrayList<Vertex> vertArr, int value) {
		
		ArrayList<Vertex> adjacentVert = new ArrayList<Vertex>();
		ArrayList<Integer> tempInt = new ArrayList<Integer>();
		
		for (int i = 0; i < vertArr.size(); i++) {
			for (int j = 0; j < vertArr.get(i).edges.size(); j++) {
				for (int k = 0; k < 2; k++) {
					if (!vertArr.get(i).edges.get(j).isEmpty() && vertArr.get(i).edges.get(j).get(k) == value) {
						if (k == 1)
							tempInt.add(vertArr.get(i).edges.get(j).get(0));
						if (k == 0)
							tempInt.add(vertArr.get(i).edges.get(j).get(1)); 
					}
				}
			}
		}
		
		for (int i = 0; i < vertArr.size(); i++) {
			for (int j = 0; j < tempInt.size(); j++) {
				if (vertArr.get(i).value == tempInt.get(j) && !vertArr.get(i).visited) {
					adjacentVert.add(vertArr.get(i));
				}
			}
		}
		
		return adjacentVert;
		
	}
 
	static int getDistance(ArrayList<Vertex> vertArr, int start, int end) {
		
		ArrayList<Vertex> adjacentV = new ArrayList<Vertex>();
		int distance = 0;
		
		adjacentV = getAdjacent(vertArr, start);
		
		for (int i = 0; i < adjacentV.size(); i++) {
			if (adjacentV.get(i).value == end) {
				vertArr.get(start - 1);
				for (int j = 0; j < vertArr.get(start - 1).edges.size(); j++) {
					if (vertArr.get(start - 1).edges.get(j).get(1) == end)
						distance += vertArr.get(start - 1).edges.get(j).get(2);
				}
				vertArr.get(end - 1);
				for (int j = 0; j < vertArr.get(end - 1).edges.size(); j++) {
					if (vertArr.get(end - 1).edges.get(j).get(1) == start)
						distance += vertArr.get(end - 1).edges.get(j).get(2);
				}
				
			}
				
		}
		
		//System.out.println("Distance between " + start + " and " + end + " is " + distance);
		return distance;
		
	}
	
	static boolean allVertVisited(ArrayList<Vertex> vertArr) {
		
		int count = 0;
		boolean flag = false;
		
		for (int i = 0; i < vertArr.size(); i++) {
			
			if (vertArr.get(i).visited)
				count++;
			
		}
		
		if (count == vertArr.size())
			flag = true;
		
		return flag;
		
	}
	
	public static void main(String[] args) {
	
		/*
		 * We will use integers in the queue. When  we get equal integers, search through list and pick first one.
		 */
		ArrayList<Vertex> vertArr = new ArrayList<Vertex>();
		ArrayList<ArrayList<Integer>> tempEdg = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tempEdgPair;
		ArrayList<Integer> soloVert = new ArrayList<Integer>();
		
		int i = 0;
		int tempVal = -1;
		int vertexCnt = 0;
		int numVer = -1;
		int source = -1;
		int numEdg = -1;
		int edgePairNum = 0;
		boolean flag = true;
		int current = -1;
		
		// First let's handle reading from input file and ignoring comments (#comment)
		PrintWriter pw;
		Scanner scanner;
		
		try {
			pw = new PrintWriter("output.txt");
			scanner = new Scanner(new File("cop3503-asn2-input.txt"));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		while(scanner.hasNextInt() || scanner.hasNext("#")) {
			
			if (scanner.hasNext("#")) 
				scanner.nextLine();
			
			switch (i) {
			case 0:
				numVer = scanner.nextInt();
				System.out.println("Number of vertices: " + numVer);
				break;
			case 1:
				source = scanner.nextInt();
				System.out.println("Source vertex: " + source);
				break;
			case 2:
				numEdg = scanner.nextInt();
				System.out.println("Number of edges: " + numEdg);
				break;
			default:
				tempEdgPair = new ArrayList<Integer>();
				tempVal = scanner.nextInt();
				if (tempVal == vertexCnt && vertexCnt > 0) {
					edgePairNum++;
					tempEdgPair.add(tempVal);
					tempEdgPair.add(scanner.nextInt());
					tempEdgPair.add(scanner.nextInt());
					
					// Keep track of largest value
					for (int j = 0; j < 2; j++) 
						if (tempEdgPair.get(j) > vertexCnt)
							soloVert.add(tempEdgPair.get(j));
					
					vertArr.get(vertexCnt - 1).edges.add(tempEdgPair);
					System.out.println("SIZE OF EDGES: " + vertArr.get(vertexCnt - 1).edges.size());
				}
				else {
					edgePairNum = 0;
					vertexCnt = tempVal;
					tempEdgPair.add(tempVal);
					tempEdgPair.add(scanner.nextInt());
					tempEdgPair.add(scanner.nextInt());
					
					// Keep track of largest value
					for (int j = 0; j < 2; j++) 
						if (tempEdgPair.get(j) > vertexCnt)
							soloVert.add(tempEdgPair.get(j));
						
					
					tempEdg.add(tempEdgPair);
					vertArr.add(createVertex(tempVal, tempEdg));
					
					System.out.print("Created vertex with value: " + vertArr.get(vertexCnt - 1).value + ", with edges: \n");
				}
				for (int j = 0; j < vertArr.get(vertexCnt - 1).edges.size(); j++) {
					System.out.print(vertArr.get(vertexCnt - 1).edges.get(j).get(0) + " to ");
					System.out.print(vertArr.get(vertexCnt - 1).edges.get(j).get(1)+ " with a weight of: ");
					System.out.println(vertArr.get(vertexCnt - 1).edges.get(j).get(2));
				}
				

			}

			for (int j = 0; j < soloVert.size(); j++) 
				if (soloVert.get(j) <= vertexCnt + 1)
					soloVert.remove(j);

			tempEdg.clear();
			
			
			i++;
		} 

		for (int j = 0; j < soloVert.size(); j++) 
			vertArr.add(createVertex(soloVert.get(0), null));
		
		i = 0;
		
		ArrayList<Vertex> adjacentV;
		int minDist = 999;
		/*for (int j = 0; j < adjacentV.size(); j++) {
			System.out.println(adjacentV.get(j).value);
		}*/
		
		// Set parent as visited
		current = vertArr.get(source - 1).value;
		System.out.println("source - 1 = " + (source - 1));
		System.out.println("current - 1 = " + (current - 1));
		int count = 0;
		
		while (flag) {
			
			// Check if all nodes are visited
			if (allVertVisited(vertArr))
				break;
			
			
			System.out.println("Current value is: " + (current));
			System.out.println("minDist = " + minDist);
			vertArr.get(current - 1).visited = true;
			adjacentV = getAdjacent(vertArr, current);
			
			for (i = 0; i < adjacentV.size(); i++) {
				if (adjacentV.get(i).visited)
					count++;
			}
			
			if (count == adjacentV.size()) {
				
				// Get new current
				for (i = 0; i < vertArr.size(); i++) {
					if (vertArr.get(i).distance < vertArr.get(current - 1).distance)
						current = i;
				}
				
			}
			
			else {	for (i = 0; i < adjacentV.size(); i++) {
					if (minDist == 999) {
						minDist = adjacentV.get(i).value;
						System.out.println("Minimum is " + minDist);
					}
					else if (getDistance(vertArr, current, adjacentV.get(i).value) < getDistance(vertArr, current, minDist)) {
						minDist = adjacentV.get(i).value;			
						vertArr.get(minDist - 1).distance = getDistance(vertArr, current, minDist);
						System.out.println("new minimum is " + minDist);
					}
					
				
				}
			}
			
			vertArr.get(current - 1).parent = minDist;
			current = minDist;
			minDist = 999;
		
		}


		
		pw.println(numVer);
		
		for (i = 0; i < numVer; i++) {
			
			pw.print(vertArr.get(i).value + " " + vertArr.get(i).distance + " " + vertArr.get(i).parent + "\n");
			
		}
		
		scanner.close();
		pw.close();
	}

}





































