
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    public static List<Nodes> backtrackList;

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
                backtrackMap.put(ls,currentnode);
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



        Scanner sc = new Scanner(System.in);
        System.out.println("\n Please Enter the space  for the blanksquare. Add input nextline");

        String[][] start = new String[3][3];
        String[][] goal = new String[3][3];
			 
			 
			 
			 //To get start node input
        System.out.println(
                "\n**********************************************************************");
        System.out.println("\n Please Enter the Start node");
        for (int i=0;i<start.length;i++)
        {
            for(int  j=0;j<start.length;j++)
            {
                start[i][j] = sc.nextLine();
                if(start[i][j].length()!=1 || (start[i][j].charAt(0)<'0' && start[i][j].charAt(0)!=' ') || start[i][j].charAt(0)>'8')
                {
                    System.out.println("Error: Input should be any number between 1 to 8 or a single space\nProgram Terminated");
                    return;
                }
            }
        }
			 
			 
        //Get Goal node input
        System.out.println("\n Please Enter the  Goal node");
        for (int i=0;i<goal.length;i++)
        {
            for(int j=0;j<goal.length;j++)
            {
                goal[i][j] = sc.nextLine();
                if(goal[i][j].length()!=1 || (goal[i][j].charAt(0)<'0' && goal[i][j].charAt(0)!=' ') || goal[i][j].charAt(0)>'8')
                {
                    System.out.println("Error: Input should be any number between 1 to 8 or a single space\nProgram Terminated");
                    return;
                }
            }
        }
			 			 
		 
        for(int m=0;m<2;m++)
        {
            isManhattan=(m==0)?true:false;
            is_puzzle_solved(start, goal);
        }

    }

    
    public static void is_puzzle_solved (String[][] initialState, String[][] goalState1)
    {
        backtrackMap=new HashMap<Nodes, Nodes>();
        backtrackList=new ArrayList<Nodes>();
        frontierPathList = new ArrayList<Nodes>();
        obj_priorityQueue = new PriorityQueue<Nodes>();
        expandedNodes = new ArrayList<Nodes>();
        System.out.println("\n-- The Initial State -- \n");
        printStates(initialState);
        System.out.println("-- The Goal State -- \n");
        printStates(goalState1);
        if (isManhattan) System.out.println("\nManhattan distance is used to calculate the heuristic and solution to goal path");
        else System.out.println("\nMiss placed tiles have been used to calculate the heuristic and solution to goal path");
        goalState = goalState1;
        Nodes node = new Nodes(initialState, 0);
        startState = node;

        new SolveEightPuzzle(startState);

        //track parents
        getPathToParent(expandedNodes.get(expandedNodes.size()-1));
        for (int i2 = backtrackList.size() - 1; i2 >= 0; i2--)
				printNodes(backtrackList.get(i2));

        if(isPuzzleSolvable)
        System.out.println("Total Cost to reach goal is : "
                + (expandedNodes.size() > 0 ? expandedNodes.get(expandedNodes.size() - 1).fn : 0));
        else
            System.out.println("The puzzle is not solvable. Incomplete");
			
        System.out.println("Total Nodes expandednodes :" + expandedNodes.size());
        System.out.println("Total Nodes generated:" + (expandedNodes.size() + obj_priorityQueue.size()));
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


    //track the parent
	private static void getPathToParent(Nodes childnode) {

		backtrackList.add(childnode);
		if (childnode != startState)
            getPathToParent(backtrackMap.get(childnode));

	}

    public static void printNodes(Nodes node) {
		System.out.println("*******************************************************************");
		for (int l = 0; l < 3; l++) {
			for (int m = 0; m < 3; m++) {
				System.out.print(node.nodeset[l][m] + "\t");
			}
			System.out.println();
		}
		System.out.println("f(n) :" + node.fn);
		System.out.println("h(n) :" + (node.fn - node.gn));
		System.out.println("g(n) :" + (node.gn));
		System.out.println('\n');

	}
}