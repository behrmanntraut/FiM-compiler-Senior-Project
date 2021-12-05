/**
*An implementation of CYK algorithm
*Note that my implementation is non-ideal, SYK runs in On^3 but this runs in On^7 ...
*@author Brandon Ehrmanntraut
*/
import java.util.*;
import java.io.*;
public class parse{
	public static void main(String args[]){
		//Test stuffs
		parse tester = new parse(new String[]{"beginfile","class","punc","report","punc", "n", "import", "iName", "punc", "n", "n", "n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","n", "endfile", "punc", "signee", "punc"});
		ArrayList<String> pass = new ArrayList<String>(Arrays.asList(input));
		
		
		compute();
		/*
		ArrayList keyss = new ArrayList(comp.keySet());
		for(int i=0;i<keyss.size();i++){
			if(comp.get(keyss.get(i))!= null){
				System.out.println(comp.get(keyss.get(i)) + "\t\t" + keyss.get(i));
			}
		}
		*/
		if(comp.get(pass)==null){
			System.out.println("False");
		}else{	
			System.out.println("True");
		}
	}
	
	
	public Boolean run(){
		//build();
		ArrayList<String> pass = new ArrayList<String>(Arrays.asList(input));
		compute();
		if(comp.get(pass)==null){
			//System.out.println("Passed in: " + pass);
			/*
			ArrayList keyss = new ArrayList(comp.keySet());
			for(int i=0;i<keyss.size();i++){
				if(comp.get(keyss.get(i))!= null){
					System.out.println(comp.get(keyss.get(i)) + "\t\t" + keyss.get(i));
				}
			}
			*/
			return false;
		}else{
			return true;
		}
		
	}
	
