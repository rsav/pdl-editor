package net.ivoa.pdl.editor.guiComponent;

import java.util.*;
import java.awt.event.*;


/**
 * from http://jcs.mobile-utopia.com/jcs/s/ListComboBoxModel
 * @author user
 *
 */
public class MapComboBoxModel extends ListComboBoxModel {

    protected Map map_data;
    protected List index;
    public MapComboBoxModel() {
        this.map_data = new HashMap();
        index = new ArrayList();
        // should we do a buildIndex here ??? TODO RS 2013-02-26 
    }
    public MapComboBoxModel(Map map) {
        this.map_data = map;
        buildIndex();
        if(index.size() > 0) {
            selected = index.get(0);
        }
    }
    protected void buildIndex() {
        index = new ArrayList(map_data.keySet());
    }


    public Object getElementAt(int i) {
    	//System.out.println("DEBUG MapComboBoxModel.getElementAt i="+i);
    	return index.get(i);
    	
    	/*
    	
    	if(i<map_data.size()) {
    		System.out.println("DEBUG MapComboBoxModel.getElementAt returning element");
    		return index.get(i);
    	} else {
    		System.out.println("DEBUG MapComboBoxModel.getElementAt returning null");
    		return null;
    	}
    	*/
    	
    }
    public int getSize() {
        return map_data.size();
    }
    
    
    public void actionPerformed(ActionEvent evt) {
        if(evt.getActionCommand().equals("update")) {
            buildIndex();
            fireUpdate();
        }
    }

    public Object getValue(Object selectedItem) {
        return map_data.get(selectedItem);
    }
    public Object getValue(int selectedItem) {
        return getValue(index.get(selectedItem));
    }
}

	
