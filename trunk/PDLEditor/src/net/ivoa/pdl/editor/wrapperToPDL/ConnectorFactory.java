package net.ivoa.pdl.editor.wrapperToPDL;

import java.util.Map;

import net.ivoa.parameter.model.And;
import net.ivoa.parameter.model.Expression;
import net.ivoa.parameter.model.LogicalConnector;
import net.ivoa.parameter.model.Or;
import net.ivoa.pdl.editor.objectModel.PDLCriterion;

public class ConnectorFactory {
	private static final ConnectorFactory instance = new ConnectorFactory();

	public static ConnectorFactory getInstance() {
		return instance;
	}

	private ConnectorFactory() {
	}

	public LogicalConnector buildConnector(String nameOfLinkedCriterion,
			String connectorType, Map<String, PDLCriterion> criterionMap,
			Map<String, Expression> expressionMap) {
		LogicalConnector toReturn = null;

		PDLCriterion containedCriterion = criterionMap
				.get(nameOfLinkedCriterion);

		if (connectorType.equalsIgnoreCase("AND")) {
			toReturn = new And();
			toReturn.withCriterion(CriterionWrapper.getInstance()
					.wrappingToPDL(containedCriterion, criterionMap,
							expressionMap));
		}
		if (connectorType.equalsIgnoreCase("OR")) {
			toReturn = new Or();
			toReturn.withCriterion(CriterionWrapper.getInstance()
					.wrappingToPDL(containedCriterion, criterionMap,
							expressionMap));
		}

		return toReturn;

	}

}
