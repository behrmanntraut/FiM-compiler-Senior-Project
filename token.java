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
	/**
	*Class constructor
	*@param FIM the file that is to be read
	*/
	public token(String FIM){
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
			while ((line = br.readLine()) != null) {
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
		
		firstLine(line.get(0));
		tokens.add("n");
		for(int l=1;l<line.size();l++){//for each line
			doLine(line.get(l));
			tokens.add("n");
		}
		tokens.remove(tokens.size()-1);//remove the last newline as the file ends
		
		return tokens.toArray(new String[tokens.size()]);
		
		
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
				continue;//empty line
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
	*At the begining of the file we have some shenanigans with the report name at the end of the line, but it is being defined by Dear at the begining so this is here to handle that one single case
	*@param keyWord the String representing the keyword
	*@return Boolean true if the keyword is beginfile
	*/
	private Boolean isSuperSpecialKeyword(String keyWord){
		if(keyWord=="beginfile"){
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
	
	/**
	*A breakdown of run that tokenizes a single line - void as all needed things to write to are already global variables
	*@param line, the String[] that makes up the line
	*/
	private void doLine(String[] line){
		ArrayList<String> thisLinesTokens = new ArrayList<String>();
		ArrayList<Integer> varLoc = new ArrayList<Integer>();
		ArrayList<Integer> varEnd = new ArrayList<Integer>();
		ArrayList<Integer> tokenPos = new ArrayList<Integer>();
		Boolean lookForEnd=false;
		Boolean specialCase=false;
		for(int start=0;start<line.length;start++){//each start word
			for(int len=line.length;len>=0;len--){//each length
				if(start+len>line.length){
					continue;
				}
				String cur = build(line,start,len);
				if(isKeyword(cur)){
					thisLinesTokens.add(keys.get(cur));
					if(lookForEnd){
						if(specialCase){
							specialCase=false;
						}else{
							varEnd.add(start-1);
							lookForEnd=false;
						}
						
					}
					start+=len-1;
					if(specialKeyword(keys.get(cur))){//next position starts a user defined name
						varLoc.add(start+1);
						lookForEnd=true;
						tokenPos.add(thisLinesTokens.size());
					}else if(isOtherSpecialKeyword(keys.get(cur))){//the position after the next position starts a user defined name
						varLoc.add(start+2);
						lookForEnd=true;
						specialCase=true;
						tokenPos.add(thisLinesTokens.size()+1);
					}
				}
			}
		}
		if(!varLoc.isEmpty()){
			if(varLoc.size()!=varEnd.size()){
				varEnd.add(varLoc.get(varLoc.size()-1));
			}
			ArrayList<String> allVars = getVars(varLoc,varEnd,line);
			symbolTable.addAll(allVars);
			ArrayList<String> types = findTypes(tokenPos,thisLinesTokens);
			thisLinesTokens = mergeTokens(thisLinesTokens,types,tokenPos);
		}
		//need to still add in all already defined names into the symbol table and the keyword hashmap
		//symbol table should become an ArrayList, 
		tokens.addAll(thisLinesTokens);
		
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
	*@return ArrayList<String> a list of all of the token types, in the same order as was dictated by the positions array
	*/
	private ArrayList<String> findTypes(ArrayList<Integer> positions,ArrayList<String> tokens){
		//take each position and look at the previous token type, if punc then look back one more time
		ArrayList<String> types = new ArrayList<String>();
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
				keys.put(tokens.get(lookAt),"class");
			}else if(tokens.get(lookAt)=="interface listing"){
				types.add("interfaceName");
				keys.put(tokens.get(lookAt),"interfaceName");
			}else if(tokens.get(lookAt)=="endfile"){
				types.add("signee");
				keys.put(tokens.get(lookAt),"author");
			}else if(tokens.get(lookAt)=="import"){
				types.add("iName");
				keys.put(tokens.get(lookAt),"iName");
			}else{
				System.out.println("If you are seeing this, then the findTypes private method was unable to find the type to a declared variable, you should not be seeing this...");
			}
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
	
	
}

//the spaces down here are to lift all the actual code up higher on the screen in my IDE (Notepad++), if this comment is still here I do apologize, as it has no actual value to the code itself.














