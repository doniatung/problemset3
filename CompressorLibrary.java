import java.io.*;
import java.util.*;


public class CompressorLibrary {

	/**
	 * Given a file with pathName, compress the file into a collection of bit codes and write to a new file 
	 * 
	 * 
	 * @param pathName		path of the file to be compressed
	 * @throws IOException	thrown if the given file does not exist or is empty
	 */
	public static void writeCFile(String pathName) throws IOException{
		//builds code map
		HashMap<Character, String> codeMap = makeCodeMap(makeHuffTree(makeCharTree(freqTableGen(pathName))), "");
		//creates new file 
		BufferedBitWriter newFile = new BufferedBitWriter("compressed" + pathName);
		//reads in original file 
		BufferedReader origFile = new BufferedReader(new FileReader(pathName));
		char c;
		//while there are still characters in the original file, do the following 
		do {
			c =  (char)origFile.read();
			//get the bit code for the given character 
			String code = codeMap.get(c);
			//for each bit in the code, write to the new file 
			for(int i = 0; i < code.length(); i++) {
				newFile.writeBit(bitToBool(code.charAt(i)));
			}
		} while (c != -1);
		//close files 
		newFile.close();
		origFile.close(); 
	}
	
	/**
	 * Given a file with pathName, decompress the file by reading through the bit codes and finding the corresponding characters 
	 * 
	 * @param pathName
	 * @throws IOException
	 */
	public static void readCFile(String pathName) throws IOException{
		//create the huffington tree that enables a tracking of codes
		BinaryTree<TreeData> huffTree = makeHuffTree(makeCharTree(freqTableGen(pathName)));
		//read in the original file 
		BufferedBitReader origFile = new BufferedBitReader("compressed" + pathName);
		//create new file to write into 
		BufferedWriter newFile = new BufferedWriter( new FileWriter("decompressed" + pathName));
		//instantiate temporary variable to track where the code is in the tree 
		BinaryTree<TreeData> temp = huffTree;
		//while there are more bits in the file, check if the given node is a leaf. If it is, write the key of the given node to the file
		//if not, check if the bit is a '0' or '1', go left or right corresponding to the value, and continue to track down the tree
		while (origFile.hasNext()) {
			if(!temp.isLeaf()) {
				boolean bit = origFile.readBit();
			  	int i = boolToBit(bit);
			  	if (i == 0) { 
				  temp = temp.getLeft();
			  	}
			  	else {
				  temp.getRight();
			  	}
			}else {
				newFile.write(temp.getData().getKey());
				temp = huffTree;
			}
		}
		//close files
		newFile.close();
		origFile.close();
		
	}
	
