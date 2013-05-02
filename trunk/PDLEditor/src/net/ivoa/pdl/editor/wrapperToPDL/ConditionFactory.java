package net.ivoa.pdl.editor.wrapperToPDL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
			temp.withValue(ConditionFactory.getInstance()
					.getExpressionListFromNames(criterion.getExps(),
							expressionMap));
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
			String expressionName = criterion.getExps().get(0);
			temp.setValue(expressionMap.get(expressionName));
			return temp;
		}
		if (conditionType.equalsIgnoreCase("ValueSmallerThan")) {
			ValueSmallerThan temp = new ValueSmallerThan();
			temp.withReached(criterion.isReachedInf());
			String expressionName = criterion.getExps().get(0);
			temp.setValue(expressionMap.get(expressionName));
			return temp;
		}
		if (conditionType.equalsIgnoreCase("ValueInRange")) {

			ValueSmallerThan supLimit = new ValueSmallerThan();
			supLimit.withReached(criterion.isReachedInf());
			// Following line get argument 1 because sup is stored in the second
			// argument of the list
			String supExpressionName = criterion.getExps().get(1);
			supLimit.setValue(expressionMap.get(supExpressionName));

			ValueLargerThan infLimit = new ValueLargerThan();
			supLimit.withReached(criterion.isReachedSup());
			// Following line get argument 0 because sup is stored in the first
			// argument of the list
			String infExpressionName = criterion.getExps().get(0);
			supLimit.setValue(expressionMap.get(infExpressionName));

			ValueInRange vir = new ValueInRange().withSup(supLimit).withInf(
					infLimit);
			return vir;
		}
		if (conditionType.equalsIgnoreCase("DefaultValue")) {
			
			DefaultValue defaultValue = new DefaultValue();
			String expressionName = criterion.getExps().get(0);
			defaultValue.withValue(expressionMap.get(expressionName));
			return defaultValue;
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
