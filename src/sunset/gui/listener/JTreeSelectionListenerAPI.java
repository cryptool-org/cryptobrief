/**
 * 
 */
package sunset.gui.listener;

import javax.swing.JTextPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import sunset.gui.api.IMutableTreeNodeAPI;



/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class JTreeSelectionListenerAPI implements TreeSelectionListener {

	private JTextPane _outputPane;
	/**
	 * 
	 */
	public JTreeSelectionListenerAPI(JTextPane jTextPane_API) {
		_outputPane = jTextPane_API;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		DefaultTreeSelectionModel model;
		MutableTreeNode node;
		//JTree tree = (JTree) 
		model = (DefaultTreeSelectionModel) arg0.getSource();
		TreePath path = model.getSelectionPath();
		node = (MutableTreeNode) path.getLastPathComponent();
		if(node instanceof IMutableTreeNodeAPI){
			_outputPane.setText(((IMutableTreeNodeAPI)node).getInfo());
		}else{
			_outputPane.setText("");
		}
	}

}
