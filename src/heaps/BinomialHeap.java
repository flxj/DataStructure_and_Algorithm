package heaps;
//2018-5-10
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinomialHeap<Key> implements Iterable<Key> {
	private Node head;//根节点列表的头结点
	private final Comparator<Key> comp;
	
	private class Node{
		Key key;
		int order;
		Node child,sibling;       //孩子与兄弟
	}
	//***************************构造函数************************************
	public BinomialHeap(){
		comp=new MyComparator();
	}
	public BinomialHeap(Comparator<Key> c){
		comp=c;
	}
	public BinomialHeap(Key[] a){
		comp=new MyComparator();
		for(Key k:a) insert(k);
	}
	public BinomialHeap(Comparator<Key> c,Key[] a){
		comp=c;
		for(Key k:a) insert(k);
	}
	//*****************************常规操作***********************************
	public boolean isEmpty(){
		return head==null;
	}
	//java.lang.ArithmeticException if there are more than 2^63-1 elements in the queue
	public int size(){
		int res=0,tmp;
		for(Node node=head;node!=null;node=node.sibling){
			if(node.order>30){
				throw new ArithmeticException("The number of elements cannot be evaluated, but the priority queue is still valid.");
			}
			tmp=1<<node.order;
			res|=tmp;
		}
		return res;
		
	}
	public void insert(Key key){
		Node x=new Node();
		x.key=key;
		x.order=0;
		BinomialHeap<Key> h=new BinomialHeap<Key>();
		h.head=x;
		this.head=this.union(h).head;
	}
	public Key minKey(){
		if(isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		Node min=head;
		Node cur=head;
		while(cur.sibling!=null){
			min=(greater(min.key,cur.sibling.key))?cur:min;
			cur=cur.sibling;
		}
		return min.key;
	}
	public Key removeMin(){
		if(isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		Node min=eraseMin();
		Node x=(min.child==null)?min:min.child;
		if(min.child!=null){
			min.child=null;
			Node pre=null,nex=x.sibling;
			while(nex!=null){
				x.sibling=pre;
				pre=x;
				x=nex;
				nex=nex.sibling;
			}
			x.sibling=pre;
			BinomialHeap<Key> h=new BinomialHeap<Key>();
			h.head=x;
			head=union(h).head;
		}
		return min.key;
	}
	//Merges two Binomial heaps together
	public BinomialHeap<Key> union(BinomialHeap<Key> heap){
		if(heap==null) throw new IllegalArgumentException("Cannot merge a Binomial Heap with null");
		this.head=merge(new Node(),this.head,heap.head).sibling;
		Node x=this.head;
		Node pre=null,nex=x.sibling;
		while(nex!=null){
			if(x.order<nex.order || (nex.sibling!=null && nex.sibling.order==x.order)){
				pre=x;
				x=nex;
			}else if(greater(nex.key,x.key)){
				x.sibling=nex.sibling;
				link(nex,x);
			}else{
				if(pre==null){
					this.head=nex;
				}else{
					pre.sibling=nex;
					link(x,nex);
					x=nex;
				}
			}
			nex=x.sibling;
		}
		return this;
	}
	//*************************辅助函数***********************************
	private boolean greater(Key n,Key m){
		if(n==null) return false;
		if(m==null) return false;
		return comp.compare(n, m)>0;
	}
	//Assuming root1 holds a greater key than root2, root2 becomes the new root
	private void link(Node root1,Node root2){
		root1.sibling=root2.child;
		root2.child=root1;
		root2.order++;
	}
	//Deletes and return the node containing the minimum key
	private Node eraseMin(){
		Node min=head;
		Node pre=null;
		Node cur=head;
		while(cur.sibling!=null){
			if(greater(min.key,cur.sibling.key)){
				pre=cur;
				min=cur.sibling;
			}
			cur=cur.sibling;
		}
		pre.sibling=min.sibling;
		if(min==head) head=min.sibling;
		return min;
	}
	//Merges two root lists into one, there can be up to 2 Binomial Trees of same order
	private Node merge(Node h,Node x,Node y){
		if(x==null && y==null) return h;
		else if (x==null) h.sibling=merge(y,null,y.sibling);
		else if(y==null) h.sibling=merge(x,x.sibling,null);
		else if(x.order<y.order) h.sibling=merge(x,x.sibling,y);
		else h.sibling=merge(y,x,y.sibling);
		return h;
	}
	//*******************迭代器************************
	public Iterator<Key> iterator(){
		return new MyIterator();
	}
	private class MyIterator implements Iterator<Key>{
		BinomialHeap<Key> data;
		public MyIterator(){
			data=new BinomialHeap<Key>(comp);
			data.head=clone(head,null);
		}
		private Node clone(Node x,Node parent){
			if(x==null) return null;
			Node node=new Node();
			node.key=x.key;
			node.sibling=clone(x.sibling,parent);
			node.child=clone(x.child,node);
			return node;
		}
		public boolean hasNext(){
			return !data.isEmpty();
		}
		public Key next(){
			if(!hasNext()) throw new NoSuchElementException();
			return data.removeMin();
		}
		public void remove(){
			throw new UnsupportedOperationException();
		}
	}
	//**************************默认比较器*************************************
	private class MyComparator implements Comparator<Key>{
		public int compare(Key key1,Key key2){
			return ((Comparable<Key>) key1).compareTo(key2);
		}
	}

}
