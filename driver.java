/**
*Controls the many other files that this compiler consists of
*@author Brandon Ehrmanntraut
*/
import java.util.*;
public class driver{
	public static void main(String[] args){
		ErrorList errors = new ErrorList();
		token test = new token("Example.txt", errors);
		//token test = new token(args[0]);
		String[] tokens = test.run();
		ArrayList<String> symbols = test.getSymbolTable();
		ArrayList<String> t = new ArrayList<String>(Arrays.asList(tokens));
		ArrayList<Method> methods = test.getMethods();
		//System.out.println(t);
		//System.out.println("\n\n" + symbols);
		if(errors.shouldThrowErrors()){
			try{
				errors.throwErrors();
			}catch(IllegalAccessException i){//this should never be run
			}
		}
		
		parse parser = new parse(tokens);
		Boolean valid = parser.fullRun();
		System.out.println("\nDid the token set pass the CFG? : " + valid + "\n");
		
		if(valid){
		//	generator gen = new generator(tokens, symbols, methods);
		//	gen.run();
		}else{
			//go line by line, looking to see which line is invalid
			Integer line=1;
			ArrayList<String> thisLine = new ArrayList<String>();
			for(int i=0;i<t.size();i++){
				if(t.get(i).equals("n")){
					if(thisLine.size()!=0){
					String arr[] = new String[thisLine.size()];
					arr = thisLine.toArray(arr);
					parser = new parse(arr);
					if(!parser.run()){
						errors.addError(Error.createMalformedSentenceError(line));
					}
					}
					line++;
					thisLine = new ArrayList<String>();
					
				}else{
					thisLine.add(t.get(i));
				}
			}
			String arr[] = new String[thisLine.size()];
			arr = thisLine.toArray(arr);
			parser = new parse(arr);
			if(!parser.run()){
				errors.addError(Error.createMalformedSentenceError(line));
			}
			try{
				errors.throwErrors();
			}catch(IllegalAccessException e){
				System.out.println("System detected an error somewhere, but was unable to determine type or location");
			}
		}
		
	}
}
