import java.util.*;

public class Graph {
    private HashMap<Integer, HashSet<Integer>> adjMap;
    private int edgNum;        // Number of Edges
    private int vexNum;       // Number of Vertices

    public Graph(){
        this.adjMap = new HashMap<Integer, HashSet<Integer>>();
    }

    public Graph(HashMap<Integer, HashSet<Integer>> adjMap, int edgNum, int vexNum) {
        this.adjMap = adjMap;
        this.edgNum = edgNum;
        this.vexNum = vexNum;

    }

    public void setAdjMap(HashMap<Integer, HashSet<Integer>> adjMap) {
        this.adjMap = adjMap;
    }

    public void setMap (int u,HashSet<Integer> adjs ) {
        if (adjMap.containsKey(u)) {
            HashSet<Integer> curAdj = adjMap.get(u);
            curAdj.addAll(adjs);
            adjMap.put(u,curAdj);
        } else {
            adjMap.put(u,adjs);
        }

    }

    public void setMap (int u, int neighbor ) {
        
        if (adjMap.containsKey(u)) {
            HashSet<Integer> curAdj = adjMap.get(u);
            curAdj.add(neighbor);
            adjMap.put(u,curAdj);
        } else {
            HashSet<Integer> adjs = new HashSet<>();
            adjs.add(neighbor);
            adjMap.put(u,adjs);
        }

    }

    public void setEdgNum(int edgNum) {
        this.edgNum = edgNum;
    }

    public void setVexNum(int vexNum) {
        this.vexNum = vexNum;
    }

    public HashMap<Integer, HashSet<Integer>> getAdjMap() {
        return adjMap;
    }

    public int getEdgNum() {
        return edgNum;
    }

    public int getVexNum() {
        return vexNum;
    }

    public int getDegree(int vex) {//return the size of its neighbors of vetex vex
        HashSet<Integer> adjs = adjMap.get(vex);
        return adjs.size();

    }

    public HashSet<Integer> getAdjVex(int v){
        return adjMap.get(v);

    }

    public boolean isExistEdge (int u, int v) {//since its will be double direction, the order of u,v shouldn't matter
        HashSet<Integer> uAdjs = adjMap.get(u);
        if (uAdjs.contains(v)) {
            return true;
        } else {
            return false;
        }

    }

}
