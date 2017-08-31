/**
 * 
 */
package sunset.gui.api;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class APITreeCellRenderer extends DefaultTreeCellRenderer {

	private MutableTreeNode _node;
	
	public APITreeCellRenderer(){
		 setLeafIcon(new ImageIcon());
         setOpenIcon(new ImageIcon());
         setClosedIcon(new ImageIcon());
	}
	@Override
	public Component getTreeCellRendererComponent(JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus){
		 Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		 _node = (MutableTreeNode) value; 
		 if(_node instanceof IMutableTreeNodeAPI){
			 tree.setToolTipText(((IMutableTreeNodeAPI)_node).getHTMLInfo());
		 }else{
			 tree.setToolTipText(null);
		 }		
		 return comp;
	}
}
