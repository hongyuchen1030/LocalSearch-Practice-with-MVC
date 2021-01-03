import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

//Use the FastVC algorithmn

public class LS1 {

    public static Set<Integer> FastVC(Graph graph, int cutoff, int seed, PrintWriter trace_writer) {
        Set<Integer> allVex = graph.getAdjMap().keySet();
        long start_time = System.currentTimeMillis();
        long elapsed_time_milis = 0;
        float elapsed_time = 0;
        Random random = new Random();
        random.setSeed(seed);

        HashSet<Edge> edges = MVCTool.parseEdge(graph);//get all the vex pair first to save time
        HashMap<Integer,Integer> lossMap = ConstructVC(graph,edges);//LossMap records the loss of vexes in the MVC
        Set<Integer> C = lossMap.keySet();
        HashMap<Integer,Integer> gainMap = new HashMap<>();//gainMap records the gain of vexes outside the MVC



        for (Integer vex : allVex) {
            if (!C.contains(vex)) {
                gainMap.put(vex,0);
            }

        }

        Set<Integer> C_opt = new HashSet<>();

        while (elapsed_time < cutoff) {
            if (coverAllEdge(C,edges)) {//if C covers all edges


                //Record the current best solution
                C_opt = new HashSet<>();
                C_opt.addAll(lossMap.keySet());

                elapsed_time_milis = System.currentTimeMillis() - start_time;
                elapsed_time = (float) elapsed_time_milis / 1000;
                trace_writer.printf("%.2f, %d%n", elapsed_time, C_opt.size());
                System.out.println("Aline right now");

                //for test only
//                System.out.println("elapsed time: " + elapsed_time);
//                System.out.println("current num of vertices: " + C_opt.size());

                // remove a vertex with minimum loss from C
                C = lossMap.keySet();
                Integer minLoss = Integer.MAX_VALUE;
                int minIdx = -1;
                for (Integer vex : C) {
                    minLoss = Math.min(minLoss, lossMap.get(vex));
                    if (minLoss == lossMap.get(vex)) {
                        minIdx = vex;
                    }
                }

                removeVex(minIdx,lossMap,gainMap,graph);

                continue;

            }

            Integer u = ChooseRmVertex(lossMap,50,random);//k=50 according to the paper

//            System.out.println("random Vex "+u);

            elapsed_time_milis = System.currentTimeMillis() - start_time;
            elapsed_time = (float) elapsed_time_milis / 1000;
            if (elapsed_time > cutoff) {
                break;
            }

            removeVex(u,lossMap,gainMap,graph);


            //get a random uncovered edge;
            Edge randomEdge = getRmEdge(edges,graph,lossMap,random);
//            System.out.println("random Edge " + randomEdge.toString());

            elapsed_time_milis = System.currentTimeMillis() - start_time;
            elapsed_time = (float) elapsed_time_milis / 1000;
            if (elapsed_time > cutoff) {
                break;
            }

            //get the endpoint of randomEdge with greater gain, breaking ties in
            //favor of the older one;
            Integer start = randomEdge.getStart(); Integer end = randomEdge.getEnd();
            Integer selVex = (gainMap.get(start) > gainMap.get(end))? start:end;
//            System.out.println("Select endpoint for the random edge " + selVex);

            lossMap.put(selVex,0);//initialization
            gainMap.remove(selVex);
            //Update the loss and gain
            Set<Integer> adjVex = graph.getAdjVex(selVex);

            elapsed_time_milis = System.currentTimeMillis() - start_time;
            elapsed_time = (float) elapsed_time_milis / 1000;
            if (elapsed_time > cutoff) {
                break;
            }

            for (Integer adj : adjVex) {
                if (lossMap.containsKey(adj)) {
                    lossMap.put(adj,lossMap.get(adj)-1);
                } else {
                    gainMap.put(adj,gainMap.get(adj)-1);
                    lossMap.put(selVex,lossMap.get(selVex)+1);

                }

            }

//            System.out.println("current cover set "+lossMap.keySet().toString());
//



        }



        return C_opt;


    }

