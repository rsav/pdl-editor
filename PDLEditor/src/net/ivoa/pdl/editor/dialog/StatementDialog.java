package net.ivoa.pdl.editor.dialog;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import net.ivoa.pdl.editor.PDLEditorApp;
import net.ivoa.pdl.editor.guiComponent.MapListModel;
import net.ivoa.pdl.editor.objectModel.PDLCriterion;
import net.ivoa.pdl.editor.objectModel.PDLStatement;


public class StatementDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private MapListModel listModelStats;
	private DefaultTreeModel treeModelGroups;
	private JComboBox comboBoxType;
	private JComboBox comboBoxGroups;
	private DefaultComboBoxModel comboBoxModelGroups;
	private TreeMap<String, PDLCriterion> mapCrits;
	private DefaultListModel listModelCrits1;
	private JList listCrits1;
	private JList listCrits2;
	private TreeMap<String, PDLStatement> mapStats;
	private DefaultListModel listModelCrits2;
	private JTextField textFieldComment;



	/**
	 * Create the dialog.
	 */
	public StatementDialog(TreeMap<String,PDLStatement> ms, MapListModel ls, TreeMap<String,PDLCriterion> mc, DefaultTreeModel tg) {
		
		mapStats = ms;
		listModelStats = ls;
		mapCrits = mc;
		treeModelGroups = tg;
		
		
		
		setBounds(100, 100, 603, 349);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblName = new JLabel("Name:");
			lblName.setBounds(6, 6, 61, 16);
			contentPanel.add(lblName);
		}
		{
			textFieldName = new JTextField();
			textFieldName.setBounds(81, 0, 134, 28);
			contentPanel.add(textFieldName);
			textFieldName.setColumns(10);
		}
		{
			JLabel lblType = new JLabel("Type:");
			lblType.setBounds(6, 34, 61, 16);
			contentPanel.add(lblType);
		}
		{
		    comboBoxType = new JComboBox();
			comboBoxType.setModel(new DefaultComboBoxModel(new String[] {"Always", "IfThen"}));
			comboBoxType.setBounds(81, 30, 134, 27);
			contentPanel.add(comboBoxType);
		}
		{
			JLabel lblCrit1 = new JLabel("Criterion 1:");
			lblCrit1.setBounds(6, 62, 72, 16);
			contentPanel.add(lblCrit1);
		}
		{
			JScrollPane scrollPaneCrit1 = new JScrollPane();
			scrollPaneCrit1.setBounds(81, 62, 198, 156);
			contentPanel.add(scrollPaneCrit1);
			
			listModelCrits1 = new DefaultListModel();
		    listCrits1 = new JList(listModelCrits1);
			listCrits1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			scrollPaneCrit1.setViewportView(listCrits1);
			
			// populate list with list of names of available criterions
			for(String crit: mapCrits.keySet()) {
				listModelCrits1.addElement(crit);
			}
			
		}
		{
			JLabel labelCrit2 = new JLabel("Criterion 2:");
			labelCrit2.setBounds(291, 62, 72, 16);
			contentPanel.add(labelCrit2);
		}
		{
			JScrollPane scrollPaneCrit2 = new JScrollPane();
			scrollPaneCrit2.setBounds(366, 62, 198, 156);
			contentPanel.add(scrollPaneCrit2);
			
			listModelCrits2 = new DefaultListModel();			
		    listCrits2 = new JList(listModelCrits2);
			listCrits2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			scrollPaneCrit2.setViewportView(listCrits2);

			// populate list with list of names of available criterions
			for(String crit: mapCrits.keySet()) {
				listModelCrits2.addElement(crit);
			}
					
		}
		
		JLabel lblGroup = new JLabel("Group:");
		lblGroup.setBounds(291, 6, 61, 16);
		contentPanel.add(lblGroup);
		
		comboBoxModelGroups = new DefaultComboBoxModel();
	    comboBoxGroups = new JComboBox(comboBoxModelGroups);
		comboBoxGroups.setBounds(366, 2, 198, 27);
		contentPanel.add(comboBoxGroups);
		
		JLabel lblComment = new JLabel("Comment:");
		lblComment.setBounds(6, 234, 72, 16);
		contentPanel.add(lblComment);
		
		textFieldComment = new JTextField();
		textFieldComment.setBounds(81, 228, 483, 28);
		contentPanel.add(textFieldComment);
		textFieldComment.setColumns(10);
		
		// populate list of groups with all the avail groups
		ArrayList<String> listGroups = new ArrayList<String>();
		
		// debug
		PDLEditorApp.printGroupsTree((DefaultMutableTreeNode) treeModelGroups.getRoot());
		
		// get the list of all groups
		listGroups = PDLEditorApp.getAllGroupsNames((DefaultMutableTreeNode) treeModelGroups.getRoot(), listGroups);

		// empty the list of groups
		comboBoxModelGroups.removeAllElements();
		
		// add all the groups except the ROOT group
		for(int g=0;g<listGroups.size();g++) {
			String groupName = listGroups.get(g);
			if(!groupName.equals("ROOT")) { // do not include the "ROOT" group
				System.out.println("DEBUG StatementDialog.ctor: adding group "+groupName);
				comboBoxModelGroups.addElement(groupName);
			}
		}
		
		
		
		
		
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				final JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
				
				okButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {

						String newName = textFieldName.getText();
						if(newName.isEmpty()) {
							
							JOptionPane.showMessageDialog(getContentPane(),"No name entered for the statement. Cannot create statement","Error",JOptionPane.ERROR_MESSAGE);
							
						} else {
						
							// check the name is not used by other statements
							boolean nameUsed=false;
							
							for(String statName: mapStats.keySet()) {
							
								if(statName.equals(newName)) {
									
									JOptionPane.showMessageDialog(getContentPane(),"There is already a statement named "+newName,"Error",JOptionPane.ERROR_MESSAGE);
									nameUsed=true;
									break;
								} // if
								
								
								
							} // for
							
							if(nameUsed==false) {
								
								System.out.println("DEBUG StatementDiagog.ctor: checking new statement name="+newName);
								
								// get the type of the statement
								String newType = (String) comboBoxType.getSelectedItem();
								
								
								// get the selected criterion 1
								String selCrit1 =  (String) listCrits1.getSelectedValue();
								
								
								// get the selected criterion 2
								String selCrit2 =  (String) listCrits2.getSelectedValue();
								
								// get the group for the statement
								String selGroup = (String) comboBoxGroups.getSelectedItem();
								if(selGroup==null) {
									
									JOptionPane.showMessageDialog(getContentPane(),"You cannot create a statement with no group selected","Error",JOptionPane.ERROR_MESSAGE);
									
								} else {
								
									System.out.println("DEBUG StatementDiagog.ctor: selCrit1="+selCrit1+" selCrit2="+selCrit2);
									
									
									// implement the rules depending on the type of the new statement
									boolean rulesOK=false;
								
									
									
									if(newType=="Always") { // the Always statement requires one selection for criterion 1
										if(selCrit1==null) {
											JOptionPane.showMessageDialog(getContentPane(),"Type "+newType+" requires at least one selection for criterion 1","Error",JOptionPane.ERROR_MESSAGE);
										} else {
											rulesOK=true;
										}
									}
									
									
									if(newType=="IfThen") { // the IfThen statement requires one selection for criterion 1 and one selection for criterion 2
										if(selCrit1==null || selCrit2==null) {
											JOptionPane.showMessageDialog(getContentPane(),"Type "+newType+" requires at least one selection for criterion 1 and one selection for criterion 2","Error",JOptionPane.ERROR_MESSAGE);
										} else {
											rulesOK=true;
										}
									}
										
									
									if(rulesOK) {
	
										// create the new statement
										System.out.println("DEBUG StatementDiagog.ctor: creating new statement name="+newName);
	
										// get the comment
										String newComment = textFieldComment.getText();
										
										PDLStatement newStat = new PDLStatement();
										newStat.setType(newType);
										newStat.setCrit1(selCrit1);
										newStat.setCrit2(selCrit2);
										newStat.setGroup(selGroup);
										newStat.setComment(newComment);
										
										mapStats.put(newName, newStat);
	
										// signals the model that we have updated the map
										listModelStats.actionPerformed(new ActionEvent(okButton,0,"update"));
										
										dispose(); // close the window
									}
								} // else
							}
						}
					}
				
				});
				
				
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
