package net.ivoa.pdl.editor.dialog;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.JSplitPane;

import net.ivoa.pdl.editor.PDLEditorApp;
import net.ivoa.pdl.editor.guiComponent.MapComboBoxModel;
import net.ivoa.pdl.editor.objectModel.PDLGroup;
import net.ivoa.pdl.editor.objectModel.PDLStatement;


public class GroupDialog extends JDialog {

	private DefaultMutableTreeNode selNode;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JTextField textFieldParent;
	private DefaultTreeModel treeModelGroups;
	private JTree treeGroups;
	private int dialogMode;
	
	private JComboBox comboBoxServiceInputs;
	private JComboBox comboBoxServiceOutputs;
	private DefaultComboBoxModel comboBoxModelServiceInputs;
	private DefaultComboBoxModel comboBoxModelServiceOutputs;
	private MapComboBoxModel comboBoxModelParams;


	private TreeMap<String, PDLStatement> mapStats;


	public final static int GroupDialogModeCreate = 1;
	public final static int GroupDialogModeModify = 2;
	

	
	


	/**
	 * check if a name is already used in a group (recursive call)
	 * @param treeModelGroups - root of the tree model containing the groups
	 * @param name - name to search
	 * @param exceptNode - if the name is used by this particular node, allow it
	 */
	public boolean isNameUsed(DefaultMutableTreeNode root, String name, DefaultMutableTreeNode exceptNode) {
		
		PDLGroup theGroup = (PDLGroup) root.getUserObject();
		String groupName = theGroup.getName();
		
		
		if(groupName.equals(name) && !root.equals(exceptNode)) {
			System.out.println("DEBUG GroupDialog.isNameUsed: found "+name+" already used and not in exceptNode");
			return true;
		}
		@SuppressWarnings("rawtypes")
		Enumeration children = root.children();
		if (children != null) {
		      while (children.hasMoreElements()) {
		    	  boolean value = isNameUsed((DefaultMutableTreeNode) children.nextElement(), name, exceptNode);
		    	  if(value==true) {
		    		  return true;
		    	  }
		      }
		    } // if
		return false;
	}

	
	
	/** 
	 * check if a parameter is already used in a group (recursive call)
	 * @param treeModelGroups - root of the tree model containing the groups
	 * @param param - parameter name to search
	 * @param exceptNode - if the parameter is used by this particular node, allow it 
	 */
	public boolean isParamUsed(DefaultMutableTreeNode root, String param, DefaultMutableTreeNode exceptNode) {
		
		PDLGroup theGroup = (PDLGroup) root.getUserObject();
		ArrayList<String> params = (ArrayList<String>) theGroup.getParams();
		
		for(int p=0;p<params.size();p++) {
			System.out.println("DEBUG GroupDialog.isParamUsed: checking if param "+param+" is used in groups");
			String theParam = params.get(p);
			if(theParam.equals(param) 
					&& !root.equals(exceptNode)) {
				System.out.println("DEBUG GroupDialog.isParamUsed: found "+param+" already used in node "+root.toString()+" which is not the exceptNode");
				return true; // param is used here, return true
			}			
		}
		
		@SuppressWarnings("rawtypes")
		Enumeration children = root.children();
		if (children != null) {
		      while (children.hasMoreElements()) {
		    	  boolean value = isParamUsed((DefaultMutableTreeNode) children.nextElement(), param, exceptNode);
		    	  if(value==true) {
		    		  return true;
		    	  }
		      }
		    } // if
		return false;
		
	}
	
	
	
