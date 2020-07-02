package project_sample ;

/*
 *            DSA MINI PROJECT
 *            Roll numbers: 2959, 2960, 2961, 2969
 *            Topic: Creating a cable television network and finding optimum costs of leasing cables between different houses
 */


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class line
//  each instance of this class consists of 2 houses and the cost of laying cable between them

{
	int cost,houseFrom,houseTo;
	line(int c,int h1,int h2)        //parameterized constructor to accept each line entities
	{
		cost=c;
		houseFrom=h1;
		houseTo=h2;
	}
	void display()        //display function
	{
		System.out.println("Distance "+cost+" From house no:"+houseFrom+" To house no:"+houseTo);
		System.out.println("--------------------------------------------------------------------");
	}

	void displayPath()
	{
		System.out.println(houseFrom+"           "+houseTo+"               "+cost);
	}
}

class Connections
// this is the actual network of houses and the associated costs
{
	int numOfHouses,numOfLines,costPerMetre;
	LinkedList<line> T=new LinkedList<line>();
	LinkedList<line> L=new LinkedList<line>();
	int adj[][] = new int[15][15] ;
	Scanner s=new Scanner(System.in);
	Connections()              //constructor initializes a null network
	{
		numOfHouses=numOfLines=0;
	}


	void createNeighbourhood()    //create function
	{
		int h1,h2,cost;
		System.out.println("Enter the cost of laying out a metre of cable line :");
		costPerMetre=s.nextInt();
		System.out.println("Enter no. of Houses:");
		numOfHouses=s.nextInt();
		System.out.println("Enter no. of possible paths to lay cable line between Houses:");
		numOfLines=s.nextInt();

		for(int i=0 ; i <15 ; i++)
		{
			for(int j=0 ; j<15 ; j++)
			{
				adj[i][j] = Integer.MAX_VALUE ;
			}
		}

		for(int i=0;i<numOfLines;i++)
		{
			System.out.println("Enter house number from:");
			h1=s.nextInt();
			System.out.println("to House no:");
			h2=s.nextInt();
			System.out.println("Enter distance of potential line between them:");
			cost=s.nextInt();

			if(h1!=h2)                               //a cable can't be laid from one house to itself
			{
				L.add(new line(cost,h1,h2));               //creates adjacency list
				L.add(new line(cost,h2,h1));
				adj[h1][h2] = cost ;                       //creates adjacency matrix
				adj[h2][h1] = cost ;
			}
			else
			{
				System.out.println("We can't have a cable laid from one house to itself!");
				i--;
			}
		}

		Collections.sort(L,new Comparator<line>()  //we are sorting the lines in the increasing order of their respective costs
				{
			//@Override
			public int compare(line n1, line n2) {
				if(n1.cost<n2.cost)
				{
					return -1;
				}
				else return 1;
			}
				});


	}
	void displayPotentialLines()
	{
		for(line e:L)
		{
			e.display();
		}
	}

	void addNewHouseToConnection()                          //adds a new house to the existing network
	{
		int numOfLines;
		System.out.println("Enter the new House number:");
		int newHouseNumber;
		numOfHouses++ ;
		newHouseNumber=s.nextInt();
		System.out.println("Enter the number of houses with which the new house can have a cable line: ");
		numOfLines=s.nextInt();
		int h2,cost;

		// adds a new line in the network using the same logic as in the create function above


		for(int i=0;i<numOfLines;i++)
		{
			System.out.println("Enter the house with which new house can have a cable path");
			h2=s.nextInt();
			System.out.println("Enter distance of the above line:");
			cost=s.nextInt();
			if(newHouseNumber!=h2)
			{

				L.add(new line(cost,h2,newHouseNumber));
				L.add(new line(cost,newHouseNumber,h2));
				adj[h2][newHouseNumber] = cost ;
				adj[newHouseNumber][h2] = cost ;
			}
			else
			{
				System.out.println("We can't have a cable laid from one house to itself!");
				i--;
			}
		}

		Collections.sort(L,new Comparator<line>()
		{
			//@Override
			public int compare(line n1, line n2) {
				if(n1.cost<n2.cost)
				{
					return -1;
				}
				else return 1;
			}
		});

	}

	void findMinimumCableRouteKruskals()                    


	//finds MST to layout the cables using edges in the increasing of their costs (no closed cycle should form so that optimum costs are achieved )


	{
		LinkedList<line> path=new LinkedList<line>();
		path.addAll(L);
		System.out.println("From House"+""+"To House"+" "+"cost till now"+" cable line Added/Not");
		int pred[]=new int[50];
		for(int i=0;i<numOfHouses;i++)
		{
			pred[i]=-1;
		}
		int i,j,v=0,u=0,sum=0,min=Integer.MAX_VALUE,count=0;
		while(!path.isEmpty() && count<numOfHouses-1)
		{
			line e=path.remove();
			u=e.houseFrom;
			v=e.houseTo;
			min=e.cost;
			if(min!=Integer.MAX_VALUE)
			{
				i=u;
				j=v;
				while(pred[j]>-1)
				{
					j=pred[j];
				}
				while(pred[i]>-1)
				{
					i=pred[i];
				}
				if(i!=j)
				{
					count++;
					pred[v]=i;
					sum+=min;
					System.out.println(u+"        "+v+"        "+sum+"         Added");
				}
				else
				{
					System.out.println(u+"        "+v+"        "+sum+"         Not Added");
				}
			}

		}
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Total distance to layout cable: "+sum);
		System.out.println("Total cost: "+(sum*costPerMetre));

	}


	void display_adj()                            //displays the adjacency matrix for prim's algorithm
	{
		for(int i=0 ; i<numOfHouses ; i++)
		{
			for(int j=0 ; j<numOfHouses ; j++)
			{
				if(adj[i][j]==2147483647)
				{
					System.out.print("INFINITY  ");
				}
				else
				{
					System.out.print(adj[i][j]+"        ");
				}
			}
			System.out.println();
		}
	}

	int minKey(int key[], Boolean mstSet[])
	{
		// Initialize min value
		int min = Integer.MAX_VALUE, min_index = -1;

		for (int v = 0; v < numOfHouses; v++)
		{
			if (mstSet[v] == false && key[v] < min)
			{
				min = key[v];
				min_index = v;
			}
		}

		return min_index;
	}

	boolean createsMST(int u, int v, boolean inMST[])
	{
		if (u == v)
			return false;
		if (inMST[u] == false && inMST[v] == false)
			return false;
		else if (inMST[u] == true && inMST[v] == true)
			return false;

		return true;
	}

	//Function to construct and print MST for a graph represented using adjacency matrix representation .
	//This function creates a MST from a desired node


	void printMinSpanningTree()
	{
		boolean inMST[] = new boolean[numOfHouses] ; //stores the constructed MST
		System.out.println("Enter the starting vertex: ");
		int start = s.nextInt() ;
		inMST[start] = true;         //making the start vertex as true so that it gets chosen initially
		int edgeNo = 0, MSTcost = 0;
		while (edgeNo < numOfHouses - 1)
		{
			int min = Integer.MAX_VALUE ;
			int a = -1 ;
			int b = -1 ;
			for (int i = 0; i < numOfHouses; i++)
			{
				for (int j = 0; j < numOfHouses; j++)
				{
					if (adj[i][j] < min)
					{
						if (createsMST(i, j, inMST))
						{
							min = adj[i][j];
							a = i;
							b = j;
						}
					}
				}
			}

			//checks for a cycle
			// if cycle is not present only then print the edge and update the cost inMST array
			if (a != -1 && b != -1)
			{
				System.out.println("Edge "+(edgeNo++)+" : ("+a+" , "+b+" ) : distance = "+min);
				MSTcost += min;
				inMST[b] = inMST[a] = true;
			}
		}
		System.out.println("Total distance to layout cable: "+MSTcost);
		System.out.println("Total Cost: "+(MSTcost*costPerMetre));
	}

	void dijkstra(int sourceVertex)
	{
		Boolean[] spt = new Boolean[numOfHouses];
		int [] distance = new int[numOfHouses];
		int INFINITY = Integer.MAX_VALUE;

		//Initialize all the distance to infinity
		for (int i = 0; i <numOfHouses ; i++) {
			distance[i] = INFINITY;
			spt[i]=false;
		}

		//start from the vertex 0
		distance[sourceVertex] = 0;

		//create SPT
		for (int i = 0; i <numOfHouses ; i++) {

			//get the vertex with the minimum distance
			int vertex_U = minKey(distance,spt);

			//include this vertex in SPT
			spt[vertex_U] = true;

			//iterate through all the adjacent vertices of above vertex and update the keys
			for (int vertex_V = 0; vertex_V <numOfHouses ; vertex_V++) {
				//check of the edge between vertex_U and vertex_V
				if(adj[vertex_U][vertex_V]>0){
					//check if this vertex 'vertex_V' already in spt and
					// if distance[vertex_V]!=Infinity

					if(spt[vertex_V]==false && adj[vertex_U][vertex_V]!=INFINITY){
						//check if distance needs an update or not
						//means check total weight from source to vertex_V is less than
						//the current distance value, if yes then update the distance

						int newKey =  adj[vertex_U][vertex_V] + distance[vertex_U];
						if(newKey<distance[vertex_V])
							distance[vertex_V] = newKey;
					}
				}
			}
		}
		//print shortest path tree
		printDijkstra(sourceVertex, distance);
	}

	public void printDijkstra(int sourceVertex, int [] key){
		System.out.println("Dijkstra Algorithm:");
		for (int i = 0; i <numOfHouses ; i++) {
			System.out.println("Source Vertex: " + sourceVertex + " to vertex " +   + i +
					" distance: " + key[i]);
		}
		System.out.println("Total distance to layout cable: "+key[numOfHouses-1]);
		System.out.println("Total cost: "+key[numOfHouses-1]*costPerMetre);

	}



}


