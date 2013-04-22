package net.ivoa.pdl.editor.objectModel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TreeMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import net.ivoa.parameter.model.Expression;


public class PDLService {

	String ID;
	String name;
	String description;
	String inputsGroup;
	String outputsGroup;
	
	TreeMap<String,PDLParameter> parameters;
	ArrayList<PDLGroupNode> groups;
	TreeMap<String, Expression> expressions;
	TreeMap<String, PDLCriterion> criterions;
	TreeMap<String, PDLStatement> statements;
	
	
	
	/**
	 * print the groups tree with the parameters used  
	 * @param root - the root node of the tree
	 */
	public void printGroupsTree(DefaultMutableTreeNode root) {
		
	    System.out.println(root+" "+((PDLGroup) root.getUserObject()).getParams());
	    @SuppressWarnings("rawtypes")
		Enumeration children = root.children();
	    if (children != null) {
	      while (children.hasMoreElements()) {
	    	  printGroupsTree((DefaultMutableTreeNode) children.nextElement());
	      }
	    }
	  }
	
	
	
	/**
	 * return the parent node of a tree node, all nodes must have different names
	 * @param root the root of the tree
	 * @param name the name to search
	 * @return node which has the PDLGroup with the name, null if not found
	 */
	private DefaultMutableTreeNode getParentByName(DefaultMutableTreeNode root, String name) {
		
		
		
		PDLGroup node = (PDLGroup) root.getUserObject();
		String nodeName = node.getName();
		
		System.out.println("DEBUG PDLService.getParentByName checking for name="+name+" in node with name="+nodeName);
		
		if(nodeName == name) {
			System.out.println("DEBUG PDLService.getParentByName found match, returning");
			return root;
		}
		
		@SuppressWarnings("rawtypes")
		Enumeration children = root.children();
	    if (children != null) {
	      while (children.hasMoreElements()) {
	    	  System.out.println("DEBUG PDLService.getParentByName examining next child");
	    	  DefaultMutableTreeNode n= getParentByName((DefaultMutableTreeNode) children.nextElement(), name);
	     
	    	  if(n!=null) {
	    		  return n;
	    	  }
	      
	      }
	    }
	    
	    System.out.println("DEBUG PDLService.getParentByName no match found, returning null");
		return null;
		
		
	}
	
	
	
	
	/**
	 * populate the arraylist groups with the tree containing the groups
	 * @param root - the root node of the tree
	 */
	public void setGroups(DefaultMutableTreeNode root) {
		
	    System.out.println(root+" "+((PDLGroup) root.getUserObject()).getParams());
	    
	    PDLGroupNode node = new PDLGroupNode((PDLGroup) root.getUserObject());
	    System.out.println("DEBUG PDLService.setGroups: adding node "+node);
	    
	    PDLGroup parent;
	    DefaultMutableTreeNode treeNodeParent = (DefaultMutableTreeNode) root.getParent();
	    if(treeNodeParent==null) {
	    	parent = null;
	    } else {
	    	parent = (PDLGroup)(treeNodeParent.getUserObject());
	    }
	    
	    System.out.println("DEBUG PDLService.setGroups: Found parent = "+parent);
	    
	    if(parent !=null) {
	    	node.setParent(parent);
	    }
	    
	    int position;
	    if(parent==null) {
	    	position = 0;
	    } else {
	    	position = root.getParent().getIndex(root);
	    }
	    
	    System.out.println("DEBUG PDLService.setGroups: Found position = "+position);
	    node.setPosition(position);
	    
	    
	    groups.add(node);
	    
	    @SuppressWarnings("rawtypes")
		Enumeration children = root.children();
	    if (children != null) {
	      while (children.hasMoreElements()) {
	    	  setGroups((DefaultMutableTreeNode) children.nextElement());
	      }
	    }
	  }
	
	
	
	public PDLService(String id) {
		ID = id;
		parameters = new TreeMap<String,PDLParameter>();
		groups = new ArrayList<PDLGroupNode>();
		expressions = new TreeMap<String,Expression>();
		criterions = new TreeMap<String,PDLCriterion>();
		statements= new TreeMap<String,PDLStatement>();

	
	}
	
