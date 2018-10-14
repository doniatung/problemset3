import java.io.*;
import java.util.*;


public class Compressor {

	BufferedBitReader file; 
	
	// TO DO: 
	//read in characters from text file
	//look up each character's code word in the code map
	//write the sequence of 0's and 1's in that code word as bits to an output file 
	
	public Compressor(String pathName) throws IOException{
		file = new BufferedBitReader(pathName);
		while(file.hasNext()) {
			//add to the bitMap 
		}
	}
	
	
	
	
	
	
	 public static HashMap<Character, Integer> freqTableGen(String pathName) throws IOException{
		 HashMap<Character, Integer> charMap = new HashMap<Character, Integer>();
		 BufferedReader input;
		 try {
		 input = new BufferedReader(new FileReader(pathName));
		 }
		 catch(IOException e) {
			 throw new IOException("No inputted file");
		 }
		 
		 if (input.read() == -1 ) {
			 throw new IOException("File is Empty");
		 }
		 while (input.read() != -1) {
			char key = (char)input.read();
			if (charMap.containsKey(key)) {
				charMap.replace(key, charMap.get(key), 1 + charMap.get(key));
			}
			 else {
				 charMap.put(key, 1);
			 }
		 }
		 return charMap;
	 } 
	 
	 public static PriorityQueue<BinaryTree<TreeData>> makeCharTree(HashMap<Character, Integer> frequencyMap) {
		 PriorityQueue<BinaryTree<TreeData>> q = new PriorityQueue<BinaryTree<TreeData>>();
		 Object[] keyArray = frequencyMap.keySet().toArray();
		 for (int i = 0; i < keyArray.length; i ++){
			 TreeData td = new TreeData((Character)keyArray[i], frequencyMap.get(keyArray[i]));
			 q.add(new BinaryTree<TreeData>(td));
		 }
		 return q;
	 }
	 
	 public static BinaryTree<TreeData> makeHuffTree(PriorityQueue<BinaryTree<TreeData>> origTree){
		 while(origTree.size() > 1) {
			 BinaryTree<TreeData> tree = new BinaryTree<TreeData>(null, origTree.poll(), origTree.poll()); 
			 tree.data.setValue(tree.getLeft().data.getValue()+tree.getRight().data.getValue());
			 origTree.add(tree);
		 }
		 return origTree.poll();
	 }
	 
	 public static HashMap<Character, String> makeCodeMap(BinaryTree<TreeData> tree, String code){
		 HashMap<Character, String> codeMap = new HashMap<Character,String>();
		 if(tree.hasLeft()) makeCodeMap(tree.getLeft(), code + "0");
		 if(tree.hasRight()) makeCodeMap(tree.getRight(), code + "1");
		 if(!tree.hasLeft() && !tree.hasRight()) codeMap.put(tree.data.getKey(), code);
		 
		 return codeMap;
	 }
	 
	 
 }
	