public class cable_sample {

	public static void main(String[] args) {
		int ch,cho,num,c;
		Connections g=new Connections();
		Scanner s=new Scanner(System.in);

		System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println();
		System.out.println();
		System.out.println("\t\t\t\t\t\t\t\t CABLE TELEVISION NETWORK OFFICE ");
		System.out.println();
		System.out.println();
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------");




		do                           //main menu
		{

			System.out.println();

			System.out.println(" 1] Enter 1 to accept houses and the costs of cabling between each house pair");
			System.out.println(" 2] Enter 2 to display houses and potential routes to install cable lines and their costs");
			System.out.println(" 3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost");  //kruskals
			System.out.println(" 4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost");   //prims
			System.out.println(" 5] Enter 5 to find minimum cabling cost of any house from a given source house");  //dijikstra's
			System.out.println(" 6] Enter 6 to add a new house in the connection");
			System.out.println(" 7] Enter 7 to if you wish to exit");
			System.out.println();

			c=s.nextInt();
			switch(c)
			{
			case 1:                                                       //Create a graph
				g.createNeighbourhood();
				break ;

			case 2 :                                   //Display only the potential routes between 2 houses for the cable lines
				g.displayPotentialLines();
				break ;

			case 3 :
				g.findMinimumCableRouteKruskals();	
				break;

			case 4:
				do
				{
					System.out.println("Enter : \n1.Display cable layout costs between each house pair in matrix form\n2.Display minimum cable lines required for connections");
					ch=s.nextInt();
					switch(ch)
					{
					case 1 :
						//g.initialize_matrix();
						g.display_adj();
						break ;

					case 2:                    //Min cable lines required for connection
						g.printMinSpanningTree();
						break;

					default:System.out.println("Invalid option!");
					}


					System.out.println("Enter 1 to continue:");
					cho=s.nextInt();
				}while(cho==1);
				break;


			case 5:
				System.out.println("Enter source and destination of the cable network:");
				int scr=s.nextInt();
				g.dijkstra(scr);

				break;

			case 6:
				g.addNewHouseToConnection();
				break;


			case 7:
				System.out.println("Thank you ! We hope to serve you again in future.");
				break;

			default:
				System.out.println("Check your choice.");

			}
		}while(c!=7);


	}

}

