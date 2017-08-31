/**
 * 
 */
package ffapl.ast.nodes;

import ffapl.ast.nodes.interfaces.INode;
import ffapl.exception.FFaplException;
import ffapl.lib.FFaplToken;
import ffapl.lib.interfaces.IToken;
import ffapl.visitor.interfaces.IRetArgVisitor;
import ffapl.visitor.interfaces.IRetVisitor;
import ffapl.visitor.interfaces.IVoidArgVisitor;
import ffapl.visitor.interfaces.IVoidVisitor;
import ffapl.Token;

/**
 * Defines a Node for a Token respectively leaf.
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplNodeToken implements INode {

	
	private IToken _token;
	private INode _parent;
	
	/**
	 * constructor
	 * @param tk
	 */
	public FFaplNodeToken(Token token) {
		_token = new FFaplToken(token);
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#getParent()
	 */
	@Override
	public INode getParent() {
		return _parent;
	}
	
	/**
	 * Returns the JavaCC Token 
	 * @return
	 */
	public IToken getToken(){
		return _token;
	}

	/* (non-Javadoc)
	 * @see ffapl.ast.nodes.interfaces.INode#setParent(ffapl.ast.nodes.interfaces.INode)
	 */
	@Override
	public void setParent(INode parent) {
		_parent = parent;
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
	
	@Override
	public String toString(){
		if(_token != null){
			return "'" + _token + "'";
		}else{
			return "<<unused>>";
		}
	}

}
