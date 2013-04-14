package spreadsheetevaluator;

import java.util.Stack;

public class SpreadSheet {
    
	private double[][] spreadsheet;
	private boolean[][] cycleDetector;
	private String[][] spreadsheetString; 
	
    public SpreadSheet(int nRows, int nCols, String... exprArray) {
        // TODO: put your code here
    	spreadsheetString=new String[nRows][nCols];
    	spreadsheet=new double[nRows][nCols];
    	cycleDetector=new boolean[nRows][nCols];
    	int k=0;
    	
    	for (int i=0;i<nRows;i++) {
    	for (int j=0;j<nCols;j++) {
    		
    			spreadsheetString[i][j]=exprArray[k++];
    		}
    	}
    	
    	//System.out.println("Count: "+count);
    }
   
    public Double[] dump() throws CircularReferenceException {
        // TODO: put your code here
    	for (int i=0;i<spreadsheet.length;i++) {
    		for (int j=0;j<spreadsheet[0].length;j++) {
    			spreadsheet[i][j]=Double.MIN_VALUE;
    		}
    	}
    	for (int i=0;i<spreadsheet.length;i++) {
    		for (int j=0;j<spreadsheet[0].length;j++) {
    				String[] splittedCell=spreadsheetString[i][j].split(" ");
    				resetCycleDetector();
    				spreadsheet[i][j]=evalsplitCell(splittedCell);
    		}		
    	}
    	int q=0;
    	Double[] spreadsheetOutput=new Double[spreadsheet.length*spreadsheet[0].length];
    	for (int i=0;i<spreadsheet.length;i++) {
    		for (int j=0;j<spreadsheet[0].length;j++) {
    			spreadsheetOutput[q++]=spreadsheet[i][j];
    		}
    	}
    	
        return spreadsheetOutput;
    }
    public class CircularReferenceException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        public CircularReferenceException(String msg) {
            super(msg);
        }
    }
    
    
    
    
    private double evalCellNametoValue(String cellName) {
    	//System.out.println("cellName: "+cellName);
    	int i=convertAlphabettoNumber(cellName.charAt(0));
    	int j=Integer.parseInt(cellName.charAt(1)+"")-1;
    	int swap=i;
    	i=j;
    	j=swap;
    	//System.out.println("Element: "+spreadsheet[i][j]);
    	if (spreadsheet[i][j]!=Double.MIN_VALUE) {
    		return spreadsheet[i][j];
    	}
    	else {
    		if (cycleDetector[i][j]!=true) {
    			cycleDetector[i][j]=true;
    			return(evalsplitCell(spreadsheetString[i][j].split(" ")));
    			
    		}
    		else {
    			throw new CircularReferenceException("CYCLE DETECTED IN GIVEN INPUT!");
    		}
    		
    	}
    }
    private double evalsplitCell(String[] splittedCell) {
    	// TODO Auto-generated method stub
    	
    	if (splittedCell.length==1) {
    		if (!isInteger(splittedCell[0])&&!isOperand(splittedCell[0])) {
    		return evalCellNametoValue(splittedCell[0]);
    		}
    		if(isInteger(splittedCell[0])||isDouble(splittedCell[0])) {
    			return Double.parseDouble(splittedCell[0]);
    		}
    	}
    	for (int z=0;z<splittedCell.length;z++) {
    		if (!isInteger(splittedCell[z])&&!isOperand(splittedCell[z])) {
    			
    			splittedCell[z]=evalCellNametoValue(splittedCell[z])+"";
    			
    		}
    	}
    		
    		//Reached here means it was an integer.
    		Stack<String> S=new Stack<String>();
    		for (int i=0;i<splittedCell.length;i++) {
    		S.push(splittedCell[i]);
    		}
    		return (rpneval(S));
    	//}
    	//return 0;
    }
    private void resetCycleDetector() {
    	// TODO Auto-generated method stub
    	for (int q=0;q<cycleDetector.length;q++) {
    		for (int w=0;w<cycleDetector[0].length;w++) {
    			cycleDetector[q][w]=false;
    		}
    	}
    	
    }
    private int convertAlphabettoNumber(char A) {
    	// TODO Auto-generated method stub
    	return (A-65);
    }
    private boolean isOperand(String string) {
            if (!string.equals("+")&&!string.equals("-")&&!string.equals("*")&&!string.equals("/")) 
            return false;
        return true;
    }

    private double rpneval(Stack<String> s) {
    	String firstItem="";
    	if (s.size()==1) {
    	firstItem=(String) s.pop();
    	}
    	if (isInteger(firstItem)&&s.size()==0) {
    		return Integer.parseInt(firstItem);
    	}
    	else if (isDouble(firstItem)&&s.size()==0) {
    		return Double.parseDouble(firstItem);
    	}
    	else {
    		Stack<String> s2=new Stack<String>();
    		//System.out.println("s.size(): "+s.size());
    		while (!s.empty()&&s.size()>1) { //Need to have &&!s2.empty()??
    			
    			
    			if (s.size()==1&&s2.size()>1) {
    				while (!s2.empty()) {
    				s.push(s2.pop());
    				}
    			}
    				
    				
    			String sfirst=(String) s.pop();
    			String sfirstnext=(String) s.pop();
    			double tempans=Double.MIN_VALUE;
    			//s2.push(sfirst);
    			if ((isInteger(sfirst)&&isInteger(sfirstnext))||isDouble(sfirst)&&isDouble(sfirstnext)) {
    				String operand=(String) s2.pop();
    				if (operand.equals("+")) {
    					tempans=Double.parseDouble(sfirst)+Double.parseDouble(sfirstnext);
    					s.push(tempans+"");
    					if (s2.size()>0&&isInteger((String) s2.peek())) {
    						s.push(s2.pop());
    					}
    				}
    				else if (operand.equals("-")) {
    					tempans=Double.parseDouble(sfirstnext)-Double.parseDouble(sfirst);
    					s.push(tempans+"");
    					if (s2.size()>0&&isInteger((String) s2.peek())) {
    						s.push(s2.pop());
    					}
    				}
    				else if (operand.equals("*")) {
    					tempans=Double.parseDouble(sfirst)*Double.parseDouble(sfirstnext);
    					s.push(tempans+"");
    					if (s2.size()>0&&isInteger((String) s2.peek())) {
    						s.push(s2.pop());
    					}
    				}
    				else if (operand.equals("/")) {
    					tempans=Double.parseDouble(sfirstnext)/Double.parseDouble(sfirst);
    					s.push(tempans+"");
    					if (s2.size()>0&&(isInteger((String) s2.peek())||isDouble((String) s2.peek()))) {
    						s.push(s2.pop());
    					}
    				}
    				
    			}
    			else if ((isInteger(sfirst)||isDouble(sfirst))&&isOperand(sfirstnext)) {
    				s2.push(sfirst);
    				s2.push(sfirstnext);
    			}
    			else if (isOperand(sfirst) && (isInteger(sfirstnext)||isDouble(sfirstnext))) {
    				s2.push(sfirst);
    				s.push(sfirstnext);
    			}
    			else if (isOperand(sfirst)&&isOperand(sfirstnext)) {
    				s2.push(sfirst);
    				s2.push(sfirstnext);
    			}
    		}
    		double actualans = 0;
    		String popped=(String)s.pop();
    		if (s.size()==0) {
    		actualans=Double.parseDouble(popped);
    		//System.out.println("ActualAns: "+actualans);
    		}
    		else System.out.println("ERROR: "+popped);
    		return actualans;
    	}
    		
    	}

    private boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        }
        return true;
    }

    private boolean isDouble(String s) {
        try { 
            Double.parseDouble(s); 
        } catch(NumberFormatException e) { 
            return false; 
        }
        return true;
    }
    
}