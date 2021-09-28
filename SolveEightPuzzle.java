
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;


public class SolveEightPuzzle
{
    
    public static Nodes startState;
    public static boolean isManhattan;
    public static boolean isPuzzleSolvable ; // is your new cumulative cost fn is less than parent fn to make the puzzle solvable
    public static PriorityQueue <Nodes> obj_priorityQueue; // store nodes based on the lowest cost function
    public static ArrayList<Nodes> expandedNodes;// to hold all the explored nodes.
    public static ArrayList <Nodes> frontierPathList;
    public static String [][]goalState;
    public static Scanner sc = new Scanner (System.in);
    public static HashMap<Nodes, Nodes> backtrackMap;
    

    SolveEightPuzzle(Nodes initial_node)
    { 
        isManhattan = true;
        isPuzzleSolvable = false;
        if(initial_node == null)
        {
            System.out.println("Invalid input");
        }
        

        obj_priorityQueue.add(initial_node); //add first node to priority queue
        
        ArrayList<Nodes> list_of_expanded_nodes = new ArrayList<Nodes>();

        while(!obj_priorityQueue.isEmpty())
        {
            int alreadyExpanded;
            Nodes currentnode = obj_priorityQueue.poll();// pop out and store in current node and delete it from pri queue
            expandedNodes.add(currentnode);

			if (Arrays.deepEquals(currentnode.nodeset, goalState)) {
				break;
			}
			list_of_expanded_nodes = currentnode.generateChilds(currentnode); // expands the current node & stores in list

            for(Nodes ls: list_of_expanded_nodes)
            {
                alreadyExpanded = 0;
                for(Nodes ex : expandedNodes)
                {
                    if (Arrays.deepEquals(ls.nodeset, ex.nodeset)) {
						alreadyExpanded = 1;
					}
                }
                if(alreadyExpanded == 1)
                continue;

                //backtrack
                backtrackMap.put(ls, currentnode);
                //Check is puzzle is solvable or not
                if((currentnode.fn-currentnode.gn)>(ls.fn-ls.gn))
                    isPuzzleSolvable=true;

                obj_priorityQueue.add(ls);
            }
            if(isPuzzleSolvable == false){
                return;
            }
            
        }

    }

    
    public static void main( String args[]) {

        
        int n;
        
        System.out.print("Enter size of input matrix for start and goal node: ");
        n = sc.nextInt();
        System.out.println("Note : For A* implementation, you may use \"*\", \"#\" or \"_\"  to represent blank tiles on a board");
        String [][]initialState = new String[n][n];
        System.out.println("Enter start state for puzzle : "); // get the start State
        initialState = get_valid_input(initialState,n);
        System.out.println("Enter Goal state for puzzle : "); // get the goal State
        goalState = get_valid_input(goalState,n);
        is_puzzle_solved(initialState,goalState);
    }

    
    public static void is_puzzle_solved (String[][] initialState, String[][] goalState)
    {
        frontierPathList = new ArrayList<Nodes>();
        obj_priorityQueue = new PriorityQueue<Nodes>();
        expandedNodes = new ArrayList<Nodes>();
        System.out.println("-- The Initial State --");
        printStates(initialState);
        System.out.println("-- The Goal State -- \n");
        printStates(goalState);
        if (isManhattan) System.out.println("Manhattan distance is used to calculate the heuristic and solution to goal path");
        else System.out.println("Miss placed tiles have been used to calculate the heuristic and solution to goal path");
        Nodes node = new Nodes(initialState, 0);
        startState = node;
    }

    public static void printStates(String [][] Node)
    {
        for (int i = 0; i < Node.length; i++) {
			for (int j = 0; j < Node.length; j++) {
				System.out.print(Node[i][j] + "\t");
            }
			System.out.println();
		}
    }
    public static String[][] get_valid_input (String [][]node, int n)
    {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                node[i][j] = sc.nextLine();
                //input entered should be between 1 - 8 and shouldn't be empty and * or mentioned characters should be used to present empty tiles.
                if( node[i][j].length() != 1 || (node[i][j].charAt(0) <'0' && node[i][j].charAt(0) != '*') || node[i][j].charAt(0) > '8' )
                {
                    System.out.println(" --  Please enter suitable input for board game between 1 to 8 -- ");
                    System.out.println("Note : For A* implementation, you may use \"*\", \"#\" or \"_\"  to represent blank tiles on a board");
                    System.out.println("Program Terminating.. Try again!");
                    break;
                }

            }
        }
        return node;
    }
}