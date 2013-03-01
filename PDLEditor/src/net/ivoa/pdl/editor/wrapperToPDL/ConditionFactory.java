package net.ivoa.pdl.editor.wrapperToPDL;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import net.ivoa.parameter.model.AbstractCondition;
import net.ivoa.parameter.model.BelongToSet;
import net.ivoa.parameter.model.DefaultValue;
import net.ivoa.parameter.model.Expression;
import net.ivoa.parameter.model.IsNull;
import net.ivoa.parameter.model.IsRational;
import net.ivoa.parameter.model.IsReal;
import net.ivoa.parameter.model.ValueInRange;
import net.ivoa.parameter.model.ValueLargerThan;
import net.ivoa.parameter.model.ValueSmallerThan;
import net.ivoa.pdl.editor.objectModel.PDLCriterion;

public class ConditionFactory {

	private static final ConditionFactory instance = new ConditionFactory();

	public static ConditionFactory getInstance() {
		return instance;
	}

	private ConditionFactory() {
	}

	public AbstractCondition getCondition(PDLCriterion criterion,
			Map<String, Expression> expressionMap) {

		String conditionType = criterion.getType();

		if (conditionType.equalsIgnoreCase("IsNull")) {
			return new IsNull();
		}
		if (conditionType.equalsIgnoreCase("BelongToSet")) {
			BelongToSet temp = new BelongToSet();
			temp.withValue(ConditionFactory.getInstance().getExpressionListFromNames(criterion.getExps(), expressionMap));
			return temp;
		}
		if (conditionType.equalsIgnoreCase("IsRational")) {
			return new IsRational();
		}
		if (conditionType.equalsIgnoreCase("IsReal")) {
			return new IsReal();
		}
		if (conditionType.equalsIgnoreCase("ValueLargerThan")) {
			ValueLargerThan temp = new ValueLargerThan();
			temp.withReached(criterion.isReachedSup());
			return temp;
			
			// TODO rajouter la valeur
		}
		if (conditionType.equalsIgnoreCase("ValueSmallerThan")) {
			return new ValueSmallerThan();
			// TODO rajouter la valeur
		}
		if (conditionType.equalsIgnoreCase("ValueInRange")) {
			return new ValueInRange();
			// TODO rajouter les deux valeurs
		}
		if (conditionType.equalsIgnoreCase("DefaultValue")) {
			return new DefaultValue();
			// TODO rajouter la valeur
		}
		return null;
	}

	public List<Expression> getExpressionListFromNames(List<String> names,
			Map<String, Expression> expressionMap) {
		List<Expression> toReturn = new ArrayList<Expression>();
		for (String name : names) {
			Expression temp = expressionMap.get(name);
			if (null != temp) {
				toReturn.add(temp);
			}
		}
		return toReturn;

	}

}
