package lazyBalanceTrees;

import java.util.ArrayList;

/*
 * A binary search tree is said to be weight-balanced if half the nodes are on the left of the root, 
 * and half on the right. An α-weight-balanced node is defined as meeting a relaxed weight balance criterion:
 * size(left) ≤ α*size(node)
 * size(right) ≤ α*size(node)
 * 
 * A binary search tree that is α-weight-balanced must also be α-height-balanced, that is :
 * height(tree) ≤ ⌊log1/α(size(tree))⌋
 * 
 * Scapegoat trees are not guaranteed to keep α-weight-balance at all times,
 *  but are always loosely α-height-balanced in that: height(scapegoat tree) ≤ ⌊log1/α(size(tree))⌋ + 1.
 */
public class ScapegoatTree<K extends Comparable<K>,V> {
	private static final double threshold=0.75;
	private static final double log23=2.4663034623764317;
	private class Node{
		K key;
		V val;
		boolean del;            //用于表示该节点是否被删除
		Node parent;            //父节点
		Node left,right;        //左右子节点
		public Node(K k,V v){
			this.key=k;
			this.val=v;
			this.parent=this.left=this.right=null;
			this.del=false;
		}
		public Node(K k,V v,Node p,Node l, Node r){
			this.key=k;
			this.val=v;
			this.parent=p;
			this.left=l;
			this.right=r;
			this.del=false;
		}
		public String toString(){
			return "{"+this.key+":"+this.val+"}";
		}
	}
	
	private Node root;
	private int nodeCount=0;
	private int realNodeCount=0;
	//构造函数
	public ScapegoatTree(){ this.root=null;}
	public ScapegoatTree(K key,V val){
		this.root=new Node(key,val,null,null,null);
		this.nodeCount+=1;
		//this.realNodeCount+=1;
	}
	//支持操作
	public boolean insert(K key,V val){
		Node x=new Node(key,val);
		int h=insertNode(x);
		if(h>log32(nodeCount)){
			Node p=x.parent;
			while(3*size(p)<=2*size(p.parent)){
				p=p.parent;
			}
			rebuildTree(p.parent);
		}
		return h>=0;	
	}
	public V remove(K key){
		Node x=removeNode(root,key);
		return x!=null ? x.val:null;
	}
	public V find(K key){
		if(findNode(key)!=null){return findNode(key).val;}
		return null;
	}
	public int size(){
		return realNodeCount;
	}
	//
	private int insertNode(Node x){//插入一个节点，并且返回该节点的深度
		Node w=root;
		if(w==null){
			root=x;
			nodeCount+=1;
			//realNodeCount+=1;
			return 0;
		}
		boolean done=false;
		int d=0;
		do{
			if(x.key.compareTo(w.key)<0){
				if(w.left==null){
					w.left=x;
					x.parent=w;
					done=true;
				}else{
					w=w.left;
				}
			}else if(x.key.compareTo(w.key)>0){
				if(w.right==null){
					w.right=x;
					x.parent=w;
					done=true;
				}else{
					w=w.right;
				}
			}else{
				return -1;
			}
			d+=1;	
		}while(!done);
		nodeCount+=1;
		//realNodeCount+=1;
		return d;
	}
	private Node findNode(K key){
		return null;
	}
	
	private int size(Node x){
			return x!=null? size(x.left)+size(x.right)+1:0;
	}
	private void rebuildTree(Node x){
		Node p=x.parent;
		ArrayList<Node> a=new ArrayList<Node>();
		sortedNodes(x,a);
		if(p==null){
			root=buildTreeFromArray(a,0,a.size());
			root.parent=null;
		}else if(p.right==x){
			p.right=buildTreeFromArray(a,0,a.size());
			p.right.parent=x;
		}else{
			p.left=buildTreeFromArray(a,0,a.size());
			p.left.parent=x;	
		}
	}
	private void sortedNodes(Node x,ArrayList<Node> a){
		if(x!=null){
			if(x.left!=null){sortedNodes(x.left,a);}
			if(!x.del) a.add(x);
			else{
				nodeCount-=1;
				//realNodeCount-=1;
			}
			if(x.right!=null){sortedNodes(x.right,a);}
		}
		else{
			return;
		}
	}
	private Node buildTreeFromArray(ArrayList<Node> a,int i,int n){//节点数组，起始下标，节点个数
		if(n==0) return null;
		int half=n/2;
		a.get(i+half).left=buildTreeFromArray(a,i,half);
		if(a.get(i+half).left!=null){
			a.get(i+half).left.parent=a.get(i+half);
		}
		a.get(i+half).right=buildTreeFromArray(a,i+half+1,n-half-1);
		if(a.get(i+half).right!=null){
			a.get(i+half).right.parent=a.get(i+half);
		}
		return a.get(i+half);
	}
	private Node removeNode(Node x,K key){
		if(x!=null){
			if(key.compareTo(x.key)<0){
				return removeNode(x.left,key);
			}else if(key.compareTo(x.key)>0){
				return removeNode(x.right,key);
			}else{
				if(!x.del){
					x.del=true;
					//realNodeCount-=1;
				}
				return x;
			}
		}
		return null;
	}
	
	
	private int log32(int n){
		return (int)(Math.ceil(log23*Math.log(n)));
	}
	private int logalpha(int n){
		return (int)(Math.ceil(Math.log(1.0/threshold)*Math.log(n)));	
	}
	
	
	
	

}