    private static Edge getRmEdge(HashSet<Edge> edges, Graph graph, HashMap<Integer, Integer> lossMap, Random random) {
        Set<Integer> C = lossMap.keySet();

        ArrayList<Edge> unCoverEdgeList = new ArrayList<>();

        for (Edge edge : edges) {
            if (!C.contains(edge.getStart()) && !C.contains(edge.getEnd())) {//both start and end are not covered
                unCoverEdgeList.add(edge);
            }
        }

        //ALso apply seed here
        int max = unCoverEdgeList.size();
        Integer idx = random.nextInt(max)%(max-0+1) + 0;
        return unCoverEdgeList.get(idx);

    }

    private static void removeVex(Integer v, HashMap<Integer, Integer> lossMap, HashMap<Integer, Integer> gainMap, Graph graph) {
        //initialization
        lossMap.remove(v);
        gainMap.put(v,0);

        //
        Set<Integer> adjVexs = graph.getAdjVex(v);
        for (Integer u : adjVexs) {
            if (lossMap.containsKey(u)) {//this vex is in the MVC
                //update loss by 1
                lossMap.put(u,lossMap.get(u)+1);

            } else {//this vex is not in the MVC

                //then this edge is counted for both u and v
                gainMap.put(u,gainMap.get(u)+1);
                gainMap.put(v,gainMap.get(v)+1);

            }

        }


    }


    private static Integer ChooseRmVertex(HashMap<Integer, Integer> lossMap, int k, Random random) {
        List<Integer> vexList = new ArrayList<>(lossMap.keySet());
        if (vexList.size() == 1) {
            return vexList.get(0);
        }

        Integer best;

        //use seed here, or the result will never change

        Collections.shuffle(vexList,random);
        best = vexList.get(0);



        Integer bestLoss = lossMap.get(Integer.valueOf(best));
        for (int i=1; i < k;i++) {
            Collections.shuffle(vexList,random);
            Integer r = vexList.get(0);
            Integer rLoss = lossMap.get(r);
            if (rLoss < bestLoss) {// the comparison function f is simply the loss function on vertices
                best = r;
                bestLoss = rLoss;

            }

        }

        return best;


    }


    private static boolean coverAllEdge(Set<Integer> C, HashSet<Edge> edges) {
        for (Edge edge : edges) {
            int start = edge.getStart();
            int end = edge.getEnd();
            if (!C.contains(start) && !C.contains(end)) {
                return false;
            }

        }
        return true;

    }

    private static HashMap<Integer,Integer> ConstructVC(Graph graph,HashSet<Edge> edges) {
        HashMap<Integer, HashSet<Integer>> adjMap = graph.getAdjMap();
        HashMap<Integer,Integer> coverMap = new HashMap<>();//key is the vex in the VC, and value is its loss
        for (Edge edge : edges) {
            Set<Integer> C = coverMap.keySet();
            if (!C.contains(edge.getStart()) && !C.contains(edge.getEnd())) {
                //add the endpoint of e with higher degree into C;
                int start = edge.getStart(); int end = edge.getEnd();
                int vex = (graph.getDegree(start) > graph.getDegree(end))? start:end;
                coverMap.put(vex,0);

            }
        }
        // loss(v), is defined as the number of covered edges that would become uncovered by removing v from C
        //calculate loss of vertices in C
        Set<Integer> C = new HashSet<>();
        C.addAll(coverMap.keySet());
        for (Edge edge : edges) {
            //if only one endpoint of e belongs to C then
            // for the endpoint v ∈ C, loss(v)++;
            if ((C.contains(edge.getStart()) && !C.contains(edge.getEnd())) || (!C.contains(edge.getStart()) && C.contains(edge.getEnd()))) {
                int containedVex = (C.contains(edge.getStart()))? edge.getStart() : edge.getEnd();
                coverMap.put(containedVex, coverMap.get(containedVex)+1);

            }

        }
        //remove redundant vertices
        for (Integer v : C) {
            if (coverMap.get(v) == 0) {
                coverMap.remove(v);//remove this vertex
                //update loss of vertices in N(v);
                HashSet<Integer> adjVex = graph.getAdjVex(v);
                for (Integer adj : adjVex) {
                    coverMap.put(adj, coverMap.get(adj)+1);
                }


            }

        }
        return coverMap;

    }






}