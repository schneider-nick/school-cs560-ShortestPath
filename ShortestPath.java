import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;
import java.io.*;
import java.lang.Throwable;

public class ShortestPath{
	public static Hex[][] hexArray = new Hex[31][15];
	public static List<vertex> nodes = new ArrayList<vertex>();
	public static void main(String[] args) {
		try{	//first, load the array with the hex data from the text file:	
			loadArray();
			loadSide();
			//printArray();
			dijkstra(225);
		}
		catch(Exception e){

		}
	}	

	public static void loadArray(){
		//load up the text file
		try{
			File textfile = new File("INPUT.TXT");
			Scanner scanner = new Scanner(textfile);
			//textFile is loaded, now load the hex aray witht he data from the text file
			int currentRow = 1,currentColumn;
			Hex hex;
			int min = Integer.MIN_VALUE;
			while(scanner.hasNextLine()){
				currentColumn = 1;
				//handle odd rows first
				if((currentRow % 2) != 0){
				//only add elements in odd columns	
					while(currentColumn <= 15){
						//only add element in odd columns(
						if((currentColumn % 2) !=0){
							//make new Hex and put it in the curent postion
							hex = new Hex(scanner.nextInt(),scanner.nextInt());
							hexArray[currentRow-1][currentColumn-1] = hex;
						} else{
							hexArray[currentRow-1][currentColumn-1] = null;
						}
						currentColumn++;
					}
				} else if((currentRow % 2) == 0){
					while(currentColumn <=15){
						//only add elements in odd columns\
						if((currentColumn % 2) !=0){
							hexArray[currentRow-1][currentColumn-1] = null;
						} else{
							hex = new Hex(scanner.nextInt(),scanner.nextInt());
							hexArray[currentRow-1][currentColumn-1] = hex;
						}
					currentColumn++;
					}
				}
				currentRow++;
			}
		}	
		catch(Exception e){
			
		}
	}

	public static void loadSide(){
		int i = 0;
		vertex node;
		for(int row = 0; row < hexArray.length;row++){
			for(int column = 0; column < hexArray[row].length; column++){
				if(hexArray[row][column] != null){
					node  = new vertex();
					node.add(hexArray[row][column]);	
					hexSide(row,column,node);
					nodes.add(node);
					i++;
				}
			}					
		}
	}
	public static void hexSide(int row, int column, vertex node){
		Hex side;
		if(row < 29){
			if((side = hexArray[row+2][column]) != null);
				node.add(side);
		}
		if(row < 30 && column < 14){
			if((side = hexArray[row+1][column+1]) != null);
				 node.add(side);
		}
		if(row > 0 && column <14){
			if((side = hexArray[row-1][column+1]) != null);
				node.add(side);
		}
		if(row > 1){
			if((side =hexArray[row-2][column]) != null);
				node.add(side);
		}
		if(row >0 && column > 0){
			if((side = hexArray[row-1][column-1]) != null);
				node.add(side);
		}
		if(row <30 && column >0){
			if((side = hexArray[row+1][column-1]) != null);
				node.add(side);
		}
		return;
	}

	 // Funtion that implements Dijkstra's single source shortest path
    // algorithm for a graph represented using adjacency list 
	public static void dijkstra(int src){
		int V = 233;
		vertex node;
		Hex hex;
		int dist[] = new int[V]; // The output array. dist[i] will hold
                                 // the shortest distance from src to i
 
        	// sptSet[i] will hold true if vertex i is included in shortest
        	// path tree or shortest distance from src to i is finalized
		Boolean sptSet[] = new Boolean[V];
 
        	// Initialize all distances as INFINITE and stpSet[] as false
		for (int i = 0; i < V; i++){
			dist[i] = Integer.MAX_VALUE;
			sptSet[i] = false;
		}
		// Distance of source vertex from itself is always its own cost
		dist[src] = nodes.get(src).getside(0).cost;
 
		// Find shortest path for all vertices
		for (int count = 0; count < V; count++){
        	    // Pick the minimum distance vertex from the set of vertices
        	    // not yet processed.  
			int u = minDistance(dist, sptSet, V);
        	     // Mark the picked vertex as processed
			sptSet[u] = true;
			node = nodes.get(u);
		
        	    // Update dist value of the adjacent vertices of the
        	    // picked vertex.
			for (int v = 1; v < 7; v++){
				hex = node.getside(v);
        	        // Update dist[v] only if is not in sptSet, there is an
        	        // edge from u to v, and total weight of path from src to
        	        // v through u is smaller than current value of dist[v]
				if (hex !=null && !sptSet[hex.index-1] && dist[u] != Integer.MAX_VALUE && dist[u]+ hex.cost < dist[hex.index-1]) {
					dist[hex.index - 1] = dist[u] + hex.cost;
					hex.parent = nodes.get(u).getside(0);
				}
			}
		}
 		//System.out.print(dist.toString());
        	// print the constructed distance array
        	printSolution(dist, sptSet, V);
	}

	public static int minDistance(int dist[], Boolean sptSet[],int V){
        	// Initialize min value
       		int min = Integer.MAX_VALUE, min_index=-1;
       	 	for (int v = 0; v < V; v++) {
				if (sptSet[v] == false && dist[v] <= min) {
					min = dist[v];
					min_index = v;
				}
			}
        	return min_index;
    	}	

	public static void printArray(){
		Hex hex;
		for(vertex node : nodes){
			for(int i =0; i < 7;i++){
				hex = node.getside(i);
				if(hex == null)
					continue;
				else	
					System.out.print(hex.index + "  ");
			}
			System.out.println();
		}
		System.out.println();

	}
	
	public static void printSolution(int dist[], Boolean sptSet[], int n) {
		/*
		System.out.println("Vertex Distance from Source");
		for (int i = 0; i < 233; i++){
			System.out.println((i + 1) + " \t\t " + dist[i]);
		}
		*/
		//print path of solution:
		//keep a stack of the hexes so we can print them in correct order:
		Stack<Hex> stack = new Stack<Hex>();
		int endHexPos = 7;
		Hex currHex = nodes.get(endHexPos).getside(0);
		while(currHex.parent != null){
			stack.push(currHex);
			currHex = currHex.parent;
		}
		stack.push(currHex);
		//print the stack:
		while(!stack.empty()){
			System.out.println(stack.pop().index);
		}
		System.out.println("MINIMAL-COST PATH COSTS: " + dist[7]);
	}
}			
