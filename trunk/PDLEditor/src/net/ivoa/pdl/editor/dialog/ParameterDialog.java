package net.ivoa.pdl.editor.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
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
import net.ivoa.parameter.model.ParameterType;
import net.ivoa.pdl.editor.guiComponent.MapComboBoxModel;
import net.ivoa.pdl.editor.objectModel.PDLParameter;
import javax.swing.JRadioButton;

import visitors.GeneralParameterVisitor;

import CommonsObjects.GeneralParameter;
import javax.swing.JCheckBox;

public class ParameterDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JTextField textFieldUCD;
	private JTextField textFieldUType;
	private JTextField textFieldSkos;
	private JTextField textFieldUnit;
	
	private MapComboBoxModel comboBoxModelDimension;
	private JComboBox comboBoxDimension;
	
	private TreeMap<String, Expression> mapExps;
	
	private DefaultComboBoxModel comboBoxModelType;
	private JComboBox comboBoxType;
	private MapComboBoxModel comboBoxModelPrecision;
	private int dialogMode;
	private JComboBox comboBoxPrecision;
	private JComboBox comboBoxParams;
	private TreeMap<String, PDLParameter> mapParams;
	private MapComboBoxModel comboBoxModelParams;
	private TreeMap<String, Expression> mapExpsForPrecision;
	
	
	public final static int ParameterDialogModeCreate = 1;
	public final static int ParameterDialogModeModify = 2;
	private JTextField textFieldDimension;
	private JTextField textFieldPrecision;
	private MapComboBoxModel comboBoxModelExps;
	private JCheckBox checkboxRequired;
	



	/**
	 * Create the dialog.
	 * @param mode create or modify
	 * @param mp treemap containing the parameters defined
	 * @param mc combo box model for the parameters
	 * @param cp combo box for the parameters
	 * @param me treemap containing the expressions defined
	 */
	public ParameterDialog(int mode, TreeMap<String,PDLParameter> mp, 
			MapComboBoxModel mc, JComboBox cp, TreeMap<String,Expression> me,
			MapComboBoxModel mee ) {
		
		dialogMode = mode;
		mapParams = mp;
		comboBoxModelParams = mc;
		comboBoxParams = cp;
		mapExps = me;
		comboBoxModelExps = mee;
		
		
		
		setBounds(100, 100, 493, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblMode = new JLabel("Mode");
		lblMode.setBounds(6, 6, 253, 16);
		contentPanel.add(lblMode);
		
		// set text of label depending on dialogMode
		switch(dialogMode) {
		case ParameterDialogModeCreate : lblMode.setText("Creating new Parameter");
			break;
		case ParameterDialogModeModify: lblMode.setText("Modifying existing Parameter");
			break;
		
		}
		
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(6, 34, 61, 16);
		contentPanel.add(lblName);

		textFieldName = new JTextField();
		textFieldName.setBounds(66, 28, 134, 28);
		contentPanel.add(textFieldName);
		textFieldName.setColumns(10);
		

		
		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(6, 66, 61, 16);
		contentPanel.add(lblType);

		
		comboBoxModelType = new DefaultComboBoxModel(ParameterType.values());
	    comboBoxType = new JComboBox(comboBoxModelType);
		comboBoxType.setBounds(66, 62, 134, 27);
		contentPanel.add(comboBoxType);
		
	
		
		
		JLabel lblUcd = new JLabel("UCD:");
		lblUcd.setBounds(6, 94, 61, 16);
		contentPanel.add(lblUcd);
		
		JLabel lblUtype = new JLabel("UType:");
		lblUtype.setBounds(6, 122, 61, 16);
		contentPanel.add(lblUtype);
		
		textFieldUCD = new JTextField();
		textFieldUCD.setBounds(66, 88, 134, 28);
		contentPanel.add(textFieldUCD);
		textFieldUCD.setColumns(10);
		
		textFieldUType = new JTextField();
		textFieldUType.setBounds(66, 116, 134, 28);
		contentPanel.add(textFieldUType);
		textFieldUType.setColumns(10);
		
		JLabel lblSkos = new JLabel("SkosConcept:");
		lblSkos.setBounds(212, 34, 101, 16);
		contentPanel.add(lblSkos);
		
		textFieldSkos = new JTextField();
		textFieldSkos.setBounds(312, 28, 134, 28);
		contentPanel.add(textFieldSkos);
		textFieldSkos.setColumns(10);
		
		JLabel lblUnit = new JLabel("Unit:");
		lblUnit.setBounds(212, 66, 61, 16);
		contentPanel.add(lblUnit);
		
		textFieldUnit = new JTextField();
		textFieldUnit.setBounds(312, 60, 134, 28);
		contentPanel.add(textFieldUnit);
		textFieldUnit.setColumns(10);
		
		JLabel lblDimension = new JLabel("Dimension:");
		lblDimension.setBounds(212, 94, 83, 16);
		contentPanel.add(lblDimension);
		
		comboBoxModelDimension = new MapComboBoxModel(mapExps);
		comboBoxDimension = new JComboBox(comboBoxModelDimension);
		comboBoxDimension.setBounds(335, 90, 111, 27);
		contentPanel.add(comboBoxDimension);
		
		JLabel lblPrecision = new JLabel("Precision:");
		lblPrecision.setBounds(212, 156, 83, 16);
		contentPanel.add(lblPrecision);
		
		// since the precision is not mandatory allow for an emtpy string
		mapExpsForPrecision = new TreeMap<String,Expression>();
		mapExpsForPrecision.put("",null); // add the empty string
		mapExpsForPrecision.putAll(mapExps); // add the other values
		
		comboBoxModelPrecision = new MapComboBoxModel(mapExpsForPrecision);
		comboBoxPrecision = new JComboBox(comboBoxModelPrecision);
		comboBoxPrecision.setBounds(335, 152, 111, 27);
		contentPanel.add(comboBoxPrecision);
		
		JRadioButton rdbtnDimensionMenu = new JRadioButton("");
		rdbtnDimensionMenu.setBounds(305, 90, 28, 23);
		rdbtnDimensionMenu.setSelected(true);
		contentPanel.add(rdbtnDimensionMenu);
		
		final JRadioButton rdbtnDimensionField = new JRadioButton("");
		rdbtnDimensionField.setBounds(305, 116, 28, 23);
		contentPanel.add(rdbtnDimensionField);
		
		ButtonGroup btngroupDimension = new ButtonGroup();
		btngroupDimension.add(rdbtnDimensionMenu);
		btngroupDimension.add(rdbtnDimensionField);
		
		
		textFieldDimension = new JTextField();
		textFieldDimension.setBounds(335, 116, 111, 28);
		contentPanel.add(textFieldDimension);
		textFieldDimension.setColumns(10);
		
		final JRadioButton rdbtnPrecisionMenu = new JRadioButton("");
		rdbtnPrecisionMenu.setBounds(305, 152, 28, 23);
		rdbtnPrecisionMenu.setSelected(true);
		contentPanel.add(rdbtnPrecisionMenu);
		
		final JRadioButton rdbtnPrecisionField = new JRadioButton("");
		rdbtnPrecisionField.setBounds(305, 180, 28, 23);
		contentPanel.add(rdbtnPrecisionField);
		
		ButtonGroup btngroupPrecision = new ButtonGroup();
		btngroupPrecision.add(rdbtnPrecisionMenu);
		btngroupPrecision.add(rdbtnPrecisionField);
				
		
		
		
		textFieldPrecision = new JTextField();
		textFieldPrecision.setBounds(335, 180, 111, 28);
		contentPanel.add(textFieldPrecision);
		textFieldPrecision.setColumns(10);
		
		checkboxRequired = new JCheckBox("Required");
		checkboxRequired.setBounds(6, 152, 128, 23);
		contentPanel.add(checkboxRequired);
		
		// if dialog is for modifying an existing parameter, populate the fields with the attribute of that existing parameter
		if(dialogMode==ParameterDialogModeModify) {
			String selParamName = (String) comboBoxParams.getSelectedItem();
			textFieldName.setText(selParamName);
			PDLParameter selParam = mapParams.get(selParamName);
			
			comboBoxType.setSelectedItem(selParam.getType());
			textFieldUCD.setText(selParam.getUCD());
			textFieldUType.setText(selParam.getUType());
			textFieldSkos.setText(selParam.getSkos());
			textFieldUnit.setText(selParam.getUnit());
			
			comboBoxDimension.setSelectedItem(selParam.getDimension());
			comboBoxPrecision.setSelectedItem(selParam.getPrecision());
			
			checkboxRequired.setSelected(selParam.getRequired());
			
		}
		
		
		
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		final JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
				String oldName = null;
				
				if(dialogMode==ParameterDialogModeModify) {
					oldName = (String) comboBoxParams.getSelectedItem();
				}
				
				
				// get the name entered
				String newName = textFieldName.getText();
				
				if(newName.isEmpty()) {
					
					JOptionPane.showMessageDialog(getContentPane(),"No name entered for the parameter.\nCannot create/modify parameter","Error",JOptionPane.ERROR_MESSAGE);
					
				} else {
				

					// check the name is not already used in an existing parameter
					boolean nameUsed=false;
					
					
					switch(dialogMode) {
					
					case ParameterDialogModeCreate:
						for(String theName: mapParams.keySet()) {
							System.out.println("DEBUG ParameterDialog.ctor: checking collision with name="+theName);
							if(newName.equals(theName)) {
								JOptionPane.showMessageDialog(getContentPane(),"There is already a parameter with the name: "+newName+"\nCannot create parameter","Error",JOptionPane.ERROR_MESSAGE);
								nameUsed=true;
								break;
							}
						}
						break;
						
					case ParameterDialogModeModify:
						
						for(String theName: mapParams.keySet()) {
							System.out.println("DEBUG ParameterDialog.ctor: checking collision with name="+theName);
							if(newName.equals(theName) && !newName.equals(oldName)) {
								JOptionPane.showMessageDialog(getContentPane(),"There is already a parameter with the name: "+newName+"\nCannot modify parameter","Error",JOptionPane.ERROR_MESSAGE);
								nameUsed=true;
								break;
							}
						}
						break;
						
						
					}
					
					
					
					if(nameUsed==false) { // new parameter name not already used 
						
						// get the type selected in the combo box
						ParameterType newType = (ParameterType) comboBoxType.getSelectedItem();
						
						// get the UCD typed by user
						String newUCD = textFieldUCD.getText();
						
						// get the Utype typed by user
						String newUType = textFieldUType.getText();
						
						// get the skos
						String newSkos = textFieldSkos.getText();
						
						// get the new unit
						String newUnit = textFieldUnit.getText();
						
						// get the required field
						Boolean newRequired = checkboxRequired.isSelected();
						
						String newDimension=null;
							
					
						if(rdbtnDimensionField.isSelected()) { // if button for automatic creation of ACE for Dimension is selected, create the ACE
							
							
							// get the constant text for the new automatic ACE
							String newACEConstant = textFieldDimension.getText();
							
							// set the type of the ACE for dimension = an integer
							ParameterType newACEType = ParameterType.INTEGER; 
							
							
							// create a automatic ACE of type integer for Dimension
							newDimension = createAutomaticACE(newACEConstant, newACEType);
							
							
					    	
							
						} else {	// get the dimension expression
							newDimension = (String) comboBoxDimension.getSelectedItem();
						}
						
						if(newDimension==null) {
							JOptionPane.showMessageDialog(getContentPane(),"The dimension needs to be an expression and cannot be empty\nCannot create/modify parameter","Error",JOptionPane.ERROR_MESSAGE);
							
						} else {
						
							String newPrecision = null;
							
							if(rdbtnPrecisionField.isSelected()) { // if button for automatic creation of ACE for Precision is selected, create the ACE
								
								
								// get the constant text for the new automatic ACE
								String newACEConstant = textFieldPrecision.getText();
								
								
								// set the type of the ACE for precision = a real (ex: 0.01)
								ParameterType newACEType = ParameterType.REAL; 
								
								
								// create an automatic ACE of type integer for Precision - this method returns null if there is a type error
								newPrecision = createAutomaticACE(newACEConstant, newACEType);
								
								
							} else { 	// get the precision expression, NB: it can be null
								newPrecision = (String) comboBoxPrecision.getSelectedItem();
									
							}
							
							// if radio button for precision is selected we don't care if it's null
							// else, the entered precision must not be null
							if(rdbtnPrecisionMenu.isSelected() || newPrecision!=null) {
							
								
								
								PDLParameter newParam = null; // will be init just later
								
								switch(dialogMode) {
								
									case ParameterDialogModeCreate: // if creating a new param 
										System.out.println("DEBUG ParameterDialog.okButton: Creating new Parameter name="+newName);
										newParam = new PDLParameter();
										break;
										
									case ParameterDialogModeModify: // if modifying an existing param
										System.out.println("DEBUG ParameterDialog.okButton: Modifying parameter old name="+oldName);
										newParam = mapParams.get(oldName); // get the old param
										break;
								}
								
								// set the attribute of the new/existing param
								
								
								System.out.println("DEBUG ParameterDialog.okButton: setting type="+newType);
								System.out.println("DEBUG ParameterDialog.okButton: setting ucd="+newUCD);
								System.out.println("DEBUG ParameterDialog.okButton: setting utype="+newUType);
								System.out.println("DEBUG ParameterDialog.okButton: setting skos="+newSkos);
								System.out.println("DEBUG ParameterDialog.okButton: setting unit="+newUnit);
								System.out.println("DEBUG ParameterDialog.okButton: setting dimension="+newDimension);
								System.out.println("DEBUG ParameterDialog.okButton: setting precision="+newPrecision);
								System.out.println("DEBUG ParameterDialog.okButton: setting required="+newRequired);
								
								newParam.setType(newType);
								newParam.setUCD(newUCD);
								newParam.setUType(newUType);
								newParam.setSkos(newSkos);
								newParam.setUnit(newUnit);
								newParam.setDimension(newDimension);
								newParam.setPrecision(newPrecision);
								newParam.setRequired(newRequired);
								
								
								// remove the param from the map if modifying an existing param
								switch(dialogMode) {
								
									case ParameterDialogModeCreate: // if creating a new param 
									
										break;
										
									case ParameterDialogModeModify: // if modifying an existing param
										
										mapParams.remove(comboBoxParams.getSelectedItem()); // remove the param from the map
										break;
								}	
								
								
								// add the new or modified param to the map with its new/modified name
								mapParams.put(newName, newParam);
		
								// signals the model that we have updated the map
								comboBoxModelParams.actionPerformed(new ActionEvent(okButton,0,"update"));
						    	 
								// select the new expression in the combo box
								comboBoxParams.setSelectedItem(newName); 
								
								
								dispose(); // close the window (we do that only when the parameter creation has succeeded)

							} // if newPrecision is not null
							
						} // dimension is not null
						
					} // name not already used
					
					
				} // else (no name entered)	
				
				
								
			} // okButton.actionPerformed

			
			/**
			 * get the number of automatic ACEs created
			 */
			protected int getInstancesOfAutomaticACE() {
				int counter=1;
				for(String expName: mapExps.keySet()) {
					if(expName.contains("_ace")) {
						counter++;
					}
					
				}
				return counter;
			}
			
			
			/**
			 * create an automatic ACE of type Integer
			 * @param newACEConstant the integer constant for the ACE
			 * @return the name of the new ACE or null if cannot be created
			 */
			protected String createAutomaticACE(String newACEConstant, ParameterType newACEType) {
				

				// get the last instance of automatic ACE
				int counter = getInstancesOfAutomaticACE();
				
				// create a new ACE called using the constant provided
				String newACEName = "_ace"+counter; 
				
				
				// Check that the constant is of the right type by trying to create a new general parameter with it
				System.out.println("DEBUG ParameterDialog.createAutomaticACE: checking type of constant by creating a GeneralParameter");

				try{
					GeneralParameter param = new GeneralParameter(newACEConstant, newACEType, "", new GeneralParameterVisitor());
				}catch(Exception e1){
					JOptionPane.showMessageDialog(getContentPane(), "Type Error: " + e1.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
					return null;  // end the method
				}
				
				
				
				// parse the string representing a vector
				String delims = ";";
				String[] newACEConstants = newACEConstant.split(delims);
				
				// put the constants in a list
				List<String> newACEConstantList = Arrays.asList(newACEConstants); 
				
				System.out.println("DEBUG ParameterDialog.createAutomaticACE: Creating new ACE name="+newACEName);
				System.out.println("DEBUG ParameterDialog.createAutomaticACE: Creating new ACE type="+newACEType);
				System.out.println("DEBUG ParameterDialog.createAutomaticACE: Creating new ACE constant list="+newACEConstantList);
				
				// create the new expression of type AtomicConstantExpression
				AtomicConstantExpression newACE = new AtomicConstantExpression()
													.withConstantType(newACEType)
													.withConstant(newACEConstantList);
				
				System.out.println("DEBUG DEBUG ParameterDialog.okButton: Adding new expression to mapExps");
				mapExps.put(newACEName,newACE);
				
				// signals the model that we have updated the map
				comboBoxModelExps.actionPerformed(new ActionEvent(okButton,0,"update"));
				
				return newACEName;
				
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
