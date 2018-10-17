import java.io.*;
import java.util.*;


public class Compressor {

	BufferedBitReader file; 
	
	// TO DO: 
	//read in characters from text file
	//look up each character's code word in the code map
	//write the sequence of 0's and 1's in that code word as bits to an output file 
	
	public static void writeCFile(String pathName) throws IOException{
		HashMap<Character, String> codeMap = makeCodeMap(makeHuffTree(makeCharTree(freqTableGen(pathName))), "");
		BufferedBitWriter newFile = new BufferedBitWriter("compressed" + pathName);
		BufferedReader origFile = new BufferedReader(new FileReader(pathName));
		char c;
		do {
			c =  (char)origFile.read();
			String code = codeMap.get(c);
			for(int i = 0; i < code.length(); i++) {
				newFile.writeBit(bitToBool(code.charAt(i)));
			}
		} while (c != -1);
		newFile.close();
		origFile.close(); 
	}
	
	public static void readCFile(String pathName) throws IOException{
		BinaryTree<TreeData> huffTree = makeHuffTree(makeCharTree(freqTableGen(pathName)));
		BufferedBitReader origFile = new BufferedBitReader("compressed" + pathName);
		BufferedWriter newFile = new BufferedWriter( new FileWriter("decompressed" + pathName));
		
		BinaryTree<TreeData> temp = huffTree;
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
		newFile.close();
		origFile.close();
		
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
		 input.close(); 
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
	 
	 
	 //helper methods below
	 
	 private static boolean bitToBool(char c) {
		 if (c == '0') return false;
		 return true;
	 }
	 
	 private static int boolToBit(boolean b) {
		 if (b == true) return 1;
		 return 0;
	 }
	 
}
	