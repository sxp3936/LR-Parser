package edu.louisiana.cacs.csce450GProject;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class Parser {

	public static File fp;
	public static String inputFile;
	public static FileReader f;
	public static BufferedReader br;
	public static int charClass;
	public static int nextToken;
	public static int lexLen;
	public static char nextChar; 
	public static final int LETTER=0;
	public static final int DIGIT=1;
	public static final int UNKNOWN=99;
	public static final int IDENT=11;
	public static final int EOF=-1;
	public static final int ASSIGN_OP= 20;
	public static final int ADD_OP= 21;
	public static final int SUB_OP= 22;
	public static final int MULT_OP= 23;
	public static final int DIV_OP= 24;
	public static final int LEFT_PAREN= 25;
	public static final int RIGHT_PAREN =26;
	public static char[] lexeme=new char[100];
	
	private static Queue <String> inputData = new LinkedList <String>();
	private static Stack <String> pushStack = new Stack <String>();
	public Parser(String fileName){
		System.out.println("File to parse : "+fileName);
		inputFile = fileName;
	}
	
	private static String ParseTable[][] = new String [][]{{ "S5","", "","S4","","","1","2","3"},
        { "","S6", "","","","accept","","",""},
        { "","R2", "S7","","R2","R2","","",""},
        { "","R4", "R4","","R4","R4","","",""},
        { "S5","", "","S4","","","8","2","3"},
        { "","R6", "R6","","R6","R6","","",""},
        { "S5","", "","S4","","","","9","3"},
        { "S5","", "","S4","","","","","10"},
        { "","S6", "","","S11","","","",""},
        { "","R1", "S7","","R1","R1","","",""},
        { "","R3", "R3","","R3","R3","","",""},
        { "","R5", "R5","","R5","R5","","",""}};


	
	public static void parse() 
	
	{
		
		try
		{
		 fp  = new File(inputFile);
		}
		catch(Exception e)
		{
		
		System.out.println("File not found!");
			
		}
	     f=null;
		try {
			f = new FileReader(fp);
			br=new BufferedReader(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
			
		}
		try {
			getChar();
			do{
				nextToken = lex();
			}while(nextToken!=EOF);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String result = "";
		pushStack.push("0");
		inputData.remove("EOF");
		inputData.add("$");
		//System.out.println("\t\t\t\t\t"+"input \t\t\t"+"action \t\t"+"action \t\t"+"value \t\t"+"length \t\t\t"+"temp \t\t\t"+"goto \t\t"+"goto \t\t"+"stack");
		//System.out.println("stack"+"tokens \t\t\t"+"lookup \t\t"+"value \t\t"+"of LHS \t\t\t"+"of RHS \t\t\t"+"stack \t\t"+"lookup \t\t"+"value \t\t"+"action");
		String out = String.format("%-30s %-30s %-10s %-15s %-10s %-10s %-30s %-10s %-10s %-15s", " ","input","action","action","value","length","temp","goto","goto","stack");
		System.out.println(out);
		out = String.format("%-30s %-30s %-10s %-15s %-10s %-10s %-30s %-10s %-10s %-15s","stack","tokens","lookup","value","of LHS","of RHS","stack","lookup","value","action");
		System.out.println(out);
		int symbol = 0;
		do{
			out = String.format ("%-30s",pushStack);
			System.out.print(out);
           String q_head = (String)inputData.peek();
           
           if (q_head.equals("id"))
           {
        	   symbol=0;
           }
           
           else if (q_head.equals("+"))
        	   
           {
        	   symbol=1;
           }
           else if (q_head.equals("*"))
        	   
           {
        	   symbol=2;
           }
           else if (q_head.equals("("))
       	   
          {
       	   symbol=3;
          }
          else if (q_head.equals(")"))
       	   
          {
       	   symbol=4;
          }
          else if (q_head.equals("$"))
       	   
          {
       	   symbol=5;
          }
          else
        	  System.out.println("Error in Grammar");
           
	      /* switch(q_head){
	           case "id": symbol = 0;break;
	           case "+": symbol = 1;break;
	           case "*": symbol = 2;break;
	           case "(": symbol = 3;break;
	           case ")": symbol = 4;break; 
	           case "$": symbol = 5;break;
	           default: break;
           } */
           
	       String tv = pushStack.peek();
	       int state = getLastDigit(tv);
	       //System.out.print(inputData+"\t");
	       out = String.format ("%-30s",inputData);
			System.out.print(out);
	       result = ProcessParse(state, symbol);		       
       }while (!result.equals("accept"));
	
		
		
	}
	
	public static String ProcessParse (int state,int symbol){
		String parseValue = ParseTable[state][symbol];
	    if(parseValue.charAt(0)=='S'){
	    	processShift(parseValue, state);
	    }else{
	    	if(parseValue.equals("R1"))
	    	{
	    		processReduce("R1", state, symbol);
	    	}
	    	else if(parseValue.equals("R2"))
	    	{
	    		processReduce("R2", state, symbol);
	    	}
	    	else if(parseValue.equals("R3"))
	    	{
	    		processReduce("R3", state, symbol);
	    	}
	    	else if(parseValue.equals("R4"))
	    	{
	    		processReduce("R4", state, symbol);
	    	}
	    	else if(parseValue.equals("R5"))
	    	{
	    		processReduce("R5", state, symbol);
	    	}
	    	else if(parseValue.equals("R6"))
	    	{
	    		processReduce("R6", state, symbol);
	    	}
	    	else if(parseValue.equals("accept"))
	    	{
	    		String out = String.format ("%-10s %-15s","["+state+","+inputData.element()+"]","accept");
    			System.out.print(out);
	    	}
	    		
	    	/*switch(parseValue){
	    		case "R1" : processReduce("R1", state, symbol);
	    					break;
	    		case "R2" : processReduce("R2", state, symbol);
	    					break;
	    		case "R3" : processReduce("R3", state, symbol);
	    					break;
	    		case "R4" : processReduce("R4", state, symbol);
	    					break;
	    		case "R5" : processReduce("R5", state, symbol);
	    					break;
	    		case "R6" : processReduce("R6", state, symbol);
	    					break;
	    		case "accept": //System.out.print("["+state+","+inputData.element()+"]"+"\t");
                			//System.out.println("accept"+"\t"); 
                			String out = String.format ("%-10s %-15s","["+state+","+inputData.element()+"]","accept");
                			System.out.print(out);
                			break;
	    	}*/
	    }
	    return parseValue;
	}
	
	public static String processRules(String type){
		String lhs = "";
		String symbol = "";
		if(type.equals("R1"))
		{
			lhs = "E";
			timePop(3, lhs);
			symbol = "6";
		}
		else if(type.equals("R2"))
		{
			lhs = "E";
			timePop(1, lhs);
			symbol = "6";
		}
		else if(type.equals("R3"))
		{
			lhs = "T";
			timePop(3, lhs);
			symbol = "7";
		}
		else if(type.equals("R4"))
		{
			lhs = "T";
			timePop(1, lhs);
			symbol = "7";
		}
		else if(type.equals("R5"))
		{
			lhs = "F";
			timePop(3, lhs);
			symbol = "8";
		}
		else if(type.equals("R6"))
		{
			lhs = "F";
			timePop(1, lhs);
			symbol = "8";
		}
			return lhs+ "#" + symbol;
		
		/*switch(type){
		case "R1":	lhs = "E";
					timePop(3, lhs);
					symbol = "6";
					break;
		case "R2": lhs = "E";
					timePop(1, lhs);
					symbol ="6";
					break;
		case "R3": lhs = "T";
					timePop(3,lhs);
					symbol = "7";
					break;
		case "R4":	lhs = "T";
					timePop(1,lhs);
					symbol = "7";
					break;
		case "R5": lhs = "F";
				timePop(3,lhs);
				symbol = "8";
				break;
		case "R6":	lhs = "F";
				timePop(1,lhs);
				symbol = "8";
				break;
		}
		return lhs+ "#" + symbol;*/
	}
	
	public static void timePop(int count, String lhs){
		//System.out.print(lhs+"\t");
		//System.out.print(Integer.toString(count)+"\t");
		String out = String.format ("%-10s %-10s",lhs,Integer.toString(count));
		System.out.print(out);
		for(int i=0;i<count;i++){
			pushStack.pop();
		}
	}
	
	public static void processReduce(String type, int state, int symbol){
		//System.out.print("["+state+","+inputData.element()+"]"+"\t");
		//System.out.print(type+"\t");
		String out = String.format ("%-10s %-15s","["+state+","+inputData.element()+"]",type);
		System.out.print(out);
		String rule = processRules(type);
		String LHS = rule.split("#")[0];
		int goto_symbol = Integer.parseInt(rule.split("#")[1]);
		//System.out.print(pushStack+"\t");
		out = String.format ("%-30s",pushStack);
		System.out.print(out);
        state = getLastDigit(pushStack.peek());
        //System.out.print("["+state+","+LHS+"]"+"\t");
        out = String.format ("%-10s","["+state+","+LHS+"]");
		System.out.print(out);
        int goto_value = Integer.parseInt(ParseTable[state][goto_symbol]);
        //System.out.print(goto_value+"\t");
        //System.out.println("Push "+LHS+goto_value + "\t");
        out = String.format("%-10s %-15s",goto_value,"Push "+LHS+goto_value );
		System.out.println(out);
        pushStack.push(LHS+Integer.toString(goto_value));
	}
	
	public static void processShift(String parseValue, int state){
		/*System.out.print("["+state+","+inputData.element()+"]"+"\t");
		System.out.print(parseValue+"\t");
		System.out.print("\t\t\t\t\t");*/
		String out = String.format ("%-10s %-15s %-10s %-10s %-30s %-10s %-10s","["+state+","+inputData.element()+"]",parseValue,"","","","","");
		System.out.print(out);
		String toPush = Integer.toString(getLastDigit(parseValue));
		//System.out.println("push "+inputData.element()+toPush+"\t");
		out = String.format ("%-15s","push "+inputData.element()+toPush);
		System.out.println(out);
		pushStack.push(inputData.remove() + toPush);
	}
	
	public static int getLastDigit(String data){
		if(data.length()>1){
		int i=data.length()-1;
		while(i>=0 &&Character.isDigit(data.charAt(i))){
			i--;
		}
		return Integer.parseInt(data.substring(i+1, data.length()));
		}else {
			return Integer.parseInt(data);
		}
				
	}
	public static int lex() throws IOException {
		// TODO Auto-generated method stub
		lexLen=0;
		getNonBlank();
		if(charClass == LETTER){
			 addChar();
	            getChar();
	           
	            int i=0;
	            while(charClass == LETTER || charClass == DIGIT)
	            {
	            	
	                 addChar();
	                 getChar();
	                 i++;
	            }
	            nextToken =IDENT;
		}
		else if(charClass== DIGIT){
			 addChar();
	            getChar();
	           while(charClass == DIGIT)
	           {
	                addChar();
	                getChar();
	           }
	           nextToken =IDENT;
		}
		else if(charClass== UNKNOWN){
			nextToken=lookup(nextChar);
            getChar();
            if(nextToken==EOF)
            {
            	lex();
            	
		    }
		}
		else if(charClass==EOF){
			
			nextToken = EOF;
        lexeme[0] ='E';
        lexeme[1] ='O';
        lexeme[2] ='F';
        lexeme[3] = 0;
        return nextToken;
		}
		
		/*switch(charClass)
		{
        case LETTER :
            addChar();
            getChar();
           
            int i=0;
            while(charClass == LETTER || charClass == DIGIT)
            {
            	
                 addChar();
                 getChar();
                 i++;
            }
            nextToken =IDENT;
            break;
        case DIGIT :
            addChar();
            getChar();
           while(charClass == DIGIT)
           {
                addChar();
                getChar();
           }
           nextToken =IDENT;
           break;
        case UNKNOWN :
            nextToken=lookup(nextChar);
            getChar();
            if(nextToken==EOF)
            {
            	lex();
            	break;
		    }
            
            break;
        case EOF:
        	
            nextToken = EOF;
            lexeme[0] ='E';
            lexeme[1] ='O';
            lexeme[2] ='F';
            lexeme[3] = 0;
            return nextToken;
           }*/
		
		String s ="";
       int i=0;
		while(lexeme[i]!=0)
			
		{
			
			s=s+lexeme[i];
			i++;
		}	
		if(!s.equals("EOF")){
			inputData.add(s.trim());
		}
	//inputData.add(s.trim());
	return nextToken;
		
	}
	public static int lookup(char nextChar2) {

// TODO Auto-generated method stub

		 
            switch(nextChar2)
	     {
	         case '(' :
	                    addChar();
	                    nextToken = LEFT_PAREN;
	                    break;
	         case ')' :
	                     addChar();
	                     nextToken = RIGHT_PAREN;
	                     break;
	         case '+' :
	                    addChar();
	                    nextToken = ADD_OP;
	                    break;	
	         case '-' :
	                    addChar();
	                    nextToken = SUB_OP;
	                    break;
	       

	     case '*' :
	                    addChar();
	                    nextToken = MULT_OP;
	                    break;
	     case '/' :
	                    addChar();
	                    nextToken = DIV_OP;
	                    break;
	     default:
	                   
	    	           
	    	           addChar();
	                   nextToken = EOF;
	                   break;
	     }
	     return nextToken;
	
	}	
		
		
		
	public static void addChar() {
		// TODO Auto-generated method stub
		  if(lexLen <= 98)
		     {
		          lexeme[lexLen++] = nextChar;
		          lexeme[lexLen] = 0;
		     }
		     else
		          System.out.println("Error:lexeme is too long");

	}
	public static void getNonBlank() throws IOException {
		// TODO Auto-generated method stub
	     while(Character.isSpaceChar(nextChar))
	           getChar();

		
		
	}
	public static void getChar() throws IOException {
		// TODO Auto-generated method stub
		
		int i;
		
            		
			if((i=br.read())!=EOF )
			{
			
				nextChar=(char)i;
				if(Character.isAlphabetic(nextChar))
				{
					charClass=LETTER;
				
				}
				else if(Character.isDigit(nextChar))
				{
					
					charClass=DIGIT;
				}
				else
				{
					charClass=UNKNOWN;
				
				}
			}
		    
		
		
		else
		{
		
			charClass=EOF;
		}
		
	}
	
          
}
