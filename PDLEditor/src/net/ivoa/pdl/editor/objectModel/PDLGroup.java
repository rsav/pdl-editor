package net.ivoa.pdl.editor.objectModel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;


public class PDLGroup {

	private String name; // name of PDL group
	
	private ArrayList<String> params; // list to hold the param names
	
	/**
	 * get the children of this group in the tree (recursive call)
	 * @param root of the tree
	 * @return a list of the children of the group or null if no children (to be initialized by caller)
	 */
	
	public ArrayList<PDLGroup> getChildren(DefaultMutableTreeNode root, ArrayList<PDLGroup> list){
		
		PDLGroup rootGroup = (PDLGroup) root.getUserObject();
		String rootGroupName = rootGroup.getName();
		if(this.name.equals(rootGroupName)) {
			@SuppressWarnings("rawtypes")
			Enumeration children = root.children();	
			while (children.hasMoreElements()) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
				list.add((PDLGroup) child.getUserObject());
			}
			return list;
		} else {
			
			@SuppressWarnings("rawtypes")
			Enumeration children = root.children();
			if (children != null) {
				while (children.hasMoreElements()) {
					ArrayList<PDLGroup> l = getChildren(
							(DefaultMutableTreeNode) children.nextElement(), list);

					if(l!=null) return l;
				}
			}
			
		}
		
		return null;
	}
	
	
	
	public PDLGroup(String n) {
		System.out.println("DEBUG PDLGroup.ctor: Creating new PDLGroup "+n);
		name = n;
		params = new ArrayList<String>(); // create the list 
		
	}
	
	public void addPDLParam(String p) {
		System.out.println("DEBUG PDLGroup.addPDLParam: Adding new parameter "+p);
		params.add(p);
	}
	
	public void removePDLParam(String p) {
		System.out.println("DEBUG PDLGroup.removePDLParam: Removing parameter "+p);
		params.remove(p);
	}
	
	public void removeAllPDLParams() {
		System.out.println("DEBUG PDLGroup.removeAllPDLParams: Removing all parameters ");
		params.clear();
		
	}
	
	
	public String toString() {
		return name;
	}
	
	
	public List<String> getParams() {
		return params;
	}
	
	// unit test
	public static void main(String[] args) {
		try {
			PDLGroup newGroup = new PDLGroup("TestGroup");
			newGroup.addPDLParam("TestParam");
			newGroup.removePDLParam("TestParam");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
		
	}

	public void setName(String n) {
		name = n;
		
	}
	
	

	

	
}
