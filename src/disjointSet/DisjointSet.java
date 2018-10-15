package disjointSet;

public class DisjointSet
{
	int[] rank,parent;//parent[i]表示第i个元素的父节点
	int n;
	
	public DisjointSet(int n){
		rank=new int[n];
		parent=new int[n];
		this.n=n;
		for(int i=0;i<n;i++){
			parent[i]=i;
		}
	}
	//查找元素k所在set的代表元
	public int find(int k){
		/*
		if(k>n || k<0) return Integer.MIN_VALUE-1;
		if(parent[k]==k){//k的父节点就是他自己，说明k就是他自己所在集合的代表元
			return k;
		}else{
			int res=find(parent[k]);//自底向上递归查找
			parent[k]=res;//将k直接挂到根节点上
			return res;
		}
		*/
		if(parent[k]!=k){
			parent[k]=find(parent[k]);//路径压缩
		}
		return parent[k];
	}
	//将包含x与y的两个集合按秩合并
	public void union(int x,int y){
		int xrep=find(x);
		int yrep=find(y);
		if(xrep==yrep) return ;
		if(rank[xrep]<rank[yrep]){
			parent[xrep]=yrep;
		}else if(rank[xrep]>rank[yrep]){
			parent[yrep]=xrep;
		}else{
			parent[yrep]=xrep;
			rank[xrep]+=1;
		}
	}
	//
	
	//

}
