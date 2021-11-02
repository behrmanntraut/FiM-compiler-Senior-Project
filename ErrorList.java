/**
*A place to store all error
*@author Brandon Ehrmanntraut
*/
import java.util.ArrayList;
public class ErrorList{
	//not implemented yet, but ideally I will learn how to create custom Java errors again and use those
	private ArrayList<Error> errors = new ArrayList<Error>();
	
	public ErrorList(){ //needs no paramters on init		
	}
	
	/**
	*Adds a new error to the error list
	*@param err the error to add
	*/
	public void addError(Error err){
		if(err.getUrgency()==0){
			System.out.println(err);
			throw new IllegalArgumentException("Urgent error detected, please adress");
		}else{
			errors.add(err);
		}
	}
	
	/**
	*Returns true if no errors are stored here
	*@return Boolean true if no errors detected
	*/
	public Boolean isEmpty(){
		return errors.isEmpty();
	}
	
	/**
	*"Throws" all of the errors
	*/
	public void throwErrors() throws IllegalAccessException{
		if(errors.isEmpty()){
			throw new IllegalAccessException("Error list was asked to throw errors but there are no errors to throw");
		}else{
			String builder = "\n";
			for(Error e : errors){
				builder = builder.concat(e.toString());
			}
			System.out.println(builder);
			System.out.println(errors.size() + " errors\n");
			System.exit(0);//tell program to stop completely
		}
	}
	
	/**
	*Returns true if there is a priority 1 error anywhere within this
	*@return Boolean true if errors should be thrown
	*/
	public Boolean shouldThrowErrors(){
		for(Error e : errors){
			if(e.getUrgency()==1){
				return true;
			}
		}
		return false;
	}
	
}