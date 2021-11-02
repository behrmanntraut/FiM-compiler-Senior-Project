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
		System.out.println(t);
		System.out.println("\n\n" + symbols);
		if(errors.shouldThrowErrors()){
			try{
				errors.throwErrors();
			}catch(IllegalAccessException i){//this should never be run
			}
		}
		/*
		parse parser = new parse(tokens);
		Boolean valid = parser.run();
		System.out.println("\nDid the token set pass the CFG? : " + valid + "\n");
		
		if(valid){
			generator gen = new generator(tokens, symbols, methods);
			gen.run();
		}else{
			System.out.println("Code failed to compile");
		}
		*/
	}
}
