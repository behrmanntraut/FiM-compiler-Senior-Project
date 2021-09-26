/**
*Controls the many other files that this compiler consists of
*@author Brandon Ehrmanntraut
*/
import java.util.*;
public class driver{
	public static void main(String[] args){
		token test = new token("Example.txt");
		String[] tokens = test.run();
		ArrayList<String> symbols = test.getSymbolTable();
		ArrayList<String> t = new ArrayList<String>(Arrays.asList(tokens));
		System.out.println(t);
		System.out.println("\n\n" + symbols);
		parse parser = new parse(tokens);
		Boolean valid = parser.run();
		System.out.println("\nDid the token set pass the CFG? : " + valid + "\n");
		/*
		if(valid){
			generator gen = new generator(tokens, symbols);
			gen.run();
		}else{
			System.out.println("Code failed to compile");
		}
		*/
	}
}
