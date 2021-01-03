import java.io.*;
import java.util.*;

public class MVCTool {
    //A class to contain several useful tool to get the MST
    public static Graph parseGraph(String filename) throws IOException {

        HashMap<Integer, HashSet<Integer>> map = new HashMap<>();//we will use the map to form our grah


        //Read the file
        File path = new File("").getCanonicalFile();
        String dataPath = path+"/Data/" + filename + ".graph";//It works on my computer
        BufferedReader br = new BufferedReader(new FileReader(dataPath));

        //Start Reading
        //1st line
        String line = br.readLine();
        String[] sizes = line.split(" ");
        int vexNum = Integer.parseInt(sizes[0]); int edgeNum = Integer.parseInt(sizes[1]);
        int vex = 0;//we start from 0 index here (but the actual vex index will start from 1(see below))
        while (vex < vexNum) {
            line = br.readLine();
            String[] adjs = line.split(" ");//get all our neighbors
            if (!adjs[0].equals("")) {
                HashSet<Integer> thisAdjs = new HashSet<>();//prepare a hashset for this vertice's adjadent vexs.
                for (int i=0;i<adjs.length;i++) {
                    thisAdjs.add(Integer.valueOf(adjs[i]));
                }
                map.put(vex+1,thisAdjs);//actual vex index starts from 1, consistent with the project description

            } else {//an isolated vertice
                HashSet<Integer> thisAdjs = new HashSet<>();//prepare a hashset for this vertice's adjadent vexs.
                map.put(vex+1,thisAdjs);//actual vex index starts from 1, consistent with the project description
            }

            vex++;
        }
        br.close();
        System.out.println("Done parseGraph");
        Graph graph = new Graph(map,edgeNum,vexNum);

        return graph;


    }


    public static HashSet<Edge> parseEdge(Graph graph) {
        HashMap<Integer, HashSet<Integer>> adjMap = graph.getAdjMap();
        HashSet<Edge> ans = new HashSet<>();
        for (Integer u : adjMap.keySet()) {
            HashSet<Integer> adjNode = adjMap.get(u);
            for (Integer v : adjNode)  {// double loop through to get each edge
                Edge thisEdge = new Edge(u,v);
                ans.add(thisEdge);


            }

        }
        return ans;
    }
}
