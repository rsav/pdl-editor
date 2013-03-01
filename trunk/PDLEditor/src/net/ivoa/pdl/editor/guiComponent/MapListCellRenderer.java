package net.ivoa.pdl.editor.guiComponent;


import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MapListCellRenderer extends JLabel implements ListCellRenderer {


    public Component getListCellRendererComponent(JList list,
                                                  Object key,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        //setText("MLCR:"+key.toString());

        MapListModel model = (MapListModel) list.getModel();
        
        Object value = model.getValue(key);
        
        setText(key.toString()+":"+value.toString());
       
        
        Color background;
        Color foreground;

        if (isSelected) {
            background = Color.WHITE;
            foreground = Color.RED;

        } else {
            background = Color.WHITE;
            foreground = Color.BLACK;
        }

        setBackground(background);
        setForeground(foreground);
        
        return this;
    }


}

