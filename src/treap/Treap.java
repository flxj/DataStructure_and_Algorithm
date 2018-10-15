package treap;
//import java.util.Random;

public class Treap<K extends Comparable<K>,V> {
	
	private class Node{
		K key;
		V val;
		int priority;
		Node left,right;
		public Node(K key,V val,int priority){
			this.key=key;
			this.priority=priority;
			this.val=val;
			this.left=this.right=null;
		}
	}
	private Node root;
	
	//
	public Treap(){
		root=null;
	}
	public void insert(K key,V val){
		insert(root,key,val);
	}
	public void remove(K key){
		delete(root,key);
	}
	//
	
	//
	private void leftRotate(Node x){
		Node y=x.right;
		x.right=y.left;
		y.left=x;
		x=y;
	}
	private void rightRotate(Node x){
		Node y=x.left;
		x.left=y.right;
		y.right=x;
		x=y;
	}
	private int rand(){
		return (int)(1+Math.random()*1000000000);
	}
	private void insert(Node x,K key,V val){
		if(x==null){
			x=new Node(key,val,rand());
		}else if(key.compareTo(x.key)<=0){
			insert(x.left,key,val);
			if(x.left.priority>x.priority){
				rightRotate(x);
			}
		}else{
			insert(x.right,key,val);
			if(x.right.priority<x.priority){
				leftRotate(x);
			}
		}
	}
	private void delete(Node x,K key){
		if(key.compareTo(x.key)==0){
			if(x.left==null || x.right==null){
				if(x.right==null){
					x=x.left;
				}else{
					x=x.right;
				}
			}else{
				if(x.left.priority<x.right.priority){
					rightRotate(x);
					delete(x.right,key);
				}else{
					leftRotate(x);
					delete(x.left,key);
				}
			}
		}
		else if(key.compareTo(x.key)>0){
			delete(x.right,key);
		}else{
			delete(x.left,key);
		}
	}

}
