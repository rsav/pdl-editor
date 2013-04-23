package net.ivoa.pdl.editor;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import net.ivoa.parameter.model.AtomicConstantExpression;
import net.ivoa.parameter.model.AtomicParameterExpression;
import net.ivoa.parameter.model.Expression;
import net.ivoa.parameter.model.ParameterReference;
import net.ivoa.parameter.model.ParameterType;
import net.ivoa.parameter.model.ParenthesisContent;
import net.ivoa.pdl.editor.dialog.AboutDialog;
import net.ivoa.pdl.editor.dialog.AtomicConstantExpressionDialog;
import net.ivoa.pdl.editor.dialog.AtomicParameterExpressionDialog;
import net.ivoa.pdl.editor.dialog.CriterionDialog;
import net.ivoa.pdl.editor.dialog.FunctionExpressionDialog;
import net.ivoa.pdl.editor.dialog.GroupDialog;
import net.ivoa.pdl.editor.dialog.ParameterDialog;
import net.ivoa.pdl.editor.dialog.ParenthesisContentExpressionDialog;
import net.ivoa.pdl.editor.dialog.StatementDialog;
import net.ivoa.pdl.editor.guiComponent.MapComboBoxModel;
import net.ivoa.pdl.editor.objectModel.PDLCriterion;
import net.ivoa.pdl.editor.objectModel.PDLGroup;
import net.ivoa.pdl.editor.objectModel.PDLParameter;
import net.ivoa.pdl.editor.objectModel.PDLService;
import net.ivoa.pdl.editor.objectModel.PDLStatement;
import net.ivoa.pdl.editor.wrapperToPDL.ParameterWrapper;
import net.ivoa.pdl.editor.guiComponent.GroupsTreeModelListener;
import net.ivoa.pdl.editor.guiComponent.MapListCellRenderer;
import net.ivoa.pdl.editor.guiComponent.MapListModel;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;

public class PDLEditorApp {

	static String appVersion = "0.5";
	static String appName = "PDL Editor";
	static String[] appAuthors={"Renaud Savalle (Paris Observatory)","Carlo Maria Zwolf (Paris Observatory)"};
	
	
	
	private JFrame appFrame;

	
	
	private JTextField textFieldParamName;
	private JTextField textFieldParamUCD;
	private JTextField textFieldParamUType;
	
	private JTextField textFieldParamSkoss;
	private JTextField textFieldParamUnit;
	
	private MapComboBoxModel comboBoxModelParams; // to store the params
	private JComboBox comboBoxParams; // to display the params

	private DefaultTreeModel treeModelGroups; // to store the groups
	private JTree treeGroups; // to display the groups
	private DefaultMutableTreeNode rootNode; 
	private JTextField textFieldExpName;

	private MapListModel listModelCrits; // to store the criterions
	private JList listCrits;  // to display the criterions
	
	private JTextField textFieldServiceID;
	private JTextField textFieldServiceName;
	private JTextArea textAreaServiceDesc;


	private DefaultComboBoxModel comboBoxModelServiceInputs; // to store the service input params
	private JComboBox comboBoxServiceInputs; // to display the service input params
	
	private DefaultComboBoxModel comboBoxModelServiceOutputs; // to store the service output params
	private JComboBox comboBoxServiceOutputs; // to display the service output params



	private MapListModel listModelStats; // to store the statements
	private JList listStats; // to display the statements


	private MapComboBoxModel comboBoxModelExps; // model to store the expressions
	private JComboBox comboBoxExps; // combo box to display the expressions

	private TreeMap<String,Expression> mapExps; // treemap to store the expressions (like a hashmap but keep the ordering)

	private TreeMap<String, PDLParameter> mapParams; // treemap to store the parameters

	private TreeMap<String, PDLStatement> mapStats; // treemap to store the statements

	private GroupsTreeModelListener treeModelGroupsListener;
	private JTextField textFieldExpType;
	private JTextField textFieldExpClass;
	private JButton btnLoadDescription;

	private JTextField textFieldParamPrecision;
	private JTextField textFieldParamDimension;
	private JTextField textFieldParamType;
	private TreeMap<String, PDLCriterion> mapCrits;



	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		// set the name of the application for Mac OS X menu and about box
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PDL Editor");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					// make the JMenuBars appear on top of the screen on MacOS X
					System.setProperty("apple.laf.useScreenMenuBar", "true");
					
					
					
