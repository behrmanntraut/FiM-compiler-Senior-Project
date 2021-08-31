/**
*Takes a given .txt that is really a FiM++ file and ceates tokens from it
*@author Brandon Ehrmanntraut
*/

/*
TODO

Handle punctuation





*/
import java.util.*;
import java.io.*;
public class token{
	public static void main(String[] args){
		
	}
	
	//Basically the main function but my driver is in another class
	public void testRun(){
		run();
	}
	
	ArrayList<String> lines = new ArrayList<String>();
	HashMap<String,String> keys = new HashMap<String,String>();//find the keywords
	HashMap<String,String> symbolTable = new HashMap<String,String>();//String[] is the token the item is being stored as and the time in which it appeared ie 
	int MAX = 5;//the longest set of words that make up one reserved phrase
	/**
	*Class constructor
	*@param FIM the file that is to be read
	*/
	public token(String FIM){
		lines = read(FIM);
		buildKeywords();
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
		keys.put("and", "interface listing"); 
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
		ArrayList<String> tokens = new ArrayList<String>();
		Boolean varComing = false;
		int varS=0;//Begin of a variable
		int varE=0;//End of a variable
		for(int l=0;l<line.size();l++){//for each line
			for(int i=0;i<line.get(l).length;i++){//for each word in the line
				//in an ideal world I would use regex here to find the patterns of words, but I do not know how to implement regex in java so we use more for loops
				//need to work  from MAX size down 
				for(int s=MAX-1;s>=0;s--){//for each valid length
					if(s+i>line.get(l).length){//looking for a phrase that is longer than the remaining number of words
						continue;
					}else{
						String keyWord = build(line.get(l),s,i);
						if(varComing && (i==line.get(l).length-1)){//variable ended the new line
							tokens.add("userVar");
							String toUse = build(line.get(l),varS,line.size()-1);
							keys.put(toUse,"userVar");
							symbolTable.put(toUse,tokens.get(tokens.size()-2));
							System.out.println("Submitting: " + toUse);
							varComing=false;
						}
						if(keys.containsKey(keyWord)){
							
							if(varComing){
								//We have the next keyword so we have the end of the var defined by the user
								varE=i-1;
								System.out.println("Ending user defined value at: " + varE);
								tokens.add("userVar");
								System.out.println(varS + "\t" + (varE-varS-1));
								String users = build(line.get(l),varS,varE-varS+1);
								keys.put(users,"userVar");
								System.out.println("Submitting: " + users);
								symbolTable.put(users,tokens.get(tokens.size()-2));
								varComing=false;
							}
							i+=s;//used s extra letters, so need to move ahead s extra spaces
							tokens.add(keys.get(keyWord));
							if(specialKeyword(keys.get(keyWord))){
								//the begining of the var is after this keyword ended
								System.out.println(keyWord + " " + i);
								varS=i;
								System.out.println("Setting user defined value start to: " + varS);
								
								varComing=true;
							}
							break;
						}
					}
				}
			}
			tokens.add("N");
		}
		tokens.remove(tokens.size()-1);//remove the last newline as the file ends
		System.out.println(tokens);
		System.out.println(keys);
		System.out.println(symbolTable);
		return null;
		
		
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
		for(int j=0;j<s;j++){
			temp = temp.concat(line[j+i]);
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
		}else if(keyWord=="endfile"){
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
	
	
}
