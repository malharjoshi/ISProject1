import java.util.ArrayList;

//create nodes and calculate heuristics

public class Nodes implements Comparable<Nodes>
{
    public int fn; //Total costFunction;
    public String[][] nodeset;
    public int gn; // cost function from root node to current node = level number

    public Nodes(String startState[][], int levelCost)
    {
        int n = startState.length;
        this.nodeset = new String[n][n];

        nodeset = copyArray(nodeset,startState);
        this.gn = levelCost;

        this.fn= SolveEightPuzzle.isManhattan ? (calculate_distance(SolveEightPuzzle.isManhattan) + gn):(calculate_distance(SolveEightPuzzle.isManhattan) + gn);     
        
    }
    public static String[][] copyArray (String array1_copyTo[][], String array2_copyFrom[][]) {
        for (int i=0;i< array1_copyTo.length;i++)
		{
			for (int j=0; j< array1_copyTo.length; j++)
			{
				array1_copyTo[i][j] = array2_copyFrom[i][j];
			}
		}
        return array1_copyTo;
    }
    private int calculate_distance(Boolean isManhattanHeuristic) //calculate heuristic 
    {
        int cost = 0;
        int []index_of_current_tile = new int[2];
        int n = SolveEightPuzzle.goalState.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!this.nodeset[i][j].trim().isEmpty())
                {
                    index_of_current_tile = get_index_from_goalState(n,Integer.parseInt(this.nodeset[i][j]));
                    if(isManhattanHeuristic)
                    {
                        // current tile pos in start state = i,j and pos of same tile in goal state = index_of_current_tile[0,1]
                        // Manhattan distance between two points = abs(x1-x2)+abs(y1-y2)
                        cost += ( Math.abs(i-index_of_current_tile[0]) + Math.abs(j - index_of_current_tile[1]) );
                    }
                    else
                    {
                        //if tile postion in goal state doesn't matches with current state just increment the tile cost by 1
                        if(index_of_current_tile[0]!=i || index_of_current_tile[1]!=j)
                        cost +=1;
                    }
                }
            }
        }

        return cost;
    }
    //method to find corresponding indices of a given element in the goal state and return them via array
    private int [] get_index_from_goalState(int n,Integer element)
    {
        int []index_of_goal_tile = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(!SolveEightPuzzle.goalState[i][j].trim().isEmpty())
                {
                    if(SolveEightPuzzle.goalState[i][j].trim().equals(String.valueOf(element)))
                    {
                        index_of_goal_tile[0] = i;
                        index_of_goal_tile[1] = j;
                        return index_of_goal_tile;
                    }
                }
            }
        }
        return index_of_goal_tile;
    }

    public ArrayList<Nodes> generateChilds(Nodes parent)
    {
        ArrayList<Nodes> nextNode = new ArrayList<Nodes>();
        int n = this.nodeset.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(parent.nodeset[i][j].trim().isEmpty())//search for an empty tile in parent node
                {
                    if(i-1>=0) //check for top position
                    {
                        Nodes changedNode = shift_tile(i,j,i-1,j,n,parent);
                        nextNode.add(changedNode);
                    }
                    if(j-1>=0)//check for left position
                    {
                        Nodes changedNode = shift_tile(i,j,i,j-1,n,parent);
                        nextNode.add(changedNode);
                    }
                    if(i+1<n)//check for down position
                    {
                        Nodes changedNode = shift_tile(i,j,i+1,j,n,parent);
                        nextNode.add(changedNode);
                    }
                    if(j+1<n)//check for right position
                    {
                        Nodes changedNode = shift_tile(i,j,i,j+1,n,parent);
                        nextNode.add(changedNode);
                    }
                }
            }
        }

        return nextNode;
    }
    public Nodes shift_tile(int row_1,int col_1, int row_2, int col_2, int n, Nodes parent)
    { 
        String temp[][] = new String[n][n];

        temp = copyArray(temp, parent.nodeset);

        temp= swap_emptySpace(temp,row_1,col_1,row_2,col_2);
        
        return new Nodes(temp,parent.gn+1);
    }
    public String[][] swap_emptySpace(String [][]arr, int row_1,int col_1,int row_2,int col_2)
    {
       //swap space with number 
        String tmp = arr[row_1][col_1];
        arr[row_1][col_1] = arr[row_2][col_2];
        arr[row_2][col_2] = tmp;
        return arr;
    }

    //Below function provide the sorting technique for the priority queue created in Solution class
	@Override
	public int compareTo(Nodes o) {
		if(this.fn==o.fn)
		{
			return ((SolveEightPuzzle.isManhattan==true)?(this.calculate_distance(true) - o.calculate_distance(true)):(this.calculate_distance(false)) - o.calculate_distance(false));
		}
		return this.fn-o.fn;
	}

}