package net.ivoa.pdl.editor.wrapperToPDL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ivoa.parameter.model.ConditionalStatement;
import net.ivoa.parameter.model.ConstraintOnGroup;
import net.ivoa.parameter.model.Expression;
import net.ivoa.parameter.model.ParameterGroup;
import net.ivoa.parameter.model.ParameterReference;
import net.ivoa.parameter.model.SingleParameter;
import net.ivoa.pdl.editor.objectModel.PDLCriterion;
import net.ivoa.pdl.editor.objectModel.PDLGroup;
import net.ivoa.pdl.editor.objectModel.PDLParameter;
import net.ivoa.pdl.editor.objectModel.PDLStatement;
import net.ivoa.pdl.editor.utilities.Utilities;

public class GroupWrapper {

	private static final GroupWrapper instance = new GroupWrapper();

	public static GroupWrapper getInstance() {
		return instance;
	}

	private GroupWrapper() {
	}

	public ParameterGroup wrapToPDL(PDLGroup group,
			Map<String, PDLStatement> mapStats,
			Map<String, PDLParameter> mapParams,
			Map<String, Expression> mapExpressions,
			Map<String, PDLCriterion> mapCrits) {

		ParameterGroup toReturn = new ParameterGroup();

		toReturn.setName(group.getName());

		// Performing conversion for parameters attached into this group
		List<String> attachedParamsNames = group.getParams();

		List<ParameterReference> singleParameterRefList = new ArrayList<ParameterReference>();
		for (String currentParamName : attachedParamsNames) {
			SingleParameter temp = ParameterWrapper.getInstance()
					.wrappingToPDL(mapParams.get(currentParamName),
							currentParamName, mapExpressions);

			ParameterReference tempRef = new ParameterReference()
					.withParameterName(currentParamName);

			singleParameterRefList.add(tempRef);
		}

		toReturn.withParameterRef(singleParameterRefList);

		// Performing conversion for statements attached into this group
		List<String> statementsAttachedIntoThisGroup = Utilities.getInstance()
				.getStatementsAttachedToGroup(group.getName(), mapStats);

		List<ConditionalStatement> statements = new ArrayList<ConditionalStatement>();

		for (String currentStatementName : statementsAttachedIntoThisGroup) {
			ConditionalStatement tempStatement = StatementFactory.getInstance()
					.getConditionalStatement(
							mapStats.get(currentStatementName), mapCrits,
							mapExpressions);
			statements.add(tempStatement);
		}

		toReturn.withConstraintOnGroup(new ConstraintOnGroup()
				.withConditionalStatement(statements));

		// Processing children
		List<ParameterGroup> childrenGroups = new ArrayList<ParameterGroup>();
		List<PDLGroup> children = group.getChildren();
		for (PDLGroup currentChild : children) {
			ParameterGroup tempChild = wrapToPDL(currentChild, mapStats,
					mapParams, mapExpressions, mapCrits);
			childrenGroups.add(tempChild);
		}

		toReturn.withParameterGroup(childrenGroups);

		return toReturn;
	}

}
