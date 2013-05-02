package net.ivoa.pdl.editor.utilities;

import java.util.ArrayList;
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
					.println("DEBUG PDLEditor.initialize: Exploring statement name="
							+ statName);

			PDLStatement stat = (PDLStatement) mapStats.get(statName);
			String statGroup = stat.getGroup();
			if (statGroup.equals(groupName)) { // group for current statement is
												// the same as the group to be
												// deleted
				statsBelongingToGivenGroup.add(statName);
			}
		}
		return statsBelongingToGivenGroup;
	}

}
