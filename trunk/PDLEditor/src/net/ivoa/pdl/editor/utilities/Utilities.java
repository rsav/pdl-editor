package net.ivoa.pdl.editor.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ivoa.pdl.editor.objectModel.PDLStatement;

public class Utilities {

	private static final Utilities instance = new Utilities();

	public static Utilities getInstance() {
		return instance;
	}

	private Utilities() {
	}

	public ArrayList<String> getStatementsAttachedToGroup(String groupName,
			Map<String, PDLStatement> mapStats) {
		ArrayList<String> statsBelongingToGivenGroup = new ArrayList<String>();

		for (String statName : mapStats.keySet()) {

			System.out
					.println("DEBUG Utilities.getStatementsAttachedToGroup: Exploring statement name="
							+ statName);

			PDLStatement stat = (PDLStatement) mapStats.get(statName);
			String statGroup = stat.getGroup();
			if (statGroup.equals(groupName)) { // group for current statement is
												// the same as the group searched for
				statsBelongingToGivenGroup.add(statName);
			}
		}
		return statsBelongingToGivenGroup;
	}

	
	// return an expression constant as a string of ;-separated values
	public String expressionConstantToString(
			List<String> expConstant) {
		
		String constant = new String("");
		for (String c: expConstant) {
			if(constant.equals(new String(""))) {
				constant = c; // first component
			} else {
				constant = constant + ";" + c; // remaining components
			}
		}
		
		
		
		return constant;
		
	}
	
	
}