	public void setName(String n) {
		name = n;
	}

	public void setDescription(String d) {
		description = d;
		
	}

	public void setParameters(TreeMap<String,PDLParameter> m) {

		parameters = m;
		
	}
	
	public void setGroups(DefaultTreeModel t) {
		
		setGroups((DefaultMutableTreeNode) t.getRoot());
		
	}

	public void setCriterions(TreeMap<String,PDLCriterion> c) {
		criterions = c;
		
	}

	public void setStatements(TreeMap<String,PDLStatement> s) {
		statements = s;
		
	}

	public void setExpressions(TreeMap<String,Expression> m) {
		expressions = m;
	}

	public String getName() {
		return name;
	}
	
	public String getID() {
		return ID;
	}
	
	public String getDescription() {
		return description;
	}

	public TreeMap<String,PDLParameter> getParameters() {
		return parameters;
	}

	/**
	 * get the groups and put them in a DefaultTreeModel
	 * @param t
	 * @return 
	 */
	public  DefaultTreeModel getGroups() {
	
		DefaultTreeModel t = null;  // temp value
		
		
		for(int nn=0;nn<groups.size();nn++) { // for all nodes
		
			System.out.println("DEBUG PDLService.getGroups: restoring group "+groups.get(nn));
			
			
			// create the new group
			PDLGroup newGroup = new PDLGroup(groups.get(nn).getGroup().getName());
			
			// add the params of the group
			ArrayList<String> params = (ArrayList<String>) groups.get(nn).getGroup().getParams();
			
			System.out.println("DEBUG PDLService.getGroups: Adding the "+params.size()+" group parameters");
			
			
			for(int np=0;np<params.size();np++) { // for all params
				System.out.println("DEBUG PDLService.getGroups: Adding param no "+np+" name="+params.get(np));
				newGroup.addPDLParam(params.get(np));
			}
			
			// find the parent group and the position
			PDLGroup newParentGroup = groups.get(nn).getParent();
			int newPosition = groups.get(nn).getPosition();
			
			System.out.println("DEBUG PPDLService.getGroups: Creating treenode for the group ="+newGroup+" parent="+newParentGroup+" position="+newPosition);
			
			DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(newGroup);
			
			
			if(newParentGroup==null) { // this is the root node
				
				System.out.println("DEBUG PPDLService.getGroups: New group name has no parent: this is the root: creating tree with it");
				 t = new DefaultTreeModel(newTreeNode);
				
			
			} else {
				System.out.println("DEBUG PPDLService.getGroups: New group name has a parent: finding parent with name="+newParentGroup.getName());
				
				// find the parent by its name, it is supposed to exist ie the tree is stored with the parents first !
				DefaultMutableTreeNode parentTreeNode = getParentByName((DefaultMutableTreeNode) t.getRoot(),newParentGroup.getName());
				
				if(parentTreeNode!=null) {
					System.out.println("DEBUG PPDLService.getGroups: Found parent with name="+newParentGroup.getName()+" Inserting tree node at position="+newPosition);
					t.insertNodeInto(newTreeNode, parentTreeNode, newPosition);
				} else {
					
					System.out.println("DEBUG PPDLService.getGroups: WARNING: Did not find parent with name="+newParentGroup.getName());
				}
				
			} 
			
			
			
			
		}
		return t;
		
				
	}
	

	
	

	public void setInputsGroup(String g) {
		inputsGroup = g;
	}
	
	public void setOutputsGroup(String g) {
		outputsGroup = g;
	}



	public String getInputsGroup() {
		return inputsGroup;
	}
	

	public String getOutputsGroup() {
		return outputsGroup;
	}



	public TreeMap<String,Expression> getExpressions() {
		return expressions;
	}



	public TreeMap<String,PDLCriterion> getCriterions() {
		return criterions;
	}



	public TreeMap<String,PDLStatement> getStatements() {
		return statements;
	}
	
	
	
	

}
