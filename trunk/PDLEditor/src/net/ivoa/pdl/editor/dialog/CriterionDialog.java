package net.ivoa.pdl.editor.dialog;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;

import net.ivoa.parameter.model.Expression;
import net.ivoa.pdl.editor.guiComponent.MapComboBoxModel;
import net.ivoa.pdl.editor.guiComponent.MapListModel;
import net.ivoa.pdl.editor.objectModel.PDLCriterion;


public class CriterionDialog extends JDialog {
	
	private JTextField textFieldName;
	private MapListModel listModelCrits;
	private TreeMap<String, PDLCriterion> mapCrits;
	private TreeMap<String, Expression> mapExps;
	private JButton okButton;
	private JButton cancelButton;



	/**
	 * Create the dialog.
	 * @param me the map containing the expressions
	 * @param mc the map containing the criterions
	 * @param lmc the model for the JList displaying the criterions
	 */
	public CriterionDialog(TreeMap<String,Expression> me, TreeMap<String,PDLCriterion> mc, MapListModel lmc) {
		
		mapExps = me;
		mapCrits = mc;
		listModelCrits = lmc;
		
		
		
		final JComboBox comboBoxCond;
		
		setBounds(100, 100, 529, 483);
		getContentPane().setLayout(null);
	
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(17, 406, 497, 39);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane);
	
		okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
				
				
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dispose(); // close the window
			}
		});
	
		
		
		
		
		
		
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(17, 20, 61, 16);
		getContentPane().add(lblName);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(81, 14, 134, 28);
		getContentPane().add(textFieldName);
		textFieldName.setColumns(10);
		
		JLabel lblCondition = new JLabel("Condition:");
		lblCondition.setBounds(241, 20, 84, 16);
		getContentPane().add(lblCondition);
		
	    comboBoxCond = new JComboBox();
		comboBoxCond.setModel(new DefaultComboBoxModel(new String[] {"IsNull", "BelongToSet", "IsInteger", "IsRational", "IsReal", "ValueLargerThan", "ValueSmallerThan", "ValueInRange", "DifferentOf", "DefaultValue"}));
		comboBoxCond.setBounds(325, 16, 167, 27);
		getContentPane().add(comboBoxCond);
		
		JLabel lblUsingExpressions = new JLabel("Using Expressions");
		lblUsingExpressions.setBounds(283, 74, 144, 16);
		getContentPane().add(lblUsingExpressions);
		
		JScrollPane scrollPaneExps = new JScrollPane();
		scrollPaneExps.setBounds(283, 100, 209, 230);
		getContentPane().add(scrollPaneExps);
		
		
		DefaultListModel listModelExps = new DefaultListModel();
		
		final JList listExps = new JList(listModelExps);
		scrollPaneExps.setViewportView(listExps);
		
		// fill the listExp1 with the names of the available expressions
		for(String exp: mapExps.keySet()) {
			listModelExps.addElement(exp);
		}
		
		
		
		JLabel lblConnector = new JLabel("Connector:");
		lblConnector.setBounds(30, 360, 84, 16);
		getContentPane().add(lblConnector);
		
		final JComboBox comboBoxConnector = new JComboBox();
		comboBoxConnector.setModel(new DefaultComboBoxModel(new String[] {"", "AND", "OR"}));
		comboBoxConnector.setBounds(122, 356, 93, 27);
		getContentPane().add(comboBoxConnector);
		
		JLabel lblCriterion = new JLabel("Criterion");
		lblCriterion.setBounds(264, 360, 91, 16);
		getContentPane().add(lblCriterion);
		
		final JComboBox comboBoxCrits = new JComboBox();
		comboBoxCrits.setBounds(338, 356, 151, 27);
		getContentPane().add(comboBoxCrits);
		
		
		// populate comboBoxCrits with avail criterions
		for(String crit: mapCrits.keySet()) {
			comboBoxCrits.addItem(crit);
		}
		
		JLabel lblConcerningExpression = new JLabel("Concerning Expression: ");
		lblConcerningExpression.setBounds(30, 74, 167, 16);
		getContentPane().add(lblConcerningExpression);
		
		final JComboBox comboBoxCExp = new JComboBox();
		comboBoxCExp.setBounds(30, 98, 167, 27);
		getContentPane().add(comboBoxCExp);
		
		final JCheckBox chckbxReachedSup = new JCheckBox("ReachedSup");
		chckbxReachedSup.setBounds(386, 48, 128, 23);
		getContentPane().add(chckbxReachedSup);
		
		final JCheckBox chckbxReachedInf = new JCheckBox("ReachedInf");
		chckbxReachedInf.setBounds(283, 48, 128, 23);
		getContentPane().add(chckbxReachedInf);
		
		
		// populate combo box with names of available expressions
		for(String exp: mapExps.keySet()) {
			comboBoxCExp.addItem(exp);
		}
		

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String newName = textFieldName.getText();
				if(newName.isEmpty()) {
					
					JOptionPane.showMessageDialog(getContentPane(),"No name entered for the criterion. Cannot create criterion","Error",JOptionPane.ERROR_MESSAGE);
					
				} else {
				
					// check the name is not used by other criterions
					boolean nameUsed=false;
					for(String critName: mapCrits.keySet()) {
						if(critName.equals(newName)) {
							
							JOptionPane.showMessageDialog(getContentPane(),"There is already a criterion named "+newName,"Error",JOptionPane.ERROR_MESSAGE);
							nameUsed=true;
							break;
						} // if
						
						
						
					} // for
					
					if(nameUsed==false) {
						
						System.out.println("DEBUG CriterionDiagog.ctor: Checking new criterion name="+newName);
						
						
						
						// get the type of the criterion
						String newType =(String) comboBoxCond.getSelectedItem();
						
						// get if this criterion is reached
						boolean newReachedInf = chckbxReachedInf.isSelected();
						boolean newReachedSup = chckbxReachedSup.isSelected();
						
						// get the selected expressions in expression1 and add them to the crit
						
						Object selObj[] =  listExps.getSelectedValues();
						int nbObjs = selObj.length;
						
						// implement the rules for creating criterion
						boolean rulesOK=false;
						
						if(newType=="IsNull" || newType=="IsInteger" || newType=="IsReal" || newType=="IsRational") {
							if(nbObjs!=0) {
								
								JOptionPane.showMessageDialog(getContentPane(),"Type "+newType+" does not allow expressions to be selected","Error",JOptionPane.ERROR_MESSAGE);
							} else {
								rulesOK=true;
							}
						}
						
						
						if(newType == "BelongToSet") {
							if(nbObjs==0) {
								JOptionPane.showMessageDialog(getContentPane(),"Type "+newType+" requires at least one selection for expression","Error",JOptionPane.ERROR_MESSAGE);
							} else {
								rulesOK=true;
							}
						}
						
						if(newType=="ValueLargerThan" || newType=="ValueSmallerThan" || newType=="DefaultValue") {
							if(nbObjs!=1) {
								JOptionPane.showMessageDialog(getContentPane(),"Type "+newType+" requires exactly one selection for expression","Error",JOptionPane.ERROR_MESSAGE);
							} else {
								rulesOK=true;
							}
						}
						
						if(newType=="ValueInRange") {
							if(nbObjs!=2) {
								JOptionPane.showMessageDialog(getContentPane(),"Type "+newType+" requires exactly 2 selections for expression","Error",JOptionPane.ERROR_MESSAGE);
							} else {
								rulesOK=true;
							}
						}
						
						
						
						
						
						if(rulesOK) {

							System.out.println("DEBUG CriterionDiagog.ctor: Creating new criterion name="+newName);
							PDLCriterion newCrit = new PDLCriterion();
							
							// add the type of the criterion
							newCrit.setType(newType);
							
							// add is this criterion is reached (for ValueLargerThan and ValueSmallerThan)
							newCrit.setReachedInf(newReachedInf);
							newCrit.setReachedInf(newReachedSup);
							
							// add the name of the expression concerned by the criterion
							newCrit.setCExp((String)comboBoxCExp.getSelectedItem());
							
							// add the connector
							String newCon = (String) comboBoxConnector.getSelectedItem(); // "", AND or OR
							if(newCon.isEmpty()) {
								newCon=null;
							}
							newCrit.setConnector(newCon);  // null, AND or OR
							
							// add the selected connected criterion
							String newConCrit=(String) comboBoxCrits.getSelectedItem(); // "" or the name of an existing criterion
							
							System.out.println("DEBUG newConCrit="+newConCrit);
							
							
							if(newConCrit==null || newConCrit.isEmpty()) {
								newConCrit=null;
							}
							
							newCrit.setCriterion(newConCrit); // other criterion name
							
							
							
							for(int o=0;o<nbObjs;o++) {
								String selExp=(String)selObj[o];
								System.out.println("DEBUG CriterionDiagog.ctor: Adding selected value for expression: "+selExp);
								newCrit.addExp(selExp);
							}
							
							
							// put the new criterion in the map under the new name
							mapCrits.put(newName, newCrit);
							
													
							// signals the model that we have updated the map
							listModelCrits.actionPerformed(new ActionEvent(okButton,0,"update"));
					    	 

							
							
							dispose(); // close the window
						}
							
							
						
						
						
						
						
					}
					
					
					
				}
			}
		});
		
		
	}
}
