package ffapl.visitor.interfaces;

import ffapl.ast.nodes.*;
import ffapl.exception.FFaplException;


public interface IVoidArgVisitor <A>{

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTAddExpr node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTAddOp node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTArgumentList node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTArrayLen node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTAssignment node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTBlock node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTComplexAlgebraicType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTRandomGeneratorType node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTCondAndExpr node, A argument) throws FFaplException;

	public void visit(ASTCondOrExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTCondition node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException
	 */
	public void visit(ASTCreationExpr node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException
	 */
	public void visit(ASTExprComplexAType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException
	 */
	public void visit(ASTExprRandomGType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTDecl node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTConstDecl node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException
	 */
	public void visit(ASTConstType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException
	 */
	public void visit(ASTContainerType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException
	 */
	public void visit(ASTParamType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException
	 */
	public void visit(ASTArrayType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException
	 */
	public void visit(ASTAlgebraicType node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTElseBlock node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTEqualExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTEqualOp node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTFormalParam node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTFormalParamList node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTForStatement node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTFunc node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTFuncBlock node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTGF node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTEC node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTECPoint node, A argument) throws FFaplException;
	public void visit(ASTECBaseField node, A argument) throws FFaplException;
	public void visit(ASTECAssignment node, A argument) throws FFaplException;

	public void visit(ASTECPAI node, A argument) throws FFaplException;
	public void visit(ASTECRandom node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTIdTerm node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTIfStatement node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTLiteral node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTMulExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTMulOp node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTPolynomial node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTPowExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTPrimaryExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTPrimitiveType node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTProc node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTProcFuncCall node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTProgram node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTPsRandomGenerator node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTRandom node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTRandomGenerator node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTRecord node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTRelExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTRelOp node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTReturnStatement node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTBreak node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	* @param argument
	 * @throws FFaplException */
	public void visit(ASTSelector node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	* @param argument
	 * @throws FFaplException */
	public void visit(ASTSameAs node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTStatement node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTBlockStatement node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTStatementList node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTTerm node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTDeclType node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTUnaryExpr node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(ASTWhileStatement node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(FFaplNodeChoice node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(FFaplNodeList node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(FFaplNodeListOpt node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(FFaplNodeOpt node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(FFaplNodeSequence node, A argument) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException */
	public void visit(FFaplNodeToken node, A argument) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argument
	 * @throws FFaplException
	 */
	public void visit(FFaplNodeType node, A argument) throws FFaplException;

	
}
