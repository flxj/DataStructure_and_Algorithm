package heaps;
//б�ѣ�2018-5-11
import java.util.Comparator;

public class SkewHeap<Key> {
	private Node root;
	private final Comparator<Key> comp;
	
	private class Node{
		Key key;
		Node left,right;
		Node(Key key,Node left,Node right){
			this.key=key;
			this.left=left;
			this.right=right;
		}
	}
	//**************************���캯��*****************************
	public SkewHeap(Comparator<Key> C) {
		comp = C;
	}
	public SkewHeap() {
		comp = new MyComparator();
	}
	public SkewHeap(Key[] a) {
		comp = new MyComparator();
		for (Key k : a) insert(k);
	}
	public SkewHeap(Comparator<Key> C, Key[] a) {
		comp = C;
		for (Key k : a) insert(k);
	}
	//********************************************************
	public void insert(Key key){
		this.root=merge(this.root,new Node(key,null,null));
	}
	public void merge(SkewHeap<Key> that){
		this.root=merge(this.root,that.root);
	}
	public Key delMin(){
		if(root==null){
			return null;
		}
		Key key=root.key;
		Node left=root.left;
		Node right=root.right;
		root=null;
		root=merge(left,right);
		return key;
	}
	//**********************************************************
	private Node merge(Node x,Node y){
		if(x==null) return y;
		if(y==null) return x;
		
		if(greater(x.key,y.key)){
			Node tmp=x;
			x=y;
			y=tmp;
		}
		// ��x���Һ��Ӻ�y�ϲ���
	    // �ϲ���ֱ�ӽ���x�����Һ��ӣ�������Ҫ�������һ���������ǵ�npl��
	    Node tmp = merge(x.right, y);
	    x.right = x.left;
	    x.left = tmp;
	    return x;
	}
	
	//************************��������*******************************
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
