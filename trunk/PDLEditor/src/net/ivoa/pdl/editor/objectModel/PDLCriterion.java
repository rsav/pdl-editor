package net.ivoa.pdl.editor.objectModel;
import java.util.ArrayList;


public class PDLCriterion {


	String cexp;	// hold the name of the expression concerned by the criterion
	ArrayList<String> exps; // holds the names of the expressions used
	String type;    // type of the criterion
	boolean reachedInf;	// for ValueLargerThan, is the equality valid ?
	boolean reachedSup; // for ValueSmallerThan, is the equality valid ?
	

	String connector; // connector with other criterion
	String criterion; // other criterion
	
	public PDLCriterion() {
		exps = new ArrayList<String>();
		connector = null;
		criterion = null;
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
			if(reachedSup) dtype="Equal or larger than";
			else dtype="Larger than";
			dexps=exps.get(0); // get the 1st expression of the list
		}
		
		if(type.equals("ValueSmallerThan")) {
			if(reachedInf) dtype="Equal or smaller than";
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
		
		
		
		
		res = cexp+" "+dtype+" "+dexps;
		
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

	public void setReachedInf(boolean r) {
		reachedInf = r;
		
	}
	
	
	public boolean isReachedInf() {
		return reachedInf;
	}

	
	public boolean isReachedSup() {
		return reachedSup;
	}

	public void setReachedSup(boolean r) {
		reachedSup = r;
	}
	
	
	public String getCriterion() {
		return criterion;
	}

	public String getConnector() {
		return connector;
	}

	public String getType() {
		return type;
	}
	
	
	
}
