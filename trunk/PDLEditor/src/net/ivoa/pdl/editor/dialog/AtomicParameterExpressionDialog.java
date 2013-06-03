package net.ivoa.pdl.editor.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;

import net.ivoa.parameter.model.AtomicConstantExpression;
import net.ivoa.parameter.model.AtomicParameterExpression;
import net.ivoa.parameter.model.Expression;
import net.ivoa.parameter.model.Operation;
import net.ivoa.parameter.model.OperationType;
import net.ivoa.parameter.model.ParameterReference;
import net.ivoa.parameter.model.ParameterType;
import net.ivoa.pdl.editor.guiComponent.MapComboBoxModel;
import net.ivoa.pdl.editor.objectModel.PDLParameter;

public class AtomicParameterExpressionDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private TreeMap<String,Expression> mapExps;
	
	private TreeMap<String, Expression> mapExpsForOperand;
	private MapComboBoxModel comboBoxModelOperand;
	
	private TreeMap<String, Expression> mapExpsForPower;
	private MapComboBoxModel comboBoxModelPower;
	
	
	private MapComboBoxModel comboBoxModelExps;
	private JComboBox comboBoxExps;
	
	private TreeMap<String, PDLParameter> mapParams;
	private MapComboBoxModel comboBoxModelParameter;
	
	
	

	/**
	 * Create the dialog.
	 */
	public AtomicParameterExpressionDialog(TreeMap<String,Expression> me, MapComboBoxModel mce, JComboBox ce, TreeMap<String,PDLParameter> mp) {
		
		mapExps = me;
		comboBoxModelExps = mce;
		comboBoxExps = ce;
		mapParams = mp;
		
		
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(6, 6, 61, 16);
		contentPanel.add(lblName);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(79, 0, 134, 28);
		contentPanel.add(textFieldName);
		textFieldName.setColumns(10);
		
		JLabel lblParameter = new JLabel("Parameter:");
		lblParameter.setBounds(6, 34, 75, 16);
		contentPanel.add(lblParameter);
		
		
		comboBoxModelParameter = new MapComboBoxModel(mapParams);
		final JComboBox comboBoxParameter = new JComboBox(comboBoxModelParameter);
		comboBoxParameter.setBounds(79, 30, 134, 27);
		contentPanel.add(comboBoxParameter);
		
		JLabel lblPower = new JLabel("Power:");
		lblPower.setBounds(225, 4, 61, 16);
		contentPanel.add(lblPower);
		
		JLabel lblOperation = new JLabel("Operation:");
		lblOperation.setBounds(225, 31, 81, 16);
		contentPanel.add(lblOperation);
		
		final JComboBox comboBoxOperation = new JComboBox(OperationType.values());
		comboBoxOperation.setBounds(302, 27, 121, 27);
		contentPanel.add(comboBoxOperation);
		
		JLabel lblOperand = new JLabel("Operand");
		lblOperand.setBounds(225, 59, 61, 16);
		contentPanel.add(lblOperand);
		
		
		mapExpsForOperand = new TreeMap<String, Expression>(mapExps);
		mapExpsForOperand.put("",null); // since the operation is not mandatory, we provide a choice for no operand
		comboBoxModelOperand = new MapComboBoxModel(mapExpsForOperand);
		
		final JComboBox comboBoxOperand = new JComboBox(comboBoxModelOperand);
		comboBoxOperand.setBounds(302, 55, 121, 27);
		contentPanel.add(comboBoxOperand);
		
		// Create map of expressions that can be used for power
		mapExpsForPower = new TreeMap<String, Expression>(); // to be filled below
		mapExpsForPower.put("",null); // since the power is not mandatory, we provide a choice for no expression
		// power must be an AtomicConstantExpression: remove expressions which are not of class AtomicConstantExpression
		for(String key: mapExps.keySet()) {
			Expression exp = mapExps.get(key);
			Class expClass = exp.getClass(); // get the class of the expression
			if(expClass == AtomicConstantExpression.class) {
				mapExpsForPower.put(key, exp);
			}
		}
		
		comboBoxModelPower = new MapComboBoxModel(mapExpsForPower);
		
		final JComboBox comboBoxPower = new JComboBox(comboBoxModelPower);
		comboBoxPower.setBounds(302, 0, 121, 27);
		contentPanel.add(comboBoxPower);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		final JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			
				String newName = textFieldName.getText();
				
				if(newName.isEmpty()) {
					JOptionPane.showMessageDialog(getContentPane(),"No name entered. Cannot create AtomicParameterExpression","Error",JOptionPane.ERROR_MESSAGE);
				} else {
				
					
					// check the name is not already used
					boolean nameUsed=false;
					
					if(mapExps.containsKey(newName)) {
						JOptionPane.showMessageDialog(getContentPane(),"There is already an expression with the name: "+newName,"Error",JOptionPane.ERROR_MESSAGE);
						nameUsed=true;
					}
					
					if(nameUsed==false) {
						
						
						// get the parameter selected
						String newParameter = (String) comboBoxParameter.getSelectedItem();
						 
						// get the power selected
						String power = (String) comboBoxPower.getSelectedItem();
						Expression newPowerExpression = mapExps.get(power);
						
						// get the operation type
						OperationType newOperationType = (OperationType) comboBoxOperation.getSelectedItem();
						
						// get the operand
						String operand = (String) comboBoxOperand.getSelectedItem();
						Expression newOperandExpression = mapExps.get(operand);
						
						// create the operation
						Operation newOperation;
						if(operand.equals("")) {
							newOperation=null;
						} else {
							newOperation = new Operation(newOperandExpression,newOperationType);
						}
						
						System.out.println("DEBUG AtomicParameterExpressionDialog.okButton: Creating new AtomicParameterExpression:");
						System.out.println("DEBUG AtomicParameterExpressionDialog.okButton: parameter="+newParameter);
						System.out.println("DEBUG AtomicParameterExpressionDialog.okButton: power="+newPowerExpression);
						System.out.println("DEBUG AtomicParameterExpressionDialog.okButton: operation="+newOperation);
							
						// create the new expression of type AtomicParameterExpression
						AtomicParameterExpression newExp = new AtomicParameterExpression()
															.withParameterRef(new ParameterReference(newParameter))
															.withPower(newPowerExpression)
															.withOperation(newOperation);
															
															
						
						
						
						// add the expression to the model
						System.out.println("DEBUG AtomicParameterExpressionDialog.okButton: Adding new expression to mapExps");
						mapExps.put(newName,newExp); 
						
						// signals the model that we have updated the hash
						comboBoxModelExps.actionPerformed(new ActionEvent(okButton,0,"update"));
				    	 
						// select the new expression in the combo box
						comboBoxExps.setSelectedItem(newName); 
						
						
						dispose(); // close the window
					} else {
						// giving up
					}
				}
			}
		});
		
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); // close the window
			}
		});
		
	
	}
}
