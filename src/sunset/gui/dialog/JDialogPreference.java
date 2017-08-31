package sunset.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import sunset.gui.FFaplJFrame;
import sunset.gui.lib.FFaplMutableTreeNode;
import sunset.gui.listener.ActionListenerCloseWindow;
import sunset.gui.listener.ActionListenerPreferencesOk;
import sunset.gui.listener.JTreeSelectionListenerPreferences;
import sunset.gui.panel.JPanelInterpreter;
import sunset.gui.panel.JPanelLanguage;
import sunset.gui.util.SunsetBundle;
import sunset.gui.util.TranslateGUIElements;


@SuppressWarnings("serial")
public class JDialogPreference extends FFaplJDialog {
	private JPanel jPanelControl;
	private JTree jTree_Preferences;
	private JButton jButton_ok;
	private JButton jButton_cancel;
	private JPanel jPanel_Preferences;

	public JDialogPreference(FFaplJFrame frame) {
		super(frame);
		initGUI();
		translate();
	}
	
	private void initGUI() {
		try {
			this.setTitle("Preferences");
			this.setName("dialog_preferences");
			{
				jPanelControl = new JPanel();
				FlowLayout jPanelControlLayout = new FlowLayout();
				jPanelControlLayout.setAlignment(FlowLayout.RIGHT);
				jPanelControl.setLayout(jPanelControlLayout);
				getContentPane().add(jPanelControl, BorderLayout.SOUTH);
				jPanelControl.setPreferredSize(new java.awt.Dimension(384, 32));
				jPanelControl.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
				{
					jButton_ok = new JButton();
					jPanelControl.add(jButton_ok);
					jButton_ok.setText("OK");
					//jButton_ok.setPreferredSize(new java.awt.Dimension(69, 23));
					jButton_ok.setName("button_ok");
				}
				{
					jButton_cancel = new JButton();
					jPanelControl.add(jButton_cancel);
					jButton_cancel.setText("Cancel");
					//jButton_cancel.setPreferredSize(new java.awt.Dimension(106, 23));
					jButton_cancel.setName("button_cancel");
				}
			}
			{
				
				jTree_Preferences = new JTree();
				//jTree_Preferences.setRootVisible(false);
				getContentPane().add(jTree_Preferences, BorderLayout.WEST);
				jTree_Preferences.setBounds(new Rectangle(2,2));
				jTree_Preferences.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.gray));
				jTree_Preferences.setPreferredSize(new java.awt.Dimension(116, 230));
				//jTree_Preferences.removeAll();
			}
			{
				jPanel_Preferences = new JPanel();
				BorderLayout jPanel_PreferencesLayout = new BorderLayout();
				jPanel_Preferences.setLayout(jPanel_PreferencesLayout);
				getContentPane().add(jPanel_Preferences, BorderLayout.CENTER);
			}
			
			
			this.setSize(400, 250);
			initComponents();
			//pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initComponents(){
		FFaplMutableTreeNode root, node;
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
            {
                setLeafIcon(new ImageIcon());
                setOpenIcon(new ImageIcon());
                setClosedIcon(new ImageIcon());
            }
        };
		
		DefaultTreeModel model = (DefaultTreeModel) jTree_Preferences.getModel();
		root = createTreeNode("preftree_preferences", "Preferences");
		//node = createTreeNode("preftree_general", "General");
		//root.insert(node, 0);
		node = createTreeNode("preftree_language", "Language");
		node.addComponent(new JPanelLanguage((FFaplJFrame) this.getOwner()));
		root.insert(node, 0);
		node = createTreeNode("preftree_interpreter", "Interpreter");
		node.addComponent(new JPanelInterpreter((FFaplJFrame) this.getOwner()));
		root.insert(node, 0);
		TreePath initPath = new TreePath(node.getPath());
		model.setRoot(root);
		jTree_Preferences.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jTree_Preferences.setCellRenderer(renderer);
		//jTree_Preferences.addMouseListener(new PreferenceJTreeMouseListener(jPanel_Preferences, _properties));
		jTree_Preferences.getSelectionModel().addTreeSelectionListener(new JTreeSelectionListenerPreferences(jPanel_Preferences));
		//jTree_Preferences.getSelectionModel().setSelectionPath(path)();	
		this.jButton_ok.addActionListener(new ActionListenerPreferencesOk(this, jTree_Preferences));
		this.jButton_cancel.addActionListener(new ActionListenerCloseWindow(this));
		jTree_Preferences.getSelectionModel().setSelectionPath(initPath);
		jTree_Preferences.setRootVisible(false);
	}
	
	
	
	private void translate(){
		TranslateGUIElements.translateButton(jButton_ok);
		TranslateGUIElements.translateButton(jButton_cancel);
		TranslateGUIElements.translateDialog(this);
	}
	
	/**
	 * Creates a Tree node
	 * @param id
	 * @param defaultStr
	 * @return
	 */
	private FFaplMutableTreeNode createTreeNode(String id, String defaultStr){
		String str;
		str = SunsetBundle.getInstance().getProperty(id);
		if(str == null){
			str = defaultStr;
		}
		return new FFaplMutableTreeNode(str);
	}
	
	

}
