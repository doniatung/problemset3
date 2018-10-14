import java.util.Comparator;

public class TreeComparator<E> implements Comparator<E>{

	@Override
	public int compare(E o1, E o2) {
		BinaryTree<TreeData> t1 = (BinaryTree<TreeData>) o1;
		BinaryTree<TreeData> t2 = (BinaryTree<TreeData>) o2;
		
		if(t1.data.getValue() == t2.data.getValue()) return 0;
		else if (t1.data.getValue() < t2.data.getValue()) return 1;
		else return -1;
	}

	
}
