package Tree;

public class TreeTest {
	public static void main(String[] args){
		//String test1="4(3(1,2),7(6(5,),8))";
		String test2="1(2(4(,8),5),3(6(,9),7))";
		//BTreeNode test_tree=BinaryTreeUtil.createBTree(test2);
		//System.out.println(BinaryTreeUtil.preOrder(test_tree));
		BinaryTree t=new BinaryTree(test2);
		System.out.println(t.postOrder());
		
	}

}
