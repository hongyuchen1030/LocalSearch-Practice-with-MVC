import java.util.ArrayList;
import java.util.Collections;

public final class Edge {
    private int start; // u
    private int end;   // v

    public Edge(int start, int end) {
        this.start = start;
        this.end = end;
    }


    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Edge)) {
            return false;
        }
        Edge otherEdge = (Edge) other;
        return this.start == otherEdge.start &&
                this.end == otherEdge.end;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + start;
        hash = hash * 31 + end;

        return hash;
    }






    public String toString(){
        return ("u: "+this.start+" v: "+this.end);
    }


    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }
}
