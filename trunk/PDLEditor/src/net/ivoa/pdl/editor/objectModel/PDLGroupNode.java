package net.ivoa.pdl.editor.objectModel;

public class PDLGroupNode {

	PDLGroup group;	 // the group
	PDLGroup parent; // its parent 
	int position; // its position among the children

	public PDLGroupNode(PDLGroup g) {
		group = g;
	}
	
	public void setParent(PDLGroup p) {
		parent = p;
	}
	
	public void setPosition(int p) {
		position = p;
	}

	public PDLGroup getGroup() {
		return group;
	}

	public PDLGroup getParent() {
		return parent;
	}
	
	public int getPosition() {
		return position;
	}
	
	
}
