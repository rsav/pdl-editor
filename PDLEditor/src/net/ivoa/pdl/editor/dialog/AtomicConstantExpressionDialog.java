package net.ivoa.pdl.editor.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import net.ivoa.parameter.model.AtomicConstantExpression;
import net.ivoa.parameter.model.Expression;
import net.ivoa.parameter.model.Operation;
import net.ivoa.parameter.model.OperationType;
import net.ivoa.parameter.model.ParameterType;
import net.ivoa.pdl.editor.guiComponent.MapComboBoxModel;

public class AtomicConstantExpressionDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private JTextField textFieldName;
	private JTextField textFieldConstant;

	// application objects for the available expressions
	private TreeMap<String, Expression> mapExps;
	private MapComboBoxModel comboBoxModelExps;
	private JComboBox comboBoxExps;

	private MapComboBoxModel comboBoxModelPower;


	private MapComboBoxModel comboBoxModelOperand;

	private TreeMap<String, Expression> mapExpsForOperand;

	private TreeMap<String, Expression> mapExpsForPower;




	/**
	 * Create the dialog.
	 */
	public AtomicConstantExpressionDialog(TreeMap<String,Expression> mte, MapComboBoxModel mce, JComboBox ce) {
		
		mapExps = mte;
		comboBoxModelExps = mce;
		comboBoxExps = ce;
		
		
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
		
		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(6, 34, 61, 16);
		contentPanel.add(lblType);
		
		final JComboBox comboBoxType = new JComboBox(ParameterType.values());
		comboBoxType.setBounds(79, 30, 134, 27);
		contentPanel.add(comboBoxType);
		
		JLabel lblConstant = new JLabel("Constant:");
		lblConstant.setBounds(6, 62, 61, 16);
		contentPanel.add(lblConstant);
		
		textFieldConstant = new JTextField();
		textFieldConstant.setBounds(79, 56, 134, 28);
		contentPanel.add(textFieldConstant);
		textFieldConstant.setColumns(10);
		
		JLabel lblPower = new JLabel("Power:");
		lblPower.setBounds(225, 6, 61, 16);
		contentPanel.add(lblPower);
		
		JLabel lblOperation = new JLabel("Operation:");
		lblOperation.setBounds(225, 33, 81, 16);
		contentPanel.add(lblOperation);
		
		
	    final JComboBox comboBoxOperation = new JComboBox(OperationType.values());
		comboBoxOperation.setBounds(302, 29, 121, 27);
		
		contentPanel.add(comboBoxOperation);
		
		JLabel lblOperand = new JLabel("Operand");
		lblOperand.setBounds(225, 61, 61, 16);
		contentPanel.add(lblOperand);
		
		mapExpsForOperand = new TreeMap<String, Expression>(mapExps);
		mapExpsForOperand.put("",null); // since the operation is not mandatory, we provide a choice for no operand
		
		comboBoxModelOperand = new MapComboBoxModel(mapExpsForOperand);
		final JComboBox comboBoxOperand = new JComboBox(comboBoxModelOperand);
		comboBoxOperand.setBounds(302, 57, 121, 27);
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
		comboBoxPower.setBounds(302, 2, 121, 27);
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
					JOptionPane.showMessageDialog(getContentPane(),"No name entered. Cannot create AtomicConstantExpression","Error",JOptionPane.ERROR_MESSAGE);
				} else {
				
					
					// check the name is not already used
					boolean nameUsed=false;
					
					if(mapExps.containsKey(newName)) {
						JOptionPane.showMessageDialog(getContentPane(),"There is already an expression with the name: "+newName,"Error",JOptionPane.ERROR_MESSAGE);
						nameUsed=true;
					}
					
					if(nameUsed==false) {
						
						
						
						
						
						// get the type selected in the combo box
						ParameterType newType = (ParameterType) comboBoxType.getSelectedItem();
						
						
						
						
						
						// get the constant text
						String newConstant = textFieldConstant.getText();
						
						// parse the string representing a vector
						String delims = ";";
						String[] newConstants = newConstant.split(delims);
						
						// put the constants in a list
						List<String> newConstantList = Arrays.asList(newConstants); 
						
						// get the power
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
						
						System.out.println("DEBUG AtomicConstantExpressionDialog.okButton: Creating new AtomicConstantExpression:");
						System.out.println("DEBUG AtomicConstantExpressionDialog.okButton: type="+newType);
						System.out.println("DEBUG AtomicConstantExpressionDialog.okButton: constants="+newConstantList);
						System.out.println("DEBUG AtomicConstantExpressionDialog.okButton: power="+newPowerExpression);
						System.out.println("DEBUG AtomicConstantExpressionDialog.okButton: operation="+newOperation);
							
						
						// create the new expression of type AtomicConstantExpression
						AtomicConstantExpression newExp = new AtomicConstantExpression()
															.withConstantType(newType)
															.withConstant(newConstantList)
															.withPower(newPowerExpression)
															.withOperation(newOperation);
															
															
						
						
						
						// add the expression to the model
						System.out.println("DEBUG AtomicConstantExpressionDialog.okButton: Adding new expression to mapExps");
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
