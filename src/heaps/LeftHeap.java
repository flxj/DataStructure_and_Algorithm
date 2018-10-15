package heaps;
//2018-5-10
public class LeftHeap<T extends Comparable<T>,V> {
	
	private Node<T,V> root;//���ڵ�
	
	class Node<T extends Comparable<T>,V>{
		private T key;
		private V val;
		private int npl;//��·������--->һ���ڵ㵽һ��"����Ĳ����ڵ�"��·�����ȡ������ڵ���ָ�øýڵ�����Һ�����������һ��ΪNULL��Ҷ�ڵ��NPLΪ0��NULL�ڵ��NPLΪ-1
		private Node<T,V> left,right;
		Node(T key,V val,Node<T,V> left,Node<T,V> right){
			this.key=key;
			this.val=val;
			this.left=left;
			this.right=right;
			this.npl=0;
		}
	}
	LeftHeap(){
		this.root=null;
	}
	
	public void insert(T key,V val){
		Node<T,V> t=new Node(key,val,null,null);
		if(t!=null){
			insert(t);
		}
	}
	public T remove(){
		if(this.root==null) return null;
		T k=this.root.key;
		Node<T,V> left=this.root.left;
		Node<T,V> right=this.root.right;
		
		this.root=null;
		this.root=merge(left,right);
		return k;
	}
	public void clear(){
		destory(this.root);
		this.root=null;
	}
	public void merge(LeftHeap<T,V> other){
		this.root=merge(this.root,other.root);
	}
	
	private Node<T,V> merge(Node<T,V> x,Node<T,V> y){
		if(x==null) return y;
		if(y==null) return x;
		//�ϲ��������ս�x�ڵ���Ϊ�ϲ���ĸ������Ҫ��֤xΪС�ڵ㣬�ʱ�ҪʱҪ����x,y�����ڵ�
		if(x.key.compareTo(y.key)>0){
			Node<T,V> tmp=x;
			x=y;
			y=tmp;
		}
		//��x���Һ�����y�ϲ�
		x.right=merge(x.right,y);
		//���x�����ӽڵ�nplС�����ӽڵ㣬��Ҫ������������
		if(x.left==null || x.left.npl<x.right.npl){
			Node<T,V> tmp=x.left;
			x.left=x.right;
			x.right=tmp;
		}
		//���������������x�ڵ��nplֵ
		if(x.right==null || x.left==null) x.npl=0;
		else{
			x.npl=(x.left.npl>x.right.npl)?(x.right.npl+1):(x.left.npl+1);
		}
		return x;
	}
	private void insert(Node<T,V> n){
		this.root=merge(this.root,n);
	}
	private void destory(Node<T,V> x){
		if(x==null) return ;
		if(x.left!=null) destory(x.left);
		if(x.right!=null) destory(x.right);
		x=null;
		
	}

}
