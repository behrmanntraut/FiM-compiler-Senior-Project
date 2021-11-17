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
	private Boolean firstCase=false;
	private ArrayList<Method> methods = new ArrayList<Method>();
	
	/**
	*Class constructor
	*@param tokens the list of tokens that was derived from the original file
	*@param symbols the list of user defined items
	*/
	public generator(String[] Tokens, ArrayList<String> Symbols, ArrayList<Method> methods){
		tokens = new ArrayList<String>(Arrays.asList(Tokens));
		symbols = Symbols;
		this.methods = methods;
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
			output.createNewFile();
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
		}else if(cur.equals("numType")){
			return numAsign();
		}else if(cur.equals("boolType")){
			return boolAsign();
		}else if(cur.equals("if")){
			return ifStatement();
		}else if(cur.equals("endConditional")){
			return ifEnd();
		}else if(cur.equals("else")){
			return elseBlock();
		}else if(cur.equals("switch")){
			return switchBlock();
		}else if(cur.equals("case") || cur.equals("defaultCase")){
			return caseBlock();
		}else if(cur.equals("switchClose")){
			return switchClose();
		}else if(cur.equals("for")){
			return For();
		}else if(cur.equals("para")){
			return Para();
		}else if(cur.equals("return")){
			return returnStatement();
		}else if(cur.equals("callPara")){
			return callingPara();
		}
		System.out.println("Token location: " + loc);
		throw new IllegalArgumentException("The code generator did not know what to do when it encountered the token: " + cur);
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
		loc++;
		if(isNumType()){
			builder = builder.concat(numStatement() + ");");
			loc++;
		}else{
			builder = builder.concat(symbols.get(varCount));
			varCount++;
			builder = builder.concat(");");
			loc += 2;
		}
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
			loc++;
			if(isNumType()){
				builder = builder.concat(numStatement() + ";");
				loc++;
			}else if(isBoolType()){
				builder = builder.concat(boolStatement() + ";");
				loc++;
			}else{
				builder = builder.concat(symbols.get(varCount) + ";");
				varCount++;
				loc += 3;
			}
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
		}else if(token.equals("boolType")){
			return "Boolean";
		}else if(token.equals("strArrayType")){
			return "String[]";
		}else if(token.equals("numArrayType")){
			return "double[]";
		}else if(token.equals("boolArrayType")){
			return "Boolean[]";
		}else{
			throw new IllegalArgumentException("primitive Type was not given a type token, was given the token: " + token);
		}
	}
	
	/**
	*The number asignment statement
	*@return String this line
	*/
	private String numAsign(){
		String builder = tabs();
		builder = builder.concat(symbols.get(varCount));
		loc++;
		varCount++;
		if(tokens.get(loc).equals("asign")){
			builder = builder.concat(" = ");
			loc++;
			builder = builder.concat(numStatement());
		}else{
			throw new IllegalArgumentException("Expecting token \"asign\" but instead recieved: " + tokens.get(loc));
		}
		builder = builder.concat(";");
		loc++;
		return builder;
	}
	
	/**
	*Builds a number statement using the basic operators and both formats
	*@return String the number statement generated
	*/
	private String numStatement(){
		String builder = "";
		while(!tokens.get(loc).equals("punc")){
			//System.out.println(tokens.get(loc));
			if(tokens.get(loc).equals("double")){//a literal value
				builder=builder.concat(symbols.get(varCount));
				loc+=2;
				varCount++;
			}else if(tokens.get(loc).equals("addInfix")){//+
				builder = builder.concat(" + ");
				loc++;
			}else if(tokens.get(loc).equals("subInfix")){//-
				builder = builder.concat(" - ");
				loc++;
			}else if(tokens.get(loc).equals("multInfix")){//*
				builder = builder.concat(" * ");
				loc++;
			}else if(tokens.get(loc).equals("divInfix")){//div
				builder = builder.concat(" / ");
				loc++;
			}else if(tokens.get(loc).equals("numType")){
				builder = builder.concat(symbols.get(varCount));
				varCount++;
				loc++;
			}else if(tokens.get(loc).equals("addInfix2")){//+
				builder = builder.concat(" + ");
				loc++;
			}else if(tokens.get(loc).equals("subInfix2")){//-
				builder = builder.concat(" - ");
				loc++;
			}else if(tokens.get(loc).equals("multInfix2")){//*
				builder = builder.concat(" * ");
				loc++;
			}else if(tokens.get(loc).equals("divInfix2")){//div
				builder = builder.concat(" / ");
				loc++;
			}else if(tokens.get(loc).equals("addPrefix")){//+
				loc++;
			}else if(tokens.get(loc).equals("subPrefix")){//-
				loc++;
			}else if(tokens.get(loc).equals("multPrefix")){//*
				loc++;
			}else if(tokens.get(loc).equals("divPrefix")){//div
				loc++;
			}else if(tokens.get(loc).equals("increment")){//increment
				builder = builder.concat("++");
				loc++;
			}else if(tokens.get(loc).equals("deccrement")){//deccrement
				builder = builder.concat("--");
				loc++;
			}else if(tokens.get(loc).equals("equals")){
				builder = builder.concat(" == ");
				loc++;
			}else if(tokens.get(loc).equals("notEqual")){
				builder = builder.concat(" != ");
				loc++;
			}else if(tokens.get(loc).equals("greaterThan")){
				builder = builder.concat(" > ");
				loc++;
			}else if(tokens.get(loc).equals("lessThan")){
				builder = builder.concat(" < ");
				loc++;
			}else if(tokens.get(loc).equals("greaterThanOrEqual")){
				builder = builder.concat(" >= ");
				loc++;
			}else if(tokens.get(loc).equals("lessThanOrEqual")){
				builder = builder.concat(" <= ");
				loc++;
			}else if(tokens.get(loc).equals("dubLit")){
				builder = builder.concat(symbols.get(varCount));
				varCount++;
				loc++;
			}else if(tokens.get(loc).equals("returnsNumType")){
				builder = builder.concat(buildMethodCall());
			}else{
				break; //unsure of what to do here, so breaking loop
			}
		}
		return builder;
	}
	
	/**
	*Returns true if the given token is probably a number
	*@return Boolean true if this is probably a number
	*/
	private Boolean isNumType(){
		if(tokens.get(loc).equals("double")){
			return true;
		}else if(tokens.get(loc).equals("numType")){
			return true;
		}else if(tokens.get(loc).equals("addPrefix")){
			return true;
		}else if(tokens.get(loc).equals("subPrefix")){
			return true;
		}else if(tokens.get(loc).equals("multPrefix")){
			return true;
		}else if(tokens.get(loc).equals("divPrefix")){
			return true;
		}else if(tokens.get(loc).equals("returnsNumType")){
			return true;
		}
		return false;
	}
	
	/**
	*Returns the string representing a boolean asignment
	*@return String the boolean asignment passed,but in java
	*/
	private String boolAsign(){
		String builder = tabs();
		builder = builder.concat(symbols.get(varCount) + " = ");
		loc += 2;
		varCount++;
		if(!tokens.get(loc).equals("returnsBoolType")){
			builder = builder.concat(boolStatement() + ";");
		}else{
			builder = builder.concat(buildMethodCall() + ";");
		}
		loc++;
		return builder;
	}
	
	/**
	*Returns the string representing the bool statement
	*@return String the boolean statement in java code
	*/
	private String boolStatement(){
		String builder = "";
		Boolean XORflag = false;
		if(tokens.get(loc).equals("XOR")){
			XORflag=true;
		}
		loc++;
		if(tokens.get(loc).equals("punc")){// Added because passing it <true><punc> was just returning a blank
			loc--;
		}
		while(!tokens.get(loc).equals("punc")){
			if(tokens.get(loc).equals("Bool")){
				//ignore
			}else if(tokens.get(loc).equals("false")){
				builder = builder.concat("false");
			}else if(tokens.get(loc).equals("true")){
				builder = builder.concat("true");
			}else if(tokens.get(loc).equals("boolType")){
				builder = builder.concat(symbols.get(varCount));
				varCount++;
			}else if(tokens.get(loc).equals("and")){
				builder = builder.concat(" && ");
			}else if(tokens.get(loc).equals("or")){
				if(XORflag){
					builder = builder.concat(" ^ ");
				}else{
					builder = builder.concat(" || ");
				}
			}else if(tokens.get(loc).equals("double") || tokens.get(loc).equals("numType")){
				builder = builder.concat(numStatement());
				loc--;
			}else{
				throw new IllegalArgumentException("Unsure of how to handle the token: " + tokens.get(loc) + " in a boolean statement");
			}
			loc++;
		}
		return builder;
	}
	
	/**
	*Returns if this statement is a boolean one
	*@return Boolean true if it is a boolean statement
	*/
	private Boolean isBoolType(){
		if(tokens.get(loc).equals("Bool")){
			return true;
		}else if(tokens.get(loc).equals("boolType")){
			return true;
		}else if(tokens.get(loc).equals("XOR")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	*An if statement
	*@return string the line for this if
	*/
	private String ifStatement(){
		String builder = tabs();
		tabCount++;
		builder = builder.concat("if(");
		builder = builder.concat(boolStatement());
		if(tokens.get(loc).equals("then")){
			loc++;
		}
		loc++;
		builder = builder.concat("){");
		return builder;
	}
	
	/**
	*The end of an if statement
	*@return String the end of an if statement
	*/
	private String ifEnd(){
		tabCount--;
		String builder = tabs();
		builder = builder.concat("}");
		loc+=2;
		return builder;
	}
	
	/**
	*An else block
	*@return, the else statement
	*/
	private String elseBlock(){
		tabCount--;
		String builder = tabs();
		builder = builder.concat("}else{");
		tabCount++;
		loc +=2;
		return builder;
	}
	
	/**
	*The switch statement
	*@return String the switch statement
	*/
	private String switchBlock(){
		String builder = tabs();
		tabCount++;
		builder = builder.concat("switch ((int) " + symbols.get(varCount) + ") {");
		loc += 3;
		varCount++;
		firstCase=true;
		return builder;
	}
	
	/**
	*Returns this case block
	*@return String the case block
	*/
	private String caseBlock(){
		tabCount--;
		String builder = tabs();
		tabCount++;
		if(tokens.get(loc).equals("defaultCase")){
			builder = builder.concat("default:");
			loc += 2;
		}else{
			builder = builder.concat("case (int) " + symbols.get(varCount) + ":");
			loc += 4;
			varCount++;
		}
		return builder;
	}
	
	/**
	*Closes the swithc statement
	*@return String the end of a switch statement
	*/
	private String switchClose(){
		tabCount--;
		String builder = tabs();
		builder = builder.concat("}");
		loc += 2;
		return builder;
	}
	
	/**
	*Returns the begining of a for or foreach loop
	*@return String the for loop
	*/
	private String For(){
		String builder = tabs();
		builder = builder.concat("for(");
		loc++;
		String varName = "";
		if(tokens.get(loc).equals("double")){
			builder = builder.concat("int ");
			loc++;
		}else if(tokens.get(loc).equals("string")){
			builder = builder.concat("String ");
			loc++;
		}else if(tokens.get(loc).equals("Bool")){
			builder = builder.concat("Boolean ");
			loc++;
		}else if(tokens.get(loc).equals("char")){
			builder = builder.concat("char ");
			loc++;
		}else{
			throw new IllegalArgumentException("Was expecting Bool, Char, Sitrng, or Double, but recieved: " + tokens.get(loc));
		}
		builder = builder.concat(symbols.get(varCount));
		varName = symbols.get(varCount);
		loc++;
		varCount++;
		if(tokens.get(loc).equals("forEach")){ //for each loop
			builder = builder.concat(" : ");
			builder = builder.concat(symbols.get(varCount) + "){");
			varCount++;
			loc+=3;
		}else{//standard for loop
			builder = builder.concat("=");
			loc++;
			if(tokens.get(loc).equals("charLit")){
				builder = builder.concat(symbols.get(varCount));
				varCount++;
				loc++;
				builder = builder.concat("; " + varName + " <= " + symbols.get(varCount) + "; " + varName + "++");
				loc+=3;
			}else{//numbers
				builder = builder.concat(numStatement());
				builder = builder.concat("; " + varName + " <= ");
				loc++;
				builder = builder.concat(numStatement());
				builder = builder.concat("; " + varName + "++");
				loc++;
			}
			builder = builder.concat("){");
		}
		tabCount++;
		return builder;
	}
	
	/**
	*Returns a string version of a paragraph declaration
	*@return String the string representation of a paragraph representation
	*/
	private String Para(){
		String builder = tabs();
		tabCount++;
		builder = builder.concat("public static ");
		String name = symbols.get(varCount);
		varCount++;
		loc+=2;
		ArrayList<String> types = new ArrayList<String>();
		ArrayList<String> params = new ArrayList<String>();
		while(tokens.get(loc).equals("param")){
			types.add(tokens.get(loc+1));
			params.add(symbols.get(varCount));
			varCount++;
			loc +=3;
		}
		types = typeConversions(types);
		if(tokens.get(loc).equals("returnType")){
			builder = builder.concat(typeConversion(tokens.get(loc+1)));
			loc += 2;
		}else if(tokens.get(loc).equals("punc")){
			//void type
			builder = builder.concat("void");
			loc += 2;
		}else{
			throw new IllegalArgumentException("Unexpected token in method declaration. Token seen: " + tokens.get(loc));
		}
		builder = builder.concat(" " + name + "(");
		Boolean haveParams = false;
		for(int i=0;i<types.size();i++){
			haveParams=true;
			builder = builder.concat(types.get(i) + " " + params.get(i) + ", ");
		}
		if(haveParams){
			builder = builder.substring(0,builder.lastIndexOf(","));
		}
		builder = builder.concat("){");
		loc++; 
		return builder;
	}
	
	/**
	*Translates FiM types to java types
	*@param types the FiM types
	*@return ArrayList<String> the java types
	*/
	private ArrayList<String> typeConversions(ArrayList<String> types){
		ArrayList<String> javaTypes = new ArrayList<String>();
		for(String s : types){
			javaTypes.add(typeConversion(s));
		}
		return javaTypes;
	}
	
	/**
	*Converts one FiM type into a java type
	*@param type the FiM type
	*@return String the java type
	*/
	private String typeConversion(String type){
		if(type.equals("double")){
			return type;
		}else if(type.equals("char")){
			return type;
		}else if(type.equals("Bool")){
			return "boolean";
		}else if(type.equals("string")){
			return "String";
		}else if(type.equals("numArray")){
			return "double[]";
		}else if(type.equals("strArray")){
			return "String[]";
		}else if(type.equals("boolArray")){
			return "boolean[]";
		}else{
			throw new IllegalArgumentException("Unsure of how to convert " + type + " into a java primitive type");
		}
	}
	
	/**
	*Returns the string representation of a return statement
	*@returns String the return statement in java code
	*/
	private String returnStatement(){
		String builder = tabs();
		builder = builder.concat("return ");
		loc++;
		//determine if I need to hand this line to the num statement builder or the bool statement builder
		
		if(tokens.get(loc).equals("callPara")){
			loc++;
		}
		
		if(tokens.get(loc).equals("dubLit")){
			builder = builder.concat(numStatement());
		}else if(tokens.get(loc).equals("numType")){
			builder = builder.concat(numStatement());
		}else if(tokens.get(loc).equals("returnsNumType")){
			builder = builder.concat(numStatement());
		}else if(tokens.get(loc).equals("double")){
			builder = builder.concat(numStatement());
		}else if(tokens.get(loc).equals("true")){
			builder = builder.concat(boolStatement());
		}else if(tokens.get(loc).equals("false")){
			builder = builder.concat(boolStatement());
		}else if(tokens.get(loc).equals("returnsBoolType")){
			builder = builder.concat(boolStatement());
		}else if(tokens.get(loc).equals("boolType")){
			builder = builder.concat(boolStatement());
		}else if(tokens.get(loc).equals("null")){
			builder = builder.concat("null");
		}else{
			
			//at this point I am handing it a literal or a varaible name, both are stored in the symbol table
			builder = builder.concat(symbols.get(varCount));
			varCount++;
			while(!tokens.get(loc).equals("punc")){
				loc++; //this loop solves the problem of seeing a type determiner before a literal
			}
		}
		
		builder = builder.concat(";");
		loc++;
		return builder;
	}
	
	/**
	*Builds up a method call
	*@return String the method call in java
	*/
	private String buildMethodCall(){
		String builder = "";
		//how do I ensure that I have the last parameter? Do I need to get the method classes built by the token generator? I think that may be best
		Method method = new Method("Bad name");
		for(Method m : methods){
			if(m.equals(new Method(symbols.get(varCount)))){
				method = m;
			}
		}
		if(method.equals(new Method("Bad name"))){
			throw new IllegalArgumentException("Code generator unable to find method named: " + symbols.get(varCount));
		}
		varCount++;
		loc +=2;
		builder = builder.concat(" " + method.getName() + "(");
		ArrayList<String> params = method.getParamVars();
		for(int i=0;i<params.size();i++){
			builder = builder.concat(symbols.get(varCount));
			varCount++;
			loc++;
			if(tokens.get(loc).equals("and") && i<params.size()-1){
				loc++;
				builder = builder.concat(", ");
			}
		}
		builder = builder.concat(")");
		return builder;
	}
	
	/**
	*A line that just calls another paragraph
	*@return Stirng, the called paragraph in java
	*/
	private String callingPara(){
		loc++;
		String temp = tabs();
		temp = temp + buildMethodCall() + ";";
		loc++;
		return temp;
	}
	
}













