public class Main {
	public static void main(String[] args) {
		System.out.println("****************************************");
		System.out.println("*** CSC8103 - Distributed Algorithms ***");
		System.out.println("****************************************");
		System.out.println("");

		System.out.println("****************************************");
		System.out.println("******* Tree Election Algorithm ********");
		System.out.println("****************************************");
		
		System.out.println("");
		System.out.println("1: Balanced Tree");
		Tree tree1 = new Tree();
		tree1.balancedTree();
    	Election.algorithm(tree1.getNodesArr());

		System.out.println("");
		System.out.println("****************************************");
		
		System.out.println("");
		System.out.println("2: Unbalanced Tree");
		Tree tree2 = new Tree();
		tree2.unbalancedTree();
		Election.algorithm(tree2.getNodesArr());

		System.out.println("");
		System.out.println("****************************************");
		System.out.println("");
		
		System.out.println("");
		System.out.println("3: Arbitrary Tree");
		Tree tree3 = new Tree();
		tree3.arbitraryTree();
		Election.algorithm(tree3.getNodesArr());
	}

}
