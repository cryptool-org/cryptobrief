/**
 * 
 */
package sunset.gui.listener;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import sunset.gui.interfaces.IFFaplComponent;
import sunset.gui.lib.FFaplMutableTreeNode;


/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerPreferencesOk implements ActionListener {

	private JDialog _dialog;
	
	private JTree _tree;
	/**
	 * 
	 */
	public ActionListenerPreferencesOk(JDialog dialog, JTree tree) {
		_dialog = dialog;
		_tree = tree;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		store((DefaultTreeModel) this._tree.getModel(),(FFaplMutableTreeNode) this._tree.getModel().getRoot());
		_dialog.setVisible(false);
	}
	
	/**
	 * calls Store changes
	 * @param model
	 * @param root
	 */
	private void store(DefaultTreeModel model, FFaplMutableTreeNode root){
		int childscount;
		FFaplMutableTreeNode node;
		IFFaplComponent comp;
		childscount = model.getChildCount(root);
		
		for(int i = 0; i < childscount; i++){
			node = (FFaplMutableTreeNode) model.getChild(root, i);
			comp = (IFFaplComponent) node.getComponent();
			if(comp != null){
				comp.storeChanges();
			}
			store(model, node);
		}
		
	}

	

}
