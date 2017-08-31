package ffapl.ast.nodes;

import ffapl.exception.FFaplException;
import ffapl.visitor.interfaces.IRetArgVisitor;
import ffapl.visitor.interfaces.IRetVisitor;
import ffapl.visitor.interfaces.IVoidArgVisitor;
import ffapl.visitor.interfaces.IVoidVisitor;

public class ASTECPoint extends FFaplNode {
	public FFaplNodeToken _node1;
	public ASTExpr _node2;
	public FFaplNodeToken _node3;
	public ASTExpr _node4;
	public FFaplNodeToken _node5;
	 

	  public ASTECPoint(int id, FFaplNodeToken node1, ASTExpr node2, FFaplNodeToken node3, ASTExpr node4, FFaplNodeToken node5)
	  {
		  super(id);
		  	_node1 = node1;
			_node2 = node2;
			_node3 = node3;
			_node4 = node4;
			_node5 = node5;
			_node1.setParent(this);
			_node2.setParent(this);
			_node3.setParent(this);
			_node4.setParent(this);
			_node5.setParent(this);
			
	  }
	  



	@Override
	public void accept(IVoidVisitor visitor) throws FFaplException {
		visitor.visit(this);
		
	}


	@Override
	public <A> void accept(IVoidArgVisitor<A> visitor, A argument) throws FFaplException {
		visitor.visit(this, argument);
	}


	@Override
	public <R> R accept(IRetVisitor<R> visitor) throws FFaplException {
		return visitor.visit(this);
		//return null;
	}


	@Override
	public <A, R> R accept(IRetArgVisitor<R, A> visitor, A argument) throws FFaplException {
		return visitor.visit(this, argument);
		//return null;
	}

}