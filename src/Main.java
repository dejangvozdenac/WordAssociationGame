/**
 * Created by dejan on 5/20/16.
 */

import edu.cmu.lti.jawjaw.pobj.Word;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.layout.Eades84Layout;
import org.graphstream.ui.swingViewer.util.*;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.util.*;
import java.util.List;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;
import org.graphstream.ui.swingViewer.*;

public class Main {
    protected static String guessWordBuffer="";
    private static final double INITIAL_REVEAL = 0.1;
    private static WordGraph wg;
    private static Graph graph;
    private static DefaultView view;

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        try {
            wg = new WordGraph("/home/dejan/Desktop/Word Association/results");
            graph = initializeDisplayGraph();
            initializeNodesEdges(wg, graph);
            Viewer viewer = getDisplay(graph);
            view =(DefaultView) viewer.getDefaultView();
//            view.
            view.addKeyListener(guessWordListener());

//            getInputFromUser(wg, graph);
//            viewer.enableAutoLayout(new Eades84Layout());
//            viewer.disableAutoLayout();
        }catch (Exception e){
            e.printStackTrace();
            exit(-1);
        }
    }

    private static KeyListener guessWordListener(){
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char c = keyEvent.getKeyChar();
                if(c=='\n'){
                    Main.guessWord(guessWordBuffer);
                    guessWordBuffer="";
                    return;
                }
                guessWordBuffer += keyEvent.getKeyChar();
                Main.updateWord();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        };
    }

    public static void updateWord(){
        view.setForeLayoutRenderer(new LayerRenderer() {
            @Override
            public void render(Graphics2D graphics2D, GraphicGraph  graphicGraph, double v, int i, int i1, double v1, double v2, double v3, double v4) {
                graphics2D.setColor(Color.black);
                graphics2D.drawString(guessWordBuffer, 10, 30);
            }
        });
    }
    protected static void guessWord(String guess){
        Node n;
        Debug(guess);
        if((n=graph.getNode(guess))!=null){
            n.setAttribute("ui.label", n.getId());
            Debug("Found "+ guess);
        }
        if(guess.equals("give up")){
            for(Node m : graph.getNodeSet()) {
                m.setAttribute("ui.label", m.getId());
            }
        }
    }
    private static Viewer getDisplay(Graph graph) {
        return graph.display();
    }

    private static void initializeNodesEdges(WordGraph wg, Graph graph) {
        Set<String> nodes = wg.getNodes();
        Random r = new Random();
        for (String s : nodes) {
            graph.addNode(s + "");
            //show INITIAL_REVEAL % of nodes
            if(r.nextDouble()<INITIAL_REVEAL){
                graph.getNode(s + "").addAttribute("ui.label",
                        s+"");
            }
            else {
                graph.getNode(s + "").addAttribute("ui.label",
                        String.format(String.format("%%%ds", s.length()), " ").replace(" ", "?"));
            }
        }
        HashMap<String, List<String>> edges = wg.getEdges();
        for (String key : edges.keySet()){
            List<String> all = edges.get(key);
            for(String s: all){
                if(wg.contains(key) && wg.contains(s)) {
                    graph.addEdge(key + "" + s, key + "", s + "");
                }
            }
        }
    }

    private static Graph initializeDisplayGraph() {
        String styleSheet =
            "node { "
                    + "     shape: freeplane; "
                    + "     padding: 5px; "
                    + "     fill-color: white; "
                    + "     stroke-mode: plain; "
                    + "     size-mode: fit; "
                    + "} "
                    + "edge { "
                    + "     shape: freeplane; "
                    + "}";
        Graph graph = new MultiGraph("embedded");
        graph.setStrict(true);
        graph.addAttribute("ui.stylesheet", styleSheet);
        return graph;
    }

    private static void Debug (String s){
        System.out.println(s);
    }
}
