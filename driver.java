/**
*Controls the many other files that this compiler consists of
*@author Brandon Ehrmanntraut
*/
import java.util.ArrayList;
public class driver{
	public static void main(String[] args){
		token test = new token("Example.txt");
		String[] tokens = test.run();
		ArrayList<String> symbols = test.getSymbolTable();
		parse parser = new parse(tokens);
		Boolean valid = parser.run();
		if(valid){
			generator gen = new generator(tokens, symbols);
			gen.run();
		}else{
			System.out.println("Code failed to compiler");
		}
		
	}
}
