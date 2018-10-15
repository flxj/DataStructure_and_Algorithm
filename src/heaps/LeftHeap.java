package heaps;
//2018-5-10
public class LeftHeap<T extends Comparable<T>,V> {
	
	private Node<T,V> root;//根节点
	
	class Node<T extends Comparable<T>,V>{
		private T key;
		private V val;
		private int npl;//零路径长度--->一个节点到一个"最近的不满节点"的路径长度。不满节点是指该该节点的左右孩子至少有有一个为NULL。叶节点的NPL为0，NULL节点的NPL为-1
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
		//合并程序最终将x节点作为合并后的根，因此要保证x为小节点，故必要时要交换x,y所引节点
		if(x.key.compareTo(y.key)>0){
			Node<T,V> tmp=x;
			x=y;
			y=tmp;
		}
		//将x的右孩子与y合并
		x.right=merge(x.right,y);
		//如果x的左子节点npl小于右子节点，需要交换两个子树
		if(x.left==null || x.left.npl<x.right.npl){
			Node<T,V> tmp=x.left;
			x.left=x.right;
			x.right=tmp;
		}
		//交换完后重新设置x节点的npl值
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
