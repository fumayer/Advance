package com.example.knowledgepointsharelib.dataStructure.graph;

import java.util.LinkedList;

public class GraphTraverse {
	private int vertexSize;//顶点数量
	private int [] vertexs;//顶点数组
	private int[][]  matrix;
	private static final int MAX_WEIGHT = 1000;
	private boolean [] isVisited;
	public GraphTraverse(int vertextSize){
		this.vertexSize = vertextSize;
		matrix = new int[vertextSize][vertextSize];
		vertexs = new int[vertextSize];
		for(int i = 0;i<vertextSize;i++){
			vertexs[i] = i;
		}
		isVisited = new boolean[vertextSize];
	}

	/**
	 * 获取某个顶点的第一个邻接点
	 */
	public int getFirstNeighbor(int index){
		for(int j = 0;j<vertexSize;j++){
			if(matrix[index][j]>0&&matrix[index][j]<MAX_WEIGHT){
				return j;
			}
		}
		return -1;
	}

	/**
	 * 根据前一个邻接点的下标来取得下一个邻接点
	 * @param v 表示要找的顶点
	 * @param index 表示该顶点相对于哪个邻接点去获取下一个邻接点
	 */
	public int getNextNeighbor(int v,int index){
		for(int j = index+1;j<vertexSize;j++){
			if(matrix[v][j]>0&&matrix[v][j]<MAX_WEIGHT){
				return j;
			}
		}
		return -1;
	}

	/**
	 * 深度优先遍历
	 */

	public void depthFirstSearch(){
		isVisited = new boolean[vertexSize];
		// 如果是连通图一次就搞定了，如果是非连通图就要遍历一下顶点
		for(int i = 0;i<vertexSize;i++){
			if(!isVisited[i]){
				System.out.println("访问到了："+i+"顶点");
				depthFirstSearch(i);
			}
		}
		isVisited = new boolean[vertexSize];
	}

	/**
	 * 图的深度优先遍历算法----类似树的前序遍历
	 */
	private void depthFirstSearch(int i){
		isVisited[i] = true;
		int w = getFirstNeighbor(i); // 获取该顶点的第一个邻接点
		while(w!=-1){
			if(!isVisited[w]){
				System.out.println("访问到了："+w+"顶点");
				depthFirstSearch(w); //递归，获取邻接点的第一邻接点
			}
			//第一邻接点如果遍历过(isVisited=true)，获取该顶点的第二邻接点，
			//以此类推，第二邻接点的顶点就会再次遍历该顶点的第一邻接点
			w = getNextNeighbor(i, w);
		}
	}


	/**
	 * 广度优先遍历
	 */
	public void breadthFirstSearch(){
		isVisited = new boolean[vertexSize];
		for(int i =0;i<vertexSize;i++){
			if(!isVisited[i]){
				breadthFirstSearch(i);
			}
		}
	}

	/**
	 * 广度优先遍历
	 */
	private void breadthFirstSearch(int i) {
		int u,w;
		LinkedList<Integer> queue = new LinkedList<Integer>();
		System.out.println("访问到了："+i+"顶点");
		isVisited[i] = true;
		queue.add(i);//第一次把v0加到队列
		while(!queue.isEmpty()){
			u = (Integer)(queue.removeFirst()).intValue();
			w = getFirstNeighbor(u);//获取第一邻接点
			while(w!=-1){
				if(!isVisited[w]){
					System.out.println("访问到了："+w+"顶点");
					isVisited[w] = true;
					queue.add(w);
				}
				// 获取第二邻接点、第三邻接点，没访问过就入栈
				w = getNextNeighbor(u, w);
			}
		}
	}


	public static void main(String [] args){
		GraphTraverse graph = new GraphTraverse(9);

		int [] a1 = new int[]{0,10,MAX_WEIGHT,MAX_WEIGHT,MAX_WEIGHT,11,MAX_WEIGHT,MAX_WEIGHT,MAX_WEIGHT};
		int [] a2 = new int[]{10,0,18,MAX_WEIGHT,MAX_WEIGHT,MAX_WEIGHT,16,MAX_WEIGHT,12};
		int [] a3 = new int[]{MAX_WEIGHT,MAX_WEIGHT,0,22,MAX_WEIGHT,MAX_WEIGHT,MAX_WEIGHT,MAX_WEIGHT,8};
		int [] a4 = new int[]{MAX_WEIGHT,MAX_WEIGHT,22,0,20,MAX_WEIGHT,MAX_WEIGHT,16,21};
		int [] a5 = new int[]{MAX_WEIGHT,MAX_WEIGHT,MAX_WEIGHT,20,0,26,MAX_WEIGHT,7,MAX_WEIGHT};
		int [] a6 = new int[]{11,MAX_WEIGHT,MAX_WEIGHT,MAX_WEIGHT,26,0,17,MAX_WEIGHT,MAX_WEIGHT};
		int [] a7 = new int[]{MAX_WEIGHT,16,MAX_WEIGHT,MAX_WEIGHT,MAX_WEIGHT,17,0,19,MAX_WEIGHT};
		int [] a8 = new int[]{MAX_WEIGHT,MAX_WEIGHT,MAX_WEIGHT,16,7,MAX_WEIGHT,19,0,MAX_WEIGHT};
		int [] a9 = new int[]{MAX_WEIGHT,12,8,21,MAX_WEIGHT,MAX_WEIGHT,MAX_WEIGHT,MAX_WEIGHT,0};

		graph.matrix[0] = a1;
		graph.matrix[1] = a2;
		graph.matrix[2] = a3;
		graph.matrix[3] = a4;
		graph.matrix[4] = a5;
		graph.matrix[5] = a6;
		graph.matrix[6] = a7;
		graph.matrix[7] = a8;
		graph.matrix[8] = a9;

//		graph.depthFirstSearch();
		graph.breadthFirstSearch();
	}
}
