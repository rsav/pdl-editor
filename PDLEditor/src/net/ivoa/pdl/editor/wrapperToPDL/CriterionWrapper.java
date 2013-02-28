package net.ivoa.pdl.editor.wrapperToPDL;

import java.util.Map;

import net.ivoa.parameter.model.Criterion;
import net.ivoa.parameter.model.Expression;
import net.ivoa.pdl.editor.objectModel.PDLCriterion;

public class CriterionWrapper {

	private static final CriterionWrapper instance = new CriterionWrapper();

	public static CriterionWrapper getInstance() {
		return instance;
	}

	private CriterionWrapper() {
	}

	public Criterion wrappingToPDL(PDLCriterion criterion,
			Map<String, PDLCriterion> criterionMap, Map<String,Expression> expressionMap) {
		Criterion toReturn = new Criterion();

		toReturn.setConditionType(ConditionFactory.getInstance().getCondition(
				criterion,expressionMap));
		toReturn.setExpression(expressionMap.get(criterion.getCExp()));
		if (null != criterion.getConnector()) {
			toReturn.setLogicalConnector(ConnectorFactory.getInstance()
					.buildConnector(criterion.getCriterion(),
							criterion.getConnector(), criterionMap,expressionMap));
		}

		return toReturn;

	}

}
