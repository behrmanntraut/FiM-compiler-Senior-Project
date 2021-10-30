/**
*A queue like structure
*@author Brandon Ehrmanntraut
*/
import java.util.*;
public class LineVars{
	private ArrayList<String> vars = new ArrayList<String>();
	
	public static void main(String[] args){
		LineVars temp = new LineVars();
		temp.addBlank();
		temp.add("var2");
		temp.addNext("var1");
		//System.out.println(temp.getVars());
	}
	
	/**
	*Class constructor, does nothing
	*/
	public LineVars(){
		
	}
	
	/**
	*Adds a blank sapce into the structure
	*/
	public void addBlank(){
		vars.add("");
	}
	
	/**
	*Adds a concrete space into the structure
	*@param var the variable to add
	*/
	public void add(String var){
		vars.add(var);
		//print();
	}
	
	/**
	*Adds the given thing to the first open space
	*@param var the variable to add
	*/
	public void addNext(String var){
		for(int i=0;i<vars.size();i++){
			if(vars.get(i).isEmpty()){
				vars.set(i,var);
				break;
			}
		}
		//print();
	}
	
	/**
	*Returns the stored variables
	*@return ArrayList<String> the stored variables
	*/
	public ArrayList<String> getVars(){
		if(!contrainsEmptySpace()){
			return vars;
		}else{
			throw new IllegalArgumentException("Not all variable slots were properly filled");
		}
	}
	
	/**
	*Adds all of the given strings using the addNext command
	*@param other the varaibles to add
	*/
	public void merge(ArrayList<String> other){
		for(String o : other){
			this.addNext(o);
		}
		//System.out.println("Running merge: ");
		//print();
	}
	
	/**
	*Prints the arraylist above
	*/
	public void print(){
		System.out.println(vars);
	}
	
	/**
	*Returns true if there are no vars stored
	*@return Boolean true if there are no vars stored
	*/
	public Boolean isEmpty(){
		if(vars.size()==0){
			return true;
		}else{
			return false;
		}
	}
	
	private Boolean contrainsEmptySpace(){
		for(String s : vars){
			if(s.isEmpty()){
				return true;
			}
		}
		return false;
	}
	
}