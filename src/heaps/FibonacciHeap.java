package heaps;
//2018-5-11
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class FibonacciHeap<Key>{
	private int size;//���нڵ�����
	private Node head;//������С��������ͷ���
	private Node min;
	private final Comparator<Key> comp;
	private HashMap<Integer, Node> table = new HashMap<Integer, Node>(); //�ϲ�����֮��
	
	private class Node{
		Key key;
		int degree;//�ڵ�Ķ�
		Node left,right;//�ڵ�������ֵܽڵ�
		Node child;//�ڵ�ĵ�һ�����ӽڵ�
		Node parent;//�ڵ�ĸ��ڵ�
		boolean childCut;//childCut��ǵ����þ����������"�ýڵ���ӽڵ��Ƿ��б�ɾ����"��������������ʵ�ּ������С�
		
		Node(Key key){
			this.key=key;
			this.degree=0;
			this.childCut=false;
			this.left=this;
			this.right=this;
			this.parent=null;
			this.child=null;
		}
	}
	//**********************���캯��*******************************
	public FibonacciHeap(Comparator<Key> C) {
		comp = C;
	}
	public FibonacciHeap() {
		comp = new MyComparator();
	}
	public FibonacciHeap(Key[] a) {
		comp = new MyComparator();
		for (Key k : a) insert(k);
	}
	public FibonacciHeap(Comparator<Key> C, Key[] a) {
		comp = C;
		for (Key k : a) insert(k);
	}
	//************************************************�������****************************************************
	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}
	public void insert(Key key) {
		Node x = new Node(key);
		size++;
		head = insert(x, head);
		if (min == null) min = head;
		else 			 min = (greater(min.key, key)) ? head : min;
	}
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Heap is empty");
		return min.key;
	}
	//����һ���Ѳ��뵱ǰ����
	public FibonacciHeap<Key> union(FibonacciHeap<Key> that) {
		this.head = meld(head, that.head);//ʵ�ʵĺϲ�����
		this.min = (greater(this.min.key, that.min.key)) ? that.min : this.min;
		this.size = this.size+that.size;
		return this;
	}
	//************************************************ɾ����С�ڵ�******************************************************
	//ɾ����С������
	//(1����Ҫ��ȡ��С����������ֱ�Ӵ����ڸ����У�
	//(2���ϲ�����degree��ȵ�����ֱ��û����ȵ�degree����
	public Key delMin() {
		if (isEmpty()) throw new NoSuchElementException("Heap is empty");
		head = cut(min, head);//����С�ڵ�min��head�б����г��������µĽڵ�(min�ĺ�̽ڵ�)��Ϊhead
		Node x = min.child;//x��ʾ��С�ڵ�ĺ����ǣ���Ϊ����ֻ��Ҫɾ����С�ڵ㣬������ĺ���Ҫ�鲢��head
		Key key = min.key;
		min.key = null;
		if (x != null) {
			head = meld(head, x);//����С�ڵ�min�ĺ����б��������б�head
			//x.parent=null;
			min.child = null;//��min�ĺ�������Ϊ�գ���ʱmin��Ϊ��һ�������ڵ㣬����ɾ������
		}
		size--;
		if (!isEmpty()) consolidate();//����С�ڵ�ĺ��Ӳ��������б�head����Ҫ�ϲ�������ͬ�ȵ���С��
		return key;
	}
	//�����ڵ�Ϊx����С�����뵽������б�
	private Node insert(Node x, Node head) {
			if (head == null) {
				x.left = x;
				x.right = x;
			} else {
				head.left.right = x;
				x.right = head;
				x.left= head.left;
				head.left = x;
			}
			return x;
	}
	//�鲢�������б�
	private Node meld(Node x, Node y) {
		if (x == null) return y;
		if (y == null) return x;
		x.left.right = y.right;
		y.right.left= x.left;
		x.left = y;
		y.right = x;
		return x;
	}
	//�Ƴ�һ����,Ҳ����˵�ڸ����˫���б�head��ɾ��һ���ڵ�x
	private Node cut(Node x, Node head) {
		if (x.right == x) {
			x.right = null;
			x.left = null;
			return null;
		} else {
			x.right.left = x.left;
			x.left.right = x.right;
			Node res = x.right;
			x.right= null;
			x.left= null;
			//x.parent=null
			if (head == x)  return res;//���ɾ������head����ô����head�ĺ�̽ڵ�
			else 			return head;//ɾ�����Ǹ���head�ڵ㣬��ô������ԭ��head
		}
	}
	//�ϲ�������б��о�����ͬ�ȵ���
	private void consolidate() {
			table.clear();
			Node x = head;
			int maxDegree = 0;
			min = head;
			Node y = null; Node z = null;
			do {
				y = x;
				x = x.right;
				z = table.get(y.degree);
				while (z != null) {
					table.remove(y.degree);
					if (greater(y.key, z.key)) {
						link(y, z);
						y = z;
					} else {
						link(z, y);
					}
					z = table.get(y.degree);
				}
				table.put(y.degree, y);
				if (y.degree > maxDegree) maxDegree = y.degree;
			} while (x != head);
			head = null;
			for (Node n : table.values()) {
				if (n != null) {
					min = greater(min.key, n.key) ? n : min;
					head = insert(n, head);
				}
			}
		}
	//����root1�ļ�ֵ����root2�����root2��Ϊ�µĸ�
	private void link(Node root1, Node root2) {
			root2.child = insert(root1, root2.child);//���ڵ�root1���뵽root2�ĺ��ӽڵ��б�root2.child��
			root1.parent=root2;
			root2.degree++;
			root1.childCut=false;
	}
	//*****************************************************��С��ֵ*******************************************************
	//��С�ڵ��ֵ��������ٽڵ���ƻ���"��С��"���ʣ�Ҫ���е���:
	//(1) ���ȣ���"����С�ڵ�"��"�����ڵ���С��"���������Ȼ��"�ýڵ�"������"������"�С� ��������С�Ľڵ㲻�ǵ���һ���ڵ㣬���ǰ���������������
	//    ���ǽ���"����С�ڵ�"Ϊ����������"��С��"�а��������Ȼ�󽫸����������������С�
	//(2) ���ţ���"�����ٽڵ�"��ԭ���ڵ����"��������"����ν"��������"�������ڱ���С�ڵ��ƻ�����С�����ʣ�����������֮���ٴ�"���ĸ��ڵ�"���еݹ鼶�����в�����
	//    �����������ľ��嶯�����ǣ������ڵ�(����С�ڵ�ĸ��ڵ�)��childCut���Ϊfalse��������Ϊtrue��Ȼ���˳���
	//    ���򣬽����ڵ����С����������(��ʽ��"�б���С�ڵ�ķ�ʽ"һ��)��Ȼ��ݹ���游�ڵ����"��������"��
	//    ���������е�����Ŀ����Ϊ�˷�ֹ"��С��"�ɶ������ݻ�������
	
	private void newDegree(Node parent,int degree){//�޸Ľڵ����
		parent.degree-=degree;
		if(parent.parent!=null)
			newDegree(parent.parent,degree);
	}
	private void cut2(Node x, Node parent) {
		x.right.left = x.left;
		x.left.right = x.right;
	    newDegree(parent, x.degree);
	    // xû���ֵ�
	    if (x == x.right) 
	        parent.child = null;
	    else 
	        parent.child = x.right;

	    x.parent = null;
	    x.left = x.right = null;
	    x.childCut = false;
	    // ��"x������"��ӵ�"������"��
	    head = insert(x, head);
	}
	//��������
	private void cascadingCut(Node x) {
	    Node parent = x.parent;
	    if (parent != null) {
	        if (x.childCut == false) 
	            x.childCut = true;
	        else {
	            cut2(x, parent);
	            cascadingCut(parent);
	        }
	    }
	}
	private void decreaseKey(Node x, Key key) {
	    if (min==null ||x==null) 
	        return ;
	    if (greater(key, x.key)) {
	    System.out.printf("decrease failed: the new key(%d) is no smaller than current key(%d)\n", key, x.key);
	        return ;
	    }
	    Node parent = x.parent;
	    x.key = key;
	    if (parent!=null && greater(parent.key,x.key)) {
	        // ��x�Ӹ��ڵ�parent�а������������node��ӵ���������
	        cut2(x, parent);
	        cascadingCut(parent);
	    }
	    // ������С�ڵ�
	    if (greater(min.key , x.key))
	        min = x;
	}
	//****************************************************���Ӽ�ֵ********************************************************
	//(1) ��"�����ӽڵ�"��"���Ӻ����ӵ������ֵ�"�����ӵ��������С�
	//(2) ����������"�����ӽڵ�"��ӵ�������������м������С�
	private void increaseKey(Node x, Key key) {
	    if (min==null ||x==null) 
	        return ;

	    if (greater(x.key,key)) {
	    System.out.printf("increase failed: the new key(%d) is no greater than current key(%d)\n", key, x.key);
	        return ;
	    }

	    // ��nodeÿһ������(����������,����,...)����ӵ�"쳲������ѵĸ�����"��
	    while (x.child != null) {
	    	
	        Node child = x.child;
	        child.right.left = child.left;
			child.left.right = child.right;              // ��child��x����������ɾ��
			
	        if (child.right == child)
	            x.child = null;
	        else
	            x.child = child.right;

	        head=insert(child,head);      // ��child��ӵ���������
	        child.parent = null;
	    }
	    x.degree = 0;
	    x.key = key;

	    // ���x���ڸ������У�
	    //     ��x�Ӹ��ڵ�parent���������а��������
	    //     ��ʹx��Ϊ"�ѵĸ�����"�е�һԱ��
	    //     Ȼ�����"��������"
	    // �������ж��Ƿ���Ҫ���¶ѵ���С�ڵ�
	    Node parent = x.parent;
	    if(parent != null) {
	        cut2(x, parent);
	        cascadingCut(parent);
	    } else if(min == x) {
	        Node right = x.right;
	        while(right != x) {
	            if(greater(x.key , right.key))
	                min = right;
	            right = right.right;
	        }
	    }
	}
	//*******************************************************ɾ���ڵ�****************************************************
	private void remove(Node x) {
	    Key minKey = min.key;
	    delMin();
	    decreaseKey(x, minKey);  
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
