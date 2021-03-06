/**
*Takes a given .txt that is really a FiM++ file and ceates tokens from it
*@author Brandon Ehrmanntraut
*/

import java.util.*;
import java.io.*;
public class token{
	public static void main(String[] args){
		
	}
	
	
	private ArrayList<String> lines = new ArrayList<String>();
	private HashMap<String,String> keys = new HashMap<String,String>();//find the keywords
	private ArrayList<String> symbolTable = new ArrayList<String>();
	private ArrayList<String> tokens = new ArrayList<String>();
	private ArrayList<Method> methods = new ArrayList<Method>();
	private ErrorList errors;
	/**
	*Class constructor
	*@param FIM the file that is to be read
	*@param errors an object to track all the errors I find
	*/
	public token(String FIM, ErrorList errors){
		this.errors = errors;
		lines = read(FIM);
		buildKeywords();
	}
	
	/**
	*Returns the symbol table
	*@return ArrayList<String> the symbol table
	*/
	public ArrayList<String> getSymbolTable(){
		return symbolTable;
	}
	
	/**
	*Reads the given input file
	*@param FIM the name of the file to open
	*@return ArrayList<Stirng> all of the lines
	*/
	private ArrayList<String> read(String FIM){
		ArrayList<String> holder = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(FIM))) {
			String line;
			//System.out.println(line);
			while ((line = br.readLine()) != null) {
			   line = line.trim();
			   holder.add(line);
			}
		}catch(Exception E){
			
		}
		return holder;
	}
	
	/**
	*Instantiates the valid keywords and their meanings
	*/
	private void buildKeywords(){
		//first is the keywords
		//Report related keywords
		keys.put("Dear", "beginfile");
		keys.put("Yourfaithfulstudent", "endfile");
		keys.put("and", "interfacelisting"); 
		keys.put("RememberwhenIwroteabout", "import");
		//punctuation
		keys.put("!","punc");
		keys.put(",","punc");
		keys.put(".","punc");
		keys.put(":","punc");
		keys.put("?","punc");
		keys.put("...","punc");
		keys.put("‽","punc");
		//mane method
		keys.put("TodayIlearned","mane");
		keys.put("That'sallabout","endMainfunc");
		//print
		keys.put("Iremembered","print");
		keys.put("Isaid","print");
		keys.put("Isang","print");
		keys.put("Iwrote","print");
		//variables
		keys.put("Didyouknowthat","varDec");
		keys.put("nothing","null");
			//Boolean
			keys.put("anargument","Bool");
			keys.put("argument","Bool");
			keys.put("theargument","Bool");
			keys.put("logic","Bool");
			keys.put("thelogic","Bool");
				//true
				keys.put("correct","true");
				keys.put("right","true");
				keys.put("true","true");
				keys.put("yes","true");
				//false
				keys.put("false","false");
				keys.put("incorrect","false");
				keys.put("no","false");
				keys.put("wrong","false");
			//asign
			keys.put("arenow","asign");
			keys.put("become","asign");
			keys.put("becomes","asign");
			keys.put("isnow","asign");
			keys.put("nowlike","asign");
			keys.put("nowlikes","asign");
			//number
			keys.put("number","double");//there is no distinction between number types in FiM so will be implementing the most encompasing type in Java
			keys.put("anumber","double");
			keys.put("thenumber","double");
			//character
			keys.put("a character","char");
			keys.put("character","char");
			keys.put("letter","char");
			keys.put("aletter","char");
			keys.put("thecharacter","char");
			keys.put("theletter","char");
			//String
			keys.put("aphrase","string");
			keys.put("aquote","string");
			keys.put("asentence","string");
			keys.put("aword","string");
			keys.put("characters","string");
			keys.put("letters","string");
			keys.put("phrase","string");
			keys.put("quote","string");
			keys.put("sentence","string");
			keys.put("the characters","string");
			keys.put("the letters","string");
			keys.put("the phrase","string");
			keys.put("the quote","string");
			keys.put("the sentence","string");
			keys.put("the word","string");
			keys.put("word","string");
		//arithmatic
			//addition
			keys.put("addedto","addInfix");
			keys.put("plus","addInfix");
			keys.put("add","addPrefix");
			keys.put("gotonemore","increment");
			// and is also an infix here, but as it is used to represent mutlpile different things, it is not identified here
			//subtraction
			keys.put("minus","subInfix");
			keys.put("without","subInfix");
			keys.put("subtract","subPrefix");
			keys.put("thediferencebetween","subPrefix");
			keys.put("gotoneless","deccrement");
			//multiplication
			keys.put("multipliedwith","multInfix");
			keys.put("times","multInfix");
			keys.put("multiply","multPrefix");
			keys.put("theproductof","multPrefix");
			//division
			keys.put("dividedby","divInfix");
			keys.put("divide","divPrefix");
		//arrays
			//Boolean
			keys.put("arguments","boolArray");
			keys.put("the arguments","boolArray");
			keys.put("logics","boolArray");
			keys.put("the logics","boolArray");
			//numbers
			keys.put("many numbers","numArray");
			keys.put("numbers","numArray");
			keys.put("the numbers","numArray");
			//Stirng
			keys.put("many phrases","strArray");
			keys.put("many words","strArray");
			keys.put("phrases","strArray");
			keys.put("many sentences","strArray");
			keys.put("many quotes","strArray");
			keys.put("quotes","strArray");
			keys.put("sentences","strArray");
			keys.put("the phrases","strArray");
			keys.put("the quotes","strArray");
			keys.put("the sentences","strArray");
			keys.put("the words","strArray");
			keys.put("words","strArray");
			//char array is considered a string, so not implemented
		//Boolean operators
		keys.put("and","and");
		keys.put("or","or");
		keys.put("not","not");
		keys.put("either","XOR");//needs unique case in generator
		//if else
		keys.put("If","if");
		keys.put("When","if");
		keys.put("then","then");
		keys.put("Otherwise","else");
		keys.put("Orelse","else");
		keys.put("That'swhatIwoulddo","endConditional");
		//switch
		keys.put("Inregardsto","switch");
		keys.put("Onthe","case");
		keys.put("ndhoof","caseClose");
		keys.put("hoof","caseClose");
		keys.put("rdhoof","caseClose");
		keys.put("sthoof","caseClose");
		keys.put("thhoof","caseClose");
		keys.put("Ifallelsefails","defaultCase");
		keys.put("That'swhatIdid","switchClose");
		//numbers to booleans
			//equals
			keys.put("had","equals");
			keys.put("has","equals");
			keys.put("is","equals");
			keys.put("was","equals");
			keys.put("were","equals");
			//not equals
			keys.put("hadn't","notEqual");
			keys.put("hadnot","notEqual");
			keys.put("hasn't","notEqual");
			keys.put("hasnot","notEqual");
			keys.put("isn't","notEqual");
			keys.put("isnot","notEqual");
			keys.put("wasn't","notEqual");
			keys.put("wasnot","notEqual");
			keys.put("weren't","notEqual");
			keys.put("werenot","notEqual");
			//greater than
			keys.put("hadmorethan","greaterThan");
			keys.put("hasmorethan","greaterThan");
			keys.put("isgreaterthan","greaterThan");
			keys.put("ismorethan","greaterThan");
			keys.put("wasgreaterthan","greaterThan");
			keys.put("wasmorethan","greaterThan");
			keys.put("weregreaterthan","greaterThan");
			keys.put("weremorethan","greaterThan");
			//greater than or equal to
			keys.put("hadnolessthan","greaterThanOrEqual");
			keys.put("hasnolessthan","greaterThanOrEqual");
			keys.put("isnolessthan","greaterThanOrEqual");
			keys.put("isnotlessthan","greaterThanOrEqual");
			keys.put("isn'tlessthan","greaterThanOrEqual");
			keys.put("wasnolessthan","greaterThanOrEqual");
			keys.put("wasnotlessthan","greaterThanOrEqual");
			keys.put("wasn'tlessthan","greaterThanOrEqual");
			keys.put("werenolessthan","greaterThanOrEqual");
			keys.put("werenotlessthan","greaterThanOrEqual");
			keys.put("weren'tlessthan","greaterThanOrEqual");
			//less than
			keys.put("hadlessthan","lessThan");
			keys.put("haslessthan","lessThan");
			keys.put("islessthan","lessThan");
			keys.put("waslessthan","lessThan");
			keys.put("werelessthan","lessThan");
			//less than or equal to : there are 19 of these...
			keys.put("hadnomorethan","lessThanOrEqual");
			keys.put("hasnomorethan","lessThanOrEqual");
			keys.put("isnogreaterthan","lessThanOrEqual");
			keys.put("isnomorethan","lessThanOrEqual");
			keys.put("isnotgreaterthan","lessThanOrEqual");
			keys.put("isnotmorethan","lessThanOrEqual");
			keys.put("isn'tgreaterthan","lessThanOrEqual");
			keys.put("isn'tmorethan","lessThanOrEqual");
			keys.put("wasnogreaterthan","lessThanOrEqual");
			keys.put("wasnomorethan","lessThanOrEqual");
			keys.put("wasnotgreaterthan","lessThanOrEqual");
			keys.put("wasnotmorethan","lessThanOrEqual");
			keys.put("wasn'tgreaterthan","lessThanOrEqual");
			keys.put("wasn'tmorethan","lessThanOrEqual");
			keys.put("werenogreaterthan","lessThanOrEqual");
			keys.put("werenomorethan","lessThanOrEqual");
			keys.put("werenotgreaterthan","lessThanOrEqual");
			keys.put("werenotmorethan","lessThanOrEqual");
			keys.put("weren'tgreaterthan","lessThanOrEqual");
			keys.put("weren'tmorethan","lessThanOrEqual");
		//for loops
		keys.put("Forevery","for");
		keys.put("to","setMaxInFor");
		keys.put("from","subInfix2");//bad solution... hopefully this doesn;t break things
		keys.put("in","forEach");
		//methods
		keys.put("as","param");
		keys.put("with","returnType");
		keys.put("toget","returnType");
		keys.put("using","param");
		keys.put("Ilearned","para");
		keys.put("Thenyouget","return");
		keys.put("Iremembered","callPara");
		keys.put("Iwould","callPara");
		// N is being used for new line, since I am removing it in the process it will be added in automatically at the end of each line
	
	}
	
	/**
	*Takes all the lines and turns them into tokens and then into CFG symbols
	*Also builds the symbol table
	*@return String[] the list of tokens
	*/
	public String[] run(){
		ArrayList<String[]> line = splits();
		
		Boolean varComing = false;
		//going to do the first line in its own function because it is weird
		for(int i=1;i<line.size();i++){
			findMethods(line.get(i));
		}
		for(Method m : methods){
			keys.put(m.getName(),m.getReturnType());
			keys.putAll(m.getVarMapping());
		}
		try{
			firstLine(line.get(0));
		}catch(IndexOutOfBoundsException ii){
			System.out.println("\nUnable to read file\n");
			System.exit(0);
		}
		tokens.add("n");
		for(int l=1;l<line.size();l++){//for each line
			doLine(line.get(l),l);
			tokens.add("n");
		}
		tokens.remove(tokens.size()-1);//remove the last newline as the file ends
		//System.out.println(keys);
		return tokens.toArray(new String[tokens.size()]);
		
		
	}
	
	/**
	*Returns all of the already generated method objects
	*@return ArrayList<Method> all of the methods for this report
	*/
	public ArrayList<Method> getMethods(){
		return methods;
	}
	
	/**
	*Builds the first line of tokens, done seperately because this line is weird
	*@param line the line itself
	*/
	private void firstLine(String[] line){
		//the big question is if there is an interface listed or not
		ArrayList<String> lines = new ArrayList<String>(Arrays.asList(line));
		ArrayList<Integer> varLoc = new ArrayList<Integer>();
		ArrayList<Integer> varEnd = new ArrayList<Integer>();
		ArrayList<Integer> varPos = new ArrayList<Integer>();
		ArrayList<String> thisLineTokens = new ArrayList<String>();
		Boolean first=true;
		if(lines.contains("and")){
			//variables start at 1, after and, and after the first punc
			for(int i=0;i<lines.size();i++){
				if(isKeyword(lines.get(i))){
					thisLineTokens.add(keys.get(lines.get(i)));
					
					if(lines.get(i).equals("Dear")){
						varLoc.add(i+1);
						varPos.add(thisLineTokens.size());
					}else if(lines.get(i).equals("and")){
						varLoc.add(i+1);
						varEnd.add(i-1);
						varPos.add(thisLineTokens.size());
					}else if(first && containsPunc(lines.get(i))){
						varLoc.add(i+1);
						varEnd.add(i-1);
						varPos.add(thisLineTokens.size());
						first=false;
					}else if(containsPunc(lines.get(i))){
						varEnd.add(i-1);
					}
				}
			}
			if(!varLoc.isEmpty()){
				if(varLoc.size()!=varEnd.size()){
					varEnd.add(varLoc.get(varLoc.size()-1));
				}
				ArrayList<String> allVars = getVars(varLoc,varEnd,line);
				ArrayList<String> types = new ArrayList<String>(Arrays.asList("class","interface","report"));
				keys.put(allVars.get(0),"class");
				keys.put(allVars.get(1),"interface");
				keys.put(allVars.get(2),"report");
				symbolTable.add(allVars.get(0));
				symbolTable.add(allVars.get(1));
				symbolTable.add(allVars.get(2));
				thisLineTokens = mergeTokens(thisLineTokens,types,varPos);
			}
		}else{
			//variables at 1 and after the first punc
			for(int i=0;i<lines.size();i++){
				if(isKeyword(lines.get(i))){
					thisLineTokens.add(keys.get(lines.get(i)));
					
					if(lines.get(i).equals("Dear")){
						varLoc.add(i+1);
						varPos.add(thisLineTokens.size());
					}else if(first && containsPunc(lines.get(i))){
						varLoc.add(i+1);
						varEnd.add(i-1);
						varPos.add(thisLineTokens.size());
						first=false;
					}else if(containsPunc(lines.get(i))){
						varEnd.add(i-1);
					}
				}
			}
			if(!varLoc.isEmpty()){
				if(varLoc.size()!=varEnd.size()){
					varEnd.add(varLoc.get(varLoc.size()-1));
				}
				ArrayList<String> allVars = getVars(varLoc,varEnd,line);
				ArrayList<String> types = new ArrayList<String>(Arrays.asList("class","report"));
				keys.put(allVars.get(0),"class");
				keys.put(allVars.get(1),"report");
				symbolTable.add(allVars.get(0));
				symbolTable.add(allVars.get(1));
				thisLineTokens = mergeTokens(thisLineTokens,types,varPos);
			}
		}
		tokens.addAll(thisLineTokens);
	}
	
	/**
	*Seperates each line into its individual words
	*@return ArrayList<String[]> An arraylist of string arrays where each string[] is a line
	*/
	private ArrayList<String[]> splits(){
		ArrayList<String[]> temp = new ArrayList<String[]>();
		for(int i=0;i<lines.size();i++){
			temp.add(lines.get(i).split(" "));
		}
		//seperate out punctuation items
		for(int i=0;i<temp.size();i++){//go through each line
			Boolean seen = false;
			String[] edited = new String[0];
			int cont=0;
			if(temp.get(i).length==1){
				if(temp.get(i)[0].isEmpty()){
					continue;//empty line
				}
			}
			for(int j=0;j<temp.get(i).length;j++){//check if each word has punctuation at the end of it
			
				if(seen && containsPunc(temp.get(i)[j])){
					edited = sepPunc(edited,j+cont);
					cont++;
				}else if(containsPunc(temp.get(i)[j])){
					edited = sepPunc(temp.get(i),j);
					seen=true;
					cont++;
				}
			}
			if(edited.length>0){
				temp.set(i,edited);
			}
		}
		return temp;
	}
	
	/**
	*Builds the key to search the map for
	*@param line the current line to build from
	*@param s the length
	*@param i the start position
	*@return String[] A String[] built from line starting at item i going for s words
	*/
	private String build(String[] line, int s, int i){
		String temp = "";
		for(int j=0;j<i;j++){
			temp = temp.concat(line[j+s]);
		}
		return temp;
	}
	
	/**
	*Checks to see if the most recently used keyword is setting up for a non-keyword item after it
	*@param keyWord the previously  found key
	*@return Boolean true if a user defined name is incoming
	*/
	private Boolean specialKeyword(String keyWord){
		if(keyWord=="beginfile"){
			return true;
		}else if(keyWord=="interface listing"){
			return true;
		}else if(keyWord=="import"){
			return true;
		}else if(keyWord=="mane"){
			return true;
		}else if(keyWord=="varDec"){
			return true;
		}
		//no user defined names incoming. very nice
		return false;
	}
	
	/**
	*Tests for if a buffer will be needed before looking for the variable
	*@param keyWord the keyword to check
	*@return true if it is one of the pre-selected keywords needed to look for
	*/
	private Boolean isOtherSpecialKeyword(String keyWord){
		if(keyWord=="endfile"){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	*Just checks if a paragraph is being declared
	*/
	private Boolean isPara(String keyWord){
		if(keyWord=="voidType"){
			return true;
		}else if(keyWord.equals("returnsNumType")){
			return true;
		}else if(keyWord.equals("returnsCharType")){
			return true;
		}else if(keyWord.equals("returnsBoolType")){
			return true;
		}else if(keyWord.equals("returnsStrType")){
			return true;
		}else if(keyWord.equals("returnsBoolArrayType")){
			return true;
		}else if(keyWord.equals("returnsStrArrayType")){
			return true;
		}else if(keyWord.equals("returnsNumArrayType")){
			return true;
		}else if(keyWord.equals("returnsCharArrayType")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	*Checks if a given string has a punctuation at the end of it
	*@param str the string to check
	*@return true if there is a punction at the end of the string provided
	*/
	private Boolean containsPunc(String str){
		int x = str.length()-1;
		if(str.charAt(x)==(',')){
			return true;
		}else if(str.charAt(x)==('!')){
			return true;
		}else if(str.charAt(x)==('.')){
			//this one also covers elipse... may need to check for this one specifically again
			return true;			
		}else if(str.charAt(x)==('?')){
			return true;
		}else if(str.charAt(x)==(':')){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	*Sets the punctuation mark in the given word to be its own item in the array
	*@param line the String[] that represents the line
	*@param word the word in the line that contains the punctuation mark
	*@return String[] 
	*/
	private String[] sepPunc(String[] line, int word){
		String[] temp = new String[line.length+1];
		if(line[word].contains("...")){
			//Special case because this could be 3 or 1 punctuation mark
			Boolean mark=false;
			for(int i=0;i<temp.length;i++){//for each word in the line
				if(i==word){//I care about this spot
					temp[i]=line[i].substring(0,line[i].length()-3); //the main word
					temp[i+1]=line[i].substring(line[i].length()-3); //the punctuation mark
					i++;
					mark=true;
				}else{//not the place I care about
					if(mark){
						temp[i]=line[i-1];
					}else{
						temp[i]=line[i];
					}
				}
			}
		}else{
			Boolean mark=false;
			for(int i=0;i<temp.length;i++){//for each word in the line
				if(i==word){//I care about this spot
					temp[i]=line[i].substring(0,line[i].length()-1); //the main word
					temp[i+1]=line[i].substring(line[i].length()-1); //the punctuation mark
					i++;
					mark=true;
				}else{//not the place I care about
					if(mark){
						temp[i]=line[i-1];
					}else{
						temp[i]=line[i];
					}
				}
			}
		}
		
		return temp;//placeholder
	}
	
	/**
	*Tests if a given string is a keyword
	*@param key the keyword to look for
	*@return true if the given String is a valid keyword
	*/
	private Boolean isKeyword(String key){
		if(keys.containsKey(key)){
			return true;
		}else{
			return false;
		}
	}
	
	private Boolean alreadyBegunUnknown=false;
	private ArrayList<Integer> tokenPos = new ArrayList<Integer>();
	/**
	*A breakdown of run that tokenizes a single line - void as all needed things to write to are already global variables
	*@param line, the String[] that makes up the line
	*@param lineNum the number of this line, used for errors
	*/
	private void doLine(String[] line, int lineNum){
		ArrayList<String> thisLinesTokens = new ArrayList<String>();
		ArrayList<Integer> varLoc = new ArrayList<Integer>();
		ArrayList<Integer> varLocStr = new ArrayList<Integer>();
		ArrayList<Integer> varEndStr = new ArrayList<Integer>();
		ArrayList<Integer> varEnd = new ArrayList<Integer>();
		tokenPos = new ArrayList<Integer>();
		LineVars vars = new LineVars();
		Boolean lookForEnd=false;
		Boolean specialCase=false;
		Boolean stringsPresent=false;
		Boolean lookingForInfix=false;
		Boolean varNameInForLoop=false;
		Boolean needFullDec=false;
		Boolean needEndOfUnknown=false;
		int beginOfUnknown=0;
		int endOfUnkown=0;
		int callingMethod=0;
		ArrayList<Method> methodsCalledOnThisLine = new ArrayList<Method>();
		ArrayList<String> storedParams = new ArrayList<String>();
		for(int start=0;start<line.length;start++){//each start word
			endOfUnkown=start;
			for(int len=line.length;len>=0;len--){//each length
				if(start+len>line.length){
					continue;
				}
				String cur = build(line,start,len);
				//System.out.println(cur);
				//System.out.println(tokens);
				//System.out.println(varLoc + "\t" + varEnd);
				//System.out.println(symbolTable);
				if(methods.contains(new Method(cur))){
					callingMethod++;
					methodsCalledOnThisLine.add(methods.get(methods.indexOf(new Method(cur))));
				}
				if(lookingForInfix && isInfix(cur)){
					buildUnknownPhraseException(needEndOfUnknown,beginOfUnknown,endOfUnkown,lineNum,line);
					needEndOfUnknown=false;
					String infixType = getInfixType(thisLinesTokens, cur);
					thisLinesTokens.add(infixType);
					if(lookForEnd){
						if(specialCase){
							specialCase=false;
						}else{
							varEnd.add(start-1);
							lookForEnd=false;
						}
						
					}
					start+=len;
					start--;
					lookingForInfix=false;
				}else if(isKeyword(cur)){
					buildUnknownPhraseException(needEndOfUnknown,beginOfUnknown,endOfUnkown,lineNum,line);
					needEndOfUnknown=false;
					if(keys.get(cur).equals("double")){
						if(trueDouble(cur)){
							thisLinesTokens.add(keys.get(cur));
						}else{
							thisLinesTokens.add("numType");
						}
					}else{
						thisLinesTokens.add(keys.get(cur));
					}
					if(lookForEnd){
						if(specialCase){
							specialCase=false;
						}else{
							varEnd.add(start-1);
							lookForEnd=false;
						}
						
					}
					start+=len;
					if(symbolTable.contains(cur) || methods.contains(new Method(cur))){
						vars.add(cur);
						//symbolTable.add(cur);
						//System.out.println("Adding to symbol table: " + cur);
					}
					if(specialKeyword(keys.get(cur)) || varNameInForLoop){//next position starts a user defined name
						varLoc.add(start);
						lookForEnd=true;
						tokenPos.add(thisLinesTokens.size());
						varNameInForLoop=false;
						vars.addBlank();
					}else if(isOtherSpecialKeyword(keys.get(cur))){//the position after the next position starts a user defined name
						varLoc.add(start+1);
						lookForEnd=true;
						specialCase=true;
						tokenPos.add(thisLinesTokens.size()+1);
						vars.addBlank();
					}else if(isPrefix(cur)){
						//need to look for the special infix item
						lookingForInfix=true;
					}else if(keys.get(cur).equals("for")){
						varNameInForLoop=true;
					}else if(isPara(keys.get(cur)) && needFullDec){//I see a paragraph and its beign declared here
						needFullDec=false;
						//symbolTable.add(cur); this line was originally need, but is now redundently add in the name of the paragraph
						Method tempMethod = new Method(cur);
						for(Method m : methods){
							if(m.equals(tempMethod)){
								tempMethod = m;
								break;
							}
						}
						storedParams.addAll(tempMethod.getParamVars());
					}else if(keys.get(cur).equals("para")){//I see the para keyword
						needFullDec=true;
					}
					start--;
					break;
				}else if(isLiteral(cur)){//this needs to be treated as both a keyword and a variable right now
					buildUnknownPhraseException(needEndOfUnknown,beginOfUnknown,endOfUnkown,lineNum,line);
					needEndOfUnknown=false;
					String thisLinesLiteralType = getLiteralType(cur);
					thisLinesTokens.add(thisLinesLiteralType);
					if(lookForEnd){
						if(specialCase){
							specialCase=false;
						}else{
							varEnd.add(start-1);
							lookForEnd=false;
						}
					}
					if(thisLinesLiteralType.equals("strLit")){
						stringsPresent=true;
						varLocStr.add(start);
						varEndStr.add(start+len-1);
						vars.addBlank();
						//start++;
					}else{
					varLoc.add(start);
					varEnd.add(start+len-1);
					vars.addBlank();
					}
					start += len;
					start--;
					//System.out.println(line[start+1]);
				}else if(len==1 && !cur.trim().isEmpty() && !lookForEnd && !alreadyBegunUnknown){//An unknown phrase begins here...
					if(line[start].charAt(line[start].length()-1) != '"'){
						beginOfUnknown=start;
						needEndOfUnknown=true;
						alreadyBegunUnknown=true;
					}
				}
			}
		}
		buildUnknownPhraseException(needEndOfUnknown,beginOfUnknown,endOfUnkown,lineNum,line);
		if(!varLoc.isEmpty() || stringsPresent){
			
			if(varLoc.size()!=varEnd.size()){
				varEnd.add(varLoc.get(varLoc.size()-1));
			}
			ArrayList<String> allVars = getVars(varLoc,varEnd,line);
			//System.out.println(allVars);
			if(stringsPresent){
				ArrayList<String> strVars = getStrVars(varLocStr, varEndStr, line);
				//need to shuffle into proper place in symbol table
				ArrayList<String> newVars = mergeStrWithVars(strVars,allVars,tokenPos,thisLinesTokens);
				vars.merge(newVars);
				symbolTable.addAll(vars.getVars());
				stringsPresent=false;
			}else{
				vars.merge(allVars);
				symbolTable.addAll(vars.getVars());
			}
			ArrayList<String> types = findTypes(tokenPos,thisLinesTokens, allVars);
			thisLinesTokens = mergeTokens(thisLinesTokens,types,tokenPos);
		}else if(!vars.isEmpty()){
			symbolTable.addAll(vars.getVars());
		}
		symbolTable.addAll(storedParams);
		tokens.addAll(thisLinesTokens);
		if(callingMethod>0  && !thisLinesTokens.contains("para") && !thisLinesTokens.contains("endMainfunc")){//I have at least one method being called in this line
			paramCheck(thisLinesTokens, methodsCalledOnThisLine, lineNum);
		}
	}
	
	/**
	*Marges the string literals with the other user defined items in preperation for the symbol tale addition
	*@param strs the string literals
	*@param vars the other defined variables
	*@param locs the positions the other varaibles are in the token list
	*@param tokens the tokens for this line
	*@return ArrayList<Stirng> the combined arraylist
	*/
	private ArrayList<String> mergeStrWithVars(ArrayList<String> strs, ArrayList<String> vars, ArrayList<Integer> locs, ArrayList<String> tokens){
		ArrayList<String> newVars = new ArrayList<String>();
		int cont=0;
		int cont2=0;
		for(int i=0;i<tokens.size();i++){
			if(tokens.get(i).equals("strLit")){
				newVars.add(strs.get(cont));
				cont++;
			}else if(locs.contains(i)){
				newVars.add(vars.get(cont2));
				cont2++;
			}
		}
		return newVars;
	}
	
	/**
	*Returns all of the string literals in this line
	*@param s the start positions of the strings
	*@param e the end positions of the String
	*@param line the collection of strings that make up this line
	*@return ArrayList<String> all of the string literals
	*/
	private ArrayList<String> getStrVars(ArrayList<Integer> s, ArrayList<Integer> e, String[] line){
		ArrayList<String> allStrVars = new ArrayList<String>();
		for(int i=0;i<s.size();i++){
			allStrVars.add(getStrVar(s.get(i),e.get(i),line));
		}
		return allStrVars;
	}
	
	/**
	*Returns the string literal existing between 2 points in line
	*@param s the start position in line
	*@param e the end position in line
	*@param line the line the string exists in
	*@return String the completed string literal
	*/
	private String getStrVar(int s, int e, String[] line){
		String builder = "";
		for(int i=s;i<=e;i++){
			builder = builder.concat(line[i]);
			if(i+1<=line.length){
				try{
					if(keys.get(line[i+1]).equals("punc")){
						//punctuation was previously removed, so now it must be reattatched
					}else{
						builder = builder.concat(" ");
					}
				}catch (NullPointerException n){
					builder = builder.concat(" ");
				}catch (ArrayIndexOutOfBoundsException A){
					builder = builder.concat(" ");
				}
			}else{
				builder = builder.concat(" ");
			}
		}
		builder = builder.trim();
		return builder;
	}
	
	/**
	*Returns true if the given string is a FiM++ literal
	*@param key the word to check
	*@return boolean true if the key is a literal
	*/
	private Boolean isLiteral(String key){
		try{
			double dub =  Double.valueOf(key);
			return true;//this is a number literal
		}catch (Exception e){//not a number literal
		}
		try{
			if(key.length()==3 && key.charAt(0)=='\'' && key.charAt(2)=='\''){
				char theChar = key.charAt(1);
				return true;//this is a char literal
			}
		}catch (Exception e){//not a char type
		}
		try{
			if(key.charAt(0)=='"' && key.indexOf('"',1)==key.length()-1){
				return true;
			}
		}catch (Exception e){}//not a string type
		return false;
	}
	
	/**
	*Returns the literal type of a given key
	*@param key the literal to examine
	*@return String the type of the literal, null if none could be found
	*/
	private String getLiteralType(String key){
		try{
			double dub =  Double.valueOf(key);
			return "dubLit";//this is a number literal
		}catch (Exception e){//not a number literal
		}
		try{
			if(key.length()==3 && key.charAt(0)=='\'' && key.charAt(2)=='\''){
				char theChar = key.charAt(1);
				return "charLit";//this is a char literal
			}
		}catch (Exception e){//not a char type
		}
		
		if(key.charAt(0)=='"' && key.indexOf('"',1)==key.length()-1){
				return "strLit";
			}
		return null;
	}
	
	/**
	*Builds a given user defined name
	*@param start the first position in the variable name
	*@param end the end position in the variable name
	*@param line the String[] that the names will be built from
	*@return String the full name
	*/
	private String buildVar(int start, int end, String[] line){
		String composite = "";
		for(int i=start;i<=end;i++){
			composite = composite.concat(line[i]);
		}
		return composite;
	}
	
	/**
	*Finds and builds the user defined variable name
	*@param start the begining positions for the variable names
	*@param end the ending position for the variable names
	*@param line the line in which the variables exist on
	*/
	private ArrayList<String> getVars(ArrayList<Integer> start, ArrayList<Integer> end, String[] line){
		ArrayList<String> allVars = new ArrayList<String>();
		for(int i=0;i<start.size();i++){
			allVars.add(buildVar(start.get(i),end.get(i),line));
		}
		return allVars;
	}
	
	/**
	*Finds the type for all of the variables in the given line
	*@param positions the positions of all of the user variables
	*@param tokens the current list of generated tokens
	*@param allVars all of the variables 
	*@return ArrayList<String> a list of all of the token types, in the same order as was dictated by the positions array
	*/
	private ArrayList<String> findTypes(ArrayList<Integer> positions,ArrayList<String> tokens, ArrayList<String> allVars){
		//take each position and look at the previous token type, if punc then look back one more time
		int var=0;
		ArrayList<String> types = new ArrayList<String>();
		ArrayList<Integer> removes = new ArrayList<Integer>();
		for(int i=0;i<positions.size();i++){
			int lookAt=0;
			if(tokens.get(positions.get(i)-1)=="punc"){
				lookAt=positions.get(i)-2;
			}else{
				lookAt=positions.get(i)-1;
			}
			//All of the codes translated
			if(tokens.get(lookAt)=="beginfile"){
				types.add("class");
				keys.put(allVars.get(var),"class");
			}else if(tokens.get(lookAt)=="interface listing"){
				types.add("interfaceName");
				keys.put(allVars.get(var),"interfaceName");
			}else if(tokens.get(lookAt)=="endfile"){
				types.add("signee");
				keys.put(allVars.get(var),"author");
			}else if(tokens.get(lookAt)=="import"){
				types.add("iName");
				keys.put(allVars.get(var),"iName");
			}else if(tokens.get(lookAt)=="mane"){
				types.add("manemethod");
				keys.put(allVars.get(var),"manemethod");
			}else if(tokens.get(lookAt)=="varDec"){
				String thisType = getBasicType(tokens,lookAt+1);
				types.add(thisType);
				keys.put(allVars.get(var),thisType);
			}else if(tokens.get(lookAt)=="double"){
				String thisType = getBasicType(tokens,lookAt);
				types.add(thisType);
				keys.put(allVars.get(var),thisType);
			}else if(tokens.get(lookAt)=="char"){
				String thisType = getBasicType(tokens,lookAt);
				types.add(thisType);
				keys.put(allVars.get(var),thisType);
			}else{
				throw new IllegalArgumentException("If you are seeing this, then the findTypes private method was unable to find the type to a declared variable, you should not be seeing this... Seen tokens was: " + tokens.get(lookAt));
				
			}
			var++;
		}
		return types;
	}
	
	/**
	*Merges the tokens with the user defined values to create the full token list for the line
	*@param preDefined the already established tokens list
	*@param userDefined the collection of user made variable names
	*@param positions the positions in the token list where the variable is located
	*@return ArrayList<String> the merged token list
	*/
	private ArrayList<String> mergeTokens(ArrayList<String> preDefined, ArrayList<String> userDefined, ArrayList<Integer> positions){
		for(int i=positions.size()-1;i>=0;i--){
			preDefined.add(positions.get(i),userDefined.get(i));
		}
		return preDefined;
	}
	
	/**
	*Returns the basic type for the given variable name
	*@param positions the positions of all of the user variables
	*@param tokens the current list of generated tokens
	*@param lookAt an int telling the method where the type actually is
	*@return String the name of the type that the findTypes needs
	*/
	private String getBasicType(ArrayList<String> tokens2, int lookAt){
		String type = "";
		for(int i=lookAt;i<tokens2.size();i++){
			if(tokens2.get(i)=="Bool"){
				return "boolType";
			}else if(tokens2.get(i)=="double"){
				return "numType";
			}else if(tokens2.get(i)=="char"){
				return "charType";
			}else if(tokens2.get(i)=="string"){
				return "strType";
			}else if(tokens2.get(i)=="strArray" || tokens2.get(i)=="returnsStrArrayType"){
				return "strArrayType";
			}else if(tokens2.get(i)=="numArray" || tokens2.get(i)=="returnsNumArrayType"){
				return "numArrayType";
			}else if(tokens2.get(i)=="boolArray" || tokens2.get(i)=="returnsBoolArrayType"){
				return "boolArrayType";
			}else if(tokens2.get(i)=="returnsNumType"){
				return "numType";
			}else if(tokens2.get(i)=="returnsBoolType"){
				return "boolType";
			}else if(tokens2.get(i)=="returnsCharType"){
				return "charType";
			}else if(tokens2.get(i)=="returnsStrType"){
				return "strType";
			}
		}
		if(type.isEmpty()){
			throw new IllegalArgumentException("Unable to asign a basic type using: " + tokens2 + " starting at: " + lookAt );
		}
		return type;
	}
	
	/**
	*Returns true if the passed token is a prefix to a arithmatic statement
	*@param token the token to check
	*@return Boolean true if it is a prefix identifier
	*/
	private Boolean isPrefix(String token){
		if(keys.get(token).equals("addPrefix")){
			return true;
		}else if(keys.get(token).equals("subPrefix")){
			return true;
		}else if(keys.get(token).equals("multPrefix")){
			return true;
		}else if(keys.get(token).equals("divPrefix")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	*Determines if the given string is an infix token
	*@param token the string to check
	*@return Boolean true if the given string is an infix token
	*/
	private Boolean isInfix(String token){
		if(token.equals("and")){
			return true;
		}else if(token.equals("from")){
			return true;
		}else if(token.equals("by")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	*Returns the type of infix that this infix is
	*@param tokens the current line of tokens
	*@param infix the particular infix to get the type of
	*@return String the infix type
	*/
	private String getInfixType(ArrayList<String> tokens, String infix){
		for(int i=0;i<tokens.size();i++){
			String token = tokens.get(i);
			if(token.equals("addPrefix") && infix.equals("and")){
				return "addInfix2";
			}else if(token.equals("subPrefix") && (infix.equals("and") || infix.equals("from"))){
				return "subInfix2";
			}else if(token.equals("multPrefix") && (infix.equals("and") || infix.equals("by"))){
				return "multInfix2";
			}else if(token.equals("divPrefix") && (infix.equals("and") || infix.equals("by"))){
				return "divInfix2";
			}
			
		}
		return null;
	}
	
	/**
	*Finds all of the methods defined in the program
	*@param line the line to check if it declares a method
	*/
	private void findMethods(String[] line){
		int endName =0;
		if(line.length<3){
			//not declaring a paragraph here
		}else{
			if(line[0].equals("I") && line[1].equals("learned")){
				//I know a method is being declared on this line
				String builder = "";
				Method method = new Method("YOU SHOULD NEVER SEE THIS METHOD NAME");
				for(int i=2;i<line.length;i++){
					if(!isKeyword(line[i])){
						builder = builder.concat(line[i]);
					}else{//variable name is done
						if(builder.isEmpty()){
							throw new IllegalArgumentException("No variable name detected for a paragraph");
						}
						method = new Method(builder);
						//need to look for parameters and return types
						endName=i;
						break;
						
					}
				}
				
				if(endName==line.length){
					//all done do nothing more
				}else{//more tokens to parse
					Boolean returns = false;
					Boolean params = false;
					for(int start=endName;start<line.length;start++){
						if(line[start].equals("using") || line[start].equals("as")){
							params=true;
							continue;
						}else if(line[start].equals("with")){
							params=false;
							returns=true;
							continue;
						}else{
							if(start+1<line.length && line[start].equals("to") && line[start+1].equals("get")){
								params=false;
								returns=true;
								start++;//needed so that the type doesn't have the word get in it
								continue;
							}
						}
						if(params){//need to look for type and variable name
							String cur = "";
							String type = "";
							int len=0;
							for(int l=0;l<line.length-start;l++){//first type
								cur = cur.concat(line[start+l]);
								if(isKeyword(cur)){//a keyword was found, assuming to be the type of the incoming parameter
									type = keys.get(cur);
									start+=l+1;
									break;
								}
							}
							cur="";
							for(int l=0;l<line.length-start-1;l++){//look for the variable name now
								//this is a subpar implementation of the varaible finding system I am using in other places, an ideal system would seperate that system and use it here but its too entrenched
								cur = cur.concat(line[start+l]);
								if(isKeyword(line[start+l+1])){
									start+=l;
									method.addParam(type,cur);
									break;
								}else if(start+l+2<line.length){
									if(isKeyword("" + line[start+l+1] + line[start+l+2])){
										start+=l;
										method.addParam(type,cur);
										break;
									}
								}
							}
							
						}else if(returns){
							String cur = "";
							String thisReturnType = "";
							for(int i=start;i<line.length;i++){
								cur = cur.concat(line[i]);
								if(isKeyword(cur)){
									thisReturnType = keys.get(cur);
									start+=i;
									break;
								}
							}
							if(thisReturnType.isEmpty()){
								//return type void, can ignore since voidType is default
							}else{
								method.setReturnType(translateType(thisReturnType));
							}
							returns=false;
						}else{
							//?
							
						}
					}
				}
				methods.add(method);
			}
			
		}
	}
	
	/**
	*Translates all nums to returnsNumType etc...
	*@param String type the type to convert
	*@return String the translated type
	*/
	private String translateType(String type){
		if(type.equals("double")){
			return "returnsNumType";
		}else if(type.equals("char")){
			return "returnsCharType";
		}else if(type.equals("Bool")){
			return "returnsBoolType";
		}else if(type.equals("string")){
			return "returnsStrType";
		}else if(type.equals("boolArray")){
			return "returnsBoolArrayType";
		}else if(type.equals("strArray")){
			return "returnsStrArrayType";
		}else if(type.equals("numArray")){
			return "returnsNumArrayType";
		}else if(type.equals("charArray")){
			return "returnsCharArrayType";
		}else{
			throw new IllegalArgumentException("In the current version returning a " + type + " is not supported");
		}
	}
	
	/**
	*Builds an unknown phrase exception
	*@param checking a flag to see if this should be run
	*@param begin the begin location of the unknown phrase
	*@param end the end location of the unknown phrase
	*@param line the line number the exception is in
	*@param words the line of words the unknown phrase is in
	*/
	private void buildUnknownPhraseException(Boolean checking, int begin, int end, int line, String[] words){
		alreadyBegunUnknown=false;
		if(checking){
			String builder = "";
			for(int i=begin;i<=end;i++){
				builder = builder.concat(" " + words[i]);
			}
			errors.addError(Error.createUnknownTokenError(builder,line));
		}
	}

	/**
	*Checks if the methods being called are given the proper parameters
	*@param tokens the tokens from this line
	*@param methods all of the methods called on this line
	*@param line the line number
	*/
	private void paramCheck(ArrayList<String> tokens, ArrayList<Method> methods, int line){
		//There is no way to decirn between one call ending and the next one begining
		//the FDS does not state if there is a required punctuation at the end of a method call, as such I have to assume there is not
		//without any punc the ands for param calls get muddled, so calling a method as a parameter to a method is banned here
		int methodNum=0;
		for(int i=0;i<tokens.size();i++){
			Method m = methods.get(methodNum);
			if(m.getReturnType().equals(tokens.get(i))){//found the call for the current method
				int numOfParam = m.getNumOfParams();
				i++;
				if(tokens.get(i).equals("param")){
					//we have parameters
					if(numOfParam==0){//we shouldn't have parameters but we do...
						createParamError(m,new ArrayList<String>(),line);
					}
					ArrayList<String> params = new ArrayList<String>();
					i++;
					params.add(tokens.get(i));
					while(tokens.get(i+1).equals("and")){//another parameter seen
						i+=2;
						params.add(tokens.get(i));
					}
					if(!m.paramMatch(params)){
						createParamError(m,params,line);
					}else{
						//System.out.println("Properly matched params for: " + m);
					}
				}
			}
		}
	}
	
	/**
	*Builds a parameter error
	*@param m the method the error is related to
	*@param given the parameters attempted to use
	*@param line the line this happened on
	*/
	private void createParamError(Method m, ArrayList<String> given, int line){
		errors.addError(Error.createBadParameterError(m.getParams(),given,line));
	}
	
	/**
	*Given a string, determines if it was one of the original keywords for double
	*@param s the string to check
	*@return Boolean true if the string is indeed an original keywork for double
	*/
	private Boolean trueDouble(String s){
		ArrayList<String> arr = new ArrayList<String>(List.of("number","anumber","thenumber"));
		return arr.contains(s);
	}
	
	
}

//the spaces down here are to lift all the actual code up higher on the screen in my IDE (Notepad++), if this comment is still here I do apologize, as it has no actual value to the code itself.














