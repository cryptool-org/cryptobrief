/**
 * 
 */
package ffapl.ast.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ffapl.ast.nodes.interfaces.INode;
import ffapl.ast.nodes.interfaces.INodeList;
import ffapl.exception.FFaplException;
import ffapl.visitor.interfaces.IRetArgVisitor;
import ffapl.visitor.interfaces.IRetVisitor;
import ffapl.visitor.interfaces.IVoidArgVisitor;
import ffapl.visitor.interfaces.IVoidVisitor;

/**
 * Defines a List of Nodes
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplNodeList implements INodeList {

	protected List<INode> _nodes;
	protected INode _parent;
	
	public FFaplNodeList(){
		_nodes = new ArrayList<INode>();
	}
	
	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INodeList#addNode(ffapl.ast.nodes.interfaces.INode)
	 */
	@Override
	public void addNode(INode node) {
		node.setParent(this);
		_nodes.add(node);
		
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INodeList#size()
	 */
	@Override
	public int size() {
		return _nodes.size();
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INodeList#iterator()
	 */
	@Override
	public Iterator<INode> iterator() {
		return _nodes.iterator();
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INodeList#elementAt(int)
	 */
	@Override
	public INode elementAt(int i){
		return _nodes.get(i);
	}

	@Override
	public INode getParent() {
		return _parent;
	}

	@Override
	public void setParent(INode parent) {
		_parent = parent;		
	}
	
	@Override
	public void accept(IVoidVisitor visitor) throws FFaplException{
		visitor.visit(this);
	}

	@Override
	public <A> void accept(IVoidArgVisitor<A> visitor, A argument) throws FFaplException{
		visitor.visit(this, argument);		
	}

	@Override
	public <R> R accept(IRetVisitor<R> visitor) throws FFaplException{
		return visitor.visit(this);
	}

	@Override
	public <A, R> R accept(IRetArgVisitor<R, A> visitor, A argument) throws FFaplException{
		return visitor.visit(this, argument);
	}
}
