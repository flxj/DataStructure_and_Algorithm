package heaps;

import java.util.Comparator;
//2018-5-12����Զ�/ż��

public class PairingHeap<Key> {
	private Node<Key> root;
	private int size;
	private Node[] A=new Node[5];//ԭ��û�ж���Position�ӿ�ʱ�򣬲��ܳ�ʼ�������飡������
	private final Comparator<Key> comp;
	
	public interface Position<Key>{
		Key getValue();
	}
	private static class Node<Key> implements Position<Key>{
		Key key;
		Node<Key> child;//����
		Node<Key> sibling;//���ֵ�
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
	//*************************���캯��***********************************
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
	
	//��ɾ������С�ڵ㣬��ɾ������Ӧ�ù鲢���ڵ�����к��ӽڵ㣬ʹ֮��Ϊһ���µ���С��
	private Node<Key> combineSiblings(Node<Key> x){//xΪԭ��С���������ӽڵ�
		if(x.sibling==null){//��ʾԭ��С������һ������
			return x;
		}
	    //������A��¼���ĺ��ӽڵ㣬�鲢��A[0]
		int numSiblings=0;
		for(;x!=null;numSiblings++){
			A=doubleFull(A,numSiblings);
			A[numSiblings]=x;
			x.prev.sibling=null;
			x=x.sibling.sibling;
		}
		A=doubleFull(A,numSiblings);
		A[numSiblings]=null;
		//�������������鲢
		int i=0;
		for(;i+1<numSiblings;i+=2){
			A[i]=merge(A[i],A[i+1]);
		}
		int j=i-2;
		//��������������ӽڵ�����
		if(j==numSiblings-3){
			A[j]=merge(A[j],A[j+2]);
		}
		//�����������һ�˹鲢
		for(;j>=2;j-=2){
			A[j-2]=merge(A[j-2],A[j]);
		}
		return (Node<Key>)A[0];
	}
	//************************��������*******************************
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
		//�Ƚ�������ֵ
		private boolean greater(Key n, Key m) {
			if (n == null) return false;
			if (m == null) return true;
			return comp.compare(n,m) > 0;
		}
		
		//************************Ĭ�ϱȽ���******************************
		private class MyComparator implements Comparator<Key> {
			@Override
			public int compare(Key key1, Key key2) {
				return ((Comparable<Key>) key1).compareTo(key2);
			}
		}
}