/*
                                                   OUTPUT
------------------------------------------------------------------------------------------------------------------------------------


                                       CABLE TELEVISION NETWORK OFFICE 


------------------------------------------------------------------------------------------------------------------------------------

1] Enter 1 to accept houses and the costs of cabling between each house pair
2] Enter 2 to display houses and potential routes to install cable lines and their costs
3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost
4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost
5] Enter 5 to find minimum cabling cost of any house from a given source house
6] Enter 6 to add a new house in the connection
7] Enter 7 to if you wish to exit

1
Enter the cost of laying out a metre of cable line :
100
Enter no. of Houses:
6
Enter no. of possible paths to lay cable line between Houses:
9
Enter house number from:
0
to House no:
1
Enter distance of potential line between them:
6
Enter house number from:
0
to House no:
2
Enter distance of potential line between them:
3
Enter house number from:
1
to House no:
2
Enter distance of potential line between them:
2
Enter house number from:
1
to House no:
3
Enter distance of potential line between them:
5
Enter house number from:
2
to House no:
3
Enter distance of potential line between them:
3
Enter house number from:
2
to House no:
4
Enter distance of potential line between them:
4
Enter house number from:
3
to House no:
4
Enter distance of potential line between them:
2
Enter house number from:
3
to House no:
5
Enter distance of potential line between them:
3
Enter house number from:
4
to House no:
5
Enter distance of potential line between them:
5

1] Enter 1 to accept houses and the costs of cabling between each house pair
2] Enter 2 to display houses and potential routes to install cable lines and their costs
3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost
4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost
5] Enter 5 to find minimum cabling cost of any house from a given source house
6] Enter 6 to add a new house in the connection
7] Enter 7 to if you wish to exit

2
Distance 2 From house no:1 To house no:2
--------------------------------------------------------------------
Distance 2 From house no:2 To house no:1
--------------------------------------------------------------------
Distance 2 From house no:3 To house no:4
--------------------------------------------------------------------
Distance 2 From house no:4 To house no:3
--------------------------------------------------------------------
Distance 3 From house no:0 To house no:2
--------------------------------------------------------------------
Distance 3 From house no:2 To house no:0
--------------------------------------------------------------------
Distance 3 From house no:2 To house no:3
--------------------------------------------------------------------
Distance 3 From house no:3 To house no:2
--------------------------------------------------------------------
Distance 3 From house no:3 To house no:5
--------------------------------------------------------------------
Distance 3 From house no:5 To house no:3
--------------------------------------------------------------------
Distance 4 From house no:2 To house no:4
--------------------------------------------------------------------
Distance 4 From house no:4 To house no:2
--------------------------------------------------------------------
Distance 5 From house no:1 To house no:3
--------------------------------------------------------------------
Distance 5 From house no:3 To house no:1
--------------------------------------------------------------------
Distance 5 From house no:4 To house no:5
--------------------------------------------------------------------
Distance 5 From house no:5 To house no:4
--------------------------------------------------------------------
Distance 6 From house no:0 To house no:1
--------------------------------------------------------------------
Distance 6 From house no:1 To house no:0
--------------------------------------------------------------------

1] Enter 1 to accept houses and the costs of cabling between each house pair
2] Enter 2 to display houses and potential routes to install cable lines and their costs
3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost
4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost
5] Enter 5 to find minimum cabling cost of any house from a given source house
6] Enter 6 to add a new house in the connection
7] Enter 7 to if you wish to exit

3
From HouseTo House cost till now cable line Added/Not
1        2        2         Added
2        1        2         Not Added
3        4        4         Added
4        3        4         Not Added
0        2        7         Added
2        0        7         Not Added
2        3        10         Added
3        2        10         Not Added
3        5        13         Added
------------------------------------------------------------------------
Total distance to layout cable: 13
Total cost: 1300

1] Enter 1 to accept houses and the costs of cabling between each house pair
2] Enter 2 to display houses and potential routes to install cable lines and their costs
3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost
4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost
5] Enter 5 to find minimum cabling cost of any house from a given source house
6] Enter 6 to add a new house in the connection
7] Enter 7 to if you wish to exit

4
Enter : 
1.Display cable layout costs between each house pair in matrix form
2.Display minimum cable lines required for connections
1
INFINITY  6        3        INFINITY  INFINITY  INFINITY  
6        INFINITY  2        5        INFINITY  INFINITY  
3        2        INFINITY  3        4        INFINITY  
INFINITY  5        3        INFINITY  2        3        
INFINITY  INFINITY  4        2        INFINITY  5        
INFINITY  INFINITY  INFINITY  3        5        INFINITY  
Enter 1 to continue:
1
Enter : 
1.Display cable layout costs between each house pair in matrix form
2.Display minimum cable lines required for connections
2
Enter the starting vertex: 
1
Edge 0 : (1 , 2 ) : distance = 2
Edge 1 : (0 , 2 ) : distance = 3
Edge 2 : (2 , 3 ) : distance = 3
Edge 3 : (3 , 4 ) : distance = 2
Edge 4 : (3 , 5 ) : distance = 3
Total distance to layout cable: 13
Total Cost: 1300
Enter 1 to continue:
2

1] Enter 1 to accept houses and the costs of cabling between each house pair
2] Enter 2 to display houses and potential routes to install cable lines and their costs
3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost
4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost
5] Enter 5 to find minimum cabling cost of any house from a given source house
6] Enter 6 to add a new house in the connection
7] Enter 7 to if you wish to exit

5
Enter source and destination of the cable network:
0
Dijkstra Algorithm:
Source Vertex: 0 to vertex 0 distance: 0
Source Vertex: 0 to vertex 1 distance: 5
Source Vertex: 0 to vertex 2 distance: 3
Source Vertex: 0 to vertex 3 distance: 6
Source Vertex: 0 to vertex 4 distance: 7
Source Vertex: 0 to vertex 5 distance: 9
Total distance to layout cable: 9
Total cost: 900

1] Enter 1 to accept houses and the costs of cabling between each house pair
2] Enter 2 to display houses and potential routes to install cable lines and their costs
3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost
4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost
5] Enter 5 to find minimum cabling cost of any house from a given source house
6] Enter 6 to add a new house in the connection
7] Enter 7 to if you wish to exit

6
Enter the new House number:
6
Enter the number of houses with which the new house can have a cable line: 
2
Enter the house with which new house can have a cable path
4
Enter distance of the above line:
6
Enter the house with which new house can have a cable path
5
Enter distance of the above line:
1

1] Enter 1 to accept houses and the costs of cabling between each house pair
2] Enter 2 to display houses and potential routes to install cable lines and their costs
3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost
4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost
5] Enter 5 to find minimum cabling cost of any house from a given source house
6] Enter 6 to add a new house in the connection
7] Enter 7 to if you wish to exit

2
Distance 1 From house no:5 To house no:6
--------------------------------------------------------------------
Distance 1 From house no:6 To house no:5
--------------------------------------------------------------------
Distance 2 From house no:1 To house no:2
--------------------------------------------------------------------
Distance 2 From house no:2 To house no:1
--------------------------------------------------------------------
Distance 2 From house no:3 To house no:4
--------------------------------------------------------------------
Distance 2 From house no:4 To house no:3
--------------------------------------------------------------------
Distance 3 From house no:0 To house no:2
--------------------------------------------------------------------
Distance 3 From house no:2 To house no:0
--------------------------------------------------------------------
Distance 3 From house no:2 To house no:3
--------------------------------------------------------------------
Distance 3 From house no:3 To house no:2
--------------------------------------------------------------------
Distance 3 From house no:3 To house no:5
--------------------------------------------------------------------
Distance 3 From house no:5 To house no:3
--------------------------------------------------------------------
Distance 4 From house no:2 To house no:4
--------------------------------------------------------------------
Distance 4 From house no:4 To house no:2
--------------------------------------------------------------------
Distance 5 From house no:1 To house no:3
--------------------------------------------------------------------
Distance 5 From house no:3 To house no:1
--------------------------------------------------------------------
Distance 5 From house no:4 To house no:5
--------------------------------------------------------------------
Distance 5 From house no:5 To house no:4
--------------------------------------------------------------------
Distance 6 From house no:0 To house no:1
--------------------------------------------------------------------
Distance 6 From house no:1 To house no:0
--------------------------------------------------------------------
Distance 6 From house no:4 To house no:6
--------------------------------------------------------------------
Distance 6 From house no:6 To house no:4
--------------------------------------------------------------------

1] Enter 1 to accept houses and the costs of cabling between each house pair
2] Enter 2 to display houses and potential routes to install cable lines and their costs
3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost
4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost
5] Enter 5 to find minimum cabling cost of any house from a given source house
6] Enter 6 to add a new house in the connection
7] Enter 7 to if you wish to exit

3
From HouseTo House cost till now cable line Added/Not
5        6        1         Added
6        5        1         Not Added
1        2        3         Added
2        1        3         Not Added
3        4        5         Added
4        3        5         Not Added
0        2        8         Added
2        0        8         Not Added
2        3        11         Added
3        2        11         Not Added
3        5        14         Added
------------------------------------------------------------------------
Total distance to layout cable: 14
Total cost: 1400

1] Enter 1 to accept houses and the costs of cabling between each house pair
2] Enter 2 to display houses and potential routes to install cable lines and their costs
3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost
4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost
5] Enter 5 to find minimum cabling cost of any house from a given source house
6] Enter 6 to add a new house in the connection
7] Enter 7 to if you wish to exit

4
Enter : 
1.Display cable layout costs between each house pair in matrix form
2.Display minimum cable lines required for connections
1
INFINITY  6        3        INFINITY  INFINITY  INFINITY  INFINITY  
6        INFINITY  2        5        INFINITY  INFINITY  INFINITY  
3        2        INFINITY  3        4        INFINITY  INFINITY  
INFINITY  5        3        INFINITY  2        3        INFINITY  
INFINITY  INFINITY  4        2        INFINITY  5        6        
INFINITY  INFINITY  INFINITY  3        5        INFINITY  1        
INFINITY  INFINITY  INFINITY  INFINITY  6        1        INFINITY  
Enter 1 to continue:
1
Enter : 
1.Display cable layout costs between each house pair in matrix form
2.Display minimum cable lines required for connections
2
Enter the starting vertex: 
3
Edge 0 : (3 , 4 ) : distance = 2
Edge 1 : (2 , 3 ) : distance = 3
Edge 2 : (1 , 2 ) : distance = 2
Edge 3 : (0 , 2 ) : distance = 3
Edge 4 : (3 , 5 ) : distance = 3
Edge 5 : (5 , 6 ) : distance = 1
Total distance to layout cable: 14
Total Cost: 1400
Enter 1 to continue:
2

1] Enter 1 to accept houses and the costs of cabling between each house pair
2] Enter 2 to display houses and potential routes to install cable lines and their costs
3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost
4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost
5] Enter 5 to find minimum cabling cost of any house from a given source house
6] Enter 6 to add a new house in the connection
7] Enter 7 to if you wish to exit

5
Enter source and destination of the cable network:
0
Dijkstra Algorithm:
Source Vertex: 0 to vertex 0 distance: 0
Source Vertex: 0 to vertex 1 distance: 5
Source Vertex: 0 to vertex 2 distance: 3
Source Vertex: 0 to vertex 3 distance: 6
Source Vertex: 0 to vertex 4 distance: 7
Source Vertex: 0 to vertex 5 distance: 9
Source Vertex: 0 to vertex 6 distance: 10
Total distance to layout cable: 10
Total cost: 1000

1] Enter 1 to accept houses and the costs of cabling between each house pair
2] Enter 2 to display houses and potential routes to install cable lines and their costs
3] In case of a sparse network ,Enter 3 to create a network including all houses with minimum costs between each pair and display the min total cost
4] In case of a denser network ,Enter 4 to create a network including all houses, starting from a desired house and add the next cheapest house in order to produce min cost
5] Enter 5 to find minimum cabling cost of any house from a given source house
6] Enter 6 to add a new house in the connection
7] Enter 7 to if you wish to exit

7
Thank you ! We hope to serve you again in future.

 */