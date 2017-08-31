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
 * Defines a Optional List of Nodes
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplNodeListOpt extends FFaplNodeList {

	
	/**
	 * Constructor
	 */
	public FFaplNodeListOpt(){
		super();
	}
	
	/**
	 * Returns <Code> true </Code> if the nodelist is present
	 * @return
	 */
	public boolean ispresent(){
		return _nodes.size() > 0;
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
