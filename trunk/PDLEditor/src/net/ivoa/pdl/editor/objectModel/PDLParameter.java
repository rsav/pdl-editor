package net.ivoa.pdl.editor.objectModel;

import net.ivoa.parameter.model.ParameterType;

public class PDLParameter {

	ParameterType type;
	String UCD;
	String UType;
	String skoss;
	String unit;
	String precision; // name of an expression
	String dimension; // name of an expression

	/**
	 * This boolean parameter is true if the parameter is required. If the
	 * parameter is optional, then the boolean is false
	 */
	Boolean required;

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}
	
	public PDLParameter() {
	}

	public ParameterType getType() {
		return type;
	}

	public void setType(ParameterType t) {
		type = t;
	}

	public String getUCD() {
		return UCD;
	}

	public void setUCD(String u) {
		UCD = u;
	}

	public String getUType() {
		return UType;
	}

	public void setUType(String u) {
		UType = u;
	}

	public String getSkoss() {
		return skoss;
	}

	public void setUnit(String u) {
		unit = u;
	}

	public String getUnit() {
		return unit;
	}

	public String getPrecision() {
		return precision;
	}

	public String getDimension() {
		return dimension;
	}

	public void setSkoss(String s) {
		skoss = s;
	}

	public void setPrecision(String p) {
		precision = p;
	}

	public void setDimension(String d) {
		dimension = d;
	}

}
