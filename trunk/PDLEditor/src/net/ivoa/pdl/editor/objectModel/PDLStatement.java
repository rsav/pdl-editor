package net.ivoa.pdl.editor.objectModel;

public class PDLStatement {

	public String getType() {
		return type;
	}

	String type;  // type: Always or IfThen
	String crit1; // first criterion	
	String crit2; // second criterion
	String group; // the group where the criterion is attached to
	
	public PDLStatement() {
	}
	
	public String toString() {

		String res = "";
		
		if(type.equals("Always")) {
			res = " Always " + crit1;
		}
		
		if(type.equals("IfThen")) {
			res = " If " + crit1 + " Then " +crit2;
		}
		
		
		res = res+ " ("+group+")";
		
		return res;
		
	}

	public void setType(String t) {
		type = t;
	}

	public void setCrit1(String c) {
		crit1 = c;
	}
	
	public void setCrit2(String c) {
		crit2 = c;
	}

	public void setGroup(String g) {
		group = g;
	}

		

	
	public String getCrit1() {
		return crit1;
	}
	
	public String getCrit2() {
		return crit2;
	}

	public String getGroup() {
		return group;		
	}
	
	
	
	
}
