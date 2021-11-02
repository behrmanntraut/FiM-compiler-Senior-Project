/**
*A FiM++ error
*@author Brandon Ehrmanntraut
*/
public abstract class Error{
	
	/**
	*Returns an int that represents how urgent this error is. 0 being exit immediatly, 1 being can wait until end of phase, and 2 being can wait until end of front end
	*@return int the urgency of this error
	*/
	public abstract int getUrgency();
	
	/**
	*The message that should be printed when the error is eventually displayed
	*@return String the string of this object
	*/
	@Override
	public String toString(){
		return "A FiM++ error message, unsure of type";
	}
	
	/**
	*A variable context error
	*@param var the varaible name for this error
	*@param method the method object that this is in but should not be in
	*@param line the line that the error appeared on
	*/
	public static Error createVariableContextError(String var, Method method, int line){
		return new VariableContextError(var,method,line);
	}
	
	/**
	*Creates and returns a new unknown token error
	*@param token the string representing the unknown phrase
	*@param line the line the error occured on
	*/
	public static Error createUnknownTokenError(String token, int line){
		return new UnknownTokenError(token,line);
	}
	
	
	
	private static class VariableContextError extends Error{
		private String var;
		private Method method;
		private int line;
		/**
		*Class constructor
		*@param var the varaible name for this error
		*@param m the method object that this is in but should not be in
		*@param l the line that the error appeared on
		*/
		public VariableContextError(String var, Method m, int l){
			this.var = var;
			this.method = m;
			this.line=l;
		}
		
		@Override
		public int getUrgency(){
			return 1; //breaks parser, but not tokenizer, so can wait until tokenizer is done
		}
		
		@Override
		public String toString(){
			String builder = "Variable context error on line " + this.line + "\n";
			builder = builder.concat("\tVariable " + var + " not declared in " + this.method.getName() + "\n");
			return builder;
		}
		
	}//end of variable context error
	
	private static class UnknownTokenError extends Error{
		private String token;
		private int line;
		
		public UnknownTokenError(String token, int line){
			this.token=token;
			System.out.println(line);
			if(token.isEmpty()){
				throw new IllegalArgumentException("Cannot have an empty line as an unknown argument");
			}
			this.line=line;
		}
		
		@Override
		public int getUrgency(){
			return 1;//not urgent but since there is something here that the parser cannot see, I cannot in my right mind give this to the parser
		}
		
		@Override
		public String toString(){
			String builder = "Unknown phrase on line " + this.line + "\n";
			builder = builder.concat("\t Unknown phrase: " + this.token + "\n");
			return builder;
		}
		
		
	}//end of Unknonw Token Error
	
	
}