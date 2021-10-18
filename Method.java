/**
*A representation of a method, holds a bunch of data
*@author Brandon Ehrmanntraut
*/
import java.util.ArrayList;
public class Method{
	private ArrayList<String> params = new ArrayList<String>();
	private ArrayList<String> containedVars = new ArrayList<String>();
	private String name;
	private String returnType;
	
	/**
	*Class constructor
	*@param name the name of the method
	*/
	public Method(String name){
		this.name = name;
		returnType = "";
	}
	
	/**
	*Adds a paramater to the method
	*@param type the type of the variable
	*@param name
	*/
	public void addParam(String type, String name){
		containedVars.add(type);
		params.add(type);
	}
	
	/**
	*Adds a variable, indicated by its name
	*@param name the name of the variable used
	*/
	public void addVar(String name){
		containedVars.add(name);
	}
	
	/**
	*Sets the return type of the method
	*@param type the return type, ie numType charType etc...
	*/
	public void setReturnType(String type){
		returnType=type;
	}
	
	/**
	*Returns the name of the method
	*@return String the name of the method
	*/
	public String getName(){
		return name;
	}
	
	/**
	*Returns the type of the method (ie numType or voidType)
	*@return String the type of the method
	*/
	public String getReturnType(){
		if(returnType.isEmpty()){
			return "voidType";
		}else{
			return returnType;
		}
	}
	
	/**
	*Returns if the given paramaters match what this method needs
	*@param given an ArrayList of all of the types of values given
	*/
	public Boolean paramMatch(){
		//this is getting into error handling territory, so will be putting implementation off until error handling week(s)
		
		return true;
	}
	
	/**
	*Returns true if a variable is defined in this scope
	*@param String name the name of the variable to check
	*@return Boolean true if the given variable is defined in this scope
	*/
	public Boolean isVarInMethod(String name){
		return containedVars.contains(name);
	}
	
	/**
	*Checks a bunch of variables for if they are in this scope
	*@param names an ArrayList of all of the variable names
	*@return Boolean true if all of the variables are defined in this scope
	*/
	public Boolean areVarsInMethod(ArrayList<String> names){
		for(String name : names){
			if(!isVarInMethod(name)){
				return false;
			}
		}
		return true;
	}
	
	/**
	*Standard equals method
	*@param other the other thing to compare this to
	*@return boolean true if they are the same object
	*/
	public boolean equals(Object other){
		try{
			return this.equals((Method) other);
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	*Standard equals method
	*@param other the other method to compare this too
	* As of right now, only comparing on name
	*@return boolean true if they have the same name
	*/
	public boolean equals(Method other){
		return other.getName().equals(this.getName());
	}
	

}