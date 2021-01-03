import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.zip.CheckedOutputStream;

public class LS2 {

    public static Set<Integer> SA(Graph graph, int cutoff, int seed, PrintWriter trace_writer) {
        Set<Integer> allVex = graph.getAdjMap().keySet();
        long start_time = System.currentTimeMillis();
        long elapsed_time_milis = 0;
        float elapsed_time = 0;

        HashSet<Edge> edges = MVCTool.parseEdge(graph);//get all the vex pair first to save time
        Random rd = new Random();
        rd.setSeed((long)seed);
        
        //Construct the initial Solution
        HashSet<Integer> VC = InitialVC(graph,edges,seed);//Using the Maximum Degree Greedy (MDG)








        Set<Integer> vertexSet =  graph.getAdjMap().keySet();


        //Start the SA Algo
        double T = 1000000;
        double coolRate = 0.95;

        Set<Integer> C_opt = new HashSet<>();

        //write the initial solution into trace first;

        C_opt = new HashSet<>();
        C_opt.addAll(VC);

        elapsed_time_milis = System.currentTimeMillis() - start_time;
        elapsed_time = (float) elapsed_time_milis / 1000;
        trace_writer.printf("%.2f, %d%n", elapsed_time, C_opt.size());


        while (elapsed_time < cutoff && T > 0){
            int vexSize = vertexSet.size();

            //randomly select a vertex
            int vex = rd.nextInt(vexSize) + 1;//since the vertex starts from 0

            HashSet<Integer> temp = new HashSet<>();
            temp.addAll(VC);


            if (VC.contains(vex)) {
                //if this vertex is in the VC
                //Try to see what will happen for VC after removing vex
                HashSet<Integer> adjVexes = graph.getAdjVex(vex);
                Boolean cover = true;

                elapsed_time_milis = System.currentTimeMillis() - start_time;
                elapsed_time = (float) elapsed_time_milis / 1000;
                if (elapsed_time > cutoff) {
                    break;
                }

                for (Integer adjVex : adjVexes) {
                    if (!VC.contains(adjVex)){
                        cover = false;
                        break;
                    }
                }

                if (cover) {
                    //Record the current best solution
                    C_opt = new HashSet<>();
                    C_opt.addAll(VC);

                    elapsed_time_milis = System.currentTimeMillis() - start_time;
                    elapsed_time = (float) elapsed_time_milis / 1000;
                    trace_writer.printf("%.2f, %d%n", elapsed_time, C_opt.size());
                    temp.remove(vex);
                }


            } else {
                temp.add(vex);

            }

            int delta = F_function(temp,VC);

            elapsed_time_milis = System.currentTimeMillis() - start_time;
            elapsed_time = (float) elapsed_time_milis / 1000;
            if (elapsed_time > cutoff) {
                break;
            }

            if (delta <0) {
                VC = temp;

            } else {
                // Finally the Temperature
                //Calcultate the Deg(vi) = Degree(i)/EdgeNum
                int deg_i = graph.getDegree(vex)/edges.size();
                double P = -1.0;//probability
                double thisPro = rd.nextDouble();

                if (VC.contains(vex)) {
                    P = Math.exp(-delta*(1+deg_i) / T);

                    if (P > thisPro) {
                        VC = temp;
                    }

                } else {
                    P = Math.exp(-delta*(1-deg_i) / T);
                    if (P > thisPro) {
                        VC = temp;
                    }
                }
            }

            elapsed_time_milis = System.currentTimeMillis() - start_time;
            elapsed_time = (float) elapsed_time_milis / 1000;
            if (elapsed_time > cutoff) {
                break;
            }

            //update the cool rate
            T = T * coolRate;
        }

        return VC;






    }

    private static int F_function(HashSet<Integer> temp, HashSet<Integer> vc) {
        return temp.size() - vc.size();
    }

    private static HashSet<Integer> InitialVC(Graph graph, HashSet<Edge> edges, int seed) {
        //Using the Maximum Degree Greedy (MDG)
        HashSet<Integer> initVC = new HashSet<>();

        //Convert a set into list for getting edges from it
        ArrayList<Edge> edgesList = new ArrayList<>();
        edgesList.addAll(edges);
        Random random = new Random();
        random.setSeed((long)seed);

        while (!edgesList.isEmpty()) {
            int edgesSize = edgesList.size();

            //select a vertex of maximum degree
            int vex = getFromMaxDegree(graph,edgesList);

            //V←V− {u};
            //use this to record all the remove edge( for iterator modification erro)
            HashSet<Edge> edgesRemove = new HashSet<>();

            for (Edge edge:edgesList) {
                if (edge.getStart() == vex || edge.getEnd() == vex) {
                    edgesRemove.add(edge);
                }
            }
            edgesList.removeAll(edgesRemove);

            //C←C∪ {u};
            initVC.add(vex);


        }
        return initVC;



    }

    private static int getFromMaxDegree(Graph graph, ArrayList<Edge> edgesList) {
        int MaxDeg = Integer.MIN_VALUE;
        int MaxVex = -1;
        for (Edge edge : edgesList) {
            //for each edge {u,v}, compare the degree for u and v and keep the max one
            int str = edge.getStart(); int end = edge.getEnd();
            int thisMax = Math.max(graph.getDegree(str),graph.getDegree(end));
            MaxDeg = Math.max(thisMax,MaxDeg);

            //update the index
            if (MaxDeg == thisMax) {
                MaxVex = (thisMax == graph.getDegree(str))? str : end;

            }

        }
        return MaxVex;
    }
}
