package heaps;

import java.util.Comparator;
//2018-5-12，配对堆/偶堆

public class PairingHeap<Key> {
	private Node<Key> root;
	private int size;
	private Node[] A=new Node[5];//原来没有定义Position接口时候，不能初始化该数组！！！！
	private final Comparator<Key> comp;
	
	public interface Position<Key>{
		Key getValue();
	}
	private static class Node<Key> implements Position<Key>{
		Key key;
		Node<Key> child;//左孩子
		Node<Key> sibling;//右兄弟
		Node<Key> prev;
		
		Node(Key key){
			this.key=key;
			this.child=null;
			this.sibling=null;
			this.prev=null;
		}
		public Key getValue(){
			return this.key;
		}
	}
	//*************************构造函数***********************************
	public PairingHeap(Comparator<Key> C) {
		comp = C;
		root=null;
		size=0;
		
	}
	public PairingHeap() {
		comp = new MyComparator();
		root=null;
		size=0;
	}
	public PairingHeap(Key[] a) {
		comp = new MyComparator();
		root=null;
		size=0;
		for (Key k : a) insert(k);
	}
	public PairingHeap(Comparator<Key> C, Key[] a) {
		comp = C;
		root=null;
		size=0;
		for (Key k : a) insert(k);
	}
	
	//************************************************************
	public boolean isEmpty(){
		return root==null;
	}
	public int size(){
		return size;
	}
	public void clear(){
		root=null;
		size=0;
	}
	public Node<Key> insert(Key key){
		Node<Key> newNode=new Node<Key>(key);
		if(root==null){
			root=newNode;
		}else{
			root=merge(root,newNode);
		}
		size++;
		return newNode;
	}
	public Key findMin(){
		if(!isEmpty()){
			return root.key;
		}
		return null;
	}
	public Key delMin(){
		if(!isEmpty()){
			Key x=findMin();
			root.key=null;
			if(root.child==null){
				root=null;
			}else{
				root=combineSiblings(root.child);
			}
			size--;
			return x;
		}
		return null;
	}
	public void decreaseKey(Node<Key> pos,Key newkey){
		if(pos==null){
			//
		}
		Node<Key> p=pos;
		if(p.key==null || greater(newkey,p.key)){
			//
		}
		p.key=newkey;
		if(p!=root){
			if(p.sibling!=null){
				p.sibling.prev=p.prev;
			}
			if(p.prev.child==p){
				p.prev.child=p.sibling;
			}
			else{
				p.prev.sibling=p.sibling;
			}
			p.sibling=null;
			root=merge(root,p);
		}
		
	}
	//*************************************************************
	private Node<Key> merge(Node<Key> x,Node<Key> y){
		if(x==null) return y;
		if(y==null) return x;
		
		if(greater(x.key,y.key)){
			y.prev=x.prev;
			x.prev=y;
			x.sibling=y.child;
			if(x.sibling!=null){
				x.sibling.prev=x;
			}
			y.child=x;
			return y;
		}else{
			y.prev=x;
			x.sibling=y.sibling;
			if(x.sibling!=null){
				x.sibling.prev=x;
			}
			y.sibling=x.child;
			if(y.sibling!=null){
				y.sibling=y;
			}
			x.child=y;
			return x;
		}
	}
	
	//当删除掉最小节点，即删掉根后，应该归并根节点的所有孩子节点，使之成为一颗新的最小树
	private Node<Key> combineSiblings(Node<Key> x){//x为原最小树的最左孩子节点
		if(x.sibling==null){//表示原最小树就这一个孩子
			return x;
		}
	    //用数组A记录根的孩子节点，归并到A[0]
		int numSiblings=0;
		for(;x!=null;numSiblings++){
			A=doubleFull(A,numSiblings);
			A[numSiblings]=x;
			x.prev.sibling=null;
			x=x.sibling.sibling;
		}
		A=doubleFull(A,numSiblings);
		A[numSiblings]=null;
		//自左至右两两归并
		int i=0;
		for(;i+1<numSiblings;i+=2){
			A[i]=merge(A[i],A[i+1]);
		}
		int j=i-2;
		//如果是奇数个孩子节点的情况
		if(j==numSiblings-3){
			A[j]=merge(A[j],A[j+2]);
		}
		//自右至左，最后一趟归并
		for(;j>=2;j-=2){
			A[j-2]=merge(A[j-2],A[j]);
		}
		return (Node<Key>)A[0];
	}
	//************************辅助函数*******************************
	private Node<Key>[] doubleFull(Node<Key>[] a,int idx){
		if( idx == A.length )
        {
            Node [ ] oldArray = A;

           A = new Node[ idx * 2 ];
            for( int i = 0; i < idx; i++ )
                A[ i ] = oldArray[ i ];
        }
        return A;
	}
		//比较两个键值
		private boolean greater(Key n, Key m) {
			if (n == null) return false;
			if (m == null) return true;
			return comp.compare(n,m) > 0;
		}
		
		//************************默认比较器******************************
		private class MyComparator implements Comparator<Key> {
			@Override
			public int compare(Key key1, Key key2) {
				return ((Comparable<Key>) key1).compareTo(key2);
			}
		}
}
