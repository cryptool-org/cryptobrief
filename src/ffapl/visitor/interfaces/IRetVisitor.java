package ffapl.visitor.interfaces;

import ffapl.ast.nodes.*;
import ffapl.exception.FFaplException;

public interface IRetVisitor <R>{

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTAddExpr node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTAddOp node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTArgumentList node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTArrayLen node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTAssignment node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTBlock node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTComplexAlgebraicType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTRandomGeneratorType node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTCondAndExpr node) throws FFaplException;
	
	public R visit(ASTCondOrExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTCondition node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTCreationExpr node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTExprComplexAType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTExprRandomGType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTDecl node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTConstDecl node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTConstType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTContainerType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTParamType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTArrayType node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTAlgebraicType node) throws FFaplException;
	
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTElseBlock node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTEqualExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTEqualOp node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTFormalParam node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTFormalParamList node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTForStatement node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTFunc node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTFuncBlock node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTGF node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTEC node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTECPoint node) throws FFaplException;
	
	public R visit(ASTECPAI node) throws FFaplException;


	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTIdTerm node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTIfStatement node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTLiteral node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTMulExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTMulOp node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTPolynomial node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTPowExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTPrimaryExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTPrimitiveType node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTProc node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTProcFuncCall node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTProgram node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTPsRandomGenerator node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTRandom node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTRandomGenerator node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTRecord node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTRelExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTRelOp node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTReturnStatement node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTBreak node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	* @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTSameAs node) throws FFaplException;

	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	* @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTECBaseField node) throws FFaplException;

	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	* @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTECAssignment node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	* @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTSelector node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTStatement node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTBlockStatement node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTStatementList node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTTerm node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTDeclType node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTUnaryExpr node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(ASTWhileStatement node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(FFaplNodeChoice node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(FFaplNodeList node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(FFaplNodeListOpt node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(FFaplNodeOpt node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(FFaplNodeSequence node) throws FFaplException;

	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @return value of type <R>
	 * @throws FFaplException */
	public R visit(FFaplNodeToken node) throws FFaplException;
	
	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argumen
	 * @return value of type <R>
	 * @throws FFaplException
	 */
	public R visit(FFaplNodeType node) throws FFaplException;


	/**
	 * Visit Method for the specified input node type
	 * @param node
	 * @param argumen
	 * @return value of type <R>
	 * @throws FFaplException
	 */
	public R visit(ASTECRandom astecRandom);


	
}
