package Tree;

import java.util.ArrayList;
import java.util.Stack;

public class BinaryTreeUtil {
	//根据广义表建立二叉树
	public static BTreeNode createBTree(String tree){
		BTreeNode root=new BTreeNode();
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
				stk.pop();
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
		return root;
	}
	//先序遍历非递归
	public static ArrayList<Integer> preOrder(BTreeNode root){
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
	//中序遍历非递归
	public static ArrayList<Integer> inOrder(BTreeNode root){
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
	//后序遍历非递归
	public static ArrayList<Integer> postOrder(BTreeNode root){
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
	
	//
	//
	//
	//
	

}
