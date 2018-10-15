package splayTree;


import java.util.Comparator;

//2018-5-12
public class SplayTree<Key> {
	private Node root;
	private Node nullNode;
	private Node newNode=null;
	private Node header=new Node();
	private final Comparator<Key> comp;
	
	private class Node{
		Key key;
		Node left,right;
		Node(){
			this.key=null;
			this.left=null;
			this.right=null;
		}
		Node(Key key){
			this.key=key;
			this.left=null;
			this.right=null;
		}
		
	}
	//**************************构造函数*******************************
	public SplayTree(Comparator<Key> C) {
		comp = C;
		nullNode=new Node();
		nullNode.left=nullNode.right=nullNode;
		root=nullNode;
	}
	public SplayTree() {
		comp = new MyComparator();
		nullNode=new Node();
		nullNode.left=nullNode.right=nullNode;
		root=nullNode;
	}
	public SplayTree(Key[] a) {
		comp = new MyComparator();
		for (Key k : a) insert(k);
	}
	public SplayTree(Comparator<Key> C, Key[] a) {
		comp = C;
		for (Key k : a) insert(k);
	}
	//****************************常规操作******************************
	public void clear(){
		root=nullNode;
	}
	public boolean isEmpty(){
		return root==nullNode;
	}
	//
	public void insert(Key key){
		if(newNode==null){
			newNode=new Node(key);
		}
		if(root==nullNode){
			newNode.left=newNode.right=nullNode;
			root=newNode;
		}else{
			//以新插入的key执行伸展操作
			root=splay(key,root);
			if(greater(root.key,key)){
				newNode.left=root.left;
				newNode.right=root;
				root.left=nullNode;
				root=newNode;
			}else if(greater(key,root.key)){
				newNode.right=root.right;
				newNode.left=root;
				root.right=nullNode;
				root=newNode;
				
			}else{
				//若伸展操作完成，伸展树的根节点key值与带插入节点相同，说明原来树中该key存在，重复了
			}
		}
		newNode=null;	//将newNode设为null，使得下次插入操作时new一个新的
	}
	public void remove(Key key){
		Node newTree;
		root=splay(key,root);
		if(!equel(root.key,key)){
			//没找到待删除Key
		}
		if(root==nullNode) newTree=root.left;
		else{
			//寻找左子树中的最大节点，伸展至根
			newTree=root.left;
			newTree=splay(key,newTree);
			newTree.right=root.right;
		}
		root=newTree;
	}
	public Key find(Key key){
		root=splay(key,root);
		if(isEmpty() || !equel(root.key,key)){
			return null;
		}
		return root.key;
	}
	public Key findMin( )
    {
        if( isEmpty( ) )
            return null;
        Node ptr = root;
        while( ptr.left != nullNode )
            ptr = ptr.left;

        root = splay( ptr.key, root );
        return ptr.key;
    }
	public Key findMax( )
    {
        if( isEmpty( ) )
            return null;
        Node ptr = root;
        while( ptr.right != nullNode )
            ptr = ptr.right;
        root = splay( ptr.key, root );
        return ptr.key;
    }
	//***********************自顶向下伸展***********************
	//当沿着树向下搜索某一节点X时，将途径的节点及其子树从原树中移除，我们要维护三棵树L,X,R.
	//当前节点X是其子树的根，L用于存储小于X的节点，R用于存储大于X的节点。初始时X为伸展树的根，L和R为空。
	//在向下搜索的过程中，不断通过旋转将路径上的节点依次提升为中间树的根，将大于/小于的节点加入L/R中
	//最终待搜索节点X将成为中间树的根，然后将L和R附加到中间树底部，结果就是X成为了整个伸展树的根，伸展操作完成。
	private Node splay(Key key,Node t){
		Node leftTreeMax,rightTreeMin;
		header.left=header.right=nullNode;
		leftTreeMax=rightTreeMin=header;
		nullNode.key=key;
		while(true){
			if(greater(t.key,key)){//根据key与当前中间树根节点key的大小关系决定向下的搜索路径
				if(greater(t.left.key,key)){//搜索路径上当前节点，当前节点的孩子与孙子节点之间的关系决定了旋转操作的类型
					t=rotateWithLeftChild(t);
				}	
			    if(t.left==nullNode)
			    	break;
				rightTreeMin.left=t;
				rightTreeMin=t;
				t=t.left;
			}else if(greater(key,t.key)){
				if(greater(key,t.right.key)){
					t=rotateWithRightChild(t);
				}
				if(t.right==nullNode)
					break;
				leftTreeMax.right=t;
				leftTreeMax=t;
				t=t.right;
			}else{
				break;
			}
		}
		leftTreeMax.right=t.left;
		rightTreeMin.left=t.right;
		t.left=header.right;
		t.right=header.left;
		return t;
	}
	//**************************旋转操作*****************************
	//将k2的左孩子节点k1作为新根返回，k2将为k1的右孩子
    private  Node rotateWithLeftChild( Node k2 )
    {
        Node k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        return k1;
    }
    //将k1的右孩子节点k2作为新根，k1将为k2的左孩子
    private  Node rotateWithRightChild( Node k1 )
    {
        Node k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        return k2;
    }
    //k3的左孩子节点的右孩子将成为新根
    private  Node doubleRotateWithLeftChild( Node k3 )
    {
        k3.left = rotateWithRightChild( k3.left );
        return rotateWithLeftChild( k3 );
    }
    //k1的右孩子节点的左孩子将成为新根
    private  Node doubleRotateWithRightChild( Node k1 )
    {
        k1.right = rotateWithLeftChild( k1.right );
        return rotateWithRightChild( k1 );
    }
	//************************辅助函数*******************************
	//比较两个键值
	private boolean greater(Key n, Key m) {
		if (n == null) return false;
		if (m == null) return true;
		return comp.compare(n,m) > 0;
	}
	private boolean equel(Key n,Key m){
		return comp.compare(n, m)==0;
	}
	//************************默认比较器******************************
	private class MyComparator implements Comparator<Key> {
		@Override
		public int compare(Key key1, Key key2) {
			return ((Comparable<Key>) key1).compareTo(key2);
		}
	}

}
