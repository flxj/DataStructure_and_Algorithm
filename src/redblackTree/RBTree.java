package redblackTree;

import java.util.NoSuchElementException;

//2018-4-10
//�����㷨�����İ棩
public class RBTree<Key extends Comparable<Key>,Value> {
	private static final boolean RED=true;
	private static final boolean BLACK=false;
	
	private Node root;
	
	class Node{
		private Key key;
		private Value val;
		private Node left,right;
		private boolean color;//ָ��ýڵ�����ӵ���ɫ
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
	//ɾ����С�ڵ�
	private Node deleteMin(Node h){
		if(h.left==null) return null;
		if(!isRed(h.left)  && !isRed(h.left.left))
			h=moveRedLeft(h);
		h.left=deleteMin(h.left);
		return balance(h);
	}
	//ɾ�����ڵ�
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
	//����Key��С�Ľڵ�
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
		if(x==null) return false;//ָ��սڵ������ΪΪ��ɫ
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
	//����hΪ�������в���Ԫ�أ��������²���Ľڵ�
	private Node put(Node h ,Key key,Value val){
		if(h==null) return new Node(key,val,RED,1);
		
		int cmp=key.compareTo(h.key);
		if(cmp<0) h.left=put(h.left,key,val);
		else if(cmp>0) h.right=put(h.right,key,val);
		else h.val=val;//key�Ѿ����ڣ�����¸ýڵ�ֵ
		//����֮���h�ڵ㿪ʼ�������ṹ����Ϊ�ǵݹ麯���������һ���Ե����ϵĹ��̣�
		//��ת�������ı�ָ��h��������ɫ����˿�����ת�����ӳ������������ӵ����󣬵��������ǵݹ�����ڷ�����һ��ݹ�ջ�󽫵õ�����
		if(isRed(h.right) && !isRed(h.left)) h=rotateLeft(h);//ָ�����ӽڵ������Ϊ�죬ָ�����ӽڵ������Ϊ�ڻ�Ϊ��----����ת
		if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);//��������ָ�����ӽڵ�����Ӿ�Ϊ��----����ת
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);//ָ�����Һ��ӽڵ�����Ӿ�Ϊ��----��ɫ��ת
        h.size = size(h.left) + size(h.right) + 1;
        
        return h;
	}
	
	//h�ڵ��������Ϊ��ɫ����Ҫ����תΪ������----����ת
	private Node rotateRight(Node h){
		Node x=h.left;
		h.left=x.right;
		x.right=h;
		x.color=x.right.color;
		x.right.color=RED;
		x.size=h.size;//x��Ϊ���������ڵ���Ϊԭ����h�Ľڵ���
		h.size=size(h.left)+size(h.right)+1;//h��Ϊx�����ӽڵ㣬���¼�����ڵ���
		return x;
	}
	//h�ڵ�������丸�����ӽڵ�����ӽڵ㣬��Ӧ���ӿɺ�ɺ�
	//h�ڵ��������Ϊ��ɫ����Ҫ����תΪ������----����ת
	private Node rotateLeft(Node h){
		Node x = h.right;
		h.right = x.left;
        x.left = h;
        x.color = x.left.color;//��Ϊxȡ��h��Ϊ�¸�����xӦ�á��̳С�ָ��ԭ��h��������ɫ
        x.left.color = RED;//xָ��h��������Ϊ��ɫ������˺�ɫ�����ӱ�Ϊ��ɫ������
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
	}
	//��תĳһ�ڵ㼰���ӽڵ����ɫ
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
