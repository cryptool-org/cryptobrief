/**
 * 
 */
package sunset.gui.listener;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import sunset.gui.lib.FFaplMutableTreeNode;


/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class JTreeSelectionListenerPreferences implements TreeSelectionListener {

	private JPanel _outputPanel;
	/**
	 * 
	 */
	public JTreeSelectionListenerPreferences(JPanel panel) {
		_outputPanel = panel;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		DefaultTreeSelectionModel model;
		FFaplMutableTreeNode node;
		//JTree tree = (JTree) 
		model = (DefaultTreeSelectionModel) arg0.getSource();
		TreePath path = model.getSelectionPath();
		node = (FFaplMutableTreeNode) path.getLastPathComponent();
		_outputPanel.removeAll();
		if(node.getComponent() != null){	
			_outputPanel.add(node.getComponent());	
		}
		_outputPanel.updateUI();
		//tree.getModel().
	}

}
