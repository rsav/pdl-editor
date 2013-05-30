package net.ivoa.pdl.editor.tmp;

import visitors.GeneralParameterVisitor;
import net.ivoa.parameter.model.ParameterType;
import CommonsObjects.GeneralParameter;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GeneralParameter a = new GeneralParameter("4.0",
					ParameterType.INTEGER, "entier",
					new GeneralParameterVisitor());
			System.out.println("ENTIER CREE");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
