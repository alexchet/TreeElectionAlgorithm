import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Election {

	//A static final integer to multiply the number of iterations the algorithm with run.
	private static final int ITERATION_MULTIPLIER = 100;
	
	public Election() { }
	
	public static void algorithm(List<Node> nodesArr)
	{
		Helpers.printTree(nodesArr);
		
		System.out.println("");
		System.out.println("---------- Algorithm Actions -----------");
		
		//Number of processes in the simulated tree
		int size = nodesArr.size();
		
		//Random number
		Random rand = new Random();
		
		//initialise matrix. 0 = Not a neighbour, 1 = Neighbour, 2 = Received token
		int[][] tokenMatrix = new int[size][size];
		tokenMatrix = Helpers.setNeighboursInMatrix(tokenMatrix, nodesArr);
		
		//Set Initiator
		int initiatorIndex = rand.nextInt(size);
		Node initiator = nodesArr.get(initiatorIndex);
		System.out.println("Initiator is set to Node " + initiatorIndex);
		System.out.println("");
		
		//Set order of priority
		String sOrderPriorities = "";
		System.out.println("Order of priority");
        for (Node node : nodesArr) {
        	node.setRank(rand.nextInt(size - 1));
        	if (sOrderPriorities != "") sOrderPriorities += ", ";
        	sOrderPriorities += "Node " + node.getValue() + " (" + node.getRank() + ")";
        }
        System.out.println("[" + sOrderPriorities + "]");
		System.out.println("");
		
		//Counting Iterations
		int iCount = 0;
		
		//Iterate for 100 * N
		for (int k = 0; k < ITERATION_MULTIPLIER * size; k++) {
			
			//Choosing R <= N nodes randomly
			int countNodes = rand.nextInt(size) + 1;
			
			//Array holds information of which node has already executed the algorithm in this process.
			int[] nodesAlreadyExecuted = new int[countNodes];
			Arrays.fill(nodesAlreadyExecuted, -1);
			
			//Iterate one for every node to be chosen
			for (int i = 0; i < countNodes; i++) 
			{
				//boolean for action taken
				boolean actionTaken = false;
				
				//Selecting a node that hasn't already run the algorithm in this Iteration of i
				int process = Helpers.getNextInteger(nodesAlreadyExecuted, rand.nextInt(size), size);
				nodesAlreadyExecuted[i] = process;
				
				Node node = nodesArr.get(process);
				
				//send wake up ONLY FOR INITIATOR
				if (initiator.getValue() == node.getValue() && !node.getWakeUpSent()) {
					node.setWakeUpSent(true);
					
					for (int neighbourIndex : node.getNeighboursValues()) {
						//send <wake up>
						Node neighbour = nodesArr.get(neighbourIndex);
						neighbour.addWakeUpTokenBuffer(node);
						System.out.println(process + " sent wake up token to Neighbour " + neighbourIndex);
						actionTaken = true;
					}
				}

				//Receive and send wake up 
				Iterator<Node> wakeUpTokenBuffer = node.getWakeUpTokenBuffer().iterator();
				while (wakeUpTokenBuffer.hasNext()) {
					node.setWakeUpReceived(node.getWakeUpReceived() + 1);
					Node wakeUpReceived = wakeUpTokenBuffer.next();
					wakeUpTokenBuffer.remove();
					System.out.println(process + " received wake up token from Neighbour " + wakeUpReceived.getValue());
					actionTaken = true;
					
					if (!node.getWakeUpSent()) {
						node.setWakeUpSent(true);
						
						for (int neighbourIndex : node.getNeighboursValues()) {
							//send <wake up>
							Node neighbour = nodesArr.get(neighbourIndex);
							neighbour.addWakeUpTokenBuffer(node);
							System.out.println(process + " sent wake up token to Neighbour " + neighbourIndex);
							actionTaken = true;
						}
					}
				}

				if (node.getWakeUpReceived() == node.getNeighbours().size()) {
					Iterator<Node> tokenBuffer = node.getTokenBuffer().iterator();
					
					//if neigh = 1, then we set silent neigh as that neigh, (either we identified a leaf)
					if (node.getNeighbours().size() == 1) {
						node.setSilentNeighbour(nodesArr.get(node.getNeighbours().get(0).getValue()));
					}
					
					//Get the row of current node from matrix to receive any incoming messages and checking if its the last 
					if (Helpers.countPendingTokens(tokenMatrix[process]) > 0) {
						while (tokenBuffer.hasNext() && node.getSilentNeighbour() == null) {
							Node tokenReceived = tokenBuffer.next();
							tokenMatrix[process][tokenReceived.getValue()] = 2;
			
							System.out.println(process + " received token from neighbour " + tokenReceived.getValue());
							actionTaken = true;
							
							//check for silent neighbour
							int silentNeighValue = Helpers.getSilentNeighbour(tokenMatrix[process]);
							if (silentNeighValue > -1) {
								Node silentNeigh = nodesArr.get(silentNeighValue);
								node.setSilentNeighbour(silentNeigh);
							}
							
							//Update Victor, Checking node rank then default to value of node
							if (tokenReceived.getVictor().getRank() < node.getVictor().getRank() ||
									(tokenReceived.getVictor().getRank() == node.getVictor().getRank() &&
									tokenReceived.getVictor().getValue() < node.getVictor().getValue())) {
								node.setVictor(tokenReceived.getVictor());
								System.out.println(process + " updated it's victor to " + node.getVictor().getValue());
								actionTaken = true;
							}
							
							tokenBuffer.remove();
						}
						
						//Check if current node has a silent neighbour to continue algorithm else no action is taken
						if (node.getSilentNeighbour() != null) {
							Node silentNeigh = nodesArr.get(node.getSilentNeighbour().getValue());
							if (!node.sentTokenToSilentNeigh()) { //send once
								node.setSentTokenToSilentNeigh();
								silentNeigh.addTokenBuffer(node);
								System.out.println(process + " sent token to silent neighbour " + silentNeigh.getValue());
								actionTaken = true;
							}
							
							//If silent neighbour sent a token back receive the token and decide
							if (tokenBuffer.hasNext()) //Silent neighbour sent a token
							{
								Node tokenReceived = tokenBuffer.next();
								if (tokenReceived.equals(node.getSilentNeighbour())) {
									tokenMatrix[process][tokenReceived.getValue()] = 2;
									tokenBuffer.remove();
									System.out.println(process + " received token from silent neighbour " + node.getSilentNeighbour().getValue());
									
									//Update Victor, Checking node rank then default to value of node
									if (tokenReceived.getVictor().getRank() < node.getVictor().getRank() ||
											(tokenReceived.getVictor().getRank() == node.getVictor().getRank() &&
											tokenReceived.getVictor().getValue() < node.getVictor().getValue())) {
										node.setVictor(tokenReceived.getVictor());
										System.out.println(process + " updated it's victor to " + node.getVictor().getValue());
									}
									
									System.out.println(process + " decided node " + node.getVictor().getValue() + " is leader.");
									
									if (node.getVictor().getValue() == node.getValue()) {
										node.setState("Leader");
									} else {
										node.setState("Lost");
									}

									//Diffusion part
									if (!node.sentDiffusionToken()) {
										node.setSentDiffusionToken();
										for (Node nNeigh : node.getNeighbours()) {
											if (nNeigh.getValue() != node.getSilentNeighbour().getValue()) {
												nNeigh.addTokenBuffer(node);
												System.out.println(process + " sent decide token to neighbour " + nNeigh.getValue());
											}
										}
									}
									actionTaken = true;
								}
							}
						}
					}
				}
				
				if (actionTaken) {
					iCount++;
					System.out.println("");
					System.out.println("-------- Round " + iCount + " just finished ---------");
					System.out.println("");
				}
			}
		}
		
		//Final outcome
		String sStates = "";
		System.out.println("");
		System.out.println("Final outcome");
        for (Node node : nodesArr) {
        	node.setRank(rand.nextInt(size - 1));
        	if (sStates != "") sStates += ", ";
        	sStates += "Node " + node.getValue() + " (" + node.getState() + ")";
        }
        System.out.println("[" + sStates + "]");
		System.out.println("");
		
		//Print matrix result for confirmation purposes.
		Helpers.printMatrix(tokenMatrix);
	}

}