	/**
	*Runs the parser but checks if both the token set is valid and that this is a full program
	*@return Boolean true if the program is fully valid
	*/
	public Boolean fullRun(){
		//build();
		ArrayList<String> pass = new ArrayList<String>(Arrays.asList(input));
		compute();
		/*
		ArrayList keyss = new ArrayList(comp.keySet());
		for(int i=0;i<keyss.size();i++){
			if(comp.get(keyss.get(i))!= null){
				System.out.println(comp.get(keyss.get(i)) + "\t\t" + keyss.get(i));
			}
		}
		*/
		HashSet<String> temp = new HashSet<String>();
		temp.add("S");
		if(comp.get(pass)==null){
			return false;
		}
		if(!comp.get(pass).equals(temp)){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	*Class constructor
	*@param input the string array that is being tested for the language
	*/
	private static String input[] = new String[0];
	private static HashMap<String,String[][]> G = new HashMap<String,String[][]>();
	private static ArrayList<String> keys = new ArrayList<String>();
	private static HashMap<ArrayList<String>, HashSet<String>> comp = new HashMap<ArrayList<String>, HashSet<String>>();
	public parse(String Input[]){
		//TODO build the grammar loader directly into this file
		HashMap<String,String[][]> G = new HashMap<String,String[][]>();
		keys = new ArrayList<String>();
		comp = new HashMap<ArrayList<String>, HashSet<String>>();
		input=Input;
		build();
	}
	
	/**
	*Build the grammar into the HashMap, used in constructor
	*/
	private void build(){
		try (BufferedReader br = new BufferedReader(new FileReader("Grammar.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
			   String[] sep = line.split(" ");
			   for(int i=0;i<sep.length;i++){
				   sep[i] = sep[i].trim();
			   }
			   String lhs = sep[0];
			   int len = sep.length;
			   if(lhs.equals("//")){
				   continue;//I am using this to put comments in the grammar file
			   }
			   //Build an arraylist of all the rules
			   ArrayList<ArrayList<String>> Rhs = new ArrayList<ArrayList<String>>();
			   int slot=0;
			   for(int i=2;i<sep.length;i++){
				   if(sep[i].charAt(0)=='|'){
					   slot++;
					   continue;
				   }
				   if(Rhs.size()<=slot){
					   Rhs.add(new ArrayList<String>());
				   }
				   ArrayList<String> temp = Rhs.get(slot);
				   temp.add(sep[i]);
				   Rhs.set(slot,temp);
			   }
			   
			   String[][] rhs = new String[Rhs.size()][];
			   for(int i=0;i<Rhs.size();i++){
				   String[] tempStr = new String[Rhs.get(i).size()];
				   for(int j=0;j<tempStr.length;j++){
					   tempStr[j]=Rhs.get(i).get(j);
				   }
				   rhs[i]=tempStr;
			   }
			   
			   /*
			   for(int i=0;i<rhs.length;i++){
				   for(int j=0;j<rhs[i].length;j++){
					   System.out.print(rhs[i][j] + " ");
				   }
				   System.out.println("Line");
			   }
			   */
			   G.put(lhs,rhs);
			   
			}
		}catch(Exception E){
			
		}
		keys = new ArrayList<String>(G.keySet());
	}
	
	public static void compute(){
		firstLayer();
		
		for(int l=1;l<input.length;l++){//length
			//System.out.println(l + "/" + input.length);
			for(int s=0;s<input.length-l;s++){//start
				for(int c=0;c<l;c++){//comma goes after s+c in input
					ArrayList<String> span = new ArrayList<String>();
					for(int i=0;i<=l;i++){
						span.add(input[s+i]);
					}
					//get all production
					String[][] allProd = getProd(s,c,l);
					//for each production, check if it is usable
					if(allProd.length==0){//a null pointer was thrown, shortcut and throw a new null pointer into the map
						if(comp.containsKey(span)){
							//ignore
						}else{
							comp.put(span,null);
						}
					}else{//can still look for valid things, may or may not exist
						HashSet<String> found = finder(allProd);
						if(found.isEmpty()){
							if(!comp.containsKey(span)){
								comp.put(span,null);
							}
						}else{
							if(comp.containsKey(span)){
								//already exists, need to add to it
								HashSet<String> cur = comp.get(span);
								if(cur==null){
									cur = new HashSet<String>();
									cur.addAll(found);
									comp.put(span,cur);
								}else{
									cur.addAll(found);
									comp.put(span,cur);
								}
							}else{
								//Does not, need to create it myself
								HashSet<String> cur = new HashSet<String>();
								cur.addAll(found);
								comp.put(span,cur);
							}
						}
					}	
				}
			}
		}
	}
	
	/**
	*Fills in the first layer, requires less pulling of things and multiplication and stuff
	*/
	private static void firstLayer(){
		for(int i=0;i<input.length;i++){
			//go accross, stopping one short from each time
			//foreach non-terminal to char at i 
				//look at the individual word and put up all non-terminals that point to it
			for(int j=0;j<keys.size();j++){
				String[][] temp = G.get(keys.get(j));
				//System.out.println(j);
				for(int k=0;k<temp.length;k++){
					if(temp[k].length==1){
						//System.out.println("Hit");
						if(temp[k][0].equals(input[i])){
							//System.out.println("Hit2");
							ArrayList<String> temp2 = new ArrayList<String>();
							temp2.add(temp[k][0]);
							HashSet<String> temp3 = new HashSet<String>();
							if(comp.containsKey(temp2)){
								temp3 = comp.get(temp2);
							}
							temp3.add(keys.get(j));
							comp.put(temp2,temp3);
						}
					}
				}
				
			}
		}
	}
	
	/**
	*Gets the current prduction set
	*/
	private static String[][] getProd(int s, int c, int l){
		ArrayList<String> beforeSpan = new ArrayList<String>();
		ArrayList<String> afterSpan = new ArrayList<String>();
		for(int i=0;i<=l;i++){
			if(i<=c){
				beforeSpan.add(input[s+i]);
			}else{
				afterSpan.add(input[s+i]);
			}
		}
		try{
			HashSet<String> beforeComp = comp.get(beforeSpan);
			HashSet<String> afterComp = comp.get(afterSpan);
			ArrayList<String> beforeComp2 = new ArrayList<String>(beforeComp);
			ArrayList<String> afterComp2 = new ArrayList<String>(afterComp);
			String[][] prods = new String[beforeComp.size()*afterComp.size()][2];
			int z=0;
			for(int x=0;x<beforeComp2.size();x++){
				for(int y=0;y<afterComp2.size();y++){
					String[] temp = new String[2];
					temp[0]=beforeComp2.get(x);
					temp[1]=afterComp2.get(y);
					prods[z]=temp;
					z++;
				}
			}
			
			return prods;
			
		}catch(NullPointerException E){
			return new String[0][0];//happens if one of the previous cells was a null
		}
		
		
	}
	
	/**
	*Finds the valid grammar rules for the given cartesian products, returns an empty set if one does not exist
	*/
	private static HashSet<String> finder(String[][] prods){
		HashSet<String> allV = new HashSet<String>();
		for(int i=0;i<prods.length;i++){//for each production
				for(int j=0;j<keys.size();j++){//for each key
					for(int k=0;k<G.get(keys.get(j)).length;k++){//for each rhs per key
						if(compare(prods[i],G.get(keys.get(j))[k])){
							allV.add(keys.get(j));
						}
					}
				}
		}
		return allV;
	}
	
	private static boolean compare(String[] prods, String[] rule){
		/*
		if(prods[0].equals("Begin")){
			for(int i=0;i<prods.length;i++){
				System.out.print(prods[i] + " ");
			}
			System.out.print("\t");
			for(int j=0;j<rule.length;j++){
				System.out.print(rule[j] + " ");
			}
			System.out.println();
		}
		*/
		if(prods.length != rule.length){
			return false;
		}else{
			for(int i=0;i<prods.length;i++){
				if(!prods[i].equals(rule[i])){
					if(prods[0].equals("Begin")){
						//System.out.println("Here");
					}
					return false;
				}
			}
			if(prods[0].equals("Begin")){
				//System.out.println("Sending true");
			}
			return true;
		}
	}
	
	
	
	
}
