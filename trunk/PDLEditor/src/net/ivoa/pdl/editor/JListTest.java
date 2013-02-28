package net.ivoa.pdl.editor;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import net.ivoa.pdl.editor.guiComponent.ListListModel;
import net.ivoa.pdl.editor.guiComponent.MapListModel;

import java.util.List;
import java.util.*;


/**
 * @author user
 *
 */
public class JListTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Map Aware Lists");
        Container root = frame.getContentPane();
        root.setLayout(new BoxLayout(root,BoxLayout.X_AXIS));
        
        
        // Map List
        final TreeMap<String,String> map = new TreeMap<String,String>();
        map.put("Red",   "#ff0000");
        map.put("Green", "#00ff00");
        map.put("Blue",  "#0000ff");
        
        final MapListModel mod3 = new MapListModel(map);
        final JList list3 = new JList();

        list3.setModel(mod3);
        
        root.add(list3);
        
        final JButton bt3 = new JButton("Test Selection");
        bt3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Human color: " + list3.getSelectedValue());
                System.out.println("Computer color: " + mod3.getValue(list3.getSelectedIndex()));
            }
        });
        root.add(bt3);
        

        
        
        
        final JButton bt4 = new JButton("Remove item");
        bt4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Removing item Blue");
                map.remove("Blue");
                mod3.actionPerformed(new ActionEvent(bt4,0,"update"));
            }
        });
        root.add(bt4);
        
        
        
        // show the frame
        frame.pack();
        frame.setVisible(true);
    }
}