					PDLEditorApp window = new PDLEditorApp();
					window.appFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * load all the information from a neodatis file
	 */
	protected void loadAllFromNeodatis() {
		
		ODB odb = null;
		
		try {
			
			
			JFileChooser chooser = new JFileChooser("/tmp");
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("PDL files", "pdl");// per Carlo, not neodatis
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(appFrame);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
			
				
				String fileName=chooser.getSelectedFile().getPath();
				
				
				System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: Opening file "+fileName);
				
				odb=ODBFactory.open(fileName);
				
				Objects<PDLService> objects = odb.getObjects(PDLService.class);
				
				System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: found "+objects.size() + " PDLService(s)");
				
				if(objects.hasNext()) {  // if there is an object of that class, use it to populate the application
					
					PDLService svc = objects.next();
					
					System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: restoring PDLService "+svc);
					
					textFieldServiceID.setText(svc.getID());
					textFieldServiceName.setText(svc.getName());
					textAreaServiceDesc.setText(svc.getDescription());
				
					System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: - restoring parameters ");
					TreeMap<String,PDLParameter> params = svc.getParameters();
					mapParams.clear();
					mapParams.putAll(params);
					
					System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: - restoring parameters: signaling comboBoxModelParams");
					comboBoxModelParams.actionPerformed(new ActionEvent(btnLoadDescription,0,"update"));
			    	 
			    	 // do the selection in the combo box of expressions
			    	 if(mapParams.size()==0) {
			    		 comboBoxParams.setSelectedIndex(-1); // do not select any
			    	 } else {
			    		 comboBoxParams.setSelectedIndex(0); // select the first one
			    	 }
			    	 
					
					System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: - restoring groups ");
					treeModelGroups = svc.getGroups(); // transform the groups in neodatis file to a treemodel and affect result to the field variable
					treeGroups.setModel(treeModelGroups); // replace the tree model of the JTree with the new one 
					
					// add the listener to the treeModelGroups to handle automatic changes in comboBoxModelServiceInputs and comboBoxModelServiceOutputs
					treeModelGroupsListener = new GroupsTreeModelListener(comboBoxModelServiceInputs,comboBoxModelServiceOutputs);
					treeModelGroups.addTreeModelListener(treeModelGroupsListener);
					
					// print the tree
					printGroupsTree((DefaultMutableTreeNode) treeModelGroups.getRoot());
					
					// populate comboBoxModelServiceInputs and comboBoxModelServiceOutputs
					initComboBoxService(comboBoxModelServiceInputs,treeModelGroups);
					initComboBoxService(comboBoxModelServiceOutputs,treeModelGroups);
					
					
					// perform selection in comboBoxModelServiceInputs and comboBoxModelServiceOutputs
					System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: - restoring inputs group and output groups");
					comboBoxModelServiceInputs.setSelectedItem(svc.getInputsGroup());
					comboBoxModelServiceOutputs.setSelectedItem(svc.getOutputsGroup());
					
					System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: - restoring expressions");
									
					
					TreeMap<String,Expression> exps = svc.getExpressions();
					mapExps.clear();
					mapExps.putAll(exps);
					
			
					
					System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: - restored expressions: signaling comboBoxModelExps");
					comboBoxModelExps.actionPerformed(new ActionEvent(btnLoadDescription,0,"update"));
			    	 
			    	 // do the selection in the combo box of expressions
			    	 if(mapExps.size()==0) {
			    		 comboBoxExps.setSelectedIndex(-1); // do not select any
			    	 } else {
			    		 comboBoxExps.setSelectedIndex(0); // select the first one
			    	 }
			    	 
					
					System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: - restoring criterions");
					
					
					
					TreeMap<String, PDLCriterion> criterions = svc.getCriterions();
					mapCrits.clear();
					mapCrits.putAll(criterions);
					
					System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: - restored criterions: signaling listModelCrits");
					listModelCrits.actionPerformed(new ActionEvent(btnLoadDescription,0,"update"));					
					
					
					System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: - restoring statements");
					
					TreeMap<String,PDLStatement> stats = svc.getStatements();
					mapStats.clear();
					mapStats.putAll(stats);
						
					
					System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: restoration done.");
				}
				
		    }
			
		} catch(Exception e) {
			
			System.err.println("ERROR during load: Caught exception "+e.getMessage());
			e.printStackTrace();
			
		} finally {
		
			if(odb!=null) {
				odb.close();
			}
		}
		
	}
	
	/**
	 * save all the information into a neodatis file
	 */
	protected void saveAllToNeodatis() {
		
		
		ODB odb = null;
		
		try {			
			
			
			
			JFileChooser chooser = new JFileChooser("/tmp");
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("PDL files", "pdl"); // per Carlo, not neodatis
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showSaveDialog(appFrame);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
			
				
				String fileName=chooser.getSelectedFile().getPath();
				
				
				System.out.println("DEBUG PDLEditorApp.loadAllFromNeodatis: Opening file "+fileName);
			
				
			
				
				File file = new File(fileName);
				if(file.exists()) {
					if(!file.delete()) {
						System.err.println("ERROR deleting file "+fileName);
					} else {
						System.out.println("DEBUG PDLEditorApp.saveAllToNeodatis: file "+fileName+" deleted OK");
					}
				
				}
				
				odb = ODBFactory.open(fileName);
				
				
				PDLService svc = new PDLService(textFieldServiceID.getText());
				svc.setName(textFieldServiceName.getText());
				svc.setDescription(textAreaServiceDesc.getText());
				svc.setInputsGroup((String)comboBoxModelServiceInputs.getSelectedItem());
				svc.setOutputsGroup((String)comboBoxModelServiceOutputs.getSelectedItem());
	
				svc.setParameters(mapParams);
				svc.setGroups(treeModelGroups);
				svc.setExpressions(mapExps);
				svc.setCriterions(mapCrits);
				svc.setStatements(mapStats);
				
				
				odb.store(svc);
	
				
		    }	
				
			
		} catch(Exception e) {
			
			System.err.println("ERROR during save: Caught exception "+e.getMessage());
			e.printStackTrace();
			
		} finally {
		
			if(odb!=null) {
				odb.close();
			}
		}
		
		
	}
	
	


	/**
	 * print the groups tree with the parameters used  
	 * @param root - the root node of the tree
	 */
	public static void printGroupsTree(DefaultMutableTreeNode root) {
		
	    System.out.println("DEBUG PDLEditor.printGroupsTree: "+((PDLGroup) root.getUserObject()).getName()+" "+((PDLGroup) root.getUserObject()).getParams());
	    @SuppressWarnings("rawtypes")
		Enumeration children = root.children();
	    if (children != null) {
	      while (children.hasMoreElements()) {
	    	  printGroupsTree((DefaultMutableTreeNode) children.nextElement());
	      }
	    }
	    
	  }
	
	
	/**
	 * get the params where an expression is used
	 * @param mp treemap containing the parameters
	 * @param en name of the expression to search for
	 * @return
	 */

	private ArrayList<String> getParamsWhereExpIsUsed(TreeMap<String, PDLParameter> mp, String en) {
		
		TreeMap<String, PDLParameter> mapParams = mp;
		String expName = en;
		
		System.out.println("DEBUG PDLEditorApp.getParamsWhereExpIsUsed: Searching for expName="+expName);
		
		ArrayList<String> paramsWhereExpIsUsed = new ArrayList<String>();
		
		for(String paramName: mapParams.keySet()) {
			System.out.println("DEBUG PDLEditorApp.getParamsWhereExpIsUsed: Exploring paramName="+paramName);
			
			PDLParameter param = mapParams.get(paramName);
			
			// get the parameter attributes that can contain an expression
			String paramDimension = param.getDimension();
			String paramPrecision = param.getPrecision();
			
			if(expName.equals(paramDimension)) {
				System.out.println("DEBUG PDLEditorApp.getParamsWhereExpIsUsed: Found match in paramDimension");
				paramsWhereExpIsUsed.add(paramName);
			}
			
			if(expName.equals(paramPrecision)) {
				System.out.println("DEBUG PDLEditorApp.getParamsWhereExpIsUsed: Found match in paramPrecision");
				paramsWhereExpIsUsed.add(paramName);
			}
			
		}
		
		System.out.println("DEBUG PDLEditorApp.getParamsWhereExpIsUsed: returning paramsWhereExpIsUsed="+paramsWhereExpIsUsed);
		
		return paramsWhereExpIsUsed;
	}
	
	
	/**
	 * get the criterions where an expression is used
	 * @param mc DefaultListModel containing the criterions
	 * @param en name of the expression to search for
	 * @return
	 */
	public ArrayList<String> getCritsWhereExpIsUsed(TreeMap<String, PDLCriterion> mc, String en) {
	 
		
		TreeMap<String, PDLCriterion> mapCrits = mc;
		String theName = en;
		
		ArrayList<String> critNamesWhereExpUsed = new ArrayList<String>();
		
		
		//System.out.println("DEBUG PDLEditorApp.getCritsWhereExpIsUsed: critNamesWhereExpUsed="+critNamesWhereExpUsed);
		
		 for(String critName: mapCrits.keySet()) {
			 
			 System.out.println("DEBUG PDLEditorApp.getCritsWhereExpIsUsed: Exploring criterion="+critName);
			 
			 // get the PDL criterion
			 PDLCriterion crit = mapCrits.get(critName);
			 
			// get the "concerning" expression of the criterion
			 String cexpName = crit.getCExp(); 
			 System.out.println("DEBUG PDLEditorApp.getCritsWhereExpIsUsed: -> found cexpression name="+cexpName);
			
			 if(theName.equals(cexpName)) {
				 System.out.println("DEBUG PDLEditorApp.getCritsWhereExpIsUsed: match!");
				 critNamesWhereExpUsed.add(critName); 
			 }
			// get the other expressions used by the criterion
			 ArrayList<String> critExps = crit.getExps(); 
			 
			 for(int ne=0; ne<critExps.size();ne++) {
				 String expName=critExps.get(ne);
				 System.out.println("DEBUG PDLEditorApp.getCritsWhereExpIsUsed: -> found expression name="+expName);
				 if(theName.equals(expName)) {
					 System.out.println("DEBUG PDLEditorApp.getCritsWhereExpIsUsed: match!");
					// add the criterion name to the list to display
					 critNamesWhereExpUsed.add(critName);
					
				 }
			 }
		 }
	
		 System.out.println("DEBUG PDLEditorApp.getCritsWhereExpIsUsed: returning critNamesWhereExpUsed="+critNamesWhereExpUsed);
		 
		 return critNamesWhereExpUsed;
	}
	
	
	
	/**
	 * get the groups where a parameter is used (recursive call)
	 * @param root root of the tree to search
	 * @param param name of parameter to search
	 * @param list list of group where the param is used, to be initialized by caller
	 * @return list of groups where the param is used
	 */
	public ArrayList<String> getGroupsWhereParamIsUsed(DefaultMutableTreeNode root, String param, ArrayList<String> list) {
		
		
		
		System.out.println("DEBUG PDLEditorApp.getGroupsWhereParamIsUsed: "+root+" "+((PDLGroup) root.getUserObject()).getParams());
	    
		ArrayList<String> params = (ArrayList<String>) ((PDLGroup) root.getUserObject()).getParams();
		if(params.contains(param)) {
			System.out.println("DEBUG PDLEditorApp.getGroupsWhereParamIsUsed: Found param "+param+" in node "+root);
			list.add(root.toString());
		}
		
		@SuppressWarnings("rawtypes")
		Enumeration children = root.children();
	    if (children != null) {
	      while (children.hasMoreElements()) {
	    	  ArrayList<String> l = getGroupsWhereParamIsUsed((DefaultMutableTreeNode) children.nextElement(), param, list);
	    	  
	      }
	    }
		
		return list;
	}
	
	
	/**
	 * get all the groups in a tree (recursive call)
	 * @param root root of the tree to search
	 * @param list list of groups, to be initialized by caller
	 * @return list of groups in the tree
	 */
	public static ArrayList<String> getAllGroups(DefaultMutableTreeNode root, ArrayList<String> list) {
		
		
	    
		PDLGroup rootGroup = (PDLGroup) root.getUserObject();
		String rootGroupName=rootGroup.getName();
		list.add(rootGroupName);
		
		System.out.println("DEBUG PDLEditorApp.getAllGroups: "+rootGroupName+" "+list);
		
		@SuppressWarnings("rawtypes")
		Enumeration children = root.children();
	    if (children != null) {
	      while (children.hasMoreElements()) {
	    	  @SuppressWarnings("unused")
	    	  ArrayList<String> l = getAllGroups((DefaultMutableTreeNode) children.nextElement(), list);
	    	  
	      }
	    }
		
		return list;
	}
	
	
		
	/**
	 * get the groups which are direct children of the ROOT in a tree (=> children at 1st level only)
	 * @param root root of the tree to search
	 * @param list list of groups, to be initialized by caller
	 * @return list of groups of the root in the tree
	 */
	public static ArrayList<String> getRootGroups(DefaultMutableTreeNode root, ArrayList<String> list) {
		
		
	    
		PDLGroup rootGroup = (PDLGroup) root.getUserObject();
		String rootGroupName=rootGroup.getName();
				
		System.out.println("DEBUG PDLEditorApp.getRootGroups: returning the children of "+rootGroupName+" "+list);
		
		//list.add(rootGroupName); // do not add the ROOT group 

		
		@SuppressWarnings("rawtypes")
		Enumeration children = root.children();
	    if (children != null) {
	      while (children.hasMoreElements()) {
	    	  list.add(children.nextElement().toString());
	      }
	    }
		
		return list;
	}
	
	
	
	
	
	
	

	

	
	/**
	 * get the params and in which groups they are used
	 * @param m - the TreeMap where the params are stored
	 * @param t - the TreeModel where the groups are stored
	 * @return
	 */
	public HashMap<String,List<String>> getParamsAndUsageInGroups(TreeMap<String,PDLParameter> m, DefaultTreeModel t) {
		
		TreeMap<String,PDLParameter> mapParams = m;
		DefaultTreeModel treeModelGroups = t;
				
		HashMap<String,List<String>> hash = new HashMap<String, List<String>>();
		
		// populate the hash with the param names
		for(String name: mapParams.keySet()) {
			hash.put(name,null);
		}
		
		Set<String> set = hash.keySet(); // the keys of the hash
		
		
		Iterator<String> iterator = set.iterator();
		if(iterator!=null) {
			
			while(iterator.hasNext()) {
				String key = (String) iterator.next();
				
				System.out.println("DEBUG PDLEditorApp.getParamsAndUsageInGroups: searching for param "+key);
				
				ArrayList<String> list = new ArrayList<String>();
				list = getGroupsWhereParamIsUsed((DefaultMutableTreeNode) treeModelGroups.getRoot(), key, list);
		
				hash.put(key, list);
			
			}
		}
		
		
		
		
		return hash;
		
		
	}
	

	
	/**
	 * initialize one of the combox box use for input/output groups for the service
	 * @param c model for the combo box to init with the available groups
	 * @param t model for the tree containing the groups
	 */
	public static void initComboBoxService(DefaultComboBoxModel c, DefaultTreeModel t) {
	
		DefaultComboBoxModel comboBoxModelService = c;
		DefaultTreeModel treeModelGroups = t;
	
		System.out.println("DEBUG PDLEditorApp.initComboBoxService: initing combobox="+comboBoxModelService);
		
		
		// clear the list
		comboBoxModelService.removeAllElements();
		
		// add a blank choice in the comboBoxServiceInputs and Outputs
		// this allows the user to not make a selection (yet)
		comboBoxModelService.addElement("");
		
		
		// add the available groups to the combo box
		ArrayList<String> listGroups = new ArrayList<String>();
		listGroups = getRootGroups((DefaultMutableTreeNode) treeModelGroups.getRoot(), listGroups);
	
		
		for(int g=0;g<listGroups.size();g++) {
			String groupName = listGroups.get(g);		
			comboBoxModelService.addElement(groupName);
			
		}
	
	}
	
	
	/**
	 * Create the application.
	 */
	public PDLEditorApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		appFrame = new JFrame();
		appFrame.setBounds(100, 100, 994, 801);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		appFrame.getContentPane().setLayout(null);

		// ---------- Menu Bar ----------
		JMenuBar menuBar = new JMenuBar();
		appFrame.setJMenuBar(menuBar);
		
		
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		JMenuItem menuItemAbout = new JMenuItem("About");
		
		menuItemAbout.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        AboutDialog aboutDialog = new AboutDialog();
		        aboutDialog.setLocationRelativeTo(appFrame); // dialog must appear in the middle of the app frame
		        aboutDialog.setVisible(true);
		    }
		});
		
		
		menuFile.add(menuItemAbout);
	    
	    JMenuItem menuItemQuit = new JMenuItem("Quit");
	    menuItemQuit.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			       appFrame.dispose(); // close the frame
		    }
		});
	    
	    menuFile.add(menuItemQuit);
	    
		
	    
	    
		
		JSplitPane splitPaneParams = new JSplitPane();
		splitPaneParams.setBounds(6, 46, 475, 209);
		appFrame.getContentPane().add(splitPaneParams);
		
		JPanel panelParamsMenu = new JPanel();
		panelParamsMenu.setLayout(new GridLayout(0, 1, 0, 0));

		splitPaneParams.setLeftComponent(panelParamsMenu);

		
		System.out.println("DEBUG PDLEditorApp.initialize: creating mapParams");
		
		mapParams = new TreeMap<String,PDLParameter>();
	    
		
		// add a dummy param for testing
		System.out.println("DEBUG PDLEditorApp.initialize: creating dummy parameter testParam1");
		PDLParameter testParam1 = new PDLParameter();
		testParam1.setType(ParameterType.INTEGER);
		testParam1.setUCD("ucd1");
		testParam1.setUType("utype1");
		testParam1.setUnit("unit1");
		testParam1.setSkoss("skoss1");
		testParam1.setPrecision("_one");
		testParam1.setDimension("_one");
		mapParams.put("TP1", testParam1);
		
		// add a dummy param for testing
		System.out.println("DEBUG PDLEditorApp.initialize: creating dummy parameter testParam2");
		PDLParameter testParam2 = new PDLParameter();
		testParam2.setType(ParameterType.REAL);
		testParam2.setUCD("ucd2");
		testParam2.setUType("utype2");
		testParam2.setUnit("unit2");
		testParam2.setSkoss("skoss2");
		testParam2.setPrecision("_one");
		testParam2.setDimension("_one");
		mapParams.put("TP2", testParam2);
		
		comboBoxModelParams = new MapComboBoxModel(mapParams);
		comboBoxParams = new JComboBox(comboBoxModelParams);
		comboBoxParams.setSelectedIndex(-1); // no selected item at first
		
		
		// when parameter is selected, update its fields
		comboBoxParams.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cmd=e.getActionCommand();
				if(cmd.equals("comboBoxChanged")) {
					String selParamName=(String)comboBoxParams.getSelectedItem();
					
					System.out.println("PDLEditorApp.ctor: selParamName="+selParamName);
					
					if(selParamName!=null) {
						
						PDLParameter selParam = mapParams.get(selParamName);
						textFieldParamName.setText(selParamName);
						textFieldParamType.setText(selParam.getType().name());
						textFieldParamUCD.setText(selParam.getUCD());
						textFieldParamUType.setText(selParam.getUType());
						textFieldParamSkoss.setText(selParam.getSkoss());
						textFieldParamUnit.setText(selParam.getUnit());
						textFieldParamPrecision.setText(selParam.getPrecision());
						textFieldParamDimension.setText(selParam.getDimension());
						
						
					} else {
						textFieldParamName.setText("");
						textFieldParamType.setText("");
						textFieldParamUCD.setText("");
						textFieldParamUType.setText("");
						textFieldParamSkoss.setText("");
						textFieldParamUnit.setText("");
						textFieldParamPrecision.setText("");
						textFieldParamDimension.setText("");
						
					}
				}
				
			}
		});
		
		
		
		panelParamsMenu.add(comboBoxParams);
		
		Component verticalStrutParam = Box.createVerticalStrut(20);
		panelParamsMenu.add(verticalStrutParam);
		
		
		JButton btnNewParam = new JButton("New Parameter");
		btnNewParam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ParameterDialog  newParameterDialog = new ParameterDialog(ParameterDialog.ParameterDialogModeCreate
																			,mapParams
																			,comboBoxModelParams
																			,comboBoxParams
																			,mapExps);
				newParameterDialog.setLocationRelativeTo(appFrame); // dialog must appear in the middle of the app frame
				newParameterDialog.setVisible(true);
				
			}
			
		});
		
		panelParamsMenu.add(btnNewParam);
		
		JButton btnEditParameter = new JButton("Edit Parameter");
		panelParamsMenu.add(btnEditParameter);
		btnEditParameter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// check that a parameter is selected
				String selParamName = (String) comboBoxParams.getSelectedItem();
				if(selParamName==null) {
					JOptionPane.showMessageDialog(appFrame,"No parameter selected. Cannot edit a parameter","Error",JOptionPane.ERROR_MESSAGE);
				} else {
					
					// check if the parameter is not used in a group
					
					 
					 HashMap<String,List<String>> hash = getParamsAndUsageInGroups(mapParams,treeModelGroups);
					 System.out.println("DEBUG PDLEditorApp.initialize: Checking for parameter usage in groups: hash="+hash.toString());
					 
					 System.out.println("DEBUG PDLEditorApp.initialize: Param "+selParamName+" is used in groups="+hash.get(selParamName));
					 
					 
					 if(!hash.get(selParamName).isEmpty()) { // if the param is used in a group
						 
						 // get the groups where it's used
						 ArrayList<String> groups = (ArrayList<String>) hash.get(selParamName);
						 
						 JOptionPane.showMessageDialog(appFrame,"Cannot modify parameter "+selParamName+" because it's used in the following groups: \n"+groups,"Error",JOptionPane.ERROR_MESSAGE);
						 
					 } else { // the param is not used in any group
					
						ParameterDialog  editParameterDialog = new ParameterDialog(ParameterDialog.ParameterDialogModeModify
																					,mapParams
																					,comboBoxModelParams
																					,comboBoxParams
																					,mapExps);
						editParameterDialog.setLocationRelativeTo(appFrame); // dialog must appear in the middle of the app frame
						editParameterDialog.setVisible(true);
						
					 }
				}
			}
			
		});
		
		final JButton btnDelParam = new JButton("Delete Parameter");
		panelParamsMenu.add(btnDelParam);
		btnDelParam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selParamName = (String) comboBoxParams.getSelectedItem();
				
				if(selParamName!=null) {
				
					PDLParameter selParam = mapParams.get(selParamName);
					 
					 // TODO: check if the param is used in an atomicParameterExpression 
					 
					 
					 // check if the param is used in a group
					
					 
					 HashMap<String,List<String>> hash = getParamsAndUsageInGroups(mapParams,treeModelGroups);
					 System.out.println("DEBUG PDLEditorApp.initialize: Checking for parameter usage in groups: hash="+hash.toString());
					 
					 System.out.println("DEBUG PDLEditorApp.initialize: Param "+selParamName+" is used in groups="+hash.get(selParamName));
					 
					 
					 if(!hash.get(selParamName).isEmpty()) { // if the param is used in a group
						 
						 // get the groups where it's used
						 ArrayList<String> groups = (ArrayList<String>) hash.get(selParamName);
						 
						 JOptionPane.showMessageDialog(appFrame,"Cannot delete parameter "+selParamName+" because it's used in the following groups: \n"+groups,"Error",JOptionPane.ERROR_MESSAGE);
						 
					 } else { // the param is not used in any group
					 
					 
					     int option = JOptionPane.showConfirmDialog(appFrame,"Are you sure you want to delete parameter "+selParamName+" ?","Confirmation",JOptionPane.OK_CANCEL_OPTION);
					     if(option==JOptionPane.OK_OPTION) {
					    	 
					    	 mapParams.remove(comboBoxParams.getSelectedItem());
					    	 comboBoxModelParams.actionPerformed(new ActionEvent(btnDelParam,0,"update"));
					    	 
					    	 // make selection in comboBox, the comboBox action event will take care of updating the fields
					    	 if(mapParams.size()==0) {
					    		 comboBoxParams.setSelectedIndex(-1);
					    	 } else {
					    		 comboBoxParams.setSelectedIndex(0);
					    	 }
					    	 
					    	 
					     }
					     
					 } 
					     
				 } else {
					 JOptionPane.showMessageDialog(appFrame,"No parameter selected","Error",JOptionPane.ERROR_MESSAGE);
				 }
			}
			
		});
		
		
		
		
		
		
		
		
		
		
		JPanel panelParamsProperties = new JPanel();
		splitPaneParams.setRightComponent(panelParamsProperties);
		panelParamsProperties.setLayout(null);
		
		JLabel lblParamName = new JLabel("Name: ");
		lblParamName.setBounds(6, 12, 44, 16);
		panelParamsProperties.add(lblParamName);
		
		textFieldParamName = new JTextField();
		textFieldParamName.setEditable(false);
		textFieldParamName.setBounds(119, 6, 134, 28);
		textFieldParamName.setColumns(10);
		
		
		panelParamsProperties.add(textFieldParamName);
		
		
		JLabel lblParamType = new JLabel("Type:");
		lblParamType.setBounds(6, 36, 61, 16);
		panelParamsProperties.add(lblParamType);
		
		
		JLabel lblParamUcd = new JLabel("UCD:");
		lblParamUcd.setBounds(6, 64, 61, 16);
		panelParamsProperties.add(lblParamUcd);
		

		textFieldParamUCD = new JTextField();
		textFieldParamUCD.setEditable(false);
		textFieldParamUCD.setBounds(119, 58, 134, 28);
		textFieldParamUCD.setColumns(10);
		
		
		panelParamsProperties.add(textFieldParamUCD);
		
		

		
		JLabel lblParamUtype = new JLabel("UType:");
		lblParamUtype.setBounds(6, 85, 61, 16);
		panelParamsProperties.add(lblParamUtype);
		
		textFieldParamUType = new JTextField();
		textFieldParamUType.setEditable(false);
		textFieldParamUType.setBounds(119, 79, 134, 28);
		textFieldParamUType.setColumns(10);
		
		
		panelParamsProperties.add(textFieldParamUType);
		
		JLabel lblParamSkoss = new JLabel("SkossConcept:");
		lblParamSkoss.setBounds(8, 107, 99, 16);
		panelParamsProperties.add(lblParamSkoss);
		
		
		textFieldParamSkoss = new JTextField();
		textFieldParamSkoss.setEditable(false);
		textFieldParamSkoss.setBounds(119, 102, 134, 28);
		panelParamsProperties.add(textFieldParamSkoss);
		textFieldParamSkoss.setColumns(10);
		
		
		
		
		
		JLabel lblParamUnit = new JLabel("Unit:");
		lblParamUnit.setBounds(6, 128, 61, 16);
		panelParamsProperties.add(lblParamUnit);
		
		textFieldParamUnit = new JTextField();
		textFieldParamUnit.setEditable(false);
		textFieldParamUnit.setBounds(119, 122, 134, 28);
		panelParamsProperties.add(textFieldParamUnit);
		textFieldParamUnit.setColumns(10);
		
		
		JLabel lblParamPrecision = new JLabel("Precision:");
		lblParamPrecision.setBounds(6, 177, 65, 16);
		panelParamsProperties.add(lblParamPrecision);
		
		JLabel lblParamDimension = new JLabel("Dimension:");
		lblParamDimension.setBounds(6, 154, 87, 16);
		panelParamsProperties.add(lblParamDimension);
		
		textFieldParamPrecision = new JTextField();
		textFieldParamPrecision.setEditable(false);
		textFieldParamPrecision.setBounds(119, 171, 134, 28);
		panelParamsProperties.add(textFieldParamPrecision);
		textFieldParamPrecision.setColumns(10);
		
		textFieldParamDimension = new JTextField();
		textFieldParamDimension.setEditable(false);
		textFieldParamDimension.setBounds(119, 148, 134, 28);
		panelParamsProperties.add(textFieldParamDimension);
		textFieldParamDimension.setColumns(10);
		
		textFieldParamType = new JTextField();
		textFieldParamType.setEditable(false);
		textFieldParamType.setBounds(119, 30, 134, 28);
		panelParamsProperties.add(textFieldParamType);
		textFieldParamType.setColumns(10);

		
		
		
		// ---------- Parameter Groups Module ----------
		
		JLabel lblParametersModule = new JLabel("Parameters Module");
		lblParametersModule.setBounds(6, 18, 128, 16);
		appFrame.getContentPane().add(lblParametersModule);
		
		
		JScrollPane scrollPaneGroups = new JScrollPane();
		scrollPaneGroups.setBounds(500, 46, 366, 209);
		appFrame.getContentPane().add(scrollPaneGroups);

		PDLGroup rootGroup = new PDLGroup("ROOT");
		rootNode = new DefaultMutableTreeNode(rootGroup);
		//rootNode.setAllowsChildren(true); // not necessary
		
		
		treeModelGroups = new DefaultTreeModel(rootNode);
		treeGroups = new JTree(treeModelGroups);
		treeGroups.setEditable(false);  // not necessary
		
		
		
		treeGroups.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // allow only one selection in tree
		treeGroups.setShowsRootHandles(true); // ? 
		
		
		// for debug
		PDLGroup testGroup1 = new PDLGroup("TG1");
		testGroup1.addPDLParam("TP1");
		DefaultMutableTreeNode testNode1 = new DefaultMutableTreeNode(testGroup1);
		treeModelGroups.insertNodeInto(testNode1, (MutableTreeNode) treeModelGroups.getRoot() , 0);
								
		// for debug
		PDLGroup testGroup2 = new PDLGroup("TG2");
		testGroup2.addPDLParam("TP2");
		DefaultMutableTreeNode testNode2 = new DefaultMutableTreeNode(testGroup2);
		treeModelGroups.insertNodeInto(testNode2, (MutableTreeNode) treeModelGroups.getRoot() , 1);
		
		
		scrollPaneGroups.setViewportView(treeGroups);
		

	
		
		
		JLabel lblParameterGroupsModule = new JLabel("Parameter Groups Module");
		lblParameterGroupsModule.setBounds(500, 18, 175, 16);
		appFrame.getContentPane().add(lblParameterGroupsModule);
		
		JButton btnNewGroup = new JButton("New Group");
		btnNewGroup.setBounds(873, 46, 117, 29);
		btnNewGroup.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				
				if(treeGroups.isSelectionEmpty()==true) {
					JOptionPane.showMessageDialog(appFrame,"No parent node selected. Cannot create new group","Error",JOptionPane.ERROR_MESSAGE);

				} else {
					
				
					// get node selected
					DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) treeGroups.getLastSelectedPathComponent();
					
					
					// create new dialog to enter param for new group
					GroupDialog newGroupDialog = new GroupDialog(GroupDialog.GroupDialogModeCreate
														   ,comboBoxModelParams
														   ,treeGroups
														   ,treeModelGroups
														   ,selNode
														   ,comboBoxServiceInputs
														   ,comboBoxServiceOutputs
														   ,comboBoxModelServiceInputs
														   ,comboBoxModelServiceOutputs
														   ,mapStats
														   );
					newGroupDialog.setLocationRelativeTo(appFrame); // dialog must appear in the middle of the app frame
					newGroupDialog.setVisible(true);
					
					
					
					
				}
				
			}
		});
		
		
		
		appFrame.getContentPane().add(btnNewGroup);
		
		JButton btnDeleteGroup = new JButton("Delete Group");
		btnDeleteGroup.setBounds(873, 78, 117, 29);
		
		btnDeleteGroup.addActionListener(new ActionListener() {


			public void actionPerformed(ActionEvent e) {
			
				if(treeGroups.isSelectionEmpty()==true) {
					JOptionPane.showMessageDialog(appFrame,"No node selected. Cannot delete group","Error",JOptionPane.ERROR_MESSAGE);

				} else {
					
					
					// get the selected node
					DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) treeGroups.getLastSelectedPathComponent();
					
					
					
					
					if(selNode.toString().equals("ROOT")) {
						JOptionPane.showMessageDialog(appFrame,"You cannot delete the ROOT group","Error",JOptionPane.ERROR_MESSAGE);
					} else {
					
						// check that the group has no children
						
						// get the children nodes
						ArrayList<String> childrenList = new ArrayList<String>();
						@SuppressWarnings("rawtypes")
						Enumeration children = selNode.children();
						if(children!=null) {
							while(children.hasMoreElements()) {
								childrenList.add(children.nextElement().toString());
							}
							System.out.println("DEBUG PDLEditorApp.initialize: children of selected node = "+childrenList);
						} else {
							System.out.println("DEBUG PDLEditorApp.initialize: selected node has no child");
						}
						
						if(!childrenList.isEmpty()) {
							
							JOptionPane.showMessageDialog(appFrame,"You cannot delete this group because it has the following children:\n"+childrenList,"Error",JOptionPane.ERROR_MESSAGE);
							
						} else {
						
							
							// check that the group is not used by any statement
							ArrayList<String> statsWhereGroupIsUsed = new ArrayList<String>();
							
							for(String statName: mapStats.keySet()) {
								
								System.out.println("DEBUG PDLEditor.initialize: Exploring statement name="+statName);

								PDLStatement stat = (PDLStatement) mapStats.get(statName);
								String statGroup = stat.getGroup();
								if(statGroup.equals(selNode.toString())) { // group for current statement is the same as the group to be deleted
									statsWhereGroupIsUsed.add(statName);
								}
							}
							
							
							
							if(!statsWhereGroupIsUsed.isEmpty()) {
								
								JOptionPane.showMessageDialog(appFrame,"You cannot delete this group because it is used by the following statements:\n"+statsWhereGroupIsUsed,"Error",JOptionPane.ERROR_MESSAGE);
								
							} else {
							
								int option = JOptionPane.showConfirmDialog(appFrame,"Are you sure you want to delete group "+selNode.toString()+" ?","Confirmation",JOptionPane.OK_CANCEL_OPTION);
							    if(option==JOptionPane.OK_OPTION) {
							    	DefaultMutableTreeNode parentNode =  (DefaultMutableTreeNode)selNode.getParent();
							    	System.out.println("DEBUG PDLEditorApp.initialize: Removing node="+selNode.toString()+" parent="+parentNode.toString());
							    	treeModelGroups.removeNodeFromParent(selNode);
							    	
							    	//parentNode.remove(selNode);
							    	//treeGroups.getModel()
							    	//treeGroups.repaint();
							    	
							    	
							    	/* this is handled by the TreeModelListener of treeModelGroups
									// init the 2 combobox for service input and output
									initComboBoxService(comboBoxModelServiceInputs,treeModelGroups);
									initComboBoxService(comboBoxModelServiceOutputs,treeModelGroups);
									// set the 2 combobox to have nothing selected by default
									comboBoxServiceInputs.setSelectedIndex(-1); 
									comboBoxServiceOutputs.setSelectedIndex(-1);
							    	*/
							    	
							    	
							    	
									
							    } // if
						    
							} // else
						} // else 
					}
				}
			
			}
		});
		
		
		appFrame.getContentPane().add(btnDeleteGroup);
		
		JButton btnEditGroup = new JButton("Edit Group");
		btnEditGroup.setBounds(873, 112, 117, 29);
		btnEditGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				if(treeGroups.isSelectionEmpty()==true) {
					JOptionPane.showMessageDialog(appFrame,"No group selected. Cannot edit a group","Error",JOptionPane.ERROR_MESSAGE);

				} else {
				
					// get node selected
					DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) treeGroups.getLastSelectedPathComponent();
					
					if(selNode.toString().equals("ROOT")) {
						JOptionPane.showMessageDialog(appFrame,"You cannot edit the ROOT group","Error",JOptionPane.ERROR_MESSAGE);
					} else {
					
						
						
						
						// create new dialog to modify existing group
						GroupDialog editGroupDialog = new GroupDialog(GroupDialog.GroupDialogModeModify
																,comboBoxModelParams
																,treeGroups
																,treeModelGroups
																,selNode
																,comboBoxServiceInputs
																,comboBoxServiceOutputs
																,comboBoxModelServiceInputs
																,comboBoxModelServiceOutputs
																,mapStats
																);
						editGroupDialog.setLocationRelativeTo(appFrame); // dialog must appear in the middle of the app frame
						editGroupDialog.setVisible(true);
					}
				}
			}
			
		});
		
		
		appFrame.getContentPane().add(btnEditGroup);
		
		
		// ---------- Expressions Module ----------
		mapExps = new TreeMap<String,Expression>();
		
		
		JLabel lblExpressionsModule = new JLabel("Expressions Module");
		lblExpressionsModule.setBounds(6, 267, 148, 16);
		appFrame.getContentPane().add(lblExpressionsModule);
		
		JSplitPane splitPaneExps = new JSplitPane();
		splitPaneExps.setBounds(6, 295, 475, 180);
		appFrame.getContentPane().add(splitPaneExps);
		
		JPanel panelExpsMenu = new JPanel();
		splitPaneExps.setLeftComponent(panelExpsMenu);
		panelExpsMenu.setLayout(new GridLayout(0, 1, 0, 0));

		
		
		// create a constant for creating expressions
		List<String> constantValues = new ArrayList<String>();
		constantValues.add("1.0");
		AtomicConstantExpression oneExp = new AtomicConstantExpression().withConstant(constantValues).withConstantType(ParameterType.INTEGER);
		mapExps.put("_one", oneExp);
		
		
		// add a dummy expression for testing
		AtomicConstantExpression testExp1 = new AtomicConstantExpression().withConstant(constantValues).withConstantType(ParameterType.REAL);
		mapExps.put("TE1", testExp1);
		
		// add a dummy expression for testing
		AtomicConstantExpression testExp2 = new AtomicConstantExpression().withConstant(constantValues).withConstantType(ParameterType.REAL);
		mapExps.put("TE2", testExp2);	
		
		
		
		
		comboBoxModelExps = new MapComboBoxModel(mapExps);
		
		comboBoxExps = new JComboBox(comboBoxModelExps);
		panelExpsMenu.add(comboBoxExps);
		
		
		comboBoxExps.setSelectedIndex(-1);
		
		
		

		
		// when parameter is selected, update its fields. If nothing is selected, set fields to blank
		comboBoxExps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cmd=e.getActionCommand();
				if(cmd.equals("comboBoxChanged")) {
					String selExpName=(String)comboBoxExps.getSelectedItem();
					if(selExpName!=null) {						
						textFieldExpName.setText(selExpName);
						// get the type
						Expression selExp = mapExps.get(selExpName);
						Class selExpClass = selExp.getClass();
						textFieldExpClass.setText(selExpClass.toString());
						/*
						AtomicConstantExpression selExp1=(AtomicConstantExpression)selExp;
						selExp1.getConstantType();
						*/
						if(selExpClass == AtomicConstantExpression.class) {
							textFieldExpClass.setText("ACE");
							AtomicConstantExpression selExp1=(AtomicConstantExpression)selExp;
							ParameterType selExpType = selExp1.getConstantType();
							textFieldExpType.setText(selExpType.toString());
						}
						if(selExpClass == AtomicParameterExpression.class) {
							textFieldExpClass.setText("APE");
							AtomicParameterExpression selExp1=(AtomicParameterExpression)selExp; 
							ParameterReference selExpParameterRef = selExp1.getParameterRef(); // get the parameter reference
							String selExpParameterName = selExpParameterRef.getParameterName(); // get the parameter name from the parameter reference
							PDLParameter selExpParameter = mapParams.get(selExpParameterName); // get the PDLParameter from the parameter name
							ParameterType selExpParameterType = selExpParameter.getType(); // get the PDLParameter type
							textFieldExpType.setText(selExpParameterType.toString());
						}
						if(selExpClass == ParenthesisContent.class) {
							textFieldExpClass.setText("PCE");
							ParenthesisContent selExp1 = (ParenthesisContent)selExp;
							//Expression selExp2 = selExp1.getExpression(); // get the expression in the ParenthesisContent Expression
							
							// TODO: I don't know how to get the type of a ParenthesisContent Expression
							textFieldExpType.setText("unknown");
							
						}
						
					} else {
						textFieldExpName.setText("");
						textFieldExpClass.setText("");
						textFieldExpType.setText("");
						
					}
				}
			}
		});
		
		final JButton btnNewAtomicConstantExpression = new JButton("New Atomic Constant Expression");
		panelExpsMenu.add(btnNewAtomicConstantExpression);
		
		
		btnNewAtomicConstantExpression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				AtomicConstantExpressionDialog newAtomicConstantExpressionDialog = new AtomicConstantExpressionDialog(mapExps, comboBoxModelExps, comboBoxExps);
				
				newAtomicConstantExpressionDialog.setLocationRelativeTo(appFrame); // dialog must appear in the middle of the app frame
				newAtomicConstantExpressionDialog.setVisible(true);
				
				
				
			}
			
		});
		
		JButton btnNewAtomicParameterExpression = new JButton("New Atomic Parameter Expression");
		btnNewAtomicParameterExpression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				AtomicParameterExpressionDialog newAtomicParameterExpressionDialog = new AtomicParameterExpressionDialog(mapExps, comboBoxModelExps, comboBoxExps, mapParams);
				
				newAtomicParameterExpressionDialog.setLocationRelativeTo(appFrame); // dialog must appear in the middle of the app frame
				newAtomicParameterExpressionDialog.setVisible(true);
				
			}
		});
		panelExpsMenu.add(btnNewAtomicParameterExpression);
		
		JButton btnNewParenthesisExpression = new JButton("New Parenthesis Expression");
		btnNewParenthesisExpression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ParenthesisContentExpressionDialog newParenthesisContentExpressionDialog = new ParenthesisContentExpressionDialog(mapExps, comboBoxModelExps, comboBoxExps, mapParams);
				
				newParenthesisContentExpressionDialog.setLocationRelativeTo(appFrame); // dialog must appear in the middle of the app frame
				newParenthesisContentExpressionDialog.setVisible(true);
				
			}
		});
		
		
		
		panelExpsMenu.add(btnNewParenthesisExpression);
		
		JButton btnNewFunctionExpression = new JButton("New Function Expression");
		
		btnNewFunctionExpression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				FunctionExpressionDialog newFunctionExpressionDialog = new FunctionExpressionDialog(mapExps, comboBoxModelExps, comboBoxExps, mapParams);
				
				newFunctionExpressionDialog.setLocationRelativeTo(appFrame); // dialog must appear in the middle of the app frame
				newFunctionExpressionDialog.setVisible(true);
				
			}
		});
		
		panelExpsMenu.add(btnNewFunctionExpression);
		
		
		final JButton btnDelExpression = new JButton("Delete Expression");
		panelExpsMenu.add(btnDelExpression);
		
		btnDelExpression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 String selExpName = (String)comboBoxExps.getSelectedItem();
				 if(selExpName!=null) {
					 					 
					 
					 // check if the expression is used in a param (in the param dimension or precision)
					 ArrayList<String> paramNamesWhereExpUsed = getParamsWhereExpIsUsed(mapParams, selExpName);
					 if(!paramNamesWhereExpUsed.isEmpty()) { // the expression is used in some parameter 
						 JOptionPane.showMessageDialog(appFrame,"Cannot delete expression "+selExpName+" because it's used in the following parameters: \n"+paramNamesWhereExpUsed,"Error",JOptionPane.ERROR_MESSAGE);
						 
						 
					 } else { // the expression is not used in any parameter
					 
					 
						 // check if the expression is used in a criterion
						 ArrayList<String> critNamesWhereExpUsed = getCritsWhereExpIsUsed(mapCrits, selExpName);
						  
						 if(!critNamesWhereExpUsed.isEmpty()) { // the expression is used in some criterion
							 JOptionPane.showMessageDialog(appFrame,"Cannot delete expression "+selExpName+" because it's used in the following criterions: \n"+critNamesWhereExpUsed,"Error",JOptionPane.ERROR_MESSAGE);
						 
						 } else {	 // the expression is not used in any criterion
						 
						 
						     int option = JOptionPane.showConfirmDialog(appFrame,"Are you sure you want to delete expression "+selExpName+" ?","Confirmation",JOptionPane.OK_CANCEL_OPTION);
						     if(option==JOptionPane.OK_OPTION) {
						    	 
						    	 System.out.println("DEBUG btnDeleteExpression.actionPerformed: Removing "+selExpName+" from mapExps");
						    	 mapExps.remove(selExpName);
						    	 comboBoxModelExps.actionPerformed(new ActionEvent(btnDelExpression,0,"update"));
						    	 
						    	 
						    	 if(mapExps.size()==0) {
						    		 comboBoxExps.setSelectedIndex(-1); // do not select any
						    		 
						    		 // clear the text fields
						    		 textFieldExpName.setText("");
						    		 textFieldExpClass.setText("");
						    		 textFieldExpType.setText("");
										
						    	 } else {
						    		 comboBoxExps.setSelectedIndex(0); // select the first one
						    	 }

									
						     } // OK
						    	 
						 } // else 
						     
					 } // expr not used by any param
					 
				 
					     
				 } else {
					 JOptionPane.showMessageDialog(appFrame,"No expression selected","Error",JOptionPane.ERROR_MESSAGE);
				 }
			}

		
			
		});
		
		
		
		JPanel panelExpsProperties = new JPanel();
		splitPaneExps.setRightComponent(panelExpsProperties);
		panelExpsProperties.setLayout(null);
		
		JLabel lblExpName = new JLabel("Name:");
		lblExpName.setBounds(6, 11, 61, 16);
		panelExpsProperties.add(lblExpName);
		
		textFieldExpName = new JTextField();
		textFieldExpName.setEditable(false);
		textFieldExpName.setBounds(64, 5, 138, 28);
		panelExpsProperties.add(textFieldExpName);
		textFieldExpName.setColumns(10);
		
		JLabel lblExpType = new JLabel("Type:");
		lblExpType.setBounds(6, 63, 61, 16);
		panelExpsProperties.add(lblExpType);
		
		textFieldExpType = new JTextField();
		textFieldExpType.setEditable(false);
		textFieldExpType.setBounds(64, 57, 134, 28);
		panelExpsProperties.add(textFieldExpType);
		textFieldExpType.setColumns(10);
		
		JLabel lblExpClass = new JLabel("Class:");
		lblExpClass.setBounds(6, 35, 61, 16);
		panelExpsProperties.add(lblExpClass);
		
		textFieldExpClass = new JTextField();
		textFieldExpClass.setEditable(false);
		textFieldExpClass.setBounds(64, 29, 134, 28);
		panelExpsProperties.add(textFieldExpClass);
		textFieldExpClass.setColumns(10);
		
		
		
	
		
		
		
		// ---------- Criterions Module -----------
		
		
		JPanel panelCrit = new JPanel();
		panelCrit.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelCrit.setBounds(500, 295, 475, 180);
		appFrame.getContentPane().add(panelCrit);
		panelCrit.setLayout(null);
		
		JScrollPane scrollPaneCrit = new JScrollPane();
		scrollPaneCrit.setBounds(6, 6, 347, 162);
		panelCrit.add(scrollPaneCrit);

		
		
		mapCrits=new TreeMap<String,PDLCriterion>();
		
		
		// create dummy criterion for debug
		PDLCriterion testCrit1 = new PDLCriterion();
		testCrit1.setCExp("TE1");
		testCrit1.setType("ValueSmallerThan");
		testCrit1.addExp("TE2");		

		mapCrits.put("TC1",testCrit1);
		
		// create dummy criterion for debug 
		PDLCriterion testCrit2 = new PDLCriterion();
		testCrit2.setCExp("TE2");
		testCrit2.setType("IsNull");
		
		mapCrits.put("TC2", testCrit2);
		
		
		listModelCrits = new MapListModel(mapCrits);

		
		listCrits = new JList(listModelCrits);
		listCrits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		MapListCellRenderer listCritsRenderer = new MapListCellRenderer();
		//renderer.setPreferredSize(new Dimension(200, 130));
		listCrits.setCellRenderer(listCritsRenderer);
		
		scrollPaneCrit.setViewportView(listCrits);
		
		JButton btnDeleteCriterion = new JButton("Delete Criterion");
		btnDeleteCriterion.setSize(117, 29);
		btnDeleteCriterion.setLocation(358, 34);
		
		//btnDeleteCriterion.setBounds(873, 336, 117, 29);
		panelCrit.add(btnDeleteCriterion);
		
		btnDeleteCriterion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// get selected criterion name
				String selCritName = (String) listCrits.getSelectedValue();
				
				
				System.out.println("DEBUG PDLEditorApp.initialize: selected criterion="+selCritName);
				
				
				// check that the criterion is not used by any statement
				
				 ArrayList<String> statNamesWhereCritUsed = new ArrayList<String>();	
				 
				 
				 for(String statName : mapStats.keySet()) {
					 
				 
					 PDLStatement stat = (PDLStatement) mapStats.get(statName);
					 System.out.println("DEBUG PDLEditorApp.initialize: Exploring statement="+statName);
					 ArrayList<String> statCrits = new ArrayList<String>();
					 
					 statCrits.add(stat.getCrit1()); // get the criterion 1 used by the statement
					 statCrits.add(stat.getCrit2()); // get the criterion 2 used by the statement
					
					 for(int c=0;c<statCrits.size();c++) {
						 String critName = statCrits.get(c);
						 System.out.println("DEBUG PDLEditorApp.initialize: -> found criterium name="+critName);
						 if(selCritName.equals(critName)) {
							 // add the statement to the list to display
							 statNamesWhereCritUsed.add(statName);
						 }
						 
					 }
					
					 
				 }
				
				
				 if(!statNamesWhereCritUsed.isEmpty()) { // the criterion is used in some statements
					 JOptionPane.showMessageDialog(appFrame,"Cannot delete criterion "+selCritName+" because it's used in the following statements: \n"+statNamesWhereCritUsed,"Error",JOptionPane.ERROR_MESSAGE);
				 
				 } else {	 // the expression is not used in any criterion
				 
				 
					
					int option = JOptionPane.showConfirmDialog(appFrame,"Are you sure you want to delete criterion "+selCritName+" ?","Confirmation",JOptionPane.OK_CANCEL_OPTION);
				    
					if(option==JOptionPane.OK_OPTION) {
					
						mapCrits.remove(selCritName);
						listModelCrits.actionPerformed(new ActionEvent(btnDelExpression,0,"update"));
				    	//listModelCrits.removeElement(selCrit);
					
					}
				 } // else
			}
		});
		
		
		
		JButton btnNewCriterion = new JButton("New Criterion");
		btnNewCriterion.setBounds(358, 3, 117, 29);
		panelCrit.add(btnNewCriterion);
		
		
		btnNewCriterion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					
				// create new dialog to enter new criterion
				CriterionDialog newCritDialog = new CriterionDialog(mapExps,mapCrits,listModelCrits);
				newCritDialog.setLocationRelativeTo(appFrame); // dialog must appear in the middle of the app frame
				newCritDialog.setVisible(true);
				
				
			}
		});
		
		JLabel lblCriterionsModule = new JLabel("Criterions Module");
		lblCriterionsModule.setBounds(500, 267, 133, 16);
		appFrame.getContentPane().add(lblCriterionsModule);
		
		JLabel lblStatementsModule = new JLabel("Statements Module");
		lblStatementsModule.setBounds(6, 487, 128, 16);
		appFrame.getContentPane().add(lblStatementsModule);
		
		JPanel panelStats = new JPanel();
		panelStats.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelStats.setBounds(6, 515, 475, 180);
		appFrame.getContentPane().add(panelStats);
		panelStats.setLayout(null);
		
		JScrollPane scrollPaneStats = new JScrollPane();
		scrollPaneStats.setBounds(6, 6, 294, 168);
		panelStats.add(scrollPaneStats);
		
		
		mapStats = new TreeMap<String,PDLStatement>();
		
		
		
		PDLStatement testStat1 = new PDLStatement();
		testStat1.setType("IfThen");
		testStat1.setCrit1("TC1");
		testStat1.setCrit2("TC2");
		testStat1.setGroup("TG1");
		
		mapStats.put("TS1", testStat1);
		
		listModelStats = new MapListModel(mapStats);
		listStats = new JList(listModelStats);
		scrollPaneStats.setViewportView(listStats);
		
		MapListCellRenderer listStatsRenderer = new MapListCellRenderer();
		//renderer.setPreferredSize(new Dimension(200, 130));
		listStats.setCellRenderer(listStatsRenderer);
		
		
		JButton btnNewStatement = new JButton("New Statement");
		btnNewStatement.setBounds(312, 6, 144, 29);
		panelStats.add(btnNewStatement);
		
		
		btnNewStatement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					
				// create new dialog to enter new criterion
				StatementDialog newStatDialog = new StatementDialog(mapStats,listModelStats,mapCrits,treeModelGroups);
				newStatDialog.setLocationRelativeTo(appFrame); // dialog must appear in the middle of the app frame
				newStatDialog.setVisible(true);
				
				
			}
		});
		
		
		
		final JButton btnDeleteStatement = new JButton("Delete Statement");
		btnDeleteStatement.setBounds(312, 35, 144, 29);
		panelStats.add(btnDeleteStatement);
		
		btnDeleteStatement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// get selected statement
				String selStatName = (String) listStats.getSelectedValue();
				
				int option = JOptionPane.showConfirmDialog(appFrame,"Are you sure you want to delete statement "+selStatName+" ?","Confirmation",JOptionPane.OK_CANCEL_OPTION);
			    
				if(option==JOptionPane.OK_OPTION) {
				
					mapStats.remove(selStatName);
					listModelStats.actionPerformed(new ActionEvent(btnDeleteStatement,0,"update"));
					
				
			    }
				
				
				
			}
		});
		
		
		JPanel panelService = new JPanel();
		panelService.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelService.setBounds(500, 515, 475, 180);
		appFrame.getContentPane().add(panelService);
		panelService.setLayout(null);
		
		JLabel lblServiceId = new JLabel("Service ID: ");
		lblServiceId.setBounds(6, 6, 81, 16);
		panelService.add(lblServiceId);
		
		JLabel lblServiceName = new JLabel("Name");
		lblServiceName.setBounds(6, 34, 142, 16);
		panelService.add(lblServiceName);
		
		JLabel lblServiceInputs = new JLabel("Inputs:");
		lblServiceInputs.setBounds(6, 62, 61, 16);
		panelService.add(lblServiceInputs);
		
		JLabel lblServiceOutputs = new JLabel("Outputs:");
		lblServiceOutputs.setBounds(6, 90, 81, 16);
		panelService.add(lblServiceOutputs);
		
		JLabel lblServiceDescription = new JLabel("Description:");
		lblServiceDescription.setBounds(6, 118, 99, 16);
		panelService.add(lblServiceDescription);
		
		JScrollPane scrollPaneServiceDesc = new JScrollPane();
		scrollPaneServiceDesc.setBounds(114, 118, 355, 56);
		panelService.add(scrollPaneServiceDesc);
		
	    textAreaServiceDesc = new JTextArea();
		scrollPaneServiceDesc.setViewportView(textAreaServiceDesc);
		
		textFieldServiceID = new JTextField();
		textFieldServiceID.setBounds(114, 0, 134, 28);
		panelService.add(textFieldServiceID);
		textFieldServiceID.setColumns(10);
		
		textFieldServiceName = new JTextField();
		textFieldServiceName.setBounds(114, 28, 134, 28);
		panelService.add(textFieldServiceName);
		textFieldServiceName.setColumns(10);
		
	    comboBoxModelServiceInputs = new DefaultComboBoxModel();
	    comboBoxServiceInputs = new JComboBox(comboBoxModelServiceInputs);
		comboBoxServiceInputs.setBounds(114, 62, 136, 27);
		panelService.add(comboBoxServiceInputs);
		
		
		comboBoxModelServiceOutputs = new DefaultComboBoxModel();
		comboBoxServiceOutputs = new JComboBox(comboBoxModelServiceOutputs);
		comboBoxServiceOutputs.setBounds(114, 86, 136, 27);
		panelService.add(comboBoxServiceOutputs);		
		
		
		
		
		
		
		// init the 2 combobox for service input and output
		initComboBoxService(comboBoxModelServiceInputs,treeModelGroups);
		initComboBoxService(comboBoxModelServiceOutputs,treeModelGroups);
		// set the 2 combobox to have nothing selected by default
		comboBoxServiceInputs.setSelectedIndex(-1); 
		comboBoxServiceOutputs.setSelectedIndex(-1);
		
		// add a listener to the treeModelGroups. This MUST be done after comboBoxModelServiceInputs and comboBoxModelServiceOutputs are initialized !
		treeModelGroupsListener = new GroupsTreeModelListener(comboBoxModelServiceInputs,comboBoxModelServiceOutputs);
		treeModelGroups.addTreeModelListener(treeModelGroupsListener);


		
		JLabel lblServiceModule = new JLabel("Service Module");
		lblServiceModule.setBounds(500, 487, 94, 16);
		appFrame.getContentPane().add(lblServiceModule);
		
		btnLoadDescription = new JButton("Load Description");
		btnLoadDescription.setBounds(6, 726, 167, 29);
		appFrame.getContentPane().add(btnLoadDescription);
		
		
		btnLoadDescription.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadAllFromNeodatis();
			}
		});
		
		
		JButton btnSaveDescription = new JButton("Save Description");
		btnSaveDescription.setBounds(211, 726, 154, 29);
		appFrame.getContentPane().add(btnSaveDescription);
		

	    
		
		
		btnSaveDescription.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAllToNeodatis();	
			}
		});
		
	}

	public static String getAppVersion() {
		return appVersion;
	}

	public static String getAppName() {
		return appName;
	}

	public static String[] getAppAuthors() {
		return appAuthors;
	}
}



	
