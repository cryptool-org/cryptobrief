package ffapl.visitor.interfaces;

import ffapl.ast.nodes.*;
import ffapl.exception.FFaplException;

public interface IVoidVisitor {

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTAddExpr node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTAddOp node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTArgumentList node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTArrayLen node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTAssignment node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTBlock node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTComplexAlgebraicType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTRandomGeneratorType node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTCondAndExpr node) throws FFaplException;
	
	
	public void visit(ASTCondOrExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTCondition node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException
	 */
	public void visit(ASTCreationExpr node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException
	 */
	public void visit(ASTExprComplexAType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException
	 */
	public void visit(ASTExprRandomGType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTDecl node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTConstDecl node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException
	 */
	public void visit(ASTConstType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException
	 */
	public void visit(ASTContainerType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException
	 */
	public void visit(ASTParamType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException
	 */
	public void visit(ASTArrayType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException
	 */
	public void visit(ASTAlgebraicType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTElseBlock node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTEqualExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTEqualOp node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTFormalParam node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTFormalParamList node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTForStatement node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTFunc node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTFuncBlock node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTGF node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTEC node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTECPoint node) throws FFaplException;
	
	public void visit(ASTECPAI node) throws FFaplException;


	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTIdTerm node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTIfStatement node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTLiteral node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTMulExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTMulOp node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTPolynomial node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTPowExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTPrimaryExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTPrimitiveType node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTProc node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTProcFuncCall node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTProgram node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTPsRandomGenerator node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTRandom node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTRandomGenerator node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTRecord node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTRelExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTRelOp node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTReturnStatement node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTBreak node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTSameAs node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTSelector node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTStatement node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTBlockStatement node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTStatementList node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTTerm node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTDeclType node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTUnaryExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(ASTWhileStatement node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(FFaplNodeChoice node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(FFaplNodeList node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(FFaplNodeListOpt node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(FFaplNodeOpt node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(FFaplNodeSequence node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException */
	public void visit(FFaplNodeToken node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException
	 */
	public void visit(FFaplNodeType node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @throws FFaplException
	 */
	public void visit(ASTECRandom astecRandom);

	public void visit(ASTECBaseField node);

	public void visit(ASTECAssignment node);

	
}