	/**
	 * Create the dialog for creating or modifying a group
	 * @param mode dialog mode: create or modify a group
	 * @param c application comboBoxModel containing the parameters
	 * @param t application tree for displaying the groups
	 * @param m application tree model containing the groups 
	 * @param n node currently selected in the groups tree
	 * @param ci application comboBox for the service inputs, to sync 
	 * @param co application comboBox for the service inputs, to sync
	 * @param mi application comboBox model for the service inputs
	 * @param mo application comboBox model for the service outputs 
	 */
	public GroupDialog(int mode, MapComboBoxModel c, JTree t, DefaultTreeModel m, DefaultMutableTreeNode n, JComboBox ci, JComboBox co, DefaultComboBoxModel mi, DefaultComboBoxModel mo, TreeMap<String,PDLStatement> ms) {
		
		dialogMode = mode;
		comboBoxModelParams = c;
		treeGroups = t;
		treeModelGroups = m;
		selNode = n;
		comboBoxServiceInputs = ci;
		comboBoxServiceOutputs = co;
		comboBoxModelServiceInputs = mi;
		comboBoxModelServiceOutputs = mo;
		mapStats = ms;
		
		System.out.println("DEBUG GroupDialog.ctor treeModelGroups="+treeModelGroups);
		System.out.println("DEBUG GroupDialog.ctor mapStats="+mapStats);

		
		setBounds(100, 100, 325, 438);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(6, 37, 61, 16);
		contentPanel.add(lblName);
	
		textFieldName = new JTextField();
		textFieldName.setBounds(79, 31, 134, 28);
		contentPanel.add(textFieldName);
		textFieldName.setColumns(10);
	
		if(dialogMode==GroupDialogModeModify) {
			textFieldName.setText(selNode.toString());
		}
		
		
		
		
		JLabel lblParent = new JLabel("Parent:");
		lblParent.setBounds(6, 65, 61, 16);
		contentPanel.add(lblParent);
	
		textFieldParent = new JTextField();
		textFieldParent.setBounds(79, 59, 134, 28);
		textFieldParent.setEditable(false);
		
		switch(dialogMode) {
			case GroupDialogModeCreate: textFieldParent.setText(selNode.toString());
				break;
			case GroupDialogModeModify: textFieldParent.setText(selNode.getParent().toString());
				break;
		}
		
		
		contentPanel.add(textFieldParent);
		textFieldParent.setColumns(10);
	
		
		JLabel lblParameters = new JLabel("Parameters:");
		lblParameters.setBounds(6, 112, 82, 16);
		contentPanel.add(lblParameters);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(6, 140, 314, 136);
		contentPanel.add(splitPane);
		
		
		final DefaultListModel listModelAvailParams = new DefaultListModel();
		final JList listAvailParams = new JList(listModelAvailParams);
		splitPane.setLeftComponent(listAvailParams);
		
		// populate available params list which are not yet used in another group
		System.out.println("DEBUG GroupDialog.ctor: initing list of params not used");
		for(int i=0;i<comboBoxModelParams.getSize();i++) {
			String paramName = comboBoxModelParams.getElementAt(i).toString();
			
			if(!isParamUsed((DefaultMutableTreeNode) treeModelGroups.getRoot(),paramName,null)) {
			
				System.out.println("DEBUG GroupDialog.ctor: param "+paramName+" is not used.");
				
				listModelAvailParams.addElement(paramName);
			}
			
		}
		
		
		
		final DefaultListModel listModelParams = new DefaultListModel();
		final JList listParams = new JList(listModelParams);
		splitPane.setRightComponent(listParams);
		
		
		// if modifying an existing group, populate params list with that group's params
		if(dialogMode==GroupDialogModeModify) {
			
			ArrayList<String> params = (ArrayList<String>) ((PDLGroup) selNode.getUserObject()).getParams();
			
			for(int p=0; p<params.size();p++) {
				String paramName = params.get(p);
				listModelParams.addElement(paramName);				
			}
		}
		
		
		JButton btnAddParam = new JButton("Add Parameter");
		btnAddParam.setBounds(102, 288, 117, 29);
		contentPanel.add(btnAddParam);
		
		btnAddParam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selParam = (String) listAvailParams.getSelectedValue();
				listModelParams.addElement(selParam); // add param to list of params
				listModelAvailParams.removeElement(selParam); // remove param from list of avail params
				

			}
		});
		
		JButton btnRemoveParam = new JButton("Remove Parameter");
		btnRemoveParam.setBounds(102, 318, 117, 29);
		contentPanel.add(btnRemoveParam);
		
		
		
		JLabel lblMode = new JLabel("Mode");
		lblMode.setBounds(6, 9, 314, 16);
		contentPanel.add(lblMode);
		
		// set text of label depending on dialogMode
		switch(dialogMode) {
		case GroupDialogModeCreate : lblMode.setText("Creating new Group");
			break;
		case GroupDialogModeModify: lblMode.setText("Modifying existing Group");
			break;
		
		}
		
		// action for button "remove parameter"
		btnRemoveParam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selParam = (String) listParams.getSelectedValue();
				listModelParams.removeElement(selParam); // remove param from list of params
				listModelAvailParams.addElement(selParam); // add param to list of avail params 
				
			}
		});
		
		
		
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						
						if(textFieldName.getText().isEmpty()) {
							
							JOptionPane.showMessageDialog(getContentPane(),"No name entered for the group. Cannot create/modify group","Error",JOptionPane.ERROR_MESSAGE);
							
						} else {
						
							switch(dialogMode) {
							
							case GroupDialogModeCreate: 
									
									
									
								System.out.println("DEBUG GroupDialog.ctor: Creating new PDL group "+textFieldName.getText());
					
								
								String newName = textFieldName.getText();
								
								// check that the name is not already used in a group
								if(isNameUsed((DefaultMutableTreeNode) selNode.getRoot(),newName, null)) {
									
									JOptionPane.showMessageDialog(getContentPane(),"The name "+newName+" is already used in a group. Cannot create new group with that name","Error",JOptionPane.ERROR_MESSAGE);
								} else {

									PDLGroup newGroup = new PDLGroup(newName);
								
								
								
									// add new group parameters from listParams
									System.out.println("DEBUG GroupDialog.ctor: adding group parameters");
									for(int p=0;p<listModelParams.getSize();p++) {
										String selParam = (String) listModelParams.getElementAt(p);
										System.out.println("DEBUG GroupDialog.ctor: adding group parameter "+selParam);
										newGroup.addPDLParam(selParam);
									}
									
									System.out.println("DEBUG GroupDialog.ctor: Inserting group in tree with parent="+selNode.toString()+" at index="+selNode.getChildCount());
									
									// insert the new group into the tree, this will call treeModelGroups.nodeChanged which will call GroupsTreeModelListener.treeNodesChanged
									DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(newGroup);
									treeModelGroups.insertNodeInto(newChild, selNode, selNode.getChildCount());
															
									
									//treeModelGroups.nodeChanged(selNode); // no need here
									//treeModelGroups.nodeStructureChanged(selNode.getRoot()); // no need here
									
									//treeModelGroups.reload();
									
									//Make sure the user can see the new node.
								    treeGroups.scrollPathToVisible(new TreePath(newChild.getPath())); // not necessary
								    
								   
								    
								    
								    dispose(); // close the window
								}
								
							    break;
							    
							case GroupDialogModeModify:
								
								
								PDLGroup theGroup = (PDLGroup) selNode.getUserObject();
								
								String oldName = theGroup.getName();
								
								System.out.println("DEBUG GroupDialog.ctor: Modifying existing PDL group "+oldName);
								
								
								
								String theName = textFieldName.getText(); // new name
								
								// check that the name is not already used in a group
								if(isNameUsed((DefaultMutableTreeNode) selNode.getRoot(),theName, selNode)) {
									
									JOptionPane.showMessageDialog(getContentPane(),"The name "+theName+" is already used in a group. Cannot modify group with that name","Error",JOptionPane.ERROR_MESSAGE);
								} else {
								
									theGroup.setName(theName); // change the group name
									
									// reset the group parameters
									theGroup.removeAllPDLParams();
									// add new group parameters from listParams
									System.out.println("DEBUG GroupDialog.ctor: adding group parameters");
									for(int p=0;p<listModelParams.getSize();p++) {
										String selParam = (String) listModelParams.getElementAt(p);
										System.out.println("DEBUG GroupDialog.ctor: adding group parameter "+selParam);
										theGroup.addPDLParam(selParam);
									}
									
									// we have changed the representation of the node in the tree, we need to call this
									// this will display the node correctly and invoke GroupsTreeModelListener.treeNodesChanged
									treeModelGroups.nodeChanged(selNode);
									
									// NB: we can't do that in GroupsTreeModelListener.treeNodesChanged because there we don't know the old name
									if(!theName.equals(oldName)) {// if the name has changed
										
										
										
										// manage the change in the input/output combo boxes for the service
										// keep the current selection of comboBoxModelServiceInputs and comboBoxModelServiceOutputs
										String input = (String) comboBoxModelServiceInputs.getSelectedItem();
										if(input!=null && input.equals(oldName)) {
											input = theName; // new name
										}
										String output = (String) comboBoxModelServiceOutputs.getSelectedItem();
										if(output!=null && output.equals(oldName)) {
											output = theName; // new name
										}
										
										
										// reinit comboBoxModelServiceInputs and comboBoxModelServiceOutputs from treeModelGroups

										PDLEditorApp.initComboBoxService(comboBoxModelServiceInputs,treeModelGroups);
										PDLEditorApp.initComboBoxService(comboBoxModelServiceOutputs,treeModelGroups);
										
										// redo the selection after we have re-inited the combo box models
										comboBoxModelServiceInputs.setSelectedItem(input);
										comboBoxModelServiceOutputs.setSelectedItem(output);
										
										// rename the group in the statements which use that group
										// => remove the statement from the TreeMap and reinsert it with its new name
										
										for(String statName: mapStats.keySet()) {
										
											if(statName.equals(oldName)) {
												PDLStatement stat = mapStats.get(statName);
												mapStats.remove(statName);
												mapStats.put(theName, stat);
											}
										}
										
										
										
									} // if the name has changed
									
									
									// Make sure the user can see the new node.
								    treeGroups.scrollPathToVisible(new TreePath(selNode.getPath())); // not necessary

								    dispose(); // close the window
									}
								break;
								    
							} // switch
							
							
							
						}
							
					}
				});
				
				
				
				
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
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
	}
}
