package BSTree;

public class BSTreeTest {
	public static void main(String[] args) throws Exception{
		int[] test={6,9,14,23,0,8,7,4};
		BSTree bst=new BSTree();
		for(int i=0;i<test.length;i++){
			bst.insert(test[i]);
		}
		System.out.println(bst.inOrder(bst.root));
	}

}
