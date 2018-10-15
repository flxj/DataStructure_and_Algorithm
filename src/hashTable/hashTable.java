package hashTable;


//2018-4-15
//来自STL
class Node<Key extends Comparable<Key>,Value>{
	public Key key;
	public Value val;
	public Node next;
	Node(Key key,Value val){
		this.key=key;
		this.val=val;
		this.next=null;
	}
}

public class hashTable<Key extends Comparable<Key>,Value> {
	private static final int num_primes=26;
	private static final int[] primes={53,         97,         193,       389,       769,
	                                   1543,       3079,       6151,      12289,     24593,
			                           49157,      98317,      196613,    393241,    786433,
			                           1572869,    3145739,    6291469,   12582917,  25165843,
			                           50331653,   100663319,  201326611, 402653189, 805306457, 
			                           1610612741};
	//private static final long[] primes={53,         97,         193,       389,       769,
	//	                                1543,       3079,       6151,      12289,     24593,
	//	                                49157,      98317,      196613,    393241,    786433,
	//	                                1572869,    3145739,    6291469,   12582917,  25165843,
	//	                                50331653,   100663319,  201326611, 402653189, 805306457, 
	//	                                1610612741, 3221225473L, 4294967291L};
	class Header{
		Node next;
		Header(){
			next=null;
		}
	}
	private Header[] buckets;
	//private ArrayList<Node> buckets2;
	private int num_elements;
	
	hashTable(int n){
		
	}
	//**********************************************************
	public int bucketCount(){
		return buckets.length;
	}
	//***********************************************************
	//返回大于n且最接近n的质数
	private int nextPrimes(long n){
		int i=0;
		while(i<num_primes){
			if(primes[i]>n){
				return primes[i];
			}
			i++;
		}
		return primes[num_primes-1];
		
	}
	private boolean insert_unique(Key key,Value val){
		resize(num_elements+1);
		return insert_unique_noresize(key,val);
	}
	
	//该函数判断是否需要扩充现有buckets
	private void resize(int n){
		int old_n=buckets.length;
		if(n>old_n){ //如果添加一个元素后，装填因子大于1，则需要扩充了
			Node[] tmp=new Node[n];
			for(int bucket=0;bucket<old_n;bucket++){
				Node first=buckets[bucket].next;
				while(first!=null){
					int new_bucket=bucket_num((Key)first.key,n);
					buckets[bucket].next=first.next;
					first.next=tmp[new_bucket].next;
					tmp[new_bucket].next=first;
					first=buckets[bucket].next;
				}
			}
			//buckets.swap(tmp);
		}
	}
	//在不需要扩充情况下插入新节点
	private boolean insert_unique_noresize(Key key,Value val){
		int n=bucket_num(key);
		Node first=buckets[n].next;
		for(Node cur=first;cur!=null;cur=cur.next){
			if(cur.key.compareTo(key)==0){
				return false;
			}
		}
		Node tmp=new Node(key,val);
		tmp.next=first;
		buckets[n].next=tmp;
		num_elements++;
		return true;
	}
	
	private boolean insert_equal(Key key,Value val){
		resize(num_elements+1);
		return insert_equal_noresize(key,val);
	}
	private boolean insert_equal_noresize(Key key,Value val){
		int n=bucket_num(key);
		Node first=buckets[n].next;
		for(Node cur=first;cur!=null;cur=cur.next){
			if(cur.key.compareTo(key)==0){
				Node tmp=new Node(key,val);
				tmp.next=cur.next;
				cur.next=tmp;
				return true;
			}
			
		}
		Node tmp=new Node(key,val);
		tmp.next=first;
		buckets[n].next=tmp;
		return true;
	}
	/*
	private int bucket_num(Comparable key,int n){
		return 0;
	}
	*/
	private int bucket_num(Key key,int n){
		return 0;
	}
	private int bucket_num(Key key){
		return bucket_num(key,buckets.length);
	}

}
