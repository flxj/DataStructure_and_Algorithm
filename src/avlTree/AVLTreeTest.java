package avlTree;

import java.util.ArrayList;
import java.util.Random;

public class AVLTreeTest {
	public static void main(String[] args){
		//AVLTreeNode<Integer,Integer> avl=new AVLTreeNode<Integer,Integer>(100,100);
		//System.out.println(avl.height);
		
		//ArrayList<Integer> keys=new ArrayList<Integer>();
		//ArrayList<String> vals=new ArrayList<String>();
		AVLTree<Integer,String> avl=new AVLTree<Integer,String>();
		
		Random r=new Random();
		for(int i=0;i<50;++i){
			avl.insert(r.nextInt(1000)+1, r.nextInt()+"");
		}
		System.out.println(avl.height());
		System.out.println(avl.findMax().data.key);
		avl.inOrder();
		avl.remove(avl.findMax().data.key);
		System.out.println(avl.findMax().data.key);
		
	}

}
