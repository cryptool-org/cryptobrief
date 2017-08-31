/**
 * 
 */
package sunset.gui.lib;

import java.awt.Component;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class FFaplMutableTreeNode extends DefaultMutableTreeNode {

	private Component _component;
	
	/**
	 * 
	 */
	public FFaplMutableTreeNode() {
		_component = null;
	}

	/**
	 * @param userObject
	 */
	public FFaplMutableTreeNode(Object userObject) {
		super(userObject);
		_component = null;
	}

	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public FFaplMutableTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		_component = null;
	}
	
	/**
	 * Sets the Component for the Node
	 * @param comp
	 */
	public void addComponent(Component comp){
		_component = comp;
	}
	
	/**
	 * Return the Component for the Node
	 * @return
	 */
	public Component getComponent(){
		return _component;
	}

}
