package net.ivoa.pdl.editor.wrapperToPDL;

import java.util.Map;

import net.ivoa.parameter.model.Expression;
import net.ivoa.parameter.model.ParameterDependency;
import net.ivoa.parameter.model.SingleParameter;
import net.ivoa.pdl.editor.objectModel.PDLParameter;

public class ParameterWrapper {

	private static final ParameterWrapper instance = new ParameterWrapper();

	public static ParameterWrapper getInstance() {
		return instance;
	}

	private ParameterWrapper() {
	}

	public SingleParameter wrappingToPDL(PDLParameter param, String paramName, Map<String, Expression> expressionsInProject){
		SingleParameter toReturn=new SingleParameter();
		
		toReturn.setName(paramName);
		toReturn.setParameterType(param.getType());
		toReturn.setDimension(expressionsInProject.get(param.getDimension()));
		toReturn.setPrecision(expressionsInProject.get(param.getPrecision()));
		toReturn.setSkossConcept(param.getSkoss());
		toReturn.setUCD(param.getUCD());
		toReturn.setUnit(param.getUnit());
		toReturn.setUType(param.getUType());
		
		if(param.getRequired()){
			toReturn.setDependency(ParameterDependency.REQUIRED);
		}else{
			toReturn.setDependency(ParameterDependency.OPTIONAL);
		}
		
		return toReturn;
	}
}
