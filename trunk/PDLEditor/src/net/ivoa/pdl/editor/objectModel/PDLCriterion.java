package net.ivoa.pdl.editor.objectModel;
import java.util.ArrayList;


public class PDLCriterion {

	
	
	String name;
	String cexp;	// hold the name of the expression concerned by the criterion
	ArrayList<String> exps; // holds the names of the expressions used
	String type;    // type of the criterion
	boolean reached;	// for ValueLargerThan and ValueSmallerThan, is the equality valid ?
	String connector; // connector with other criterion
	String criterion; // other criterion
	
	public PDLCriterion(String n) {
		name = n;
		exps = new ArrayList<String>();
		connector = null;
		criterion = null;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		
		String res;
		String dtype = type; // type to display
		String dexps = exps.toString(); // expressions to display
		
		// set type to display
		if(type.equals("IsNull")) {
			dtype="Is null";
			dexps="";
		}
		if(type.equals("BelongToSet")) dtype="Belong to set defined by";
		if(type=="IsInteger") {
			dtype="Is integer";
			dexps="";
		}
		if(type.equals("IsRational")) {
			dtype="Is rational";
			dexps="";
		}
		if(type.equals("IsReal")) {
			dtype="Is real";
			dexps="";
		}
		
		if(type.equals("ValueLargerThan")) {
			if(reached) dtype="Equal or larger than";
			else dtype="Larger than";
			dexps=exps.get(0); // get the 1st expression of the list
		}
		
		if(type.equals("ValueSmallerThan")) {
			if(reached) dtype="Equal or smaller than";
			else dtype="Smaller than";
			dexps=exps.get(0); // get the 1st expression of the list
		}		
		
		if(type.equals("ValueInRange")) dtype="Value in range";
		if(type.equals("DifferentOf")) {
			dtype="Different of";
			dexps=exps.get(0); // get the 1st expression of the list
		}
		if(type.equals("DefaultValue")) {
			dtype="Default value equal to";
			dexps=exps.get(0); // get the 1st expression of the list
		}
		
		
		
		
		res = name+": "+cexp+" "+dtype+" "+dexps;
		
		if(connector!=null) {
			res = res + " "+connector+" "+criterion;
		}
		
		return res;
	}

	public void setType(String t) {
		type = t;
	}
	
	public void addExp(String e) {
		exps.add(e);
	}
	
	public ArrayList<String> getExps() {
		return exps;
	}

	public void setCExp(String c) {
		cexp = c;
	}

	public String getCExp() {
		return cexp;
	}

	public void setConnector(String c) {
		connector = c;
	}

	public void setCriterion(String c) {
		criterion = c;	
	}

	public void setReached(boolean r) {
		reached = r;
		
	}
	
}
