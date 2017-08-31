/**
 * 
 */
package ffapl.ast.nodes.interfaces;

import java.util.Iterator;


/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public interface INodeList extends INode {

	/**
	 * adds a Node to the list
	 * @param node
	 */
	public void addNode(INode node);
	
	/**
	 * Returns the size of the Node list
	 * @return
	 */
	public int size();
	
	/**
	 * returns a Iterator for the Node list
	 * @return
	 */
	public Iterator<INode> iterator();
	
	/**
	 * Returns the Node at position <Code> i </Code>
	 * @param i
	 * @return <null> if no element exists
	 */
	public INode elementAt(int i);
	
}
