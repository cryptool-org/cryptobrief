/**
 * 
 */
package ffapl.ast.nodes;

import ffapl.exception.FFaplException;
import ffapl.visitor.interfaces.IRetArgVisitor;
import ffapl.visitor.interfaces.IRetVisitor;
import ffapl.visitor.interfaces.IVoidArgVisitor;
import ffapl.visitor.interfaces.IVoidVisitor;

/**
 * Represents a node for <Code>ConstDecl</Code>
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ASTConstDecl extends FFaplNode {

	public FFaplNodeToken _node1;
	public FFaplNodeToken _node2;
	public FFaplNodeToken _node3;
	public ASTConstType _node4;
	public FFaplNodeToken _node5;
	public ASTExpr _node6;
	public FFaplNodeToken _node7;
	
	
	public ASTConstDecl(int id,  FFaplNodeToken node1,  FFaplNodeToken node2,
			 FFaplNodeToken node3, ASTConstType node4, FFaplNodeToken node5, ASTExpr node6,  FFaplNodeToken node7) {
		super(id);
		_node1 = node1;
		_node2 = node2;
		_node3 = node3;
		_node4 = node4;
		_node5 = node5;
		_node6 = node6;
		_node7 = node7;
		_node1.setParent(this);
		_node2.setParent(this);
		_node3.setParent(this);
		_node4.setParent(this);
		_node5.setParent(this);	
		_node6.setParent(this);	
		_node7.setParent(this);	
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#accept(ffapl.visitor.interfaces.IVoidVisitor)
	 */
	@Override
	public void accept(IVoidVisitor visitor) throws FFaplException{
		visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#accept(ffapl.visitor.interfaces.IVoidArgVisitor, java.lang.Object)
	 */
	@Override
	public <A> void accept(IVoidArgVisitor<A> visitor, A argument) throws FFaplException{
		visitor.visit(this, argument);
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#accept(ffapl.visitor.interfaces.IRetVisitor)
	 */
	@Override
	public <R> R accept(IRetVisitor<R> visitor) throws FFaplException{
		return visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#accept(ffapl.visitor.interfaces.IRetArgVisitor, java.lang.Object)
	 */
	@Override
	public <A, R> R accept(IRetArgVisitor<R, A> visitor, A argument) throws FFaplException{
		return visitor.visit(this, argument);
	}
}
