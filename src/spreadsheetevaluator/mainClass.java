package spreadsheetevaluator;

public class mainClass {
public static void main(String args[]) {
    SpreadSheet sp=new SpreadSheet(2,2,
    		"B1",       "4 5 *",
            "A1 B2 /",  "2"
    );
    Double[] out=sp.dump();
    for (int p=0;p<out.length;p++) {
    	System.out.println(out[p]);	
    }
    
    }
}
