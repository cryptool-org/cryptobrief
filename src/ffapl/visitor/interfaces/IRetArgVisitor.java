package ffapl.visitor.interfaces;

import java.util.Vector;

import ffapl.ast.nodes.*;
import ffapl.exception.FFaplException;
import ffapl.lib.interfaces.IAttribute;

public interface IRetArgVisitor <R,A> {

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTAddExpr node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R><
	 * @throws FFaplException
	 */
	public R visit(ASTAddOp node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTArgumentList node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTArrayLen node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTAssignment node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTBlock node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTComplexAlgebraicType node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTRandomGeneratorType node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTCondAndExpr node, A argument) throws FFaplException;
	
	public R visit(ASTCondOrExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTCondition node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTCreationExpr node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTExprComplexAType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTExprRandomGType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTDecl node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTConstDecl node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTConstType node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTContainerType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTParamType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTArrayType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTAlgebraicType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTElseBlock node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTEqualExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTEqualOp node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 */
	public R visit(ASTFormalParam node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTFormalParamList node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTForStatement node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTFunc node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTFuncBlock node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTGF node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTEC node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTECPoint node, A argument) throws FFaplException;
	public R visit(ASTECBaseField node, A argument) throws FFaplException;
	public R visit(ASTECAssignment node, A argument) throws FFaplException;
	
	public R visit(ASTECPAI node, A argument) throws FFaplException;
	public R visit(ASTECRandom node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTIdTerm node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTIfStatement node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTLiteral node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTMulExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTMulOp node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTPolynomial node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTPowExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTPrimaryExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTPrimitiveType node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTProc node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTProcFuncCall node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTProgram node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTPsRandomGenerator node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 */
	public R visit(ASTRandom node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTRandomGenerator node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTRecord node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTRelExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTRelOp node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTReturnStatement node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTBreak node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTSameAs node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTSelector node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTStatement node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTBlockStatement node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTStatementList node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTTerm node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTDeclType node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTUnaryExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTWhileStatement node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(FFaplNodeChoice node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(FFaplNodeList node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(FFaplNodeListOpt node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(FFaplNodeOpt node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(FFaplNodeSequence node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(FFaplNodeToken node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @return Values of type <R>
	 * @throws FFaplException
	 */
	public R visit(FFaplNodeType node, A argument) throws FFaplException;


	
	
}
