
import java.util.ArrayList;
import java.util.List;

/**
 * This class describes a Node. It holds pointers to any children, parent and silent neighbour
 * of Node.
 * 
 * @author Alexander Chetcuti
 *
 */
public class Node {
	
	//A list of nodes. Since node has a list the children could have a list of children
	private List<Node> children = null;

	private List<Node> neighbours = null;
	
	//parent of node
	private Node parent = null;
	
	//silent neighbour
	private Node silentNeigh = null;
	
	//Value of that particular node.
	private int value;

	//A token buffer used to receive tokens
	private List<Node> tokenBuffer = new ArrayList<Node>();

	private boolean sentTokenToSilentNeigh = false;
	
	private boolean sentDiffusionToken = false;
	
	//ELECTION Specific
	private boolean wakeUpSent = false;
	private int wakeUpReceived = 0;
	private List<Node> wakeUpTokenBuffer = new ArrayList<Node>();
	private int rank = 0;
	private Node victor = this;
	private String state = "sleep"; //(sleep, leader, lost)
	
	/**
	 * Constructor of class node
	 * 
	 * @param val The value to be given to the node
	 */
	public Node(int value) {
		this.children = new ArrayList<>();
		this.value = value;
	}
	
	/**
	 * Adds a node to the to the children of this node, and sets parent of this node.
	 * 
	 * @param Node Is the node to be added to the list.
	 */
	public void addNode(Node node)
	{
		children.add(node);
		node.parent = this;
	}

	/**
	 * Gets the value of this node
	 * 
	 * @return An integer representing the value of the node
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Gets the children of this node.
	 * 
	 * @return returns The children of this node
	 */
	public List<Node> getChildren()
	{
		return this.children;
	}
	
	/**
	 * Gets the list of Neighbours of a node.
	 * 
	 * @return returns A list of nodes that are the neighbours of this node.
	 */
	public List<Node> getNeighbours()
	{
		if (this.neighbours == null) {
			this.neighbours = this.children;
			if (this.parent != null) {
				this.neighbours.add(this.parent);
			}
		}
		return this.neighbours;
	}
	
	/**
	 * Gets an integer array of Neighbours of a node.
	 * 
	 * @return An Integer array of nodes that are the neighbours of this node.
	 */
	public int[] getNeighboursValues()
	{
		List<Node> neighbours = this.getNeighbours();
		int[] neighValues = new int[neighbours.size()];
		int indexCount = 0;
		for (Node node : neighbours) {
			neighValues[indexCount] = node.value;
			indexCount++;
		}
		
		return neighValues;
	}
	
	/**
	 * Gets a string from an the integer array of Neighbours of a node.
	 * 
	 * @return A string of the Integer array of nodes that are the neighbours of this node.
	 */
	public String getNeighboursValuesString()
	{
		return java.util.Arrays.toString(getNeighboursValues());
	}
	
	/**
	 * Set the silent neighbour to a node.
	 * 
	 * @param node The silent neighbour to be set.
	 */
	public void setSilentNeighbour(Node node)
	{
		this.silentNeigh = node;
	}
	
	/**
	 * Get the silent neighbour of a node.
	 * 
	 * @return The silent neighbour of the node.
	 */
	public Node getSilentNeighbour()
	{
		return this.silentNeigh;
	}
	
	/**
	 * Set the sent token to silent neighbour.
	 * 
	 */
	public void setSentTokenToSilentNeigh()
	{
		this.sentTokenToSilentNeigh = true;
	}
	
	/**
	 * Checks if the diffusion token  was sent
	 * 
	 * @return True if diffusion token sent, false if not.
	 */
	public boolean sentDiffusionToken()
	{
		return this.sentDiffusionToken;
	}
	
	/**
	 * Set the sent token to silent neighbour.
	 * 
	 */
	public void setSentDiffusionToken()
	{
		this.sentDiffusionToken = true;
	}
	
	/**
	 * Checks if the token to silent neighbour was sent
	 * 
	 * @return True if token to silent neighbour sent, false if not.
	 */
	public boolean sentTokenToSilentNeigh()
	{
		return this.sentTokenToSilentNeigh;
	}
	
	/**
	 * Get the wake up sent state of this node.
	 * 
	 * @return True if wake up sent, false if wake up not sent.
	 */
	public boolean getWakeUpSent()
	{
		return this.wakeUpSent;
	}
	
	/**
	 * Set the wake up sent state of this node.
	 * 
	 * @param wakeUpSent Boolean to represent the current sate of wake up state token.
	 */
	public void setWakeUpSent(boolean wakeUpSent)
	{
		this.wakeUpSent = wakeUpSent;
	}
	
	/**
	 * Get the count of wake up tokens received.
	 * 
	 * @return The count of wake up tokens.
	 */
	public int getWakeUpReceived()
	{
		return this.wakeUpReceived;
	}
	
	/**
	 * Set the count of wake up tokens received.
	 * 
	 * @param wakeUpReceived The count of wake up tokens.
	 */
	public void setWakeUpReceived(int wakeUpReceived)
	{
		this.wakeUpReceived = wakeUpReceived;
	}
	
	/**
	 * Get the rank of this node.
	 * 
	 * @return The rank of the node.
	 */
	public int getRank()
	{
		return this.rank;
	}
	
	/**
	 * Set the rank of the node.
	 * 
	 * @param rank The rank of the node.
	 */
	public void setRank(int rank)
	{
		this.rank = rank;
	}
	
	/**
	 * Get the victor known to this node.
	 * 
	 * @return The victor node known to this node.
	 */
	public Node getVictor()
	{
		return this.victor;
	}
	
	/**
	 * Set the victor that is known for this node.
	 * 
	 * @param victor The victor known for this node.
	 */
	public void setVictor(Node victor)
	{
		this.victor = victor;
	}
	
	/**
	 * Add a node to to token buffer of this node.
	 * 
	 * @param node The node to be added to the token buffer.
	 */
	public void addTokenBuffer(Node node)
	{
		this.tokenBuffer.add(node);
	}
	
	/**
	 * Gets the current list of nodes in the wake up token buffer.
	 * 
	 * @return The current list of tokens in the wake up buffer.
	 */
	public List<Node> getWakeUpTokenBuffer()
	{
		return this.wakeUpTokenBuffer;
	}
	
	/**
	 * Add a node to to token buffer of this node.
	 * 
	 * @param node The node to be added to the token buffer.
	 */
	public void addWakeUpTokenBuffer(Node node)
	{
		this.wakeUpTokenBuffer.add(node);
	}
	
	/**
	 * Gets the current list of nodes in the token buffer.
	 * 
	 * @return The current list of tokens in the buffer.
	 */
	public List<Node> getTokenBuffer()
	{
		return this.tokenBuffer;
	}

	/**
	 * Empty the token buffer for next use.
	 */
	public void resetTokenBuffer()
	{
		this.tokenBuffer = new ArrayList<Node>();
	}
	
	/**
	 * Sets the state for this node;
	 * 
	 * @param state The state to be set for this node.
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * Gets the state of this node.
	 * 
	 * @return The String result of the state of this node.
	 */
	public String getState() {
		return this.state;
	}
 }
