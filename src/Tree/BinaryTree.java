package Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

class BTreeNode{
	public BTreeNode left;
	public BTreeNode right;
	public Integer val;
	BTreeNode(){
	}
	BTreeNode(Integer val){
		this.val=val;
		this.left=null;
		this.right=null;
	}
	BTreeNode(Integer val,BTreeNode l,BTreeNode r){
		this.val=val;
		this.left=l;
		this.right=r;
	}
	
	//get,set方法
	public Integer getElement(){
		return this.val;
	}
	public BTreeNode getLeft(){
		return this.left;
	}
	public BTreeNode getRight(){
		return this.right;
	}
	public void setElement(Integer val){
		this.val=val;
	}
	public void setLeft(BTreeNode t){
		this.left=t;
	}
	public void setRight(BTreeNode t){
		this.right=t;
	}
	//计算树的节点个数，高度
	public static Integer size(BTreeNode root){
		if(root==null) return 0;
		return size(root.left)+size(root.right)+1;
	}
	public static Integer height(BTreeNode root){
		if(root==null) return 0;
		return Integer.max(height(root.left),height(root.right))+1;
	}
	
	//返回二叉树先序遍历列表
	public ArrayList<Integer> preOrder(){
		ArrayList<Integer> pre=new ArrayList<>();
		pre.add(val);
		if(left!=null){
		    pre.addAll(left.preOrder());
		 }
		if(right!=null){
			pre.addAll(right.preOrder());
		}
		return pre;	
		
    }
	//返回二叉树中序遍历列表
	public ArrayList<Integer> inOrder(){
		ArrayList<Integer> in=new ArrayList<>();
		if(left!=null){
			in.addAll(left.inOrder());
		}
		in.add(val);
		if(right!=null){
			in.addAll(right.inOrder());
		}
		return in;
	}	
	//返回二叉树后序遍历列表
	public ArrayList<Integer> postOrder(){
		ArrayList<Integer> post=new ArrayList<>();	
		if(left!=null){
			post.addAll(left.postOrder());
		}
		if(right!=null){
			post.addAll(right.postOrder());
		}
		post.add(val);	
		return post;
			
	}
	//返回二叉树层次遍历列表
	public ArrayList<Integer> levelOrder(){
		ArrayList<Integer> level=new ArrayList<>();
		Queue<BTreeNode> q=new LinkedList<BTreeNode>();
		q.offer(this);	
		BTreeNode p=null;
		while(!q.isEmpty()||q.size()!=0){
			p=q.poll();
			level.add(p.val);
			if(p.left!=null){
				q.offer(p.left);
			}
			if(p.right!=null){
				q.offer(p.right);
			}	
		}
		return level;
			
	}
	
}	

public class BinaryTree {
	public BTreeNode root;
	
	//构造空树
	BinaryTree(){root=null;};
	//
	BinaryTree(Integer val){
		
	}
	//由二叉树的广义表表示构造树
	BinaryTree(String tree){
		if(tree.length()==0){
			root=null;
		}
		Stack<BTreeNode> stk=new Stack<BTreeNode>();
		BTreeNode p=new BTreeNode();
		BTreeNode t=new BTreeNode();
		int k=0;
		for(int i=0;i<tree.length();i++){
			switch(tree.charAt(i)){
			case '(':
				stk.push(p);
				k=1;
				break;
			case ')':
				t=stk.pop();
				break;
			case ',':
				k=2;
				break;
			default:
				p=new BTreeNode(tree.charAt(i)-'0');
				if(root==null){
					root=p;
				}else if(k==1){
					t=stk.lastElement();
					t.setLeft(p);
				}else{
					t=stk.lastElement();
					t.setRight(p);
				}
			}
			
		}
		
		
	}
	//返回二叉树先序遍历列表
	public ArrayList<Integer> preOrder(){
		/***
		if(root==null) return null;
		return root.preOrder();
		***/
		ArrayList<Integer> pre=new ArrayList<>();
		Stack<BTreeNode> stk=new Stack<>();
		if(root!=null){
			stk.push(root);
		}
		while(!stk.empty()){
			BTreeNode p=stk.pop();
			pre.add(p.val);
			if(p.right!=null){
				stk.push(p.right);
			}
			if(p.left!=null){
				stk.push(p.left);
			}
		}
		return pre;
		
	}
	//返回二叉树中序遍历列表
	public ArrayList<Integer> inOrder(){
		/***
		if(root==null) return null;
		return root.inOrder();
		***/
		ArrayList<Integer> in = new ArrayList<>();
		Stack<BTreeNode> stk=new Stack<>();
		BTreeNode p=root;
		while(!stk.empty()||p!=null){
			while(p!=null){
				stk.push(p);
				p=p.left;
			}
			if(!stk.empty()){
				p=stk.pop();
				in.add(p.val);
			}
			p=p.right;
		}
		return in;
		
	}
	//返回二叉树后序遍历列表
	public ArrayList<Integer> postOrder(){
		/***
		if(root==null) return null;
		return root.postOrder();
		***/
		ArrayList<Integer> post=new ArrayList<>();
		Stack<BTreeNode> stk=new Stack<>();
		BTreeNode cur=null;
		BTreeNode pre=null;
		if(root!=null){
			stk.push(root);
		}
		while(!stk.empty()){
			cur=stk.lastElement();
			if((cur.left==null && cur.right==null)||(pre!=null && (pre==cur.left || pre==cur.right))){
				post.add(cur.val);
				stk.pop();pre=cur;
			}
			else{
				if(cur.right!=null){
					stk.push(cur.right);
				}
				if(cur.left!=null){
					stk.push(cur.left);
				}
			}
		}
		return post;
		
	}
	//返回二叉树层次遍历列表
	public ArrayList<Integer> levelOrder(){
		if(root==null) return null;
		return root.levelOrder();
		
	}
	//
	//
}
