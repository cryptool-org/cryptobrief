/**
 * 
 */
package ffapl.visitor;

import java.util.Iterator;
import java.util.Vector;

import ffapl.FFaplInterpreterConstants;
import ffapl.ast.nodes.ASTAddExpr;
import ffapl.ast.nodes.ASTAddOp;
import ffapl.ast.nodes.ASTAlgebraicType;
import ffapl.ast.nodes.ASTArgumentList;
import ffapl.ast.nodes.ASTArrayLen;
import ffapl.ast.nodes.ASTArrayType;
import ffapl.ast.nodes.ASTAssignment;
import ffapl.ast.nodes.ASTBlock;
import ffapl.ast.nodes.ASTBlockStatement;
import ffapl.ast.nodes.ASTBreak;
import ffapl.ast.nodes.ASTComplexAlgebraicType;
import ffapl.ast.nodes.ASTCondAndExpr;
import ffapl.ast.nodes.ASTCondOrExpr;
import ffapl.ast.nodes.ASTCondition;
import ffapl.ast.nodes.ASTConstDecl;
import ffapl.ast.nodes.ASTConstType;
import ffapl.ast.nodes.ASTContainerType;
import ffapl.ast.nodes.ASTCreationExpr;
import ffapl.ast.nodes.ASTDecl;
import ffapl.ast.nodes.ASTDeclType;
import ffapl.ast.nodes.ASTEC;
import ffapl.ast.nodes.ASTECAssignment;
import ffapl.ast.nodes.ASTECBaseField;
import ffapl.ast.nodes.ASTECPAI;
import ffapl.ast.nodes.ASTECPoint;
import ffapl.ast.nodes.ASTECRandom;
import ffapl.ast.nodes.ASTElseBlock;
import ffapl.ast.nodes.ASTEqualExpr;
import ffapl.ast.nodes.ASTEqualOp;
import ffapl.ast.nodes.ASTExpr;
import ffapl.ast.nodes.ASTExprComplexAType;
import ffapl.ast.nodes.ASTExprRandomGType;
import ffapl.ast.nodes.ASTForStatement;
import ffapl.ast.nodes.ASTFormalParam;
import ffapl.ast.nodes.ASTFormalParamList;
import ffapl.ast.nodes.ASTFunc;
import ffapl.ast.nodes.ASTFuncBlock;
import ffapl.ast.nodes.ASTGF;
import ffapl.ast.nodes.ASTIdTerm;
import ffapl.ast.nodes.ASTIfStatement;
import ffapl.ast.nodes.ASTLiteral;
import ffapl.ast.nodes.ASTMulExpr;
import ffapl.ast.nodes.ASTMulOp;
import ffapl.ast.nodes.ASTParamType;
import ffapl.ast.nodes.ASTPolynomial;
import ffapl.ast.nodes.ASTPowExpr;
import ffapl.ast.nodes.ASTPrimaryExpr;
import ffapl.ast.nodes.ASTPrimitiveType;
import ffapl.ast.nodes.ASTProc;
import ffapl.ast.nodes.ASTProcFuncCall;
import ffapl.ast.nodes.ASTProgram;
import ffapl.ast.nodes.ASTPsRandomGenerator;
import ffapl.ast.nodes.ASTRandom;
import ffapl.ast.nodes.ASTRandomGenerator;
import ffapl.ast.nodes.ASTRandomGeneratorType;
import ffapl.ast.nodes.ASTRecord;
import ffapl.ast.nodes.ASTRelExpr;
import ffapl.ast.nodes.ASTRelOp;
import ffapl.ast.nodes.ASTReturnStatement;
import ffapl.ast.nodes.ASTSameAs;
import ffapl.ast.nodes.ASTSelector;
import ffapl.ast.nodes.ASTStatement;
import ffapl.ast.nodes.ASTStatementList;
import ffapl.ast.nodes.ASTTerm;
import ffapl.ast.nodes.ASTUnaryExpr;
import ffapl.ast.nodes.ASTWhileStatement;
import ffapl.ast.nodes.FFaplNodeChoice;
import ffapl.ast.nodes.FFaplNodeList;
import ffapl.ast.nodes.FFaplNodeListOpt;
import ffapl.ast.nodes.FFaplNodeOpt;
import ffapl.ast.nodes.FFaplNodeSequence;
import ffapl.ast.nodes.FFaplNodeToken;
import ffapl.ast.nodes.FFaplNodeType;
import ffapl.ast.nodes.interfaces.INode;
import ffapl.exception.FFaplException;
import ffapl.java.logging.FFaplLogger;
import ffapl.lib.FFaplAttribute;
import ffapl.lib.FFaplAttributeOperation;
import ffapl.lib.FFaplPredefinedProcFuncDeclaration;
import ffapl.lib.FFaplProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IAttribute;
import ffapl.lib.interfaces.IAttributeOperation;
import ffapl.lib.interfaces.ICompilerError;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.lib.interfaces.IToken;
import ffapl.types.FFaplArray;
import ffapl.types.FFaplEllipticCurve;
import ffapl.types.FFaplGaloisField;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplPsRandomGenerator;
import ffapl.types.FFaplRandomGenerator;
import ffapl.types.FFaplRecord;
import ffapl.types.FFaplResidueClass;
import ffapl.types.FFaplTypeConversation;
import ffapl.types.FFaplTypeCrossTable;
import ffapl.types.Type;
import ffapl.visitor.interfaces.IRetArgVisitor;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplSymbolTypeCheckingVisitor implements IRetArgVisitor<Vector<IAttribute>, Object> {

	private ISymbolTable _symbolTable;
	private IAttributeOperation _attribOperation;
	private FFaplLogger _logger;
	private Thread _thread;
	
	public FFaplSymbolTypeCheckingVisitor( ISymbolTable symbolTable, FFaplLogger logger, Thread thread) {
		_symbolTable = symbolTable;
		_logger = logger;
		_thread = thread;
		_attribOperation = new FFaplAttributeOperation(_logger);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTAddExpr)
	 */
	@Override
	public Vector<IAttribute> visit(ASTAddExpr node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;
		IAttribute a1;
		IToken t1;
		FFaplNodeSequence s1;
		node.setSymbolScope(_symbolTable.currentScope());
		av1 = node._node1.accept(this, symbol);
		
		if(node._node2.ispresent()){
			
			for(Iterator<INode> itr = node._node2.iterator(); itr.hasNext();){
				s1 = (FFaplNodeSequence) itr.next();
				t1 = ((FFaplNodeToken)((ASTAddOp)s1.elementAt(0))._node1.getNode()).getToken();
				av2 = s1.elementAt(1).accept(this, symbol);
				a1 = this._attribOperation.op2(av1.firstElement(), t1, av2.firstElement());
				av1 = new Vector<IAttribute>(0,1);
				av1.add(a1);
			}	
		}
		return av1;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTAddOp)
	 */
	@Override
	public Vector<IAttribute> visit(ASTAddOp node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTArgumentList)
	 */
	@Override
	public Vector<IAttribute> visit(ASTArgumentList node, Object symbol) throws FFaplException{	
		Vector<IAttribute> attribv = new Vector<IAttribute>(0,1);
		node.setSymbolScope(_symbolTable.currentScope());
		appendElement(attribv, node._node1.accept(this, symbol));
		appendElement(attribv, node._node2.accept(this, symbol));
		return attribv;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTArrayLen)
	 */
	@Override
	public Vector<IAttribute> visit(ASTArrayLen node, Object symbol) throws FFaplException{
		ISymbol s1, found;
		Vector<IAttribute> attribv;
		IAttribute a1;
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		node._node2.accept(this, symbol);
		//node._node3.accept(this, symbol);
		
		
		s1 = new FFaplSymbol(node._node2.getToken().toString(), node._node2.getToken(), ISymbol.VARIABLE);
		s1.setReference(true);
		found = _symbolTable.lookup(s1);
		//if(found.getType() instanceof FFaplArray){
		//	System.out.println(found.hashCode());
		//System.out.println(found + " - " + found.getType() + " - " + 
		//		((FFaplArray)found.getType()).getDim() + " - " + found.isInitialized());
		//}
		if(found == null){
			Object[] arguments = {"'" + node._node2.getToken() + "'"};
			throw new FFaplException(arguments, ICompilerError.IDENT_NOT_DECL, node._node2.getToken());
		}else if(!found.isInitialized()){
			Object[] arguments = {"'" + node._node2.getToken() + "'"};
			throw new FFaplException(arguments, ICompilerError.SYMBOL_NOT_INIT, node._node2.getToken());
		}else{
			attribv = node._node3.accept(this, found);
			if(node._node3.ispresent()){
				//selector
			}else{
				attribv = new Vector<IAttribute>(0,1);
				attribv.add(new FFaplAttribute(IAttribute.ADDRESS, found.getType()));
			}
		}
		a1 = this._attribOperation.arraylen(node._node1.getToken(), attribv.firstElement());
		
		attribv.clear();
		attribv.add(a1);
		
		
		return attribv;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTAssignment)
	 */
	@Override
	public Vector<IAttribute> visit(ASTAssignment node, Object symbol) throws FFaplException{
		FFaplNodeToken t1;
		//FFaplNodeOpt t2;
		Vector<IAttribute> av1, av2;
		ISymbol variable, found;
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		try{
			t1 = node._node1;
			//t2 = node._node2;
			variable = new FFaplSymbol(t1.getToken().toString(), t1.getToken(), ISymbol.VARIABLE);
			variable.setReference(true);
			found = _symbolTable.lookup(variable);
			if(found == null){
				Object[] arguments = {"'" + t1.getToken() + "'"};
				throw new FFaplException(arguments, ICompilerError.IDENT_NOT_DECL, t1.getToken());
			}else if(found.readonly() && !(found.getType() instanceof FFaplRecord)){
				Object[] arguments = {"'" + t1.getToken() + "'"};
				throw new FFaplException(arguments, ICompilerError.READONLY_ASSIGN, t1.getToken());
			}
			//System.out.println(found + " - " + found.getType() + " - " + found.isInitialized());
			//System.out.println(found.hashCode());
			
			av1 = node._node2.accept(this, found);
			//if(av1 != null){
			//	System.out.println(found.getName() + " - " + found.getType() + " -- " + av1.firstElement().getType());
			//}
			node._node3.accept(this, symbol);
			if(av1 != null){
				av2 = node._node4.accept(this, av1.firstElement().getType());
			}else{
				av2 = node._node4.accept(this, found.getType());
			}
			
			if(node._node2.ispresent()){
				if(!found.isInitialized()){
					Object[] arguments = {"'" + t1.getToken() + "'"};
					throw new FFaplException(arguments, ICompilerError.SYMBOL_NOT_INIT, t1.getToken());
				}
				this._attribOperation.assignment(av1.firstElement(), node._node3.getToken(), av2.firstElement());
			}else{
				//�berarbeiten
				this._attribOperation.assignment(new FFaplAttribute(IAttribute.REGISTER,found.getType()), 
						 						node._node3.getToken(), av2.firstElement());
			}
			found.setInitialized(true);
			//if(node._node4._node1.getPos() == 2){//creation Expression
			
			//}
		}catch(FFaplException fe){
			fe.addToken(node._node3.getToken());
			throw fe;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTBlock)
	 */
	@Override
	public Vector<IAttribute> visit(ASTBlock node, Object symbol) throws FFaplException{
		ISymbol block;
		node.setSymbolScope(_symbolTable.currentScope());
		block = new FFaplSymbol("_FuncBlock",
                ISymbol.BLOCK);
		_symbolTable.openScope(block, false);
			node._node1.accept(this, symbol);
			node._node2.accept(this, symbol);
			node._node3.accept(this, symbol);
			node._node4.accept(this, symbol);
		_symbolTable.closeScope();
		return null;
		
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCompType)
	 */
	@Override
	public Vector<IAttribute> visit(ASTComplexAlgebraicType node, Object symbol) throws FFaplException{
		
		FFaplNodeOpt opt;
		node.setSymbolScope(_symbolTable.currentScope());
		
		switch(node._node1.getPos()){
		case 1: //ResidueClass
		
			opt = (FFaplNodeOpt) ((FFaplNodeSequence)((FFaplNodeType) node._node1.getNode()).getNode()).elementAt(3);
			if(opt.ispresent()){
				if(!((FFaplNodeToken)((FFaplNodeSequence) opt.getNode()).elementAt(1)).getToken().toString().equals("x")){
					Object[] arguments = {"'" + ((FFaplNodeToken)((FFaplNodeSequence)opt.getNode()).elementAt(1)).getToken().toString() + "'"};
					throw new FFaplException(arguments, ICompilerError.INVALID_INDETERMINATE, ((FFaplNodeToken)((FFaplNodeSequence)opt.getNode()).elementAt(1)).getToken());
				}
			}
		
		}
		return node._node1.accept(this, symbol);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCondAndExpr)
	 */
	@Override
	public Vector<IAttribute> visit(ASTCondAndExpr node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;
		IAttribute a1;
		IToken t1;
		FFaplNodeSequence s1;
		node.setSymbolScope(_symbolTable.currentScope());
		av1 = node._node1.accept(this, symbol);
		
		if(node._node2.ispresent()){
			for(Iterator<INode> itr = node._node2.iterator(); itr.hasNext();){
				s1 = (FFaplNodeSequence) itr.next();
				t1 = ((FFaplNodeToken)s1.elementAt(0)).getToken();
				av2 = s1.elementAt(1).accept(this, symbol);
				a1 = this._attribOperation.op2boolean(av1.firstElement(), t1, av2.firstElement());
				av1 = new Vector<IAttribute>(0,1);
				av1.add(a1);
			}	
		}
		return av1;
	}
	
	
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCondOrExpr)
	 */
	@Override
	public Vector<IAttribute> visit(ASTCondOrExpr node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;
		IAttribute a1;
		IToken t1;
		FFaplNodeSequence s1;
		node.setSymbolScope(_symbolTable.currentScope());
		av1 = node._node1.accept(this, symbol);
		
		if(node._node2.ispresent()){
			for(Iterator<INode> itr = node._node2.iterator(); itr.hasNext();){
				s1 = (FFaplNodeSequence) itr.next();
				t1 = ((FFaplNodeToken)s1.elementAt(0)).getToken();
				av2 = s1.elementAt(1).accept(this, symbol);
				a1 = this._attribOperation.op2boolean(av1.firstElement(), t1, av2.firstElement());
				av1 = new Vector<IAttribute>(0,1);
				av1.add(a1);
			}	
		}
		return av1;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCondition)
	 */
	@Override
	public Vector<IAttribute> visit(ASTCondition node, Object symbol) throws FFaplException{
		Vector<IAttribute> a1;
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		a1 = node._node2.accept(this, symbol);
		node._node3.accept(this, symbol);
		if (a1.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLBOOLEAN){
			Object[] arguments = { "'" + a1.firstElement().getType() + "'",
								   "'" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLBOOLEAN] + "'" };
			throw new FFaplException(arguments, ICompilerError.COND_NOT_BOOL, node._node1.getToken());
		}
		return a1;
	}

	@Override
	public Vector<IAttribute> visit(ASTConstDecl node, Object symbol) throws FFaplException{
		ISymbol constant;
		Vector<IAttribute> av1, av2;
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		node._node2.accept(this, symbol);
		node._node3.accept(this, symbol);
		av1 = node._node4.accept(this, symbol);
		node._node5.accept(this, symbol);
		if(av1 != null){
			av2 = node._node6.accept(this, av1.firstElement().getType());
		}else{
			av2 = node._node6.accept(this, null);
		}
		node._node7.accept(this, symbol);
		constant = new FFaplSymbol(node._node2.getToken().toString(), 
                node._node2.getToken(),
                av1.firstElement().getType(),
                ISymbol.CONSTANT);
		//TODO readonly 
		constant.setReadonly(true);
		
		_symbolTable.addSymbol(constant);
		//TODO �berarbeiten Adresse Attribute
		this._attribOperation.assignment(av1.firstElement(), node._node5.getToken(), av2.firstElement());
		constant.setInitialized(true);
		return null;
	}
	
	@Override
	public Vector<IAttribute> visit(ASTConstType node, Object symbol)
			throws FFaplException {
		node.setSymbolScope(_symbolTable.currentScope());
		return node._node1.accept(this, symbol);
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCreationExpr)
	 */
	@Override
	public Vector<IAttribute> visit(ASTCreationExpr node, Object symbol)
			throws FFaplException {
		Vector<IAttribute> av1, av2;
		IAttribute a1;
		int i = 1;
		symbol = null;
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		av1 = node._node2.accept(this, symbol);
		
		av2 = node._node3.accept(this, symbol);
		//Check if expression are integer or prime
		for(Iterator<IAttribute> itr = av2.iterator(); itr.hasNext(); ){
			a1 = itr.next();
			if(!(a1.getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER ||
					a1.getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME)){
				    Object[] arguments = {"'" + a1.getType() + "'", i, 
				    					  FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLARRAY],
				    					  "[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
				    						FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"};
					throw new FFaplException(arguments, ICompilerError.ILLEGAL_TYPE_ARGUMENT, node._node1.getToken());
			}
			i++;
	    }

		if(node._node3.size() > 0){
			av1.add( new FFaplAttribute(IAttribute.TYPE, 
					new FFaplArray(av1.firstElement().getType(), node._node3.size())));
			av1.remove(0);//must be removed, simulates replace
			node._arrayType = (FFaplArray) av1.firstElement().getType();
			return av1;
		}else{
			Object[] arguments = {"CreationExpr"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}			
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTCreationExprComp)
	 */
	@Override
	public Vector<IAttribute> visit(ASTExprComplexAType node, Object symbol)
			throws FFaplException {
		Vector<IAttribute> av1;
		FFaplNodeToken t1;
		FFaplNodeType tp1;
		FFaplNodeOpt opt;
		node.setSymbolScope(_symbolTable.currentScope());
		av1 = node._node1.accept(this, symbol);
		switch(node._node1.getPos()){
		case 1: //Residualclass
		    //checks if argument is integer or prime
			tp1 = (FFaplNodeType) node._node1.getNode();
			t1 = (FFaplNodeToken)((FFaplNodeSequence) tp1.getNode()).elementAt(0);
			if (! ( av1.elementAt(1).getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER || 
					av1.elementAt(1).getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME ) ){
				Object[] arguments = {"'" + av1.elementAt(1).getType() + "'", 1, 
  					  tp1.getType(),
  					"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
					FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"};
				throw new FFaplException(arguments, ICompilerError.ILLEGAL_TYPE_ARGUMENT, t1.getToken());
			}
			opt = (FFaplNodeOpt) ((FFaplNodeSequence) tp1.getNode()).elementAt(4);
			if(opt.ispresent()){
				if(!((FFaplNodeToken)((FFaplNodeSequence)opt.getNode()).elementAt(1)).getToken().toString().equals("x")){
					//not x in Z(..)[x]
						Object[] arguments = {"'" + ((FFaplNodeToken)((FFaplNodeSequence)opt.getNode()).elementAt(1)).getToken().toString() + "'"};
						throw new FFaplException(arguments, ICompilerError.INVALID_INDETERMINATE, ((FFaplNodeToken)((FFaplNodeSequence)opt.getNode()).elementAt(1)).getToken());
				}
			}
			break;
		}
		return av1;
	}
	

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTDecl)
	 */
	@Override
	public Vector<IAttribute> visit(ASTDecl node, Object symbol) throws FFaplException{
		Vector<IAttribute> attrib;
		FFaplNodeToken ntk;
		Vector<ISymbol> symbolV = new Vector<ISymbol>();
		ISymbol s1, s2;
		node.setSymbolScope(_symbolTable.currentScope());
		//node1
		node._node1.accept(this, symbol);
		//add Symbol to Symboltable
		s1 = new FFaplSymbol(node._node1.getToken().toString(), 
                node._node1.getToken(),
                ISymbol.VARIABLE);
		symbolV.add(s1);
		_symbolTable.addSymbol(s1);
		
		
		//examine Node 2
		node._node2.accept(this, symbol);
				
		//examine Node 3
		node._node3.accept(this, symbol);
	
		attrib = node._node4.accept(this, symbol);
		//add Symbols to Symboltable
		for (final Iterator<INode> e = node._node2.iterator(); e.hasNext();) {
		      ntk = (FFaplNodeToken)((FFaplNodeSequence) e.next()).elementAt(1);
		      s1 = new FFaplSymbol(ntk.getToken().toString(), 
	                    ntk.getToken(),
	                    ISymbol.VARIABLE);
		      symbolV.add(s1);
		      _symbolTable.addSymbol(s1);
		      s1.setShielded(true);
		      s1.setLocal(
		    		  _symbolTable.cloneSubTree(symbolV.firstElement().local(), s1)
		    		  );
		}
		
		node._node5.accept(this, symbol);
		
		
		
		//set type
		for (final Iterator<ISymbol> e = symbolV.iterator(); e.hasNext();) { 
			s2 = e.next();
			s2.setType(attrib.firstElement().getType().clone());
			//record is initialized by creation
			if(s2.getType() instanceof FFaplRecord ||
					s2.getType() instanceof FFaplPsRandomGenerator ||
					s2.getType() instanceof FFaplRandomGenerator){
				   s2.setInitialized(true);
				 //records cannot be override, while elements of records can be overwritten
					s2.setReadonly(true);
			}		    
		   
		}		
	
		return null;
		
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTElseBlock)
	 */
	@Override
	public Vector<IAttribute> visit(ASTElseBlock node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTEqualExpr)
	 */
	@Override
	public Vector<IAttribute> visit(ASTEqualExpr node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;
		IAttribute a1;
		IToken t1;
		//TODO �berarbeiten
		node.setSymbolScope(_symbolTable.currentScope());
		av1 = node._node1.accept(this, symbol);
		//node._node2.accept(this, symbol);
		if (node._node2.ispresent()){
			t1 = ((FFaplNodeToken)
					((ASTEqualOp)(
						(FFaplNodeSequence)node
							._node2.getNode())
								.elementAt(0))._node1.getNode())
								    .getToken();
			av2 = ((FFaplNodeSequence)node
						._node2.getNode())
							.elementAt(1).accept(this,symbol);
			a1 = this._attribOperation.equalop(av1.firstElement(), t1, av2.firstElement());
			av1 = new Vector<IAttribute>(0,1);
			av1.add(a1);
		}
		return av1;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTEqualOp)
	 */
	@Override
	public Vector<IAttribute> visit(ASTEqualOp node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTExpr)
	 */
	@Override
	public Vector<IAttribute> visit(ASTExpr node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1 = null, av2;
		IAttribute a1;
		IToken t1;
		FFaplNodeSequence s1;
		node.setSymbolScope(_symbolTable.currentScope());
		FFaplNodeSequence n1;
		FFaplNodeListOpt n2;
		
		switch (node._node1.getPos()){
		case 0:
			n1 = (FFaplNodeSequence)(node._node1.getNode());
			av1 = n1.elementAt(0).accept(this, symbol);
			n2 = (FFaplNodeListOpt)n1.elementAt(1);
			if(n2.ispresent()){
				for(Iterator<INode> itr = n2.iterator(); itr.hasNext();){
					s1 = (FFaplNodeSequence) itr.next();
					t1 = ((FFaplNodeToken)s1.elementAt(0)).getToken();
					av2 = s1.elementAt(1).accept(this, symbol);
					a1 = this._attribOperation.op2boolean(av1.firstElement(), t1, av2.firstElement());
					av1 = new Vector<IAttribute>(0,1);
					av1.add(a1);
				}	
			}
			break;
		case 1:
			av1 = node._node1.accept(this, symbol);
		}
		return av1;
		
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTFormalParam)
	 */
	@Override
	public Vector<IAttribute> visit(ASTFormalParam node, Object symbol) throws FFaplException{
		Vector<IAttribute> attrib;
		FFaplNodeToken ntk;
		node.setSymbolScope(_symbolTable.currentScope());
		
		node._node1.accept(this, symbol);
		node._node2.accept(this, symbol);
		node._node3.accept(this, symbol);
		attrib = node._node4.accept(this, symbol);
		//RECORD not allowed
		/*if (attrib.firstElement().getType() instanceof FFaplRecord){
			Object[] arguments = {attrib.firstElement().getType()};
			throw new FFaplException(arguments,
					ICompilerError.ILLEGAL_FORMAL_PARAM_TYPE, node._node3.getToken());
		}*/
		//
		_symbolTable.addSymbol(new FFaplSymbol(node._node1.getToken().toString(), 
                node._node1.getToken(),
                attrib.firstElement().getType().clone(),
                ISymbol.PARAMETER));
		for (final Iterator<INode> e = node._node2.iterator(); e.hasNext();) {
			ntk = (FFaplNodeToken)((FFaplNodeSequence) e.next()).elementAt(1);
			_symbolTable.addSymbol(new FFaplSymbol(ntk.getToken().toString(), 
					ntk.getToken(),
					attrib.firstElement().getType().clone(),
					ISymbol.PARAMETER));
		}		
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTFormalParamList)
	 */
	@Override
	public Vector<IAttribute> visit(ASTFormalParamList node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		node._node2.accept(this, symbol);
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTForStatement)
	 */
	@Override
	public Vector<IAttribute> visit(ASTForStatement node, Object symbol) throws FFaplException{
		ISymbol s1, block;
		Vector<IAttribute> a1, a2, a3;
		IToken tk;
		block = new FFaplSymbol("_ForStatement",
                ISymbol.BLOCK);
		_symbolTable.openScope(block, false);
		node.setSymbolScope(_symbolTable.currentScope());
			node._node1.accept(this, symbol);
			node._node2.accept(this, symbol);
			
			s1 = new FFaplSymbol(node._node2.getToken().toString(), 
	                node._node2.getToken(),
	                ISymbol.VARIABLE);
			s1.setType(new FFaplInteger());
			s1.setInitialized(true);
			_symbolTable.addSymbol(s1);
			
			
			node._node3.accept(this, symbol);
			a1 = node._node4.accept(this, symbol);
			if(! FFaplTypeCrossTable.FORSTATEMENT_compatibility[ s1.getType().typeID() ][ a1.firstElement().getType().typeID() ]){
				Object[] arguments = {"'" + node._node3.getToken() + "'",
										a1.firstElement().getType(), 
										FFaplTypeConversation.getExpected(s1.getType().typeID(), FFaplTypeCrossTable.FORSTATEMENT_compatibility)};
				throw new FFaplException(arguments,
						ICompilerError.TYPE_MISMATCH_FORSTATEMENT, node._node3.getToken());
			}
			node._node5.accept(this, symbol);
			a2 = node._node6.accept(this, symbol);
			if(! FFaplTypeCrossTable.FORSTATEMENT_compatibility[ s1.getType().typeID() ][ a2.firstElement().getType().typeID() ]){
				Object[] arguments = {"'" + node._node5.getToken() + "'",
						a1.firstElement().getType(), 
						FFaplTypeConversation.getExpected(s1.getType().typeID(), FFaplTypeCrossTable.FORSTATEMENT_compatibility)};
				throw new FFaplException(arguments,
							ICompilerError.TYPE_MISMATCH_FORSTATEMENT, node._node5.getToken());
			}
			a3 = node._node7.accept(this, symbol);
			if(node._node7.ispresent()){
					if(! FFaplTypeCrossTable.FORSTATEMENT_compatibility[ s1.getType().typeID() ][ a3.firstElement().getType().typeID() ]){
						tk = ((FFaplNodeToken)((FFaplNodeSequence)node._node7.getNode()).elementAt(0)).getToken();
						
						Object[] arguments = {"'" + tk + "'",
								a1.firstElement().getType(), 
								FFaplTypeConversation.getExpected(s1.getType().typeID(), FFaplTypeCrossTable.FORSTATEMENT_compatibility)};
						throw new FFaplException(arguments,
									ICompilerError.TYPE_MISMATCH_FORSTATEMENT, tk);
					}
			}
			
			
			
			node._node8.accept(this, symbol);
		_symbolTable.closeScope();
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTFunc)
	 */
	@Override
	public Vector<IAttribute> visit(ASTFunc node, Object symbol) throws FFaplException{
		ISymbol function;
		Vector<IAttribute> attrib;
		    node.setSymbolScope(_symbolTable.currentScope());
		    node._node1.accept(this, symbol);
			node._node2.accept(this, symbol);
			function = new FFaplProcFuncSymbol(node._node2.getToken().toString(), 
                    node._node2.getToken(),
                    null,
                    ISymbol.FUNCTION, node);
			_symbolTable.addSymbol(function);//adds function to symbol table
		_symbolTable.openScope(false);//for parameter and Block
			node._node3.accept(this, symbol);
			node._node4.accept(this, symbol);
			node._node5.accept(this, symbol);
			node._node6.accept(this, symbol);
			attrib = node._node7.accept(this, symbol);
			function.setType(attrib.firstElement().getType());// sets the Type of the function
			node._node8.accept(this, function);
		_symbolTable.closeScope();
		return attrib;//TODO �berarbeiten
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTFuncBlock)
	 */
	@Override
	public Vector<IAttribute> visit(ASTFuncBlock node, Object symbol) throws FFaplException{
		ISymbol block;
		Vector<IAttribute> a1;
		node.setSymbolScope(_symbolTable.currentScope());
		block = new FFaplSymbol("_FuncBlock",
                ISymbol.BLOCK);
		_symbolTable.openScope(block, false);
			node._node1.accept(this, symbol);
			node._node2.accept(this, symbol);
			node._node3.accept(this, symbol);
			a1 = node._node4.accept(this, symbol);
			node._node5.accept(this, symbol);
		_symbolTable.closeScope();
		return a1;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTGF)
	 */
	@Override
	public Vector<IAttribute> visit(ASTGF node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		node._node2.accept(this, symbol);
		av1 = node._node3.accept(this, symbol);
		if(! (av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER ||
				av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME)){
			Object[] arguments = {av1.firstElement().getType(), 1, 
					FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLGF], 
					"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
					FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"};
			throw new FFaplException(arguments,
					ICompilerError.ILLEGAL_TYPE_ARGUMENT, node._node2.getToken());
		}
		node._node4.accept(this, symbol);
		av2 = node._node5.accept(this, symbol);
		if(! (av2.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPOLYNOMIAL) ){
			Object[] arguments = {av2.firstElement().getType(), 2, 
					FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLGF], 
					"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPOLYNOMIAL] + "]"};
			throw new FFaplException(arguments,
					ICompilerError.ILLEGAL_TYPE_ARGUMENT, node._node2.getToken());
		}
		
		node._node6.accept(this, symbol);
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTEC)
	 */
	@Override
	public Vector<IAttribute> visit(ASTEC node, Object symbol) throws FFaplException{

		
		Vector<IAttribute> av1, av2;
		
		
		node.setSymbolScope(_symbolTable.currentScope());

		node._node1.accept(this, symbol); //EC
		node._node2.accept(this, symbol); //Klammer auf
		av1 = node._node3.accept(this, symbol); //GF oder Z()
		//av1 = node._node4.accept(this, symbol); 
		
		
		
		IAttribute a1;
		IToken t1;
		FFaplNodeSequence s1;
		node.setSymbolScope(_symbolTable.currentScope());
	
		if(node._node4.ispresent()){
			
			for(Iterator<INode> itr = node._node4.iterator(); itr.hasNext();){
				s1 = (FFaplNodeSequence) itr.next();
				t1 = ((FFaplNodeToken)s1.elementAt(1)).getToken();
				av2 = s1.elementAt(3).accept(this, symbol);
				
				
				if (!t1.toString().equals("a1") && !t1.toString().equals("a2") && !t1.toString().equals("a3") && !t1.toString().equals("a4") && !t1.toString().equals("a6"))
				{
					Object[] arguments = {"'" + t1.toString() + "'"};
					throw new FFaplException(arguments, ICompilerError.WRONG_EC_PARAMETER, t1); //Text anpassen
				}
				
				
				if (node._node3 instanceof ASTExpr && av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLINTEGER && av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPRIME)
				{
					Object[] arguments = {"'" + t1.toString() + "'"};
					throw new FFaplException(arguments, ICompilerError.WRONG_EC_PARAMETER_TYPE, t1); //Text anpassen
				}
				
				if (node._node3 instanceof ASTGF && av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPOLYNOMIAL && av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE)
				{
					Object[] arguments = {"'" + t1.toString() + "'"};
					throw new FFaplException(arguments, ICompilerError.WRONG_EC_PARAMETER_TYPE, t1); //Text anpassen
				}
				
			}	
		}
		
		node._node5.accept(this, symbol); //Klammer zu

		
		return null;
	}

	
	
	public Vector<IAttribute> visit(ASTECPoint node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;

		node._node1.accept(this, null);
		av1 = node._node2.accept(this, null);
		node._node3.accept(this, null);
		av2 = node._node4.accept(this, null);
		node._node5.accept(this, null);
		
		Object[] arguments = {""};

		if (av1.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLINTEGER && av1.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPRIME && av1.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPOLYNOMIAL && av1.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE && av1.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLGF)
		{
			throw new FFaplException(arguments, ICompilerError.WRONG_EC_COORDINATE, null); 
		}
		if (av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLINTEGER && av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPRIME && av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPOLYNOMIAL && av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE && av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLGF)
		{
			throw new FFaplException(arguments, ICompilerError.WRONG_EC_COORDINATE, null); 
		}
		
		if ((av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER || av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME) && (av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLINTEGER && av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPRIME))
		{
			throw new FFaplException(arguments, ICompilerError.WRONG_EC_COORDINATE, null); 
		}
		if ((av1.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLINTEGER && av1.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPRIME) && (av2.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER || av2.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME))
		{
			throw new FFaplException(arguments, ICompilerError.WRONG_EC_COORDINATE, null);
		}
		
		if ((av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPOLYNOMIAL || av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE && av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLGF) && (av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPOLYNOMIAL && av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE && av2.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLGF))
		{
			throw new FFaplException(arguments, ICompilerError.WRONG_EC_COORDINATE, null); 
		}
		if ((av1.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPOLYNOMIAL && av1.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE && av1.firstElement().getType().typeID() != FFaplTypeCrossTable.FFAPLGF) && (av2.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPOLYNOMIAL || av2.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE && av2.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLGF))
		{
			throw new FFaplException(arguments, ICompilerError.WRONG_EC_COORDINATE, null);
		}
		
		
		/*
		if (node._node2 instanceof ASTExpr && !(node._node4 instanceof ASTExpr))
		{
			
		}
		*/
		
		Vector<IAttribute> av3 = new Vector<IAttribute>();
		av1.add(new FFaplAttribute(IAttribute.REGISTER, new FFaplEllipticCurve()));
		return av3;
	}
	

	public Vector<IAttribute> visit(ASTECPAI node, Object symbol) throws FFaplException
	{
		return null;
	}
	

	public Vector<IAttribute> visit(ASTECRandom node, Object symbol) throws FFaplException
	{
		return null;
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTIdTerm)
	 */
	@Override
	public Vector<IAttribute> visit(ASTIdTerm node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1;
		node.setSymbolScope(_symbolTable.currentScope());
		
		if(node._node1.getToken().toString().equals("x")){
			node._node1.accept(this, symbol);
			if(node._node2.ispresent()){
				av1 = node._node2.accept(this, symbol);
				if( !(av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER ||
						av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME )){
					Object[] arguments = {av1.firstElement().getType(), "'^'",
							"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
							FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"};
						throw new FFaplException(arguments,
								ICompilerError.TYPE_ILLEGAL_USE, node._node1.getToken());
						}
			}
		}else{
			Object[] arguments = {"'" + node._node1.getToken().toString() + "'"};
			throw new FFaplException(arguments, ICompilerError.INVALID_INDETERMINATE, node._node1.getToken());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTIfStatement)
	 */
	@Override
	public Vector<IAttribute> visit(ASTIfStatement node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		//_symbolTable
		node._node1.accept(this, symbol);
		node._node2.accept(this, symbol);
		node._node3.accept(this, symbol);
		node._node4.accept(this, symbol);
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTLiteral)
	 */
	@Override
	public Vector<IAttribute> visit(ASTLiteral node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		return node._node1.accept(this, symbol);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTMulExpr)
	 */
	@Override
	public Vector<IAttribute> visit(ASTMulExpr node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;
		IAttribute a1;
		IToken t1;
		FFaplNodeSequence s1;
		node.setSymbolScope(_symbolTable.currentScope());
		av1 = node._node1.accept(this, symbol);
		
		if(node._node2.ispresent()){
			
			for(Iterator<INode> itr = node._node2.iterator(); itr.hasNext();){
				s1 = (FFaplNodeSequence) itr.next();
				t1 = ((FFaplNodeToken)((ASTMulOp)s1.elementAt(0))._node1.getNode()).getToken();
				av2 = s1.elementAt(1).accept(this, symbol);
				a1 = this._attribOperation.op2(av1.firstElement(), t1, av2.firstElement());
				av1 = new Vector<IAttribute>(0,1);
				av1.add(a1);
			}	
		}
		return av1;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTMulOp)
	 */
	@Override
	public Vector<IAttribute> visit(ASTMulOp node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		return node._node1.accept(this, symbol);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTPolynom)
	 */
	@Override
	public Vector<IAttribute> visit(ASTPolynomial node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		try{
			symbol = null;
		node._node1.accept(this, symbol);
		node._node2.accept(this, symbol);
		node._node3.accept(this, symbol);
		node._node4.accept(this, symbol);
		}catch(FFaplException e){
			if(e.errorLine() < 0){
				//no Token Set
				e.addToken(node._node1.getToken());
			}
			throw e;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTPowExpr)
	 */
	@Override
	public Vector<IAttribute> visit(ASTPowExpr node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;
		IAttribute a1;
		IToken t1;
		FFaplNodeSequence s1;
		node.setSymbolScope(_symbolTable.currentScope());
		av1 = node._node1.accept(this, symbol);
		
		if(node._node2.ispresent()){
			
			for(Iterator<INode> itr = node._node2.iterator(); itr.hasNext();){
				s1 = (FFaplNodeSequence) itr.next();
				t1 = ((FFaplNodeToken)s1.elementAt(0)).getToken();
				av2 = s1.elementAt(1).accept(this, null);//must be null because exponent is another type
				a1 = this._attribOperation.pow(av1.firstElement(), t1, av2.firstElement());
				av1 = new Vector<IAttribute>(0,1);
				av1.add(a1);
			}	
		}
		return av1;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTPrimaryExpr)
	 */
	@Override
	public Vector<IAttribute> visit(ASTPrimaryExpr node, Object symbol) throws FFaplException{
		Vector<IAttribute> attribv;
		//TODO �berarbeiten
		FFaplNodeToken t1;
		FFaplArray arr;
		Type type;
		//FFaplNodeOpt t2;
		ISymbol s1, found;
		node.setSymbolScope(_symbolTable.currentScope());
		
		switch(node._node1.getPos()){
		
		case 2: //means ProcFuncCall
			attribv = node._node1.accept(this, symbol);
			if(attribv.firstElement().getType() == null){
				t1 = ((ASTProcFuncCall)node._node1.getNode())._node1;
				Object[] arguments = {t1.toString()};
				throw new FFaplException(arguments, 
						ICompilerError.PROC_NOT_FUNC_EXPR, t1.getToken());
			}
			break;
		
		case 3: //means Variable
			if (!(node._node1.getNode() instanceof FFaplNodeSequence)){// Control if node sequence
				Object[] arguments = {"PrimaryExpr"};
				throw new FFaplException(arguments, ICompilerError.INTERNAL);
			}
			t1 = (FFaplNodeToken) ((FFaplNodeSequence)node._node1.getNode()).elementAt(0);
			//t2 = (FFaplNodeOpt) ((FFaplNodeSequence)node._node1.getNode()).elementAt(1);
			s1 = new FFaplSymbol(t1.getToken().toString(), t1.getToken(), ISymbol.VARIABLE);
			s1.setReference(true);
			found = _symbolTable.lookup(s1);
			//if(found.getType() instanceof FFaplArray){
			//	System.out.println(found.hashCode());
			//System.out.println(found + " - " + found.getType() + " - " + 
			//		((FFaplArray)found.getType()).getDim() + " - " + found.isInitialized());
			//}
			if(found == null){
				Object[] arguments = {"'" + t1.getToken() + "'"};
				throw new FFaplException(arguments, ICompilerError.IDENT_NOT_DECL, t1.getToken());
			}else if(!found.isInitialized()){
				Object[] arguments = {"'" + t1.getToken() + "'"};
				throw new FFaplException(arguments, ICompilerError.SYMBOL_NOT_INIT, t1.getToken());
			}else{
				attribv = node._node1.accept(this, found);
				if(((FFaplNodeOpt)
						((FFaplNodeSequence)node._node1.getNode()).elementAt(1)).ispresent()){
					//System.out.println(attribv.firstElement().getType());
				}else{
					attribv.clear();
					attribv.add(new FFaplAttribute(IAttribute.ADDRESS, found.getType()));
				}
			}
			break;
		
		case 5: //argument List
			try{
				if(symbol != null && symbol instanceof FFaplArray){   
					System.out.println(((FFaplArray) symbol).subarray(1));
					attribv = node._node1.accept(this, ((FFaplArray) symbol).subarray(1));
					attribv = FFaplTypeConversation.castTo(attribv, ((FFaplArray) symbol).subarray(1));
					type = attribv.firstElement().getType();
					int len = -1;
					if(type.typeID() == FFaplTypeCrossTable.FFAPLARRAY){
						len = ((FFaplArray) type).getLength();
						
						for(int i = 1; i < attribv.size(); i++){
							
							arr = (FFaplArray) attribv.elementAt(i).getType();
							System.out.println(len + " here " + arr.getLength());
							if(len != arr.getLength()){
								Object[] arguments = {len, arr.getLength()};
								throw new FFaplException(arguments, ICompilerError.INVALID_SUBARRAY_LENGTH);
							}
						}						
						arr = new FFaplArray(((FFaplArray)type).baseType(), ((FFaplArray)type).getDim() +1);
						arr.setLength(attribv.size());
					}else{
						System.out.println(type);
						//TODO: ARRAY initialsierung beachten
						arr = new FFaplArray(type, 1);
						arr.setLength(attribv.size());
					}
					
				}else{
					Object[] arguments = {""};
					throw new FFaplException(arguments, ICompilerError.ARGUMENTLIST_NOT_ALLOWED); 
				}
				attribv.clear();
				attribv.add(new FFaplAttribute(IAttribute.REGISTER, arr));
				//System.out.println(attribv);
			}catch(FFaplException fe){
				fe.addToken(
						((FFaplNodeToken)
								((FFaplNodeSequence)node._node1.getNode()).elementAt(0)).getToken());
				throw fe;
			}
			break;
			
		default:
			attribv = node._node1.accept(this, symbol);
		}
		
		if(symbol != null && symbol instanceof Type){
			if(((Type)symbol).typeID() != FFaplTypeCrossTable.FFAPLARRAY){
				if(((Type)symbol).typeID() == FFaplTypeCrossTable.FFAPLEC && (attribv.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER || attribv.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME) ){
					
				}
				else{
					attribv = FFaplTypeConversation.castToMax(attribv, (Type) symbol);
				}
			}
		}
		
		return attribv;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTPrimType)
	 */
	@Override
	public Vector<IAttribute> visit(ASTPrimitiveType node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		return node._node1.accept(this, symbol);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTProc)
	 */
	@Override
	public Vector<IAttribute> visit(ASTProc node, Object symbol) throws FFaplException{
		    node.setSymbolScope(_symbolTable.currentScope());
			node._node1.accept(this, symbol);
			node._node2.accept(this, symbol);
		_symbolTable.addSymbol(new FFaplProcFuncSymbol(node._node2.getToken().toString(), 
                    node._node2.getToken(),
                    null,
                    ISymbol.PROCEDURE, node));
		_symbolTable.openScope(false);//for Parameter and Block
			node._node3.accept(this, symbol);
			node._node4.accept(this, symbol);
			node._node5.accept(this, symbol);
			node._node6.accept(this, symbol);
		_symbolTable.closeScope();
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTProcFuncCall)
	 */
	@Override
	public Vector<IAttribute> visit(ASTProcFuncCall node, Object symbol) throws FFaplException{
		Vector<IAttribute> a1, a2;
		FFaplNodeToken t1;
		ISymbol s1, s2;
		node.setSymbolScope(_symbolTable.currentScope());
		symbol = null;
		node._node1.accept(this, symbol);
		t1 = node._node1;
		node._node2.accept(this, symbol);
		a1 = node._node3.accept(this, symbol);
		node._node4.accept(this, symbol);
		//TODO Procfunc unterschied
		s1 = new FFaplSymbol(
				t1.getToken().toString(), t1.getToken(), ISymbol.PROCEDURE);
		s1.setReference(true);
		//----debug
		//if(attribv != null){
			//System.out.println(t1.getToken().toString()+ " - " + attribv.size());
		//}else{
			//System.out.println(t1.getToken().toString()+ " - " + "<leer>");
		//}
		//-----debug
		s2 = _symbolTable.lookup(s1, a1);
		if(s2 == null){
			Object[] arguments = {"'" + t1.getToken()+ getTypes(a1) + "'"};
			throw new FFaplException(arguments, ICompilerError.IDENT_NOT_DECL, t1.getToken());
		}
		node._procfuncSymbol = s2;//needed for Interpreter, specifies the Proc func symbol
		a2 = new Vector<IAttribute>(0,1);
		a2.add(new FFaplAttribute(IAttribute.TYPE, s2.getType()));
		return a2;
		//TODO Attribute Return
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTProgram)
	 */
	@Override
	public Vector<IAttribute> visit(ASTProgram node, Object symbol) throws FFaplException {
		FFaplSymbol program;
		
		    program = new FFaplSymbol(node._node2.getToken().toString(), 
					node._node2.getToken(), ISymbol.PROGRAM);//Program Symbol
			
			_symbolTable.openScope(program, true);//opens the scope
			//only program scope is Global
			
			node.setSymbolScope(_symbolTable.currentScope());//sets the scope of the current node
			
			//Predefined procedures
			FFaplPredefinedProcFuncDeclaration.fill(_symbolTable, _logger, _thread);
			
			node._node1.accept(this, symbol);
			node._node2.accept(this, symbol);
			node._node3.accept(this, symbol);
			//ConstDecl
			node._node4.accept(this, symbol);
			//Proc Func
			node._node5.accept(this, symbol);
			//--------------
			//Decl - no more global 
			program = new FFaplSymbol("_MainBlock", null, ISymbol.BLOCK);
			_symbolTable.openScope(program, false);
				//Decl
				node._node6.accept(this, symbol);
				//Statementlist
				node._node7.accept(this, symbol);
			
			_symbolTable.closeScope();
			//--------------
			node._node8.accept(this, symbol);
			node._node9.accept(this, symbol);
		
		_symbolTable.closeScope(); //close the scope
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTPsRandomGenerator)
	 */
	@Override
	public Vector<IAttribute> visit(ASTPsRandomGenerator node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		node._node2.accept(this, symbol);
		av1 = node._node3.accept(this, symbol);
		if(! (av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER ||
				av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME)){
			Object[] arguments = {av1.firstElement().getType(), 1, node._node1.getToken(),
					"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
					FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"};
			throw new FFaplException(arguments,
					ICompilerError.ILLEGAL_TYPE_ARGUMENT, node._node1.getToken());
		}
		node._node4.accept(this, symbol);
		av2 = node._node5.accept(this, symbol);
		if(! (av2.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER ||
				av2.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME)){
			Object[] arguments = {av2.firstElement().getType(), 2, node._node1.getToken(),
					"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
					FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"};
			throw new FFaplException(arguments,
					ICompilerError.ILLEGAL_TYPE_ARGUMENT, node._node1.getToken());
		}
		node._node6.accept(this, symbol);
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTRandom)
	 */
	@Override
	public Vector<IAttribute> visit(ASTRandom node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1;
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		if(node._node2.ispresent()){
			av1 = node._node2.accept(this, symbol);
			if(! (av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER ||
					av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME)){
				Object[] arguments = {av1.firstElement().getType(), 1, node._node1.getToken(),
						"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
						FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"};
				throw new FFaplException(arguments,
						ICompilerError.ILLEGAL_TYPE_ARGUMENT, node._node1.getToken());
			}
			if(av1.size() > 1){//2nd Argument Present
				if(! (av1.elementAt(1).getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER ||
						av1.elementAt(1).getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME)){
					Object[] arguments = {av1.elementAt(1).getType(), 2, node._node1.getToken(),
							"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
							FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"};
					throw new FFaplException(arguments,
							ICompilerError.ILLEGAL_TYPE_ARGUMENT, node._node1.getToken());
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTRandomGenerator)
	 */
	@Override
	public Vector<IAttribute> visit(ASTRandomGenerator node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		node._node2.accept(this, symbol);
		av1 = node._node3.accept(this, symbol);
		if(! (av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER ||
				av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME)){
			Object[] arguments = {av1.firstElement().getType(), 1, node._node1.getToken(),
					"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
					FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"};
			throw new FFaplException(arguments,
					ICompilerError.ILLEGAL_TYPE_ARGUMENT, node._node1.getToken());
		}
		
		if (node._node4.ispresent()){
			av2 = node._node4.accept(this, symbol);
			//first element of value in sequence is EXPR
			if(! (av2.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER ||
					av2.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME)){
				Object[] arguments = {av2.firstElement().getType(), 2, node._node1.getToken(),
						"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
						FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"};
				throw new FFaplException(arguments,
						ICompilerError.ILLEGAL_TYPE_ARGUMENT, node._node1.getToken());
			}
		}
		node._node5.accept(this, symbol);
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTRecord)
	 */
	@Override
	public Vector<IAttribute> visit(ASTRecord node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		_symbolTable.openScope(false, true);
			node._node1.accept(this, symbol);
			node._node2.accept(this, symbol);
			node._node3.accept(this, symbol);
		_symbolTable.closeScope();
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTRelExpr)
	 */
	@Override
	public Vector<IAttribute> visit(ASTRelExpr node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;
		IAttribute a1;
		IToken t1;
		FFaplNodeSequence s1;
		node.setSymbolScope(_symbolTable.currentScope());
		av1 = node._node1.accept(this, symbol);
		
		if(node._node2.ispresent()){
				s1 = (FFaplNodeSequence) node._node2.getNode();
				t1 = ((FFaplNodeToken)((ASTRelOp)s1.elementAt(0))._node1.getNode()).getToken();
				av2 = s1.elementAt(1).accept(this, symbol);
				a1 = this._attribOperation.relop(av1.firstElement(), t1, av2.firstElement());
				av1 = new Vector<IAttribute>(0,1);
				av1.add(a1);	
		}
		return av1;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTRelOp)
	 */
	@Override
	public Vector<IAttribute> visit(ASTRelOp node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		return node._node1.accept(this, symbol);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTReturnStatement)
	 */
	@Override
	public Vector<IAttribute> visit(ASTReturnStatement node, Object symbol) throws FFaplException{
		Vector<IAttribute> a1;
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		a1 = node._node2.accept(this, symbol);
		node._node3.accept(this, symbol);
		if(a1.firstElement().getType().typeID()!= ((ISymbol)symbol).getType().typeID()){
			Object[] arguments = {"'" + ((ISymbol)symbol).getName() + "'",
									a1.firstElement().getType(), ((ISymbol)symbol).getType()};
			throw new FFaplException(arguments, ICompilerError.INVALID_RETURN_TYPE, node._node1.getToken());
		}
		return a1;
	}

	
	
	
	

	@Override
	public Vector<IAttribute> visit(ASTSameAs node, Object symbol) throws FFaplException {
		node.setSymbolScope(_symbolTable.currentScope());

		
		ISymbol s1, found;
		
		s1 = new FFaplSymbol(node._node1.getToken().toString(), node._node1.getToken(), ISymbol.VARIABLE);
		s1.setReference(true);
		found = _symbolTable.lookup(s1);
		
		if (found == null)
		{
			Object[] arguments = {"'" + node._node1.getToken() + "'"};
			throw new FFaplException(arguments, ICompilerError.IDENT_NOT_DECL, node._node1.getToken());
		}
			

		return node._node1.accept(this,symbol);
	}
	
	
	
	

	@Override
	public Vector<IAttribute> visit(ASTECBaseField node, Object symbol) throws FFaplException {
		node.setSymbolScope(_symbolTable.currentScope());

		

		return node._node1.accept(this,symbol);
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTSelector)
	 */
	@Override
	public Vector<IAttribute> visit(ASTSelector node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		FFaplNodeToken t1;
		ISymbol s1, s2;
		FFaplArray a1;
		Vector<IAttribute> av1 = null;
		FFaplNodeSequence ns1;
		
		if (symbol == null){// Control if symbol is set
			Object[] arguments = {"Selector"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		switch(node._node1.getPos()){
		case 0://Array
			ns1 = (FFaplNodeSequence) node._node1.getNode();
			//check if array type
			if(!(((ISymbol)symbol).getType().typeID() == FFaplTypeCrossTable.FFAPLARRAY)){
				Object[] arguments = {((ISymbol)symbol).getType(), 
						FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLARRAY]};
				throw new FFaplException(arguments,
						ICompilerError.SELECTOR_NOT_ARRAY, ((FFaplNodeToken)ns1.elementAt(0)).getToken());
			}
			
			
						
			a1 = (FFaplArray) ((ISymbol)symbol).getType();
			((ISymbol)symbol).setType(a1.subarray(1));//decrease dimension
			av1 = node._node1.accept(this, symbol);
			//check if expression is integer or prime
			t1 = (FFaplNodeToken) ns1.elementAt(0);
			if(!(av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER ||
					av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME)){
					Object[] arguments = {av1.firstElement().getType(), 
							FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLARRAY],
							"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
							FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"
					};
					throw new FFaplException(arguments,
						ICompilerError.TYPE_ILLEGAL_USE, t1.getToken());
					}
			while(av1.size() > 1){
				av1.remove(0); //fix Type, because argument types are filled from beginn
			}		
			
			if(!((FFaplNodeOpt)(
					(FFaplNodeSequence)
							node._node1.getNode()).elementAt(3)).ispresent()){
				//there is another selector
				av1 = new Vector<IAttribute>(0,1);
				av1.add(new FFaplAttribute(IAttribute.TYPE, a1.subarray(1)));
			}
			((ISymbol)symbol).setType(a1);//reset type
			//System.out.println(av1.size());
			//System.out.println("ttu: " + av1.firstElement().getType());
			break;
		case 1://Recordelement
			
			if(!(((ISymbol)symbol).getType() instanceof FFaplRecord)){
				Object[] arguments = {((ISymbol)symbol).getType(), 
						FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLRECORD]};
				throw new FFaplException(arguments,
						ICompilerError.SELECTOR_NOT_RECORD,
						((FFaplNodeToken)((FFaplNodeSequence) node._node1.getNode()).elementAt(0)).getToken());
			}
			t1 = (FFaplNodeToken)((FFaplNodeSequence) node._node1.getNode()).elementAt(1);
			s1 = new FFaplSymbol(t1.getToken().toString(), t1.getToken(), ISymbol.VARIABLE);
			s2 = _symbolTable.lookuplocal(((ISymbol)symbol), s1);
			if(s2 == null){//Symbol not found
				Object[] arguments = {"'" + t1.getToken() + "'"};
				throw new FFaplException(arguments, ICompilerError.IDENT_NOT_DECL, t1.getToken());
			}
			
			av1 = node._node1.accept(this, s2);
			if(!((FFaplNodeOpt)(
					(FFaplNodeSequence)
							node._node1.getNode()).elementAt(2)).ispresent()){
				//there is no other selector
				av1 = new Vector<IAttribute>(0,1);
				av1.add(new FFaplAttribute(IAttribute.TYPE, s2.getType()));
			}
			break;
		default://error
			Object[] arguments = {"Selector"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		
		
		return av1;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTStatement)
	 */
	@Override
	public Vector<IAttribute> visit(ASTStatement node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		return null;
	}
	
	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTBlockStatement)
	 */
	@Override
	public Vector<IAttribute> visit(ASTBlockStatement node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTStatementList)
	 */
	@Override
	public Vector<IAttribute> visit(ASTStatementList node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTTerm)
	 */
	@Override
	public Vector<IAttribute> visit(ASTTerm node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1, av2;
		FFaplNodeSequence ns1;
		node.setSymbolScope(_symbolTable.currentScope());
		switch(node._node1.getPos()){
		case 0://Expr [IdTerm]
			ns1 = (FFaplNodeSequence) node._node1.getNode();
			av1 = ns1.elementAt(0).accept(this, symbol);
			if( ! (av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER ||
					av1.firstElement().getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME ) ){
					Object[] arguments = { av1.firstElement().getType(), FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPOLYNOMIAL],
											"[" + FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLINTEGER] + ", " +
											FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLPRIME] + "]"};
					throw new FFaplException(arguments, 
						ICompilerError.TYPE_ILLEGAL_USE);
					}
			av2 = ns1.elementAt(1).accept(this, symbol);
			av1 = this.appendElement(av1, av2);
			break;
		case 1://IdTerm
			av1 = node._node1.accept(this, symbol);
			break;
		default:
			Object[] arguments = {"Term"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		
		return av1;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTTypePlain)
	 */
	@Override
	public Vector<IAttribute> visit(ASTDeclType node, Object symbol) throws FFaplException{
		Vector<IAttribute> av1;
		FFaplNodeListOpt t1;
		node.setSymbolScope(_symbolTable.currentScope());
		IAttribute a1;
		if( node._node1.getPos() > 0 && node._node1.getPos() < 4){//ComplexAlgebraic or primitive or Algebraic and array
			av1 = ((FFaplNodeSequence)(node._node1.getNode())).elementAt(0).accept(this, symbol);
			t1 = (FFaplNodeListOpt)((FFaplNodeSequence)(node._node1.getNode())).elementAt(1);
			if(t1.size() > 0){
				a1 = new FFaplAttribute(IAttribute.TYPE, 
						new FFaplArray(av1.firstElement().getType(), t1.size()));
				av1.clear();//must be removed, simulates replace
				av1.add(a1);
				return av1;
			}else{
				return av1;
			}
		}else if (node._node1.getPos() == 5) //SameAs
		{
			ASTSameAs sa = (ASTSameAs)node._node1.getNode();
			sa.accept(this,symbol);
			ISymbol s1, found;
			
			s1 = new FFaplSymbol(sa._node1.getToken().toString(), sa._node1.getToken(), ISymbol.VARIABLE);
			s1.setReference(true);
			found = _symbolTable.lookup(s1);
			
			a1 = new FFaplAttribute(IAttribute.TYPE, found.getType());
			av1 = new Vector<IAttribute>();
			av1.add(a1);

			return av1;
		}else if(node._node1.getPos() == 6) //ECBaseField
		{
			ASTECBaseField bf = (ASTECBaseField)node._node1.getNode();
			bf.accept(this,symbol);

			if (bf._node3.getPos() == 0) //BaseGF
			{
				a1 = new FFaplAttribute(IAttribute.TYPE,new FFaplGaloisField());
			}
			else //BaseZ
			{
				a1 = new FFaplAttribute(IAttribute.TYPE,new FFaplResidueClass());
			}
			
			av1 = new Vector<IAttribute>();
			av1.add(a1);
			
			return av1;
		}
		else{//rest no arrays
			av1 = node._node1.getNode().accept(this, symbol);
			return av1;
		}
	}
	
	@Override
	public Vector<IAttribute> visit(ASTContainerType node, Object symbol)
			throws FFaplException {
		node.setSymbolScope(_symbolTable.currentScope());
		return node._node1.accept(this, symbol);
	}

	@Override
	public Vector<IAttribute> visit(ASTParamType node, Object symbol)
			throws FFaplException {
		Vector<IAttribute> attrib;
		FFaplNodeListOpt t1;
		node.setSymbolScope(_symbolTable.currentScope());
		if( node._node1.getPos() > 0 ){//complexAlgebraic or primitive or Algebraic and array
			attrib = ((FFaplNodeSequence)(node._node1.getNode())).elementAt(0).accept(this, symbol);
			t1 = (FFaplNodeListOpt)((FFaplNodeSequence)(node._node1.getNode())).elementAt(1);
			if(t1.size() > 0){
				attrib.add( new FFaplAttribute(IAttribute.TYPE, 
						new FFaplArray(attrib.firstElement().getType(), t1.size())));
				attrib.remove(0);//must be removed, simulates replace
				return attrib;
			}else{
				return attrib;
			}
		}else{//rest no arrays
			attrib = node._node1.getNode().accept(this, symbol);
			return attrib;
		}
	}

	@Override
	public Vector<IAttribute> visit(ASTArrayType node, Object symbol)
			throws FFaplException {
		node.setSymbolScope(_symbolTable.currentScope());
		return node._node1.accept(this, symbol);
	}

	@Override
	public Vector<IAttribute> visit(ASTAlgebraicType node, Object symbol)
			throws FFaplException {
		node.setSymbolScope(_symbolTable.currentScope());
		return node._node1.accept(this, symbol);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTUnaryExpr)
	 */
	@Override
	public Vector<IAttribute> visit(ASTUnaryExpr node, Object symbol) throws FFaplException{
		Vector<IAttribute> a1, a2;
		FFaplNodeChoice nc;
		//TODO �berarbeiten
		node.setSymbolScope(_symbolTable.currentScope());
		//node._node1.accept(this, symbol);
		a1 = node._node2.accept(this, symbol);
		if(node._node1.ispresent()){
			a2 = new Vector<IAttribute>(0,1);
			nc = (FFaplNodeChoice)node._node1.getNode();
			switch(nc.getPos()){
			case 0: //addop
				a2.add( 
						this._attribOperation.op1(
						((FFaplNodeToken)((ASTAddOp) nc.getNode())._node1.getNode()).getToken(), 
						a1.firstElement())
						);
				break;
			case 1: // not operator
				a2.add( 
						this._attribOperation.op1(
						((FFaplNodeToken) nc.getNode()).getToken(), 
						a1.firstElement())
						);
				break;
			}
			
			
			a1 = a2;
		}
		
			return a1;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.ASTWhileStatement)
	 */
	@Override
	public Vector<IAttribute> visit(ASTWhileStatement node, Object symbol) throws FFaplException{
		node.setSymbolScope(_symbolTable.currentScope());
		node._node1.accept(this, symbol);
		node._node2.accept(this, symbol);
		node._node3.accept(this, symbol);
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeChoice)
	 */
	@Override
	public Vector<IAttribute> visit(FFaplNodeChoice node, Object symbol) throws FFaplException{
		return node.getNode().accept(this, symbol);
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeList)
	 */
	@Override
	public Vector<IAttribute> visit(FFaplNodeList node, Object symbol) throws FFaplException{
		Vector<IAttribute> attribv = new Vector<IAttribute>(0,1);
		
		for (final Iterator<INode> e = node.iterator(); e.hasNext();) {
			appendElement(attribv, e.next().accept(this, symbol)); 
		}
		return attribv;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeListOpt)
	 */
	@Override
	public Vector<IAttribute> visit(FFaplNodeListOpt node, Object symbol) throws FFaplException{
		Vector<IAttribute> attribv = null;
		
		if(node.ispresent()){
			attribv = new Vector<IAttribute>(0,1);
			for (final Iterator<INode> e = node.iterator(); e.hasNext();) {
				appendElement(attribv, e.next().accept(this, symbol));
			}
		}
		return attribv;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeOpt)
	 */
	@Override
	public Vector<IAttribute> visit(FFaplNodeOpt node, Object symbol) throws FFaplException{
		if(node.ispresent()){
			return node.getNode().accept(this, symbol);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeSequence)
	 */
	@Override
	public Vector<IAttribute> visit(FFaplNodeSequence node, Object symbol) throws FFaplException{
		Vector<IAttribute> attribv = new Vector<IAttribute>(0,1);
		
		for (final Iterator<INode> e = node.iterator(); e.hasNext();) {
			  attribv = appendElement(attribv, e.next().accept(this, symbol));
		}
		return attribv;
	}

	/* (non-Javadoc)
	 * @see ffapl.visitor.interfaces.IVoidVisitor#visit(ffapl.ast.nodes.FFaplNodeToken)
	 */
	@Override
	public Vector<IAttribute> visit(FFaplNodeToken node, Object symbol) throws FFaplException{
		return null;
	}

	@Override
	public Vector<IAttribute> visit(FFaplNodeType node, Object symbol)
			throws FFaplException {
		Vector<IAttribute> av1;
		Vector<IAttribute> av2 = new Vector<IAttribute>(0,1);
		av1 = node.getNode().accept(this, symbol);
		av2.add(new FFaplAttribute(IAttribute.TYPE,node.getType()));
		av2 = this.appendElement(av2, av1);//adds possible type of arguments at the end e.g. Integer(Expr)
		return av2;
	}
	
	/**
	 * Appends all elements from <code> toadd </code> to <code>attrib</code> vector
	 * except null elements. 
	 * @param attribv
	 * @param toadd
	 * @return
	 */
	private Vector<IAttribute> appendElement(Vector<IAttribute> attribv, Vector<IAttribute> toadd){
		IAttribute attrib;
		if(toadd != null){
			for (Iterator<IAttribute> attribI = toadd.iterator(); attribI.hasNext();){
				attrib = attribI.next();
				if(attrib != null){
					attribv.add(attrib);
				}
			}
		}
		return attribv;
	}
	
	/**
	 * Returns string with attribute types
	 * @param attribv
	 * @return
	 */
	private String getTypes(Vector<IAttribute> attribv){
		String result = "(";
		IAttribute attrib;
		
		if(attribv != null){
			for (Iterator<IAttribute> attribI = attribv.iterator(); attribI.hasNext();){
				attrib = attribI.next();
				result = result +  attrib.getType();
				if(attribI.hasNext()){
					result = result + ", ";
				}
			}
		}
		return result + ")";
	}

	
	@Override
	public Vector<IAttribute> visit(ASTRandomGeneratorType node,
			Object symbol) throws FFaplException {
		node.setSymbolScope(_symbolTable.currentScope());
		return node._node1.accept(this, symbol);
	}

	@Override
	public Vector<IAttribute> visit(ASTExprRandomGType node, Object symbol)
			throws FFaplException {
		node.setSymbolScope(_symbolTable.currentScope());
		return node._node1.accept(this, symbol);
	}

	@Override
	public Vector<IAttribute> visit(ASTBreak node, Object argument)
			throws FFaplException {
		node.setSymbolScope(_symbolTable.currentScope());
		checkValidBreak(node);
		return null;
	}
	
	private void checkValidBreak(ASTBreak node) throws FFaplException{
		INode tmpNode = node.getParent();
		boolean valid = false;
		while(tmpNode != null){
			if(tmpNode instanceof ASTForStatement || tmpNode instanceof ASTWhileStatement){
				valid = true;
				break;
			}
			tmpNode = tmpNode.getParent();
		}
		if(!valid){
			Object[] args = {FFaplTypeConversation.getTokenString(FFaplInterpreterConstants.BREAK)};
			throw new FFaplException(args, ICompilerError.BREAK_WRONG_POSITION, node._node1.getToken());
		}
	}

	@Override
	public Vector<IAttribute> visit(ASTECAssignment node, Object argument)
			throws FFaplException {

		node.setSymbolScope(_symbolTable.currentScope());

		FFaplSymbol v1;
		FFaplSymbol v2;
		FFaplSymbol v3;
		
		ISymbol found1, found2, found3;
		
		v1 = new FFaplSymbol(node._node1.getToken().toString(), node._node1.getToken(), ISymbol.VARIABLE);
		v1.setReference(true);
		found1 = _symbolTable.lookup(v1);
		if (found1 != null)
			found1.setInitialized(true);
		else
		{
			Object[] arguments = {"'" + node._node1.getToken() + "'"};
			throw new FFaplException(arguments, ICompilerError.IDENT_NOT_DECL, node._node1.getToken());

		}

		
		v2 = new FFaplSymbol(node._node2.getToken().toString(), node._node2.getToken(), ISymbol.VARIABLE);
		v2.setReference(true);
		found2 = _symbolTable.lookup(v2);
		if (found2 != null)
			found2.setInitialized(true);
		else
		{
			Object[] arguments = {"'" + node._node2.getToken() + "'"};
			throw new FFaplException(arguments, ICompilerError.IDENT_NOT_DECL, node._node2.getToken());
		}
		
		
		v3 = new FFaplSymbol(node._node3.getToken().toString(), node._node3.getToken(), ISymbol.VARIABLE);
		v3.setReference(true);
		found3 = _symbolTable.lookup(v3);
		if (found3 == null)
		{
			Object[] arguments = {"'" + node._node3.getToken() + "'"};
			throw new FFaplException(arguments, ICompilerError.IDENT_NOT_DECL, node._node3.getToken());
		}
		if (!found3.isInitialized())
		{
			Object[] arguments = {"'" + node._node3.getToken() + "'"};
			throw new FFaplException(arguments, ICompilerError.SYMBOL_NOT_INIT, node._node3.getToken());
		}

		if ((found3.getType().typeID() != FFaplTypeConversation.FFAPLEC ))
		{
			Object[] arguments = {"'" + node._node3.getToken() + "'"};
			throw new FFaplException(arguments, ICompilerError.TYPE_MISMATCH_ASSIGN, node._node3.getToken());
		}

		
		
		return null;
	}



	
	
    

	
	

}
