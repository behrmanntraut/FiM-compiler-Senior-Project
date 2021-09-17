/**
*Code generator the takes the tokens after they are validated and builds a java file
*@author Brandon Ehrmanntraut
*/
import java.util.*;
import java.io.*;
public class generator{
	public static void main(String[] args){
		
	}
	private int tabCount=0;
	private ArrayList<String> tokens = new ArrayList<String>();
	private ArrayList<String> symbols = new ArrayList<String>();
	private String master = new String();
	private int varCount=0;//current location in ST
	private int loc=0;//current token we are at
	private String ReportName = "";
	
	/**
	*Class constructor
	*@param tokens the list of tokens that was derived from the original file
	*@param symbols the list of user defined items
	*/
	public generator(String[] Tokens, ArrayList<String> Symbols){
		tokens = new ArrayList<String>(Arrays.asList(Tokens));
		symbols = Symbols;
	}
	
	/**
	*Creates the String that represents the Java file
	*/
	public void run(){
		while(loc<tokens.size()){ //builds a string that is the file
			String newThing = navigate(tokens.get(loc));
			master = master.concat(newThing);
		}
		String fileName = ReportName.concat(".java");
		try{
			File output = new File(fileName);
			if(output.createNewFile()){
				
			}else{
				System.out.println("File " + fileName + " already exists, please delete old file or rename your report");
			}
			try{
				FileWriter writer = new FileWriter(fileName);
				System.out.println(master);
				writer.write(master);
				writer.close();
			}catch(Exception e){
				System.out.println("File writing error");
				System.out.println(e);
			}
		}catch(Exception E){
			System.out.println("File creation error");
			System.out.println(E);
		}
	}
	
	/**
	*Call the required function dictated by the current symbol
	*@param cur the current String
	*@return String the String to add to the master String
	*/
	private String navigate(String cur){
		if(cur.equals("beginfile")){
			return beginfile();
		}else if(cur.equals("import")){
			return Import();
		}else if(cur.equals("endfile")){
			return end();
		}else if(cur.equals("n")){
			return n();
		}else if(cur.equals("mane")){
			return Main();
		}else if(cur.equals("endMainfunc")){
			return MainEnd();
		}else if(cur.equals("print")){
			return Print();
		}else if(cur.equals("varDec")){
			return varDec();
		}
		
		System.out.println("The code generator did not know what to do when it encountered the token: " + cur);
		return "";
	}
	
	/**
	*Returns a String with the current number of tabs in it
	*@return String a bunch of tabs
	*/
	private String tabs(){
		String temp="";
		for(int i=0;i<tabCount;i++){
			temp=temp.concat("\t");
		}
		return temp;
	}
	
	/**
	*Handles the begining of the file stuff
	*@return String the java code for this line, expressed as a string
	*/
	private String beginfile(){
		String builder = "public class ";
		//need the report name, and maybe the interface name
		//defining the class
		if(tokens.get(loc+2).equals("interface")){
			String face = symbols.get(varCount+1);
			String report = symbols.get(varCount+2);
			varCount += 3;
			builder = builder.concat(report);
			ReportName = report;
			builder = builder.concat(" extends ");
			builder = builder.concat(face);
			builder = builder.concat("{");
			loc += 7;
		}else{
			String report = symbols.get(varCount+1);
			varCount += 2;
			builder = builder.concat(report);
			ReportName = report;
			builder = builder.concat("{");
			loc += 5;
		}
		tabCount++;
		return builder;
	}
	
	/**
	*Handles the import calls *Directly changes master String*
	*@return String the java code for this line, expressed as a string
	*/
	private String Import(){
		String builder = "import ";
		builder = builder.concat(symbols.get(varCount));
		builder = builder.concat(";\n");
		master = builder.concat(master);
		varCount++;
		loc += 4;
		return "";
	}
	
	/**
	*Handles new lines
	*@return String the java code for this line, expressed as a string
	*/
	private String n(){
		loc++;
		return "\n";
	}
	
	/**
	*Handles the end and author
	*This method does directly edit the master string, generally a bad idea but it only does so to add the java doc of the authors name to the file
	*@return String the java code for this line, expressed as a string
	*/
	private String end(){
	// need to make sure to add } at the end of the file
		String auth = "/**\n*@author ";
		auth = auth.concat(symbols.get(symbols.size()-1));
		auth = auth.concat("\n*/\n");
		master = auth.concat(master);
		loc = tokens.size();//catch to ensure that the prgoram does not loop forever
		return close();
	}
	
	/**
	*Closes off things, aka }
	*@return String the closing symbol
	*/
	private String close(){
	return "}";
	}
	
	/**
	*Builds the mane paragraph
	*@return String the line that builds the mane paragraph
	*/
	private String Main(){
		String builder = tabs();
		builder = builder.concat("public static void main(String[] args){");
		loc+=3;
		varCount++;
		tabCount++;
		return builder;
	}
	
	/**
	*Ends the mane paragraph
	*@return String the end of the main paragraph
	*/
	private String MainEnd(){
		tabCount--;
		String builder = tabs();
		builder = builder.concat(close());
		varCount++;
		loc+=3;
		return builder;
	}
	
	/**
	*Thr print statement
	*@return String the print statement
	*/
	private String Print(){
		String builder = tabs();
		builder = builder.concat("System.out.println(");
		builder = builder.concat(symbols.get(varCount));
		varCount++;
		builder = builder.concat(");");
		loc += 3;
		return builder;
	}
	
	/**
	*Runs the variable declaration line, does both versions
	*@return String the representation of this line
	*/
	private String varDec(){
		String builder = tabs();
		//in FiM the type comes after the variable name, need to switch that around
		builder = builder.concat(primitiveType(tokens.get(loc+1)) + " ");
		loc += 2;
		builder = builder.concat(symbols.get(varCount));
		varCount++;
		if(tokens.get(loc).equals("asign")){
			builder = builder.concat(" = ");
			builder = builder.concat(symbols.get(varCount) + ";");
			varCount++;
			loc += 4;
		}else{
			builder = builder.concat(";");
			loc += 2;
		}
		return builder;
	}
	
	/**
	*Given a type token, will return the appropriate stirng type for Java
	*@param token the token to analyze
	*@return String the type that token is
	*/
	private String primitiveType(String token){
		if(token.equals("numType")){
			return "double";
		}else if(token.equals("charType")){
			return "char";
		}else if(token.equals("strType")){
			return "String";
		}else if(token.equals("numType")){
			return "Boolean";
		}else{
			throw new IllegalArgumentException("primitive Type was not given a type token, was given the token: " + token);
		}
	}
	
	
}













