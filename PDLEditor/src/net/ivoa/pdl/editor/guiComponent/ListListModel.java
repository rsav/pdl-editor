package net.ivoa.pdl.editor.guiComponent;



import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


/**
 * from MapListModel
 *
 */
public class ListListModel implements ListModel, ActionListener {

	ListListModel() {
	    this.listeners = new ArrayList();
	    data = new ArrayList();
	}
	
	protected List data;
	
	public ListListModel(List list) {
	    this();
	    this.data = list;
	    if(list.size() > 0) {
	        selected = list.get(0);
	    }
	}
	
	protected Object selected;
	public void setSelectedItem(Object item) {
	    this.selected = item;
	}
	
	public Object getSelectedItem() {
	    return this.selected;
	}
		
	public Object getElementAt(int index) {
	    return data.get(index);
	}
	public int getSize() {
	    return data.size();
	}
	
	protected List listeners;
	public void addListDataListener(ListDataListener l) {
	    listeners.add(l);
	}
	
	public void removeListDataListener(ListDataListener l) {
	    this.listeners.remove(l);
	}
	
	public void actionPerformed(ActionEvent evt) {
	    if(evt.getActionCommand().equals("update")) {
	        this.fireUpdate();
	    }
	}
	
	public void fireUpdate() {
	    ListDataEvent le = new ListDataEvent(this, 
	        ListDataEvent.CONTENTS_CHANGED,
	        0,
	        data.size());
	    for(int i=0; i<listeners.size(); i++) {
	        ListDataListener l = (ListDataListener)listeners.get(i);
	        l.contentsChanged(le);
	    }
	}

}