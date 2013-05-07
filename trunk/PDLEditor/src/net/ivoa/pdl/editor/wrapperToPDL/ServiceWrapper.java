package net.ivoa.pdl.editor.wrapperToPDL;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultTreeModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import net.ivoa.parameter.model.Expression;
import net.ivoa.parameter.model.Parameters;
import net.ivoa.parameter.model.Service;
import net.ivoa.parameter.model.SingleParameter;
import net.ivoa.pdl.editor.objectModel.PDLCriterion;
import net.ivoa.pdl.editor.objectModel.PDLGroup;
import net.ivoa.pdl.editor.objectModel.PDLParameter;
import net.ivoa.pdl.editor.objectModel.PDLService;
import net.ivoa.pdl.editor.objectModel.PDLStatement;

public class ServiceWrapper {
	private static final ServiceWrapper instance = new ServiceWrapper();

	public static ServiceWrapper getInstance() {
		return instance;
	}

	private ServiceWrapper() {
	}

	private Service WrapToPDL(PDLService service, PDLGroup inputs,
			PDLGroup outputs, Map<String, PDLStatement> mapStats,
			Map<String, PDLParameter> mapParams,
			Map<String, Expression> mapExpressions,
			Map<String, PDLCriterion> mapCrits,
			DefaultTreeModel treeModelGroups) {

		Service toReturn = new Service();
		toReturn.withDescription(service.getDescription());
		toReturn.withInputs(GroupWrapper.getInstance().wrapToPDL(inputs,
				mapStats, mapParams, mapExpressions, mapCrits, treeModelGroups));
		toReturn.withOutputs(GroupWrapper.getInstance().wrapToPDL(outputs,
				mapStats, mapParams, mapExpressions, mapCrits, treeModelGroups));
		toReturn.withServiceId(service.getID());
		toReturn.withServiceName(service.getName());

		// building the list of parameters attached to the service
		List<SingleParameter> singleParameterList = new ArrayList<SingleParameter>();

		List<String> paramNames = inputs.getParams();
		paramNames.addAll(outputs.getParams());

		for (String currentParamName : paramNames) {
			SingleParameter temp = ParameterWrapper.getInstance()
					.wrappingToPDL(mapParams.get(currentParamName),
							currentParamName, mapExpressions);

			singleParameterList.add(temp);
		}
		toReturn.withParameters(new Parameters(singleParameterList));

		return toReturn;
	}

	public void serializeToXML(PDLService service, PDLGroup inputs,
			PDLGroup outputs, Map<String, PDLStatement> mapStats,
			Map<String, PDLParameter> mapParams,
			Map<String, Expression> mapExpressions,
			Map<String, PDLCriterion> mapCrits,
			DefaultTreeModel treeModelGroups,
			String outputXMLFileName)
			throws JAXBException, PropertyException, FileNotFoundException {

		Service wrappedPDLservice = ServiceWrapper.getInstance().WrapToPDL(
				service, inputs, outputs, mapStats, mapParams, mapExpressions,
				mapCrits, treeModelGroups);

		JAXBContext jaxbContext = JAXBContext
				.newInstance("net.ivoa.parameter.model");

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(
				true));
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
				"http://www.ivoa.net/xml/Parameter/v0.1 PDL-V1.0.xsd");

		marshaller.marshal(wrappedPDLservice, new FileOutputStream(
				outputXMLFileName));
	}

}
