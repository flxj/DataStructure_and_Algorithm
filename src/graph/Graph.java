package graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

class Vertex{
	public String name;
	public List<Edge> adj;//邻接边
	public double dist;//从起始顶点到当前顶点的最短路径长度
	public Vertex prev;//记录最短路径上的前驱定点
	public int scratch;
	public Vertex(String nm){
		name=nm;
		adj=new LinkedList<Edge>();
		rest();
	}
	public void rest(){
		dist=Graph.INFINITY;
		prev=null;
		//pos=null;
		scratch=0;
	}
}
class Edge{
	public Vertex dest;//邻接顶点
	public double cost;//边权重
	public Edge(Vertex d,double c){
		dest=d;
		cost=c;
	}
	
}

public class Graph {
	public static final double INFINITY=Double.MAX_VALUE;
	private Map<String,Vertex> vertexMap=new HashMap<String,Vertex>();
	
	//在图上添加一条边
	public void addEdge(String sourceName,String destName,double cost){
		Vertex v=getVertex(sourceName);
		Vertex w=getVertex(destName);
		v.adj.add(new Edge(w,cost));
		
	}
	//打印最短路径
	public void printPath(String destName){
		Vertex w =vertexMap.get(destName);
		if(w==null){
			throw new NoSuchElementException();
		}else if(w.dist==INFINITY){
			System.out.println(destName+"is unreachable");
		}else{
			System.out.print("(Cost is:"+w.dist+")");
			printPath(w);
			System.out.println();
		}
		
	}
	//非加权图最短路径(广度优先搜索）
	public void unweighted(String startName){
		clearAll();
		Vertex start=vertexMap.get(startName);
		if(start==null){
			throw new NoSuchElementException("Start nor found");
		}
		Queue<Vertex> q=new LinkedList<Vertex>();
		q.add(start);
		start.dist=0;
		while(!q.isEmpty()){
			Vertex v=q.remove();
			for(Edge e:v.adj){
				Vertex w=e.dest;
				if(w.dist==INFINITY){
					w.dist=v.dist+1;
					w.prev=v;
					q.add(w);
				}
			}
		}
		
	}
	public void dijkstra(String startName){
		PriorityQueue<Path> pq=new PriorityQueue<Path>();
		Vertex start =vertexMap.get(startName);
		if(start==null){
			throw new NoSuchElementException("Start not found");
		}
		clearAll();
		pq.add(new Path(start,0));
		start.dist=0;
		int nodesSeen=0;
		while(!pq.isEmpty() && nodesSeen<vertexMap.size()){
			Path vrec=pq.remove();
			Vertex v=vrec.dest;
			if(v.scratch!=0){
				continue;
			}
			v.scratch=1;
			nodesSeen++;
			for(Edge e:v.adj){
				Vertex w=e.dest;
				double cvw =e.cost;
				if(cvw<0){
					throw new GraphException("Graph has negative edges");
				}
				if(w.dist>v.dist+cvw){
					w.dist=v.dist+cvw;
					w.prev=v;
					pq.add(new Path(w,w.dist));
				}
			}
		}
		
	}
	//Bellman-Ford算法
	public void negative(String startName){
		clearAll();
		Vertex start =vertexMap.get(startName);
		if(start==null){
			throw new NoSuchElementException("Start not found");
		}
		Queue<Vertex> q=new LinkedList<Vertex>();
		q.add(start);
		start.dist=0;
		start.scratch++;
		while(!q.isEmpty()){
			Vertex v=q.remove();
			if(v.scratch++>2*vertexMap.size()){
				throw new GraphException("Negative cycle detected");
			}
			for(Edge e:v.adj){
				Vertex w=e.dest;
				double cvw=e.cost;
				if(w.dist>v.dist+cvw){
					w.dist=v.dist+cvw;
					w.prev=v;
					if(w.scratch++%2==0){
						q.add(w);
					}else{
						w.scratch--;
					}
				}
			}
		}
	}
	//无环图的最短路径算法(拓扑排序）
	public void acylic(String startName){
		
	}
	//返回一个表示vertexName的顶点对象，必要时新建该对象
	private Vertex getVertex(String vertexName){
		Vertex v=vertexMap.get(vertexName);
		if(v==null){
			v=new Vertex(vertexName);
			vertexMap.put(vertexName, v);
		}
		return v;
	}
	//用来初始化最短路径算法中使用的数据成员
	private void clearAll(){
		for(Vertex v:vertexMap.values()){
			v.rest();
		}
	}
	//递归打印最短路径（假设该路径存在）
	private void printPath(Vertex dest){
		if(dest.prev!=null){
			printPath(dest.prev);
			System.out.print("to");
		}
		System.out.print(dest.name);
	}

}

class GraphException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GraphException(String name){
		super(name);
	}
}
//Dijkstra算法中优先队列所要存放的项
class Path implements Comparable<Path>{
	public Vertex  dest;
	public double cost;
	public Path(Vertex d,double c){
		dest=d;
		cost=c;
	}
	public int compareTo(Path rhs){
		double otherCost=rhs.cost;
		return cost<otherCost?-1:cost>otherCost?1:0;
	}
}
