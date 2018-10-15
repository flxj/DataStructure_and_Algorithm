package heaps;
//来自《算法第四版》,2018-5-10

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FibHeap<Key> implements Iterable<Key> {
	private Node head;					//最小数根层次双向链表的头结点
	private Node min;					//指向键值最小的节点
	private int size;					//节点总数
	private final Comparator<Key> comp;	//比较器
	private HashMap<Integer, Node> table = new HashMap<Integer, Node>(); //合并操作之用
	
	private class Node {
		Key key;						
		int order;						//该节点的度
		Node prev, next;				//左右兄弟节点
		Node child;						//一个孩子节点
	}
	
	//**************构造函数********************
	public FibHeap(Comparator<Key> C) {
		comp = C;
	}
	public FibHeap() {
		comp = new MyComparator();
	}
	public FibHeap(Key[] a) {
		comp = new MyComparator();
		for (Key k : a) insert(k);
	}
	public FibHeap(Comparator<Key> C, Key[] a) {
		comp = C;
		for (Key k : a) insert(k);
	}
	
	//**************常规操作******************
	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public void insert(Key key) {
		Node x = new Node();
		x.key = key;
		size++;
		head = insert(x, head);
		if (min == null) min = head;
		else 			 min = (greater(min.key, key)) ? head : min;
	}
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Heap is empty");
		return min.key;
	}

	//删除最小键值所对应节点
	public Key delMin() {
		if (isEmpty()) throw new NoSuchElementException("Heap is empty");
		head = cut(min, head);
		Node x = min.child;
		Key key = min.key;
		min.key = null;
		if (x != null) {
			head = meld(head, x);
			min.child = null;
		}
		size--;
		if (!isEmpty()) consolidate();
		return key;
	}
	//合并两个堆
	public FibHeap<Key> union(FibHeap<Key> that) {
		this.head = meld(head, that.head);//实际的合并函数
		this.min = (greater(this.min.key, that.min.key)) ? that.min : this.min;
		this.size = this.size+that.size;
		return this;
	}
	
	//************************辅助函数********************************
	private boolean greater(Key n, Key m) {
		if (n == null) return false;
		if (m == null) return true;
		return comp.compare(n,m) > 0;
	}
	
	//假设root1的键值大于root2，因此root2变为新的根
	private void link(Node root1, Node root2) {
		root2.child = insert(root1, root2.child);//将节点root1插入到root2的孩子节点列表root2.child中
		root2.order++;
	}

	//Coalesce the roots, thus reshapes the tree
	private void consolidate() {
		table.clear();
		Node x = head;
		int maxOrder = 0;
		min = head;
		Node y = null; Node z = null;
		do {
			y = x;
			x = x.next;
			z = table.get(y.order);
			while (z != null) {
				table.remove(y.order);
				if (greater(y.key, z.key)) {
					link(y, z);
					y = z;
				} else {
					link(z, y);
				}
				z = table.get(y.order);
			}
			table.put(y.order, y);
			if (y.order > maxOrder) maxOrder = y.order;
		} while (x != head);
		head = null;
		for (Node n : table.values()) {
			if (n != null) {
				min = greater(min.key, n.key) ? n : min;
				head = insert(n, head);
			}
		}
	}
	
	//在head双向列表里插入一个节点, 返回一个新的头结点（即新插入节点）
		private Node insert(Node x, Node head) {
			if (head == null) {
				x.prev = x;
				x.next = x;
			} else {
				head.prev.next = x;
				x.next = head;
				x.prev = head.prev;
				head.prev = x;
			}
			return x;
		}
		
		//移除一棵树,也就是说在根层次双向列表中删除一个节点x
		private Node cut(Node x, Node head) {
			if (x.next == x) {
				x.next = null;
				x.prev = null;
				return null;
			} else {
				x.next.prev = x.prev;
				x.prev.next = x.next;
				Node res = x.next;
				x.next = null;
				x.prev = null;
				if (head == x)  return res;//如果删除的是head，那么返回head的后继节点
				else 			return head;//删除的是个非head节点，那么还返回原来head
			}
		}
		
		//归并两个根列表
		private Node meld(Node x, Node y) {
			if (x == null) return y;
			if (y == null) return x;
			x.prev.next = y.next;
			y.next.prev = x.prev;
			x.prev = y;
			y.next = x;
			return x;
		}
		
		//************************迭代器*********************************
		public Iterator<Key> iterator() {
			return new MyIterator();
		}
		
		private class MyIterator implements Iterator<Key> {
			private FibHeap<Key> copy;
			
			
			//Constructor takes linear time
			public MyIterator() {
				copy = new FibHeap<Key>(comp);
				insertAll(head);
			}
			
			private void insertAll(Node head) {
				if (head == null) return;
				Node x = head;
				do {
					copy.insert(x.key);
					insertAll(x.child);
					x = x.next;
				} while (x != head);
			}
			
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
			public boolean hasNext() {
				return !copy.isEmpty();
			}
			
			//Takes amortized logarithmic time
			public Key next() {
				if (!hasNext()) throw new NoSuchElementException();
				return copy.delMin();
			}
		}
		
		//************************默认比较器*******************************
		private class MyComparator implements Comparator<Key> {
			@Override
			public int compare(Key key1, Key key2) {
				return ((Comparable<Key>) key1).compareTo(key2);
			}
		}
	

}
