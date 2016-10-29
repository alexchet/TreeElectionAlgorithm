
import java.util.List;
import java.util.Random;

/**
 * A class that holds helper methods which are need in varies parts 
 * of the whole program. This serves as a place to keep these methods
 * in one location
 * 
 * @author Alexander Chetcuti
 *
 */
public abstract class Helpers {

	/**
	 * Constructor of class Helpers.
	 */
	public Helpers() { }

	/**
	 * From a list of Nodes (which have already been ordered in a tree) indexes are traced,
	 * and a token matrix is constructed to show which nodes are the neighbours of every
	 * other node.
	 * 
	 * @param tokenMatrix The actual matrix that will hold the information of neighbours.
	 * @param nodesArr The array of nodes that are already ordered in a tree.
	 * @return A token matrix with some of the bits updated to show neighbours of nodes.
	 */
	public static int[][] setNeighboursInMatrix(int[][] tokenMatrix, List<Node> nodesArr) {
		int sentTokensSize = tokenMatrix.length;
		int recieveTokensSize = tokenMatrix[0].length;
		
		for(int i = 0; i < sentTokensSize; i++) {
			Node node = nodesArr.get(i);
			int[] nodeNeigh = node.getNeighboursValues();
			for (int j = 0; j < recieveTokensSize; j++) {
				if (find(nodeNeigh, j)) {
					tokenMatrix[i][j] = 1;
				}
			}
		}
		
		return tokenMatrix;
	}
	
	/**
	 * Gets the silent neighbour of the index the passed row has in the token matrix. Ex:
	 * Array of token with from index 2 is passed. Method will return silent neighbour of Node 2.
	 * 
	 * @param tokens The token array of the current index that needs the silent neighbour
	 * @return The index of the silent neighbour
	 */
	public static int getSilentNeighbour(int[] tokens) {
		int countSilentNiegh = 0;
		int lastNeighFound = -1;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i] == 1) {
				countSilentNiegh++;
				lastNeighFound = i;
			}
		}
		if (countSilentNiegh == 1) {
			return lastNeighFound;
		}
		
		return -1;
	}
	
	public static int countPendingTokens(int[] tokens) {
		int count = 0;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i] == 1) {
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Gets the count of tokens being received for the current node.
	 * 
	 * @param tokens The token array of the current index that needs the count of
	 * tokens being received.
	 * 
	 * @return The count of tokens being received by the current node.
	 */
	public static int countReceiving(int[] tokens, int sleepToken, int sentToken) {
		int countReceiving = 0;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i] == sleepToken || tokens[i] == sentToken) {
				countReceiving++;
			}
		}
		
		return countReceiving;
	}
	
	/**
	 * Prints the result of the token matrix passed as a parameter.
	 * 
	 * @param tokenMatrix The token matrix that needs to be printed.
	 */
	public static void printMatrix(int[][] tokenMatrix)
	{
		System.out.println("");
		System.out.println("---------- Token Matrix -----------");
		int sentTokensSize = tokenMatrix.length;
		int recieveTokensSize = tokenMatrix[0].length;

		System.out.println("Legend: 0 = Not a neighbour, 1 = Neighbour, 2 = Received token");
		System.out.print("    ");
		
		for (int i = 0; i < sentTokensSize; i++) {
			System.out.print(i + " ");
		}
		System.out.println("");
		System.out.print("    ");
		for (int i = 0; i < sentTokensSize; i++) {
			System.out.print("_ ");
		}
		System.out.println("");
		for (int i = 0; i < sentTokensSize; i++) {
			String printBuilder = i + "  |";
			for (int j = 0; j < recieveTokensSize; j++) {
				printBuilder += tokenMatrix[i][j] + " ";
			}
			System.out.println(printBuilder);
		}
	}
	
	/**
	 * Prints the neighbour node values of all nodes in a node array.
	 * 
	 * @param nodesArr The Node array that contains nodes in a tree format.
	 */
	public static void printTree(List<Node> nodesArr)
	{
		System.out.println("");
		System.out.println("------------ Tree Structure ------------");
		for (Node node : nodesArr)
		{
			int currentIndex = nodesArr.indexOf(node);
			String neighbours = node.getNeighboursValuesString();
			
			System.out.println(currentIndex + " -> " + neighbours);
		}
	}
	
	/**
	 * A recursive method that randomly gets the next integer to be chosen. The
	 * Integers that have already been chosen are stored in an array of indexes to 
	 * keep track of what integers have already been chosen.
	 * 
	 * @param arr The array of integers already chosen.
	 * @param node The chosen integer.
	 * @param size The size of the main pool from which the node can be chosen.
	 * @return The index of the next integer chosen.
	 */
	public static int getNextInteger(int[] arr, int current, int size)
	{	
		if (find(arr, current)) {
			Random rand = new Random();
			int nextNode = rand.nextInt(size);
			return getNextInteger(arr, nextNode, size);
		} else {
			return current;
		}
	}
	
	/**
	 * Finds a value in an integer array.
	 * 
	 * @param array The array to be searched.
	 * @param value The value to be found.
	 * @return True if this value is found. False if the value has not been found.
	 */
	private static boolean find(int[] array, int value)
	{
		for(int i=0; i<array.length; i++) 
	         if(array[i] == value)
	             return true;
		return false;
	}
}
