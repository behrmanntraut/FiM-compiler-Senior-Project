/**
*A representation of a method, holds a bunch of data
*@author Brandon Ehrmanntraut
*/
import java.util.*;
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
		returnType = "voidType";
	}
	
	/**
	*Adds a paramater to the method
	*@param type the type of the variable
	*@param name the name of the variable
	*/
	public void addParam(String type, String name){
		containedVars.add(name);
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
	public Boolean paramMatch(ArrayList<String> given){
		if(params.size() != given.size()){
			return false; //not the same number, obviously not correct
		}
		ArrayList<String> typesGiven = translateTypes(given);
		for(int i=0;i<typesGiven.size();i++){
			if(!typesGiven.get(i).equals(params.get(i))){
				if(!typesGiven.get(i).equals("null")){
					return false;
				}					
			}
		}
		return true;
	}
	
	/**
	*Turns all var types into a generic statement so it can be used, ex: dubLit -> double or numType -> double
	*@param given all of the different tpyes given
	*@return ArrayList<String> all of the given types generic forms
	*/
	private ArrayList<String> translateTypes(ArrayList<String> given){
		//having a state pattern for tokens is looking pretty nice right here, but not enough time for me to refactor everything to do that sadly
		//I am not currently supporting calling a method in another methods declaration, so no need to check for those types
		ArrayList<String> types = new ArrayList<String>();
		for(String s : given){
			types.add(translateType(s));
		}
		return types;
	}
	
	/**
	*Translate one single item
	*@param type the type given
	*@return String the generalized type
	*/
	private String translateType(String type){
		if(type.equals("dubLit")){
			return "double";
		}else if(type.equals("dubLit")){
			return "double";
		}else if(type.equals("numType")){
			return "double";
		}else if(type.equals("double")){
			return "double";
		}else if(type.equals("charLit")){
			return "char";
		}else if(type.equals("strLit")){
			return "string";
		}else if(type.equals("charType")){
			return "char";
		}else if(type.equals("strType")){
			return "string";
		}else if(type.equals("char")){
			return "char";
		}else if(type.equals("string")){
			return "string";
		}else if(type.equals("true")){
			return "Bool";
		}else  if(type.equals("false")){
			return "Bool";
		}else  if(type.equals("boolType")){
			return "Bool";
		}else  if(type.equals("Bool")){
			return "Bool";
		}else{
			throw new IllegalArgumentException("Unable to determine the general type of: " + type + ", this may not be a fully supported type to pass in a method");
		}			
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
	@Override
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
	
	/**
	*A toStirng method
	*@return String the string of this object
	*/
	@Override
	public String toString(){
		String builder = "{ Name: " + name;
		builder = builder.concat(" Parameters: " + params);
		builder = builder.concat(" Contains: " + containedVars);
		builder = builder.concat(" Return Type: " + returnType + " } ");
		return builder;
	}
	
	/**
	*Returns a hashmap of all of the params mapped to their types
	*@return HashMap the params mapped to their types
	*/
	public HashMap<String,String> getVarMapping(){
		HashMap<String,String> temp = new HashMap<String,String>();
		for(int i=0;i<params.size();i++){
			temp.put(containedVars.get(i),params.get(i));
		}
		return temp;
	}
	
	public static void main(String[] args){
		Method method = new Method("HelloWorld");
		method.addParam("numType","myNum");
		System.out.println(method);
	}
	
	/**
	*Returns all of the varaible names of the parameters
	*@return ArrayList<String> all of the varaibles that are also parameters
	*/
	public ArrayList<String> getParamVars(){
		ArrayList<String> paramVars = new ArrayList<String>();
		for(int i=0;i<params.size();i++){
			paramVars.add(containedVars.get(i));
		}
		return paramVars;
	}
	
	/**
	*Returns the number of parameters this method needs
	*@return int the number of parameters
	*/
	public int getNumOfParams(){
		return params.size();
	}
	
	/**
	*Returns the parameters for this method
	*@return ArrayList<String> the parameters
	*/
	public ArrayList<String> getParams(){
		//I should be making a copy but just send it
		return params;
	}
	
	
}