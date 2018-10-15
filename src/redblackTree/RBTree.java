package redblackTree;

import java.util.NoSuchElementException;

//2018-4-10
//来自算法（第四版）
public class RBTree<Key extends Comparable<Key>,Value> {
	private static final boolean RED=true;
	private static final boolean BLACK=false;
	
	private Node root;
	
	class Node{
		private Key key;
		private Value val;
		private Node left,right;
		private boolean color;//指向该节点的链接的颜色
		private int size;
		
		Node(Key key,Value val,boolean color,int size){
			this.key=key;
			this.val=val;
			this.color=color;
			this.size=size;
		}
	}
	
	RBTree(){
		
	}
	public int size(){
		return size(root);
	}
	public boolean isEmpty(){
		return root==null;
	}
	public Value get(Key key){
		if(key==null) throw new IllegalArgumentException("argument to get() is null");
		return get(root,key);
	}
	public boolean contains(Key key){
		return get(key)!=null;
	}
	public void put(Key key,Value val){
		if(key==null) throw new IllegalArgumentException("argument to get() is null");
		if(val==null){
			delete(key);
			return ;
		}
		root=put(root,key,val);
		root.color=BLACK;
	}
	public int height(){
		return height(root);
	}
	public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    } 
	public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).key;
    }
	public void delete(Key key){
		if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(key)) return;

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;
		
	}
	public void deleteMin(){
		if (isEmpty()) throw new NoSuchElementException("BST underflow");
       
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK;
		
	}
	public void deleteMax(){
		if (isEmpty()) throw new NoSuchElementException("BST underflow");

        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMax(root);
        if (!isEmpty()) root.color = BLACK;
		
	}
	//***************************************************************************
	//删除最小节点
	private Node deleteMin(Node h){
		if(h.left==null) return null;
		if(!isRed(h.left)  && !isRed(h.left.left))
			h=moveRedLeft(h);
		h.left=deleteMin(h.left);
		return balance(h);
	}
	//删除最大节点
	private Node deleteMax(Node h) { 
        if (isRed(h.left))
            h = rotateRight(h);

        if (h.right == null)
            return null;

        if (!isRed(h.right) && !isRed(h.right.left))
            h = moveRedRight(h);

        h.right = deleteMax(h.right);

        return balance(h);
    }
	private Node delete(Node h, Key key) { 
        // assert get(h, key) != null;

        if (key.compareTo(h.key) < 0)  {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = delete(h.left, key);
        }
        else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.val = x.val;
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            }
            else h.right = delete(h.right, key);
        }
        return balance(h);
    }
	//返回Key最小的节点
	private Node min(Node x) { 
        // assert x != null;
        if (x.left == null) return x; 
        else return min(x.left); 
    } 
	private Node max(Node x) { 
        // assert x != null;
        if (x.right == null) return x; 
        else   return max(x.right); 
    } 
	private int height(Node x){
		if(x==null) return -1;
		return Integer.max(height(x.left),height(x.right))+1;
		
	}
	private boolean isRed(Node x){
		if(x==null) return false;//指向空节点的链接为为黑色
		return x.color==RED;
	}
	private int size(Node x){
		if(x==null) return 0;
		return x.size;
	}
	private Value get(Node x,Key key){
		while(x!=null){
			int cmp=key.compareTo(x.key);
			if(cmp<0) x=x.left;
			else if(cmp>0) x=x.right;
			else return x.val;
		}
		return null;
	}
	//在以h为根的树中插入元素，并返回新插入的节点
	private Node put(Node h ,Key key,Value val){
		if(h==null) return new Node(key,val,RED,1);
		
		int cmp=key.compareTo(h.key);
		if(cmp<0) h.left=put(h.left,key,val);
		else if(cmp>0) h.right=put(h.right,key,val);
		else h.val=val;//key已经存在，则更新该节点值
		//插入之后从h节点开始调整树结构（因为是递归函数，因此是一个自底向上的过程）
		//旋转操作不改变指向h的链接颜色，因此可能旋转完了扔出现连续红链接的现象，但是由于是递归程序，在返回上一层递归栈后将得到调整
		if(isRed(h.right) && !isRed(h.left)) h=rotateLeft(h);//指向右子节点的链接为红，指向左子节点的链接为黑或为空----左旋转
		if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);//两条连续指向左子节点的链接均为红----右旋转
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);//指向左右孩子节点的链接均为红----颜色翻转
        h.size = size(h.left) + size(h.right) + 1;
        
        return h;
	}
	
	//h节点的左链接为红色，需要将其转为右链接----右旋转
	private Node rotateRight(Node h){
		Node x=h.left;
		h.left=x.right;
		x.right=h;
		x.color=x.right.color;
		x.right.color=RED;
		x.size=h.size;//x变为根，因此其节点数为原来根h的节点数
		h.size=size(h.left)+size(h.right)+1;//h降为x的右子节点，重新计算其节点数
		return x;
	}
	//h节点可能是其父的左子节点或右子节点，对应链接可红可黑
	//h节点的右链接为红色，需要将其转为左链接----左旋转
	private Node rotateLeft(Node h){
		Node x = h.right;
		h.right = x.left;
        x.left = h;
        x.color = x.left.color;//因为x取代h成为新根，故x应该“继承”指向原来h的链接颜色
        x.left.color = RED;//x指向h的链接设为红色，完成了红色右链接变为红色左链接
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
	}
	//翻转某一节点及其子节点的颜色
	private void flipColors(Node h){
		h.color=!h.color;
		h.left.color=!h.left.color;
		h.right.color=!h.right.color;
		
	}
	// Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
	private Node moveRedLeft(Node h){
		if(isRed(h.right.left)){
			h.right=rotateRight(h.right);
			h=rotateLeft(h);
			flipColors(h);
		}
		return h;
	}
	// Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
	private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) { 
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }
	// restore red-black tree invariant
	private Node balance(Node h){
	    if (isRed(h.right))  h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))     flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
	}
}
