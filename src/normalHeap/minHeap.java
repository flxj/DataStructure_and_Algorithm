package normalHeap;

import java.util.ArrayList;

public class minHeap<T extends Comparable> {
	private ArrayList<T> data;
	private Integer curSize;
	private Integer maxSize;
	private static final Integer defaultSize=10;
	private static final Integer MaxSize=10^8;	
	minHeap(){
		this.data=new ArrayList<T>();
		this.maxSize=defaultSize;
		this.curSize=0;
	}
	minHeap(T[] a){
		if(a.length>0){
			curSize=0;
			maxSize=MaxSize;
			for(T item:a){
				data.add(item);
				curSize+=1;
				if(curSize==maxSize){
					break;
				}
			}
			//¶Ñ»¯´¦Àí
			for(int i=(curSize-2)/2;i>=0;i--){
				siftDown(i,curSize-1);
			}
			
		}else{
			this.data=new ArrayList<T>();
			this.maxSize=defaultSize;
			this.curSize=0;
		}
	}
	public boolean insert(T x){
		if(curSize==maxSize) return false;
		data.add(x);
		siftUp(curSize);
		curSize++;
		return true;
	}
	public boolean removeMin(){
		if(curSize==0) return false;
		data.set(0, data.get(curSize-1));
		curSize--;
		siftDown(0,curSize-1);
		return true;
	}
	public boolean isEmpty(){
		return curSize==0?true:false;
	}
	public boolean isFull(){
		return curSize==maxSize?true:false;
	}
	public void setEmpty(){
		curSize=0;
	}
	
	private void siftDown(int start,int end){
		int i=start,j=2*i+1;
		T temp=data.get(i);
		while(j<=end){
			if(j<end && data.get(j).compareTo(data.get(j+1))>0){
				j++;
			}
			if(temp.compareTo(data.get(j))<=0){
				break;
			}else{
				data.set(i, data.get(j));
				i=j;
				j=2*j+1;
			}
		}
		data.set(i, temp);
	}
	private void siftUp(int start){
		int j=start,i=(j-1)/2;
		T temp=data.get(j);
		while(j>0){
			if(temp.compareTo(data.get(i))>=0){
			    break;
		    }else{
		    	data.set(j, data.get(i));
		    	j=i;
		    	i=(i-1)/2;
		    }
	    }
		data.set(j,temp);
	
	}

}
