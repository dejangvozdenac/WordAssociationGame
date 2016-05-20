import scala.util.parsing.combinator.testing.Str;

import java.io.*;
import java.util.*;

/**
 * Created by dejan on 5/20/16.
 */
public class WordGraph {
    private BufferedReader inputFile;
    private Set<String> nodes;
    private Set<String> usedNodes;
    private HashMap<String, List<String>> edges;
    private static final double CUTOFF = 0.94;

    public WordGraph(String inputFile) throws Exception{
        this.inputFile = new BufferedReader(new FileReader(inputFile));
        nodes = new HashSet<>();
        usedNodes = new HashSet<>();
        edges = new HashMap<>();
        initialize();
        prune();
    }

    public void initialize() throws Exception{
        String newline;
       while((newline=inputFile.readLine())!=null){
            String[] split = newline.split(" ");
           String a = split[0].split("#")[0];
           String b = split[2].split("#")[0];
           nodes.add(a);
           nodes.add(b);
           if(Double.parseDouble(split[4])>CUTOFF){
               List<String> previous;
               if(edges.containsKey(a)) {
                   previous = edges.get(a);
               }
               else {
                   previous = new ArrayList<>();
               }
               if(!checkIfPresent(edges,a,b)) {
                   previous.add(b);
                   edges.put(a, previous);
                   usedNodes.add(a);
                   usedNodes.add(b);
               }
           }
       }
    }

    public boolean checkIfPresent(HashMap<String, List<String>> edges, String a, String b){
        if((edges.get(a)!=null && edges.get(a).contains(b))
                || (edges.get(b)!=null && edges.get(b).contains(a))){
            return true;
        }
        else{
            return false;
        }
    }
    public void prune(){
        //remove nodes
        ArrayList toRemove = new ArrayList();
        for(String s : nodes){
            if(!usedNodes.contains(s)){
                toRemove.add(s);
            }
        }
        nodes.removeAll(toRemove);
    }

    public boolean contains(String a){
        return nodes.contains(a);
    }

    public Set<String> getNodes() {
        return nodes;
    }

    public HashMap<String, List <String>> getEdges() {
        return edges;
    }
}
