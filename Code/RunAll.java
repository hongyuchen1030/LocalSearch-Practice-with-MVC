import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RunAll {
    public static PrintWriter sol_writer;
    public static PrintWriter trace_writer;

    public static void main(String @NotNull [] args) throws IOException {
        String[] fileNames = new String[]{"star2","star","as-22july06","delaunay_n10","email","football","hep-th","jazz","karate","netscience","power"};//"as-22july06","delaunay_n10","dummy1","dummy2","email","football","hep-th","jazz","karate","netscience","power",
        String[] methods = new String[]{"LS1"};

        for (int i = 0; i<fileNames.length; i++) {
            for (int j = 0; j <methods.length;j++) {
                String filename = fileNames[i];
                String method = methods[j];
                Integer cutoff =600;
                int[] seeds = new int[]{50,100,150,200,250,300,350,400,450};
                for (int s=0;s<seeds.length;s++) {
                    int seed = seeds[s];
                    File outDir = new File("").getCanonicalFile();
                    String outD = "";
                    Set<Integer> vc = new HashSet<>();//just initialization

                    if (method.equals("LS1")) {
                        outD = outDir + "/Output/LS1/" + filename;
                        // initialize writers
                        String sol_out = outD + "_" + method + "_" + cutoff + "_" + seed + ".sol";

                        String trace_out = outD + "_" + method + "_" + cutoff + "_" + seed + ".trace";

                        sol_writer = new PrintWriter(sol_out);
                        trace_writer = new PrintWriter(trace_out);
                        Graph graph = MVCTool.parseGraph(filename);
                        vc = LS1.FastVC(graph, cutoff, seed, trace_writer);

                        // write to solution
                        Iterator<Integer> iter = vc.iterator();
                        sol_writer.printf("%d%n", vc.size());

                        while (iter.hasNext()) {
                            sol_writer.printf("%s", iter.next());
                            if (iter.hasNext()) {
                                sol_writer.printf(",");
                            }
                        }
                        sol_writer.close();
                        trace_writer.close();

                    } else if(method.equals("LS2")) {
                        outD = outDir + "/Output/LS2/" + filename;
                        // initialize writers
                        String sol_out = outD + "_" + method + "_" + cutoff + "_" + seed + ".sol";

                        String trace_out = outD + "_" + method + "_" + cutoff + "_" + seed + ".trace";

                        sol_writer = new PrintWriter(sol_out);
                        trace_writer = new PrintWriter(trace_out);
                        Graph graph = MVCTool.parseGraph(filename);

                        vc = LS2.SA(graph, cutoff, seed, trace_writer);

                        // write to solution
                        Iterator<Integer> iter = vc.iterator();
                        sol_writer.printf("%d%n",vc.size());

                        while (iter.hasNext()) {
                            sol_writer.printf("%s",iter.next());
                            if (iter.hasNext()) {
                                sol_writer.printf(",");
                            }
                        }
                        sol_writer.close();
                        trace_writer.close();

                    }


                    System.out.println("Done" + outD + "_" + method + "_" + cutoff + "_" + seed);


                }

            }


        }



    }
}
