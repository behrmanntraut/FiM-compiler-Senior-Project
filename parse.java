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
		
		parse tester = new parse(new String[]{"b","a","a","b","a"});
		ArrayList<String> pass = new ArrayList<String>(Arrays.asList(input));
		//System.out.println("Passing in: " + pass);
		compute();
		//System.out.println(pass);
		if(comp.get(pass)==null){
			System.out.println("False");
		}else{
			System.out.println("True");
		}
	}
	
	
	public Boolean run(){
		build();
		ArrayList<String> pass = new ArrayList<String>(Arrays.asList(input));
		//System.out.println("Passing in: " + pass);
		compute();
		//System.out.println(pass);
		if(comp.get(pass)==null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	*Class constructor
	*@param input the string array that is being tested for the language
	*/
	static String input[] = new String[0];
	static HashMap<String,String[][]> G = new HashMap<String,String[][]>();
	static ArrayList<String> keys = new ArrayList<String>();
	static HashMap<ArrayList<String>, HashSet<String>> comp = new HashMap<ArrayList<String>, HashSet<String>>();
	public parse(String Input[]){
		//TODO build the grammar loader directly into this file
		input=Input;
		
	}
	
	/**
	*Build the grammar into the HashMap, used in constructor
	*/
	private void build(){
		try (BufferedReader br = new BufferedReader(new FileReader("Grammar.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
			   String[] sep = line.split(" ");
			   int state=0;
			   String[][] rhs = new String[2][];
			   String lhs = sep[0];
			   int len = sep.length;
			   if(len<4){//only one terminal
				   rhs[0] = new String[]{sep[2]};
			   }else if(len<5){//only one set of things
				   rhs[0] = new String[]{sep[2],sep[3]};
			   }else if(len<7){//a set of two and then a set of one
				   rhs[0] = new String[]{sep[2],sep[3]};
				   rhs[1] = new String[]{sep[5]};
			   }else{//two sets of two
				   rhs[0] = new String[]{sep[2],sep[3]};
				   rhs[1] = new String[]{sep[5],sep[6]};
			   }
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
					
					/*Shows how to view the correct current iteration
					for(int i=0;i<=l;i++){
						if(i==c){
							System.out.print(input[s+i] + ",");
						}else{
						System.out.print(input[s+i]);
						}
					}
					System.out.println();
					*/
					
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
				for(int k=0;k<temp.length;k++){
					if(temp[k].length==1 && temp[k][0].equals(input[i])){
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
		if(prods.length != rule.length){
			return false;
		}else{
			for(int i=0;i<prods.length;i++){
				if(!prods[i].equals(rule[i])){
					return false;
				}
			}
			return true;
		}
	}
	
	
	
	
}