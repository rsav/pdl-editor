package net.ivoa.pdl.editor.guiComponent;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;

import net.ivoa.pdl.editor.objectModel.PDLGroup;

public class GroupsTreeModelListener implements TreeModelListener {
	
	DefaultComboBoxModel comboBoxModelServiceInputs;
	DefaultComboBoxModel comboBoxModelServiceOutputs;
	
	
	public GroupsTreeModelListener(DefaultComboBoxModel mi, DefaultComboBoxModel mo) {
		
		comboBoxModelServiceInputs = mi;
		comboBoxModelServiceOutputs = mo;
		
		System.out.println("DEBUG GroupsTreeModelListener.ctor comboBoxModelServiceInputs="+comboBoxModelServiceInputs);
		System.out.println("DEBUG GroupsTreeModelListener.ctor comboBoxModelServiceOutputs="+comboBoxModelServiceOutputs);

		
		
		
	}
	
	
	
	
	public void treeNodesChanged(TreeModelEvent e) {
		
		System.out.println("DEBUG GroupsTreeModelListener.treeNodesChanged "+e.toString());
		
		// get the parent node of the node changed
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) e.getTreePath().getLastPathComponent();

		// get the nodes changed in the the tree
		Object[] chgNodes = e.getChildren();
		
		for(int nc=0;nc<chgNodes.length;nc++) { // for all changed nodes
			
			// get the changed node
			DefaultMutableTreeNode chgNode = (DefaultMutableTreeNode) chgNodes[nc];
			
			// get the PDL group and its name
			PDLGroup chgGroup = (PDLGroup) chgNode.getUserObject();
            String chgGroupName = chgGroup.getName();
		
            System.out.println("DEBUG GroupsTreeModelListener.treeNodesChanged: the node "+chgGroupName+" was changed");

            // we don't know the name before the change !
            
            
		
		}
		
	}

	public void treeNodesInserted(TreeModelEvent e) {
		System.out.println("DEBUG GroupsTreeModelListener.treeNodesInserted "+e.toString());
		
		// get the parent node of the node inserted
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) e.getTreePath().getLastPathComponent();
		
		// get the nodes inserted to the tree
		Object[] insNodes = e.getChildren();
		
		for(int ni=0;ni<insNodes.length;ni++) { // for all inserted nodes
			
			// get the inserted node
			DefaultMutableTreeNode insNode = (DefaultMutableTreeNode) insNodes[ni]; 
			
			// get the PDL Group and its name
			PDLGroup insGroup = (PDLGroup) insNode.getUserObject();
            String insGroupName = insGroup.getName();
			
            System.out.println("DEBUG GroupsTreeModelListener.treeNodesInserted: the node "+insGroupName+" was inserted");
			
            // if the parent group is ROOT add the group name to the dependant comboBoxModels
            
            PDLGroup parentGroup = (PDLGroup) parentNode.getUserObject();
            String parentGroupName = parentGroup.getName();
            
            if(parentGroupName.equals("ROOT")) {
            	comboBoxModelServiceInputs.addElement(insGroupName);
            	comboBoxModelServiceOutputs.addElement(insGroupName);
            }
		}
		
		
		
		
		
		
	}

	
	public void treeNodesRemoved(TreeModelEvent e) {
		System.out.println("DEBUG GroupsTreeModelListener.treeNodesRemoved "+e.toString());
		// get the parent node of the node removed
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) e.getTreePath().getLastPathComponent();
		
        
		// get the nodes removed from the tree
        Object[] delNodes = e.getChildren();
        
        for(int nd=0;nd<delNodes.length;nd++) { // for all deleted nodes
        
        	// get the deleted node
            DefaultMutableTreeNode delNode = (DefaultMutableTreeNode) delNodes[nd]; 
			
			// get the deleted PDL Group, and its name
            PDLGroup delGroup = (PDLGroup) delNode.getUserObject();
            String delGroupName = delGroup.getName();
            
            System.out.println("DEBUG GroupsTreeModelListener.treeNodesRemoved: the node "+delGroupName+" was removed");
            
            // if the parent group is ROOT remove the group name from the dependant comboBoxModels
            		            
            PDLGroup parentGroup = (PDLGroup) parentNode.getUserObject();
            String parentGroupName = parentGroup.getName();
            
            if(parentGroupName.equals("ROOT")) {
            	comboBoxModelServiceInputs.removeElement(delGroupName);
            	comboBoxModelServiceOutputs.removeElement(delGroupName);
            }
        }
        
        
	}

	
	public void treeStructureChanged(TreeModelEvent e) {
		System.out.println("DEBUG GroupsTreeModelListener.treeStructureChanged "+e.toString());
		
	}
	
}
