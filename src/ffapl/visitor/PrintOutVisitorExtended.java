/**
 * 
 */
package ffapl.visitor;

import java.util.Iterator;

import ffapl.ast.nodes.*;
import ffapl.visitor.interfaces.IVoidArgVisitor;
import ffapl.ast.nodes.interfaces.*;
import ffapl.exception.FFaplException;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class PrintOutVisitorExtended implements IVoidArgVisitor<Integer> {

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTAddExpr)
	 */
	@Override
	public void visit(ASTAddExpr node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTAddOp)
	 */
	@Override
	public void visit(ASTAddOp node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTArgumentList)
	 */
	@Override
	public void visit(ASTArgumentList node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTArrayLen)
	 */
	@Override
	public void visit(ASTArrayLen node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTAssignment)
	 */
	@Override
	public void visit(ASTAssignment node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTBlock)
	 */
	@Override
	public void visit(ASTBlock node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCompType)
	 */
	@Override
	public void visit(ASTComplexAlgebraicType node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCondAndExpr)
	 */
	@Override
	public void visit(ASTCondAndExpr node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCondOrExpr)
	 */
	@Override
	public void visit(ASTCondOrExpr node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCondition)
	 */
	@Override
	public void visit(ASTCondition node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCreationExpr)
	 */
	@Override
	public void visit(ASTCreationExpr node, Integer level)
			throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCreationExprComp)
	 */
	@Override
	public void visit(ASTExprComplexAType node, Integer level)
			throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTDecl)
	 */
	@Override
	public void visit(ASTDecl node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);
		//node._node6.accept(this, level + 1);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTConstDecl)
	 */
	@Override
	public void visit(ASTConstDecl node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);		
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTElseBlock)
	 */
	@Override
	public void visit(ASTElseBlock node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTEqualExpr)
	 */
	@Override
	public void visit(ASTEqualExpr node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTEqualOp)
	 */
	@Override
	public void visit(ASTEqualOp node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTExpr)
	 */
	@Override
	public void visit(ASTExpr node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTFormalParam)
	 */
	@Override
	public void visit(ASTFormalParam node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTFormalParamList)
	 */
	@Override
	public void visit(ASTFormalParamList node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTForStatement)
	 */
	@Override
	public void visit(ASTForStatement node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);
		node._node6.accept(this, level + 1);
		node._node7.accept(this, level + 1);
		node._node8.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTFunc)
	 */
	@Override
	public void visit(ASTFunc node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);
		node._node6.accept(this, level + 1);
		node._node7.accept(this, level + 1);
		node._node8.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTFuncBlock)
	 */
	@Override
	public void visit(ASTFuncBlock node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTGF)
	 */
	@Override
	public void visit(ASTGF node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);
		node._node6.accept(this, level + 1);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTEC)
	 */
	@Override
	public void visit(ASTEC node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTEC)
	 */
	@Override
	public void visit(ASTECPoint node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);
	}

	

	@Override
	public void visit(ASTECPAI node, Integer level) throws FFaplException {
		printOut(node, level);		
	}
	

	@Override
	public void visit(ASTECRandom node, Integer level) throws FFaplException {
		printOut(node, level);		
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTIdTerm)
	 */
	@Override
	public void visit(ASTIdTerm node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTIfStatement)
	 */
	@Override
	public void visit(ASTIfStatement node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTLiteral)
	 */
	@Override
	public void visit(ASTLiteral node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTMulExpr)
	 */
	@Override
	public void visit(ASTMulExpr node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTMulOp)
	 */
	@Override
	public void visit(ASTMulOp node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTPolynom)
	 */
	@Override
	public void visit(ASTPolynomial node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTPowExpr)
	 */
	@Override
	public void visit(ASTPowExpr node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTPrimaryExpr)
	 */
	@Override
	public void visit(ASTPrimaryExpr node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTPrimType)
	 */
	@Override
	public void visit(ASTPrimitiveType node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTProc)
	 */
	@Override
	public void visit(ASTProc node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);
		node._node6.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTProcFuncCall)
	 */
	@Override
	public void visit(ASTProcFuncCall node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTProgram)
	 */
	@Override
	public void visit(ASTProgram node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);
		node._node6.accept(this, level + 1);
		node._node7.accept(this, level + 1);
		node._node8.accept(this, level + 1);
		node._node9.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTPsRandomGenerator)
	 */
	@Override
	public void visit(ASTPsRandomGenerator node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);
		node._node6.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTRandom)
	 */
	@Override
	public void visit(ASTRandom node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTRandomGenerator)
	 */
	@Override
	public void visit(ASTRandomGenerator node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
		node._node4.accept(this, level + 1);
		node._node5.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTRecord)
	 */
	@Override
	public void visit(ASTRecord node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTRelExpr)
	 */
	@Override
	public void visit(ASTRelExpr node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTRelOp)
	 */
	@Override
	public void visit(ASTRelOp node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTReturnStatement)
	 */
	@Override
	public void visit(ASTReturnStatement node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTSelector)
	 */
	@Override
	public void visit(ASTSameAs node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTSelector)
	 */
	@Override
	public void visit(ASTSelector node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTStatement)
	 */
	@Override
	public void visit(ASTStatement node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTBlockStatement)
	 */
	@Override
	public void visit(ASTBlockStatement node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTStatementList)
	 */
	@Override
	public void visit(ASTStatementList node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTTerm)
	 */
	@Override
	public void visit(ASTTerm node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTTypePlain)
	 */
	@Override
	public void visit(ASTDeclType node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTUnaryExpr)
	 */
	@Override
	public void visit(ASTUnaryExpr node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTWhileStatement)
	 */
	@Override
	public void visit(ASTWhileStatement node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		node._node2.accept(this, level + 1);
		node._node3.accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeChoice)
	 */
	@Override
	public void visit(FFaplNodeChoice node, Integer level) throws FFaplException {
		//printOut(node, level);
		node.getNode().accept(this, level + 1);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeList)
	 */
	@Override
	public void visit(FFaplNodeList node, Integer level) throws FFaplException {
		for (final Iterator<INode> e = node.iterator(); e.hasNext();) {
		      e.next().accept(this, level + 1);
		}
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeListOpt)
	 */
	@Override
	public void visit(FFaplNodeListOpt node, Integer level) throws FFaplException {
		if(node.ispresent()){
			for (final Iterator<INode> e = node.iterator(); e.hasNext();) {
			      e.next().accept(this, level + 1);
			}
		}
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeOpt)
	 */
	@Override
	public void visit(FFaplNodeOpt node, Integer level) throws FFaplException {
		if(node.ispresent()){
			node.getNode().accept(this, level + 1);
		}
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeSequence)
	 */
	@Override
	public void visit(FFaplNodeSequence node, Integer level) throws FFaplException {
		for (final Iterator<INode> e = node.iterator(); e.hasNext();) {
			  e.next().accept(this, level + 1);
		}
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeToken)
	 */
	@Override
	public void visit(FFaplNodeToken node, Integer level) throws FFaplException {
		printOut(node, level);
	}
	
	@Override
	public void visit(FFaplNodeType node, Integer level)
			throws FFaplException {
		printOut(node, level);
		node.getNode().accept(this, level + 1);
	}
	
	private void printOut(INode node, Integer level){
		String temp = "";
		
		for(int i = level; i > 0; i--){
			temp = temp + ".";
		}
		System.out.println(temp + node);		
	}

	@Override
	public void visit(ASTConstType node, Integer level)
			throws FFaplException {
		node._node1.accept(this, level + 1);		
	}

	@Override
	public void visit(ASTContainerType node, Integer level)
			throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		
	}

	@Override
	public void visit(ASTParamType node, Integer level)
			throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		
	}

	@Override
	public void visit(ASTArrayType node, Integer level)
			throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		
	}

	@Override
	public void visit(ASTAlgebraicType node, Integer level)
			throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	@Override
	public void visit(ASTRandomGeneratorType node, Integer level)
			throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
		
	}

	@Override
	public void visit(ASTExprRandomGType node, Integer level)
			throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	@Override
	public void visit(ASTBreak node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);
	}

	@Override
	public void visit(ASTECBaseField node, Integer level) throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);		
	}

	@Override
	public void visit(ASTECAssignment node, Integer level)
			throws FFaplException {
		printOut(node, level);
		node._node1.accept(this, level + 1);	
		node._node2.accept(this, level + 1);	
		node._node3.accept(this, level + 1);	
		
	}



		


}
