package net.ivoa.pdl.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import net.ivoa.pdl.editor.guiComponent.ListComboBoxModel;
import net.ivoa.pdl.editor.guiComponent.MapComboBoxModel;

import java.util.List;
import java.util.*;


/**
 * from http://jcs.mobile-utopia.com/jcs/s/ListComboBoxModel
 * @author user
 *
 */
public class CBTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hack 4: Lists Aware Combo Box");
        Container root = frame.getContentPane();
        root.setLayout(new BoxLayout(root,BoxLayout.X_AXIS));
        
        // List combo box
        final List list = new ArrayList();
        list.add("Blinky");
        list.add("Pinky");
        list.add("Inky");
        
        final ListComboBoxModel mod2 = new ListComboBoxModel(list);
        JComboBox cb2 = new JComboBox();
        cb2.setModel(mod2);
        root.add(cb2);
        
        final JButton bt2 = new JButton("Add Item");
        bt2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                list.add("Clyde");
                mod2.actionPerformed(new ActionEvent(bt2,0,"update"));
                
            }
        });
        root.add(bt2);
        
        
        // Map Combo Box
        final Map map = new HashMap();
        map.put("Red",   "#ff0000");
        map.put("Green", "#00ff00");
        map.put("Blue",  "#0000ff");
        
        final MapComboBoxModel mod3 = new MapComboBoxModel(map);
        final JComboBox cb3 = new JComboBox();
        cb3.setModel(mod3);
        root.add(cb3);
        final JButton bt3 = new JButton("Test Selection");
        bt3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Human color: " + cb3.getSelectedItem());
                System.out.println("Computer color: " + mod3.getValue(cb3.getSelectedIndex()));
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