	/**
	 * Creates a frequency table using hashmap implementation to keep track of how many times each character is within a file
	 * 
	 * @param pathName		path of the given file
	 * @return				returns the HashMap where Keys are characters and Values are the number of times of occurance
	 * @throws IOException	if the file is empty or does not exist 
	 */
	 public static HashMap<Character, Integer> freqTableGen(String pathName) throws IOException{
		 HashMap<Character, Integer> charMap = new HashMap<Character, Integer>();
		 BufferedReader input;
		 //Checks if the file exists and contains characters
		 try {
		 input = new BufferedReader(new FileReader(pathName));
		 }
		 catch(IOException e) {
			 throw new IOException("No inputted file");
		 }
		 if (input.read() == -1 ) {
			 throw new IOException("File is Empty");
		 }
		 //while there are more characters in the file, check if the character is already a key in the HashMap
		 //if it is, then add one to the value, if it's not, then add the Key with a value of 1 to the HashMap 
		 int c = input.read();
		 while (c != -1) {
			char key = (char)c;
			if (charMap.containsKey(key)) {
				charMap.replace(key, charMap.get(key), 1 + charMap.get(key));
			}
			 else {
				 charMap.put(key, 1);
			 }
			c = input.read();
		 }
		 //closes inputed file 
		 input.close(); 
		 return charMap;
	 } 
	 
	 
	 /**
	  * Given a HashMap with character frequencies, create a PriorityQueue with the given keys and values stored as Tree Nodes  
	  * 
	  * @param frequencyMap		HashMap with keys of chars and values of frequency
	  * @return					returns a PriorityQueue with frequencyMap data stored in nodes in a Binary Tree 
	  */
	 public static PriorityQueue<BinaryTree<TreeData>> makeCharTree(HashMap<Character, Integer> frequencyMap) {
		
		 PriorityQueue<BinaryTree<TreeData>> q = new PriorityQueue<BinaryTree<TreeData>>(new TreeComparator<Object>());
		 //creates an array of the keys in the frequency map 
		 Set<Character> kSet = frequencyMap.keySet();
		 //for each key in the map, create a new tree node with the key and value stored
		 //then add that individual tree node to the priorityqueue
		 //for (int i = 0; i < keyArray.length; i ++){
		 for(char c : kSet) {	
		 	TreeData td = new TreeData(c, frequencyMap.get(c));
			 q.add(new BinaryTree<TreeData>(td));
		 }
		 System.out.println(q);
		 return q;
	 }
	 
	 
	 /**
	  * Given a PriorityQueue holding BinaryTree nodes, create a Huffman tree given the specs defined on the website (cs.dartmouth.edu)
	  * 
	  * @param origTree		PriorityQueue of individual tree nodes
	  * @return				returns a Huffman BinaryTree containing tree nodes storing key and value 
	  */
	 public static BinaryTree<TreeData> makeHuffTree(PriorityQueue<BinaryTree<TreeData>> origTree){
		 //Loop through to each element within the given priorityqueue
		 while(origTree.size() > 1) {
			 //create a new tree with children of the two smallest values in the priorityqueue
			 //set the data of that tree node to the sum of their two values
			 //add this new tree to the tree priorityqueue
			 BinaryTree<TreeData> tree = new BinaryTree<TreeData>(new TreeData(' ', 0), origTree.poll(), origTree.poll());
			 System.out.println(origTree);
			 System.out.println(tree.getLeft());
			 System.out.println(tree.getRight());
			 tree.data.setValue(tree.getLeft().data.getValue()+tree.getRight().data.getValue());
			 origTree.add(tree);
		 }
		 return origTree.poll();
	 }
	 
	 
	 /**
	  * Recursive function, creates a HashMap with the '0' and '1' composed bit codes for each character
	  * 
	  * @param tree
	  * @param code
	  * @return
	  */
	 public static HashMap<Character, String> makeCodeMap(BinaryTree<TreeData> tree, String code){
		 //loops down the code, for each child traveled to, adds either a '0' or '1' depending on direction of path
		 HashMap<Character, String> codeMap = new HashMap<Character,String>();
		 if(tree.hasLeft()) makeCodeMap(tree.getLeft(), code + "0");
		 if(tree.hasRight()) makeCodeMap(tree.getRight(), code + "1");
		//if a leaf node, adds the code to HashMap storing keys of char and values of bit codes
		 if(!tree.hasLeft() && !tree.hasRight()) codeMap.put(tree.data.getKey(), code);
		 
		 return codeMap;
	 }
	 
	 
	 //helper methods below
	 /**
	  * Returns boolean value for either '0' or '1' 
	  * 
	  * @param c	character (read from file) 
	  * @return		the boolean equivalent of the given char 
	  */
	 private static boolean bitToBool(char c) {
		 if (c == '0') return false;
		 return true;
	 }
	 
	 /**
	  * Returns '0' or '1' for a given boolean value 
	  * 
	  * @param b	boolean value (read from file) 
	  * @return		the integer equivalent of inputed boolean 
	  */
	 private static int boolToBit(boolean b) {
		 if (b == true) return 1;
		 return 0;
	 }
	 
}