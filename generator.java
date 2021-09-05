/**
*Code generator the takes the tokens after they are validated and builds a java file
*@author Brandon Ehrmanntraut
*/
import java.util.*;
import java.io.*;
public class generator{
	public static void main(String[] args){
		
	}
	
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
			master = master.concat(navigate(tokens.get(loc)));
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
		}
		
		System.out.println("The code generator did not know what to do when it encountered the token: " + cur);
		return "";
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
		return builder;
	}
	
	/**
	*Handles the import calls
	*@return String the java code for this line, expressed as a string
	*/
	private String Import(){
		String builder = "import ";
		builder = builder.concat(symbols.get(varCount));
		builder = builder.concat(";");
		varCount++;
		loc += 3;
		return builder;
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
		auth = auth.concat("\n*/");
		master = auth.concat(master);
		loc = tokens.size();//catch to ensure that the prgoram does not loop forever
		return "}";
	}
	
	
	
	
}













