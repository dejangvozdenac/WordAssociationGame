import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.*;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import org.ibex.nestedvm.util.Seekable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class wordnetExample {
    public static final double CUTOFF = 0.3;
    private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator rc = new WuPalmer(db);
//    new HirstStOnge(db), new LeacockChodorow(db), new Lesk(db),,
//    new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db)

    private static double similarity( String word1, String word2 ) {
        WS4JConfiguration.getInstance().setMFS(false);
        double s = rc.calcRelatednessOfWords(word1, word2);
        return s;
    }
    public static void main(String[] args) throws Exception{
        Scanner sc = new Scanner(new File("/home/dejan/Desktop/Word Association/exampleStory.txt"));
        List<String> words = new ArrayList<String>();
        while(sc.hasNext()){
            String s = sc.next();
            words.add(s);
        }
        int size = words.size();
        int count = 0;
        for(int i = 0; i< size; i++){
            for(int j=i+1;j<size;j++){
                String a = words.get(i);
                String b = words.get(j);
                if(a.equalsIgnoreCase(b)||
                        a.length()<=3 ||
                        b.length()<=3){
                    continue;
                }
                double s = similarity(a, b);
                if(s>CUTOFF){
                    System.out.format("Similarity of %s and %s : %.2f\n",a,b, s);
                    count++;
                }
            }
        }
        double s = similarity( "house","hello" );
        System.out.format("Similarity : %.3f\n",s);
//        System.out.println(size);
//        System.out.println(count);
    }
}