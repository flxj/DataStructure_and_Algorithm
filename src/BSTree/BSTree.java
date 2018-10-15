package BSTree;

import java.util.ArrayList;

import javax.management.InstanceNotFoundException;

class BSTNode{
	//public Integer key;
	//public String val;
	public Integer val;
	public BSTNode left;
	public BSTNode right;
	BSTNode(Integer val){
		//this.key=key;
		this.val=val;
		this.left=null;
		this.right=null;
	}
	BSTNode(){
		this.val=null;
		this.right=null;
		this.left=null;
	}
}

public class BSTree {
	public BSTNode root;
	
	BSTree(){
		root=null;
	}
	//共有方法，调用私有隐藏方法
	
	public void insert(Integer x) throws Exception{
		root=insert(root,x);
	}
	
	public void remove(Integer x) throws InstanceNotFoundException{
		root=remove(root,x);
	}
	public void removeMin() throws InstanceNotFoundException{
		root=removeMin(root);
	}
	public void removeMax() throws InstanceNotFoundException{
		root=removeMax(root);
	}
	public Integer findMin(){
		return elementAt(findMin(root));
	}
	public Integer findMax(){
		return elementAt(findMax(root));
	} 
	public Integer find(Integer x){
		return elementAt(find(root,x));
	}
	public void setEmpty(){
		root=null;
	}
	public boolean isEmpty(){
		return root==null;
	}
	//私有方法
	
	private  BSTNode insert(BSTNode t,Integer x) throws Exception{
		if(t==null){
			t=new BSTNode(x);
		}else if(t.val>x){
			t.left=insert(t.left,x);
		}else if(t.val<x){
			t.right=insert(t.right,x);
		}else{
			throw new Exception(x.toString()+"has existed in the tree");
		}
		return t;
	}
	private BSTNode remove(BSTNode t,Integer x) throws InstanceNotFoundException{
		if(t==null){
			throw new InstanceNotFoundException("not found"+x.toString());
		}else if(x<t.val){
			t.left=remove(t.left,x);
		}else if(x>t.val){
			t.right=remove(t.right,x);
		}else if(t.left!=null && t.right!=null){
			t.val=findMin(t.right).val;
			t.right=removeMin(t.right);
		}else{
			t=(t.left!=null)?t.left:t.right;
		}
		return t;
	}
	private Integer elementAt(BSTNode t){
		return t==null?null:t.val;
	}
	private BSTNode find(BSTNode t,Integer x){
		while(t!=null){
			if(t.val==x){
				return t;
			}else if(x<t.val){
				t=t.left;
			}else{
				t=t.right;
			}
		}
		return null;
		/***
		if(t!=null){
			if(t.val==x) return t;
			else if(x<t.val){
				return find(t.left,x);
			}else{
				return find(t.right,x);
			}
		}
		return null;
		***/
	}
	private BSTNode findMin(BSTNode t){
		if(t!=null){
			while(t.left!=null){
				t=t.left;
			}
		}
		return t;
	}
	private BSTNode findMax(BSTNode t){
		if(t!=null){
			while(t.right!=null){
				t=t.right;
			}
		}
		return t;
	}
	private BSTNode removeMin(BSTNode t) throws InstanceNotFoundException{
		if(t==null){
			throw new InstanceNotFoundException();
		}else if(t.left!=null){
			t.left=removeMin(t.left);
			return t;
		}else{
			return t.right;
		}
		
	}
	private BSTNode removeMax(BSTNode t) throws InstanceNotFoundException{
		if(t==null){
			throw  new InstanceNotFoundException();
		}else if(t.right!=null){
			t.right=removeMax(t.right);
			return t;
		}else{
			return t.left;
		}
	}
	//中序输出,只是用于测试
	public ArrayList<Integer> inOrder(BSTNode t){
		ArrayList<Integer> in=new ArrayList<>();
		if(t!=null){
			if(t.left!=null){
				in.addAll(inOrder(t.left));
			}
			in.add(t.val);
			if(t.right!=null){
				in.addAll(inOrder(t.right));
			}
		}
		return in;
		
	}
	
}
