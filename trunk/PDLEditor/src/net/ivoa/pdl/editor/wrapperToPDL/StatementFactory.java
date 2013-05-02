package net.ivoa.pdl.editor.wrapperToPDL;

import java.util.Map;

import net.ivoa.parameter.model.Always;
import net.ivoa.parameter.model.AlwaysConditionalStatement;
import net.ivoa.parameter.model.ConditionalStatement;
import net.ivoa.parameter.model.Criterion;
import net.ivoa.parameter.model.Expression;
import net.ivoa.parameter.model.If;
import net.ivoa.parameter.model.IfThenConditionalStatement;
import net.ivoa.parameter.model.Then;
import net.ivoa.pdl.editor.objectModel.PDLCriterion;
import net.ivoa.pdl.editor.objectModel.PDLStatement;

public class StatementFactory {

	private static final StatementFactory instance = new StatementFactory();

	public static StatementFactory getInstance() {
		return instance;
	}

	private StatementFactory() {
	}

	public ConditionalStatement getConditionalStatement(PDLStatement statement,
			Map<String, PDLCriterion> criterionMap,
			Map<String, Expression> expressionMap) {

		if (statement.getType().equalsIgnoreCase("Always")) {
			AlwaysConditionalStatement stat = new AlwaysConditionalStatement();

			String criterionName = statement.getCrit1();

			Criterion containedCriterion = CriterionWrapper.getInstance()
					.wrappingToPDL(criterionMap.get(criterionName),
							criterionMap, expressionMap);

			stat.withAlways(new Always().withCriterion(containedCriterion));

			return stat;
		}

		if (statement.getType().equalsIgnoreCase("IfThen")) {
			String ifCriterionName = statement.getCrit1();
			String thenCriterionName = statement.getCrit2();

			Criterion ifCriterion = CriterionWrapper.getInstance()
					.wrappingToPDL(criterionMap.get(ifCriterionName),
							criterionMap, expressionMap);

			Criterion thenCriterion = CriterionWrapper.getInstance()
					.wrappingToPDL(criterionMap.get(thenCriterionName),
							criterionMap, expressionMap);

			IfThenConditionalStatement itcs = new IfThenConditionalStatement()
					.withIf(new If().withCriterion(ifCriterion)).withThen(
							new Then().withCriterion(thenCriterion));

			return itcs;
		}

		return null;
	}
}
