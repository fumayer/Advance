package com.example.knowledgepointsharelib.dataStructure.graph;

/**
 * Created by sunjie on 2018/9/23
 * 求最小生成树---普利姆算法
 */

public class GraphMinimunSpanningTreePrimAlgorithm {
    private int vertexSize;//顶点数量
    private int[] vertexs;//顶点数组
    private int[][] matrix;
    private static final int MAX_WEIGHT = 1000;

    public GraphMinimunSpanningTreePrimAlgorithm(int vertextSize) {
        this.vertexSize = vertextSize;
        matrix = new int[vertextSize][vertextSize];
        vertexs = new int[vertextSize];
        for (int i = 0; i < vertextSize; i++) {
            vertexs[i] = i;
        }
    }


    /**
     * 普里姆算法
     */
    public void prim() {
        int[] lowcost = new int[vertexSize];//最小代价顶点权值的数组,为0表示已经获取最小权值
        int[] adjvex = new int[vertexSize];//放顶点权值
        int min, minId, sum = 0;
        for (int i = 1; i < vertexSize; i++) {
            lowcost[i] = matrix[0][i];
        }
        for (int i = 1; i < vertexSize; i++) {
            min = MAX_WEIGHT;
            minId = 0;
            for (int j = 1; j < vertexSize; j++) {
                if (lowcost[j] < min && lowcost[j] > 0) {
                    min = lowcost[j];
                    minId = j;
                }
            }
            System.out.println("顶点：" + adjvex[minId] + "权值：" + min);
            sum += min;
            lowcost[minId] = 0;
            for (int j = 1; j < vertexSize; j++) {
                if (lowcost[j] != 0 && matrix[minId][j] < lowcost[j]) {
                    lowcost[j] = matrix[minId][j];
                    adjvex[j] = minId;
                }
            }
        }
        System.out.println("最小生成树权值和:" + sum);
    }


    public static void main(String[] args) {
        GraphMinimunSpanningTreePrimAlgorithm graph = new GraphMinimunSpanningTreePrimAlgorithm(9);

        int[] a1 = new int[]{0, 10, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 11, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT};
        int[] a2 = new int[]{10, 0, 18, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 16, MAX_WEIGHT, 12};
        int[] a3 = new int[]{MAX_WEIGHT, MAX_WEIGHT, 0, 22, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 8};
        int[] a4 = new int[]{MAX_WEIGHT, MAX_WEIGHT, 22, 0, 20, MAX_WEIGHT, MAX_WEIGHT, 16, 21};
        int[] a5 = new int[]{MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 20, 0, 26, MAX_WEIGHT, 7, MAX_WEIGHT};
        int[] a6 = new int[]{11, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 26, 0, 17, MAX_WEIGHT, MAX_WEIGHT};
        int[] a7 = new int[]{MAX_WEIGHT, 16, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 17, 0, 19, MAX_WEIGHT};
        int[] a8 = new int[]{MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 16, 7, MAX_WEIGHT, 19, 0, MAX_WEIGHT};
        int[] a9 = new int[]{MAX_WEIGHT, 12, 8, 21, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 0};

        graph.matrix[0] = a1;
        graph.matrix[1] = a2;
        graph.matrix[2] = a3;
        graph.matrix[3] = a4;
        graph.matrix[4] = a5;
        graph.matrix[5] = a6;
        graph.matrix[6] = a7;
        graph.matrix[7] = a8;
        graph.matrix[8] = a9;

        graph.prim();
    }
}

