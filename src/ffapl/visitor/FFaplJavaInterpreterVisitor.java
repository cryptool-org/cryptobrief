/**
 * 
 */
package ffapl.visitor;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Vector;

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
import ffapl.java.FFaplVm;
import ffapl.java.classes.Array;
import ffapl.java.classes.BInteger;
import ffapl.java.classes.EllipticCurve;
import ffapl.java.classes.GaloisField;
import ffapl.java.classes.JBoolean;
import ffapl.java.classes.JString;
import ffapl.java.classes.PRNG_Standard;
import ffapl.java.classes.Polynomial;
import ffapl.java.classes.PolynomialRC;
import ffapl.java.classes.Prime;
import ffapl.java.classes.RNG_Placebo;
import ffapl.java.classes.ResidueClass;
import ffapl.java.enums.LoopState;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;
import ffapl.java.interfaces.IPseudoRandomGenerator;
import ffapl.java.interfaces.ITrueRandomGenerator;
import ffapl.java.logging.FFaplLogger;
import ffapl.lib.FFaplPreProcFuncSymbol;
import ffapl.lib.FFaplProcFuncSymbol;
import ffapl.lib.FFaplSymbol;
import ffapl.lib.interfaces.IAttribute;
import ffapl.lib.interfaces.ICompilerError;
import ffapl.lib.interfaces.ISymbol;
import ffapl.lib.interfaces.ISymbolTable;
import ffapl.lib.interfaces.IToken;
import ffapl.lib.interfaces.IVm;
import ffapl.types.ComplexAlgebraicType;
import ffapl.types.FFaplArray;
import ffapl.types.FFaplBoolean;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplString;
import ffapl.types.FFaplTypeCrossTable;
import ffapl.visitor.interfaces.IRetArgVisitor;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplJavaInterpreterVisitor implements IRetArgVisitor<Object, Object> {

	private ISymbolTable _symbolTable;
	private IVm _interpreter;
	private FFaplLogger _logger;
	private Thread _thread;
	
	/**
	 * 
	 * @param symbolTable
	 * @param logger
	 * @param thread
	 */
	public FFaplJavaInterpreterVisitor(ISymbolTable symbolTable, FFaplLogger logger, Thread thread) {
		_symbolTable = symbolTable;
		_logger = logger;
		_thread = thread;
		_interpreter = new FFaplVm(_logger, _thread);
		
	}

	@Override
	public Object visit(ASTAddExpr node, Object argument) throws FFaplException {
		INode n;
		FFaplNodeSequence s;
		IToken tk = null;
		ASTAddOp addOp;
		try{
			node._node1.accept(this, argument);
			
			if(node._node2.ispresent()){
				for(Iterator<INode> itr = node._node2.iterator(); itr.hasNext();){
					n = itr.next();
					s = (FFaplNodeSequence) n;
					addOp = (ASTAddOp)s.elementAt(0);
					tk = ((FFaplNodeToken) addOp._node1.getNode()).getToken();
					n.accept(this, argument);
					
					switch(addOp._node1.getPos()){
					case 0: //+
					    _interpreter.add();
					    break;
					case 1: // -
						_interpreter.sub();
						break;
					}	
				}
			}
		}catch(FFaplAlgebraicException e){
			if(tk != null){
				e.addToken(tk);
			}
			throw e;
		}
		
		return null;
	}

	@Override
	public Object visit(ASTAddOp node, Object argument) throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTArgumentList node, Object argument)
			throws FFaplException {
		IJavaType t1;
		ISymbol s1;
		int bt1;
		Array arr;
		Object[] values;
		int dim;
		int baseType;
		IJavaType jType, val;
		
		if(argument != null){
			if(argument instanceof ISymbol){
				s1 = (ISymbol) argument;
				if(s1.getType().typeID() == FFaplTypeCrossTable.FFAPLARRAY){
					_interpreter.loadValue(s1.getOffset(), s1.isGlobal());
					arr = (Array) _interpreter.popStack();
					if(arr.getjType()!= null){
						argument = arr.getjType();
					}else{
						argument = arr.getBaseType();
					}
				}
			}			
			
			node._node1.accept(this, argument);
			node._node2.accept(this, argument);
			values = new Object[node._node2.size() + 1];
			for(int i = node._node2.size(); i >= 0; i--){
				//load arguments to Object Array
				val = (IJavaType) _interpreter.popStack();
				if(val instanceof BInteger && (Integer)argument == FFaplTypeCrossTable.FFAPLPRIME){
					val = new Prime((BInteger)val, _thread);
				}
				values[i] = val;
			}
			//System.out.println("her " + argument);
			if(argument instanceof IJavaType){
				t1 = (IJavaType) argument;
				jType = t1;
				if(values[0] instanceof Array){
					dim = ((Array)values[0]).dim() + 1;
					baseType = ((Array)values[0]).getBaseType();
				}else{
					baseType = IJavaType.FFapl_Type_Compatibility[t1.typeID()];
					dim = 1;
				}	
			}else if(argument instanceof Integer){
				bt1 = (Integer) argument;
				baseType = bt1;
				jType = null;
				if(values[0] instanceof Array){
					dim = ((Array)values[0]).dim() + 1;
				}else{
					dim = 1;
				}
			}else{
				Object[]arguments = {"ArgumentList"};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
			}
			//create array
			arr = new Array(baseType, dim, values, jType, _thread);
			_interpreter.pushStack(arr);
		}else{
			node._node1.accept(this, argument);
			node._node2.accept(this, argument);
		}
		
		return null;
	}

	@Override
	public Object visit(ASTArrayLen node, Object argument)
			throws FFaplException {
		ISymbol s1;
		try{
			s1 = new FFaplSymbol(node._node2.getToken().toString(), node._node2.getToken(), ISymbol.VARIABLE);
			s1 = _symbolTable.lookup(s1);
			if(s1.getType().typeID() == FFaplTypeCrossTable.FFAPLRECORD){
				Object[] arguments = {s1, false}; //symbol, store
				node._node3.accept(this, arguments);
				
			}else{
				_interpreter.loadValue(s1.getOffset(), s1.isGlobal());
				//for array
				node._node3.accept(this, true);
			}
			
			_interpreter.arrayLen();
		}catch(FFaplAlgebraicException e){
			e.addToken(node._node1.getToken());
			throw e;
		}
		return null;
	}

	@Override
	public Object visit(ASTAssignment node, Object argument)
			throws FFaplException {
		FFaplNodeToken t1;
		ISymbol s1;
		Array arr;
			//error on left site of assignment
		try{
			_symbolTable.setScope(node.getSymbolScope());
		
		//node._node1.accept(this, symbol);
		
			t1 = node._node1;
			
			s1 = new FFaplSymbol(t1.getToken().toString(), t1.getToken(), ISymbol.VARIABLE);
			
			s1 = _symbolTable.lookup(s1);
			if(node._node2.ispresent()){
				//selector
				if(s1.getType().typeID() == FFaplTypeCrossTable.FFAPLRECORD){
					//record
					Object [] arguments = {s1, true, node._node4};//Symbol, store, node value
					node._node2.accept(this, arguments);
					//s2 = (ISymbol) _interpreter.popStack();
					
					//node._node4.accept(this, s2);
					//_interpreter.storeValue(s2.getOffset(), false);
					
				}else{
					//array
					_interpreter.loadValue(s1.getOffset(), false);
					arr = (Array) _interpreter.peekStack();
					
					node._node2.accept(this, false);
					
					if(arr.getjType()!= null){
						node._node4.accept(this, arr.getjType());
					}else{
						node._node4.accept(this, arr.getBaseType());
					}
					
					//cast needs reference object on stack, null is allowed for
					//algebraic and primitive Type
					//TODO integrate cast if needed in Version 1.0 no cast needed
					//_interpreter.pushStack(null);
				    //_interpreter.castElementOnStackTo(((FFaplArray)found.getType()).baseType().typeID());
					_interpreter.storeArrayElement();
				}
				
			}else{
				if(node._node4._node1.getPos() == 1){
					//creation Expression
					_interpreter.loadValue(s1.getOffset(), false);	
				}
				//System.out.println(_interpreter);
				node._node4.accept(this, s1);
				_interpreter.storeValue(s1.getOffset(), false);
			}
		}catch(FFaplAlgebraicException e){
				e.addToken(node._node3.getToken());
			throw e;
		}
		return null;
	}

	@Override
	public Object visit(ASTBlock node, Object argument) throws FFaplException {
		
		//node._node1.accept(this, argument);
		node._node2.accept(this, argument);
		return node._node3.accept(this, argument);
		//node._node4.accept(this, argument);
	}

	@Override
	public Object visit(ASTComplexAlgebraicType node, Object argument)
			throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTCondAndExpr node, Object argument)
			throws FFaplException {
		INode n;
		IToken tk = null;
		try{
			node._node1.accept(this, argument);
			
			if(node._node2.ispresent()){
				for(Iterator<INode> itr = node._node2.iterator(); itr.hasNext();){
					n = itr.next();
					tk = ((FFaplNodeToken) ((FFaplNodeSequence) n).elementAt(0)).getToken();
					
					n.accept(this, argument);
					//System.out.println(_interpreter);
						_interpreter.and();	
				}
			}
		}catch(FFaplAlgebraicException e){
			if(tk != null){
				e.addToken(tk);
			}
			throw e;
		}
		return null;
	}
	
	

	@Override
	public Object visit(ASTCondOrExpr node, Object argument)
			throws FFaplException {
		INode n;
		IToken tk = null;
		try{
			node._node1.accept(this, argument);
			
			if(node._node2.ispresent()){
				for(Iterator<INode> itr = node._node2.iterator(); itr.hasNext();){
					n = itr.next();
					tk = ((FFaplNodeToken) ((FFaplNodeSequence) n).elementAt(0)).getToken();
					
					n.accept(this, argument);
					//System.out.println(_interpreter);
						_interpreter.or();	
				}
			}
		}catch(FFaplAlgebraicException e){
			if(tk != null){
				e.addToken(tk);
			}
			throw e;
		}
		return null;
	}

	@Override
	public Object visit(ASTCondition node, Object argument)
			throws FFaplException {
		//node._node1.accept(this, argument);
		node._node2.accept(this, argument);
		//node._node3.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTCreationExpr node, Object argument)
			throws FFaplException {
		//node._node1.accept(this, null);
		//node._node2.accept(this, null);
		try{
			Array initArray = (Array) _interpreter.popStack();
			node._node3.accept(this, null);
			//System.out.println(_interpreter);
			
			_interpreter.allocArray(node._arrayType, initArray);
		}catch(FFaplAlgebraicException e){
				e.addToken(node._node1.getToken());
			throw e;
		}
		return null;
	}

	@Override
	public Object visit(ASTExprComplexAType node, Object argument)
			throws FFaplException {
		FFaplNodeType t;
		BigInteger modulus;
		node._node1.accept(this, argument);
		switch(node._node1.getPos()){
		case 1://residue class or Polynomial ring
			//System.out.println(_interpreter);
			t = (FFaplNodeType)node._node1.getNode();
			modulus = (BigInteger) _interpreter.popStack();
			switch (t.getType().typeID()){
			case FFaplTypeCrossTable.FFAPLRESIDUECLASS:
				_interpreter.pushStack(new ResidueClass(modulus));
				break;
			case FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE:
				_interpreter.pushStack(new PolynomialRC(modulus, _thread));
				break;
			default:
				Object[] arguments = {"CrExprComplexType"};
				throw new FFaplAlgebraicException(arguments ,
						IAlgebraicError.INTERNAL);
			}
				
			break;
		}
		return null;
	}

	@Override
	public Object visit(ASTDecl node, Object argument) throws FFaplException {
		ISymbol s1, s2;
		FFaplNodeToken ntk;
		this._symbolTable.setScope(node.getSymbolScope());
		
		s1 = new FFaplSymbol(node._node1.getToken().toString(), 
                node._node1.getToken(),
                ISymbol.VARIABLE);
		s1 = _symbolTable.lookup(s1);
		
		//allocate 
		//if(argument instanceof FFaplRecord){
			//in Record scope
		//}else{
			//rest
			
			node._node4.accept(this, argument);
			
			if(!s1.setOffset()){
				if(s1.getType().typeID() == FFaplTypeCrossTable.FFAPLARRAY){
					//create init array
					//System.out.println(((FFaplArray) s1.getType()).baseType());
					if(((FFaplArray) s1.getType()).baseType() instanceof ComplexAlgebraicType){
						_interpreter.pushStack(new Array(((FFaplArray) s1.getType()).baseType().typeID(),
							((FFaplArray) s1.getType()).getDim(), (IJavaType) _interpreter.popStack(), _thread));
					}else{   
						_interpreter.pushStack(new Array(((FFaplArray) s1.getType()).baseType().typeID(),
							((FFaplArray) s1.getType()).getDim(), _thread));
					}
				}
				if(!(s1.getType().typeID()  == FFaplTypeCrossTable.FFAPLRECORD)){
					s1.setOffset(this._interpreter.allocStack(s1));
				}
			}else if(!_interpreter.isStackOffsetAllocated(s1.getOffset())){
				//already allocated in previous func/proc call put no place on stack
				if(s1.getType().typeID() == FFaplTypeCrossTable.FFAPLARRAY){
					//create init array
					//System.out.println(((FFaplArray) s1.getType()).baseType());
					if(((FFaplArray) s1.getType()).baseType() instanceof ComplexAlgebraicType){
						
						_interpreter.pushStack(new Array(((FFaplArray) s1.getType()).baseType().typeID(),
							((FFaplArray) s1.getType()).getDim(), (IJavaType) _interpreter.popStack(), _thread));
					}else{   
						_interpreter.pushStack(new Array(((FFaplArray) s1.getType()).baseType().typeID(),
							((FFaplArray) s1.getType()).getDim(), _thread));
					}
				}
				if(!(s1.getType().typeID()  == FFaplTypeCrossTable.FFAPLRECORD)){
					if(s1.getOffset() != _interpreter.allocStack(s1)){
			    		  Object[] arguments = {"Decl1"};
			  			throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
			    	}
				}  
		      }
			
	        
			//allocate
			for (final Iterator<INode> e = node._node2.iterator(); e.hasNext();) {
			      ntk = (FFaplNodeToken)((FFaplNodeSequence) e.next()).elementAt(1);
			      s2 = new FFaplSymbol(ntk.getToken().toString(), 
		                    ntk.getToken(),
		                    ISymbol.VARIABLE);
			      
			      s2 = _symbolTable.lookup(s2);
			      if(!s2.setOffset()){
			    	  if(s2.getType().typeID() == FFaplTypeCrossTable.FFAPLARRAY){
							//create init array
							_interpreter.pushStack(new Array(((FFaplArray) s2.getType()).baseType().typeID(),
									((FFaplArray) s2.getType()).getDim(), _thread));
						}
			    	  if(!(s2.getType().typeID()  == FFaplTypeCrossTable.FFAPLRECORD)){
			    		  	s2.setOffset(this._interpreter.alloStack(s2,s1.getOffset()));
					  }
			    	 
			      }else if(!_interpreter.isStackOffsetAllocated(s2.getOffset())){
			    	  if(s2.getType().typeID() == FFaplTypeCrossTable.FFAPLARRAY){
							//create init array
							_interpreter.pushStack(new Array(((FFaplArray) s2.getType()).baseType().typeID(),
									((FFaplArray) s2.getType()).getDim(), _thread));
					  }
			    	  if(!(s2.getType().typeID()  == FFaplTypeCrossTable.FFAPLRECORD)){
				    		if(s2.getOffset() != _interpreter.alloStack(s2,s1.getOffset())){
					    		  Object[] arguments = {"Decl2"};
					  			throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
					    	}
					  }
			      }
			}		
		//}
			//System.out.println(_interpreter);
		return null;
		
	}

	@Override
	public Object visit(ASTConstDecl node, Object argument)
			throws FFaplException {
		_symbolTable.setScope(node.getSymbolScope());
		
		ISymbol constant;
		try{//Type
			constant = new FFaplSymbol(node._node2.getToken().toString(), 
	                node._node2.getToken(),
	                null,
	                ISymbol.CONSTANT);
			//TODO readonly 
			constant = _symbolTable.lookup(constant);
			node._node4.accept(this, argument);
			constant.setOffset(_interpreter.allocGlobal(constant.getType()));
		}catch(FFaplException fe){
			fe.addToken(node._node3.getToken());
			throw fe;
		}
		
		try{//Expr
			node._node6.accept(this, constant);
			_interpreter.storeValue(constant.getOffset(), true);
		}catch(FFaplException fe){
			fe.addToken(node._node5.getToken());
			throw fe;
		}
		return null;
	}

	@Override
	public Object visit(ASTConstType node, Object argument)
			throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTContainerType node, Object argument)
			throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTParamType node, Object argument)
			throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTArrayType node, Object argument)
			throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTAlgebraicType node, Object argument)
			throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTElseBlock node, Object argument)
			throws FFaplException {
		return node._node1.accept(this, argument);
	}

	@Override
	public Object visit(ASTEqualExpr node, Object argument)
			throws FFaplException {
		INode n;
		FFaplNodeSequence s;
		IToken tk = null;
		ASTEqualOp equalOp;
		
		try{
			node._node1.accept(this, argument);
			//System.out.println(_interpreter);
			if(node._node2.ispresent()){
				   
					n = node._node2.getNode();
					s = (FFaplNodeSequence) n; 
					equalOp = (ASTEqualOp)s.elementAt(0);
					tk = ((FFaplNodeToken)equalOp._node1.getNode()).getToken();
					node._node2.accept(this, argument);
					switch(equalOp._node1.getPos()){
					case 0: //'=='
					    _interpreter.isEqual();
					    break;
					case 1: // '!='
						_interpreter.isEqual();
						_interpreter.not();
						break;
					}	
			}
		}catch(FFaplAlgebraicException e){
			if(tk != null){
				e.addToken(tk);
			}
			throw e;
		}
		return null;
	}

	@Override
	public Object visit(ASTEqualOp node, Object argument) throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTExpr node, Object argument) throws FFaplException {
		
		INode n;
		FFaplNodeSequence s;
		ASTCondOrExpr coe;
		FFaplNodeListOpt o1;
		IToken tk = null;
		//node._node1.accept(this, argument);
		switch(node._node1.getPos()){
		case 0: //CondOrExpr { 'XOR' CondOrExpr } 
			try{
				s = (FFaplNodeSequence) node._node1.getNode();
				coe = (ASTCondOrExpr) s.elementAt(0);
				o1 = (FFaplNodeListOpt) s.elementAt(1);
				
				coe.accept(this, argument);
				
				if(o1.ispresent()){
					for(Iterator<INode> itr = o1.iterator(); itr.hasNext();){
						n = itr.next();
						tk = ((FFaplNodeToken) ((FFaplNodeSequence) n).elementAt(0)).getToken();
						n.accept(this, argument);
						//System.out.println(_interpreter);
						_interpreter.xor();	
					}
				}
			}catch(FFaplAlgebraicException e){
				if(tk != null){
					e.addToken(tk);
				}
				throw e;
			}
			break;
		case 1: // CreationExpr 
			node._node1.accept(this, argument);
			break;
		}
		
		
		return null;
	}

	@Override
	public Object visit(ASTFormalParam node, Object argument)
			throws FFaplException {
		ISymbol s1, s2;
		FFaplNodeToken ntk;
		Vector<Integer[]> of = new Vector<Integer[]> (0,1);
		Integer[] entry;
		this._symbolTable.setScope(node.getSymbolScope());
		
		s1 = new FFaplSymbol(node._node1.getToken().toString(), 
                node._node1.getToken(),
                ISymbol.VARIABLE);
		s1 = _symbolTable.lookup(s1);
		//allocate 
		//node._node4.accept(this, argument);
		if(!s1.setOffset()){
			s1.setOffset(this._interpreter.allocFormalParam(s1));
		}else if(!_interpreter.isStackOffsetAllocated(s1.getOffset())){
	    	  if(s1.getOffset() != _interpreter.allocFormalParam(s1)){
	    		  Object[] arguments = {"FormalParam1"};
	  			throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
	    	  }
	     }
		entry = new Integer[2];
		entry[0] = s1.getOffset();
		entry[1] = s1.getType().typeID();
		of.add(entry);
		
		
        
		//allocate
		for (final Iterator<INode> e = node._node2.iterator(); e.hasNext();) {
		      ntk = (FFaplNodeToken)((FFaplNodeSequence) e.next()).elementAt(1);
		      s2 = new FFaplSymbol(ntk.getToken().toString(), 
	                    ntk.getToken(),
	                    ISymbol.VARIABLE);
		      
		      s2 = _symbolTable.lookup(s2);
		      if(!s2.setOffset()){
		    	  s2.setOffset(_interpreter.allocFormalParam(s2));
		      }else if(!_interpreter.isStackOffsetAllocated(s2.getOffset())){
		    	  if(s2.getOffset() != _interpreter.allocFormalParam(s2)){
		    		  Object[] arguments = {"FormalParam2"};
		  			throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
		    	  }
		      }
		      entry = new Integer[2];
			  entry[0] = s2.getOffset(); //offset
			  entry[1] = s2.getType().typeID(); //type
		      of.add(entry);
		      //s2.resetOffset();
		}	
		//s1.;
	    
		return of;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTFormalParamList node, Object argument)
			throws FFaplException {
		Vector<Integer[]> of1, of2;
		of1 = (Vector<Integer[]>) node._node1.accept(this, argument);
		if(node._node2.ispresent()){
			for (final Iterator<INode> e = node._node2.iterator(); e.hasNext();) {
				of2 = (Vector<Integer[]>) ((FFaplNodeSequence)e.next()).elementAt(1).accept(this, argument); 
				of1.addAll(of2);
			}
			
		}
		
		for(int i = ( of1.size() - 1 ); i >= 0; i--){
			IJavaType val;
			if(of1.elementAt(i)[1] == FFaplTypeCrossTable.FFAPLPRIME){
                                val = (IJavaType) _interpreter.popStack();
                                if(!(val instanceof Prime)){
                                        //must be Integer
                                        _interpreter.pushStack(new Prime((BInteger)val, _thread));
                                }else{
                                        _interpreter.pushStack(val);
                                }
			}
			_interpreter.storeFormalParamValue(of1.elementAt(i)[0]);
		}
		return null;
	}

	@Override
	public Object visit(ASTForStatement node, Object argument)
			throws FFaplException {
		ISymbol s1;
		BInteger to, from;
		BInteger step = new BInteger("1", _thread);
		boolean asc = true;//ascending
		JBoolean condition;
		
		try{
		
			_symbolTable.setScope(node.getSymbolScope());
			//node._node1.accept(this, argument);
			//node._node2.accept(this, argument);
			//node._node3.accept(this, argument);
			s1 = new FFaplSymbol(node._node2.getToken().toString(), 
	                node._node2.getToken(),
	                new FFaplInteger(),
	                ISymbol.VARIABLE);
			s1 = _symbolTable.lookup(s1);
			if(!s1.setOffset()){//if not allocated yet
				s1.setOffset(_interpreter.allocStack(s1));
			}else if(!_interpreter.isStackOffsetAllocated(s1.getOffset())){
				if(s1.getOffset() != _interpreter.allocStack(s1)){
		    		  Object[] arguments = {"for"};
		  			throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
		    	}
			}
			
			node._node4.accept(this, argument);
			
			_interpreter.storeValue(s1.getOffset(), false);
			_interpreter.loadValue(s1.getOffset(), false);
			
			from = (BInteger) _interpreter.popStack();
			
			//node._node5.accept(this, argument);
			node._node6.accept(this, argument);
			to = (BInteger) _interpreter.popStack();
			
			if(node._node7.ispresent()){//step
				node._node7.accept(this, argument);
				step = (BInteger) _interpreter.popStack();
				if(step.compareTo(BigInteger.ZERO) <= 0){
					Object[] arguments = {"step", step};
					throw new FFaplAlgebraicException(arguments, IAlgebraicError.VAL_LESS_EQUAL_ZERO);
				}
			}
			if(from.compareTo(to) > 0){
				asc = false;
				step = new BInteger(step.negate(), _thread);
			}
			
			_interpreter.loadValue(s1.getOffset(), false);//from
			_interpreter.pushStack(to);
			if(asc){
				_interpreter.isLessEqual();
			}else{
				_interpreter.isGreaterEqual();
			}
			condition = (JBoolean) _interpreter.popStack();
			//int c = 0;
			Object obj;
			while(condition.getValue()){
				isRunning();
				
				obj = node._node8.accept(this, argument);
				
				//i = i + step
				_interpreter.loadValue(s1.getOffset(), false);//from
				_interpreter.pushStack(step);
				_interpreter.add();
				_interpreter.storeValue(s1.getOffset(), false);
				// compare from and to
				_interpreter.loadValue(s1.getOffset(), false);//from
				_interpreter.pushStack(to);
				if(asc){
					_interpreter.isLessEqual();
				}else{
					_interpreter.isGreaterEqual();
				}
				condition = (JBoolean) _interpreter.popStack();	
				if(obj instanceof LoopState){
					if(LoopState.BREAK == obj){
						break;
					}
				}
				//System.out.println(_interpreter);
				/*c ++;
				if(c > 10){
					condition = new JBoolean(false);
				}*/
			}
		}catch(FFaplException e){
			e.addToken(node._node1.getToken());
			throw e;
		}
		
		return null;
	}

	@Override
	public Object visit(ASTFunc node, Object argument) throws FFaplException {
		//node._node1.accept(this, argument);
		//node._node2.accept(this, argument);
		//node._node3.accept(this, argument);
		//System.out.println(_interpreter);
		_interpreter.enterProcFunc();
		node._node4.accept(this, argument);
		//node._node5.accept(this, argument);
		node._node8.accept(this, argument);
		//System.out.println(_interpreter);
		
		//System.out.println(_interpreter);
		
		_interpreter.exitProcFunc();
		return null;
	}

	@Override
	public Object visit(ASTFuncBlock node, Object argument)
			throws FFaplException {
		//node._node1.accept(this, argument);
		node._node2.accept(this, argument);
		node._node3.accept(this, argument);
		node._node4.accept(this, argument);
		_interpreter.funcReturn();
		//node._node5.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTGF node, Object argument) throws FFaplException {
		//node._node1.accept(this, argument);
		//node._node2.accept(this, argument);
		
		
		try{
		Polynomial irr;
		BigInteger c;
		node._node3.accept(this, argument);
		c = (BigInteger) _interpreter.popStack();
		//node._node4.accept(this, argument);
		
		node._node5.accept(this, argument);
		_interpreter.pushStack(new Polynomial(_thread));//for casting
		_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLPOLYNOMIAL);
		irr = (Polynomial) _interpreter.popStack();
		
		
		_interpreter.pushStack(new GaloisField(c, irr, _thread));
		//node._node6.accept(this, argument);
		}catch(FFaplAlgebraicException e){
				e.addToken(node._node1.getToken());
			throw e;
		}
		return null;
	}


	public Object visit(ASTEC node, Object argument) throws FFaplException {
		//node._node1.accept(this, argument);
		//node._node2.accept(this, argument);
		

		IToken t1, t2;
		FFaplNodeSequence s1;
	
		
		
		
 		if (node._node3 instanceof ASTGF)
		{
			try {	
				GaloisField gf;
				Polynomial a1 = new Polynomial(0,0, _thread), a2 = new Polynomial(0,0, _thread), a3 = new Polynomial(0,0, _thread), a4 = new Polynomial(0,0, _thread), a6 = new Polynomial(0,0, _thread);
				
				
				node._node3.accept(this, argument);
				gf = (GaloisField) _interpreter.popStack();
				
				
				if(node._node4.ispresent()){
					
					for(Iterator<INode> itr = node._node4.iterator(); itr.hasNext();)
					{
						s1 = (FFaplNodeSequence) itr.next();
						t1 = ((FFaplNodeToken)s1.elementAt(1)).getToken();
						s1.elementAt(3).accept(this, argument);
						

						if (t1.toString().equals("a1"))
						{
							//_interpreter.pushStack(new Polynomial(_thread));
							//_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLPOLYNOMIAL);
							a1 = (Polynomial) _interpreter.popStack();
						}
						else if (t1.toString().equals("a2"))
						{
							//_interpreter.pushStack(new Polynomial(_thread));
							//_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLPOLYNOMIAL);
							a2 = (Polynomial) _interpreter.popStack();
						}
						else if (t1.toString().equals("a3"))
						{
							//_interpreter.pushStack(new Polynomial(_thread));
							//_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLPOLYNOMIAL);
							a3 = (Polynomial) _interpreter.popStack();
						}
						else if (t1.toString().equals("a4"))
						{
							//_interpreter.pushStack(new Polynomial(_thread));
							//_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLPOLYNOMIAL);
							a4 = (Polynomial) _interpreter.popStack();
						}
						else if (t1.toString().equals("a6"))
						{
							//_interpreter.pushStack(new Polynomial(_thread));
							//_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLPOLYNOMIAL);
							a6 = (Polynomial) _interpreter.popStack();
						}
					}
				}
				
				
				_interpreter.pushStack(new EllipticCurve(gf, a1, a2, a3, a4, a6, _thread));
			}catch (FFaplAlgebraicException e){
				e.addToken(node._node1.getToken());
				throw e;
			}
		}
		else if (node._node3 instanceof ASTExpr)
		{
			try {	
				BInteger a1 = new BInteger("0",_thread);
				BInteger a2 = new BInteger("0",_thread);
				BInteger a3 = new BInteger("0",_thread);
				BInteger a4 = new BInteger("0",_thread);
				BInteger a6 = new BInteger("0",_thread);
				
				

				
				node._node3.accept(this, argument);

				BigInteger modulus = null;
				Object modobj = _interpreter.popStack();
				if (modobj instanceof BigInteger)
				{
					modulus = (BigInteger)modobj;
					Prime p = new Prime(modulus, _thread); // Primzahltest
				}
				else
				{
					Object[] arguments = {"ASTEC"};
					throw new FFaplAlgebraicException(arguments, IAlgebraicError.INVALID_EC_RESIDUE_CLASS_CHARACTERISTIC);
				}
				
				
				
				if(node._node4.ispresent()){
					
					for(Iterator<INode> itr = node._node4.iterator(); itr.hasNext();)
					{
						s1 = (FFaplNodeSequence) itr.next();
						t1 = ((FFaplNodeToken)s1.elementAt(1)).getToken();
						s1.elementAt(3).accept(this, argument);
						


						
						
						if (t1.toString().equals("a1"))
						{
							//_interpreter.pushStack(new Polynomial(_thread));
							//_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLINTEGER);
							a1 = (BInteger) _interpreter.popStack();
						}
						else if (t1.toString().equals("a2"))
						{
							//_interpreter.pushStack(new Polynomial(_thread));
							//_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLINTEGER);
							a2 = (BInteger) _interpreter.popStack();
						}
						else if (t1.toString().equals("a3"))
						{
							//_interpreter.pushStack(new Polynomial(_thread));
							//_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLINTEGER);
							a3 = (BInteger) _interpreter.popStack();
						}
						else if (t1.toString().equals("a4"))
						{
							//_interpreter.pushStack(new Polynomial(_thread));
							//_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLINTEGER);
							a4 = (BInteger) _interpreter.popStack();
						}
						else if (t1.toString().equals("a6"))
						{
							//_interpreter.pushStack(new Polynomial(_thread));
							//_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLINTEGER);
							a6 = (BInteger) _interpreter.popStack();
						}
												
					}
				}


				
				
				
				_interpreter.pushStack(new EllipticCurve(new ResidueClass(modulus), a1, a2, a3, a4, a6, _thread));
			}catch (FFaplAlgebraicException e){
				e.addToken(node._node1.getToken());
				throw e;
			}
		}
		
		
		
		
		
			
		return null;
	}
	
	
	
	public Object visit(ASTECPoint node, Object argument) throws FFaplException {
		BInteger x_rc,y_rc;
		Polynomial x_gf,y_gf;
		Object x,y;
		
		
		try{
	
			node._node2.accept(this, null);
			x = _interpreter.popStack();
			node._node4.accept(this, null);
			y = _interpreter.popStack();
			
			if (x instanceof BigInteger && y instanceof BigInteger)
			{
				x_rc = (BInteger)x;
				y_rc = (BInteger)y;
				_interpreter.pushStack(new EllipticCurve(x_rc,y_rc, _thread));
			}
			else if (x instanceof Polynomial && y instanceof Polynomial)
			{
				x_gf = (Polynomial)x;
				y_gf = (Polynomial)y;
				_interpreter.pushStack(new EllipticCurve(x_gf,y_gf, _thread));
			}
			else if (x instanceof GaloisField && y instanceof GaloisField)
			{
				x_gf = (Polynomial) ((GaloisField)x).value();
				y_gf = (Polynomial) ((GaloisField)y).value();
				_interpreter.pushStack(new EllipticCurve(x_gf,y_gf, _thread));
			}
			
		}catch (FFaplAlgebraicException e){
			e.addToken(node._node1.getToken());
			throw e;
		}
		
		
		return null;		
	}
	
	
	public Object visit(ASTECPAI node, Object argument) throws FFaplException {
		EllipticCurve ec = new EllipticCurve(_thread);
		ec.setPAI(true);
		_interpreter.pushStack(ec);

		return null;
	}
	
	
	public Object visit(ASTECRandom node, Object argument) throws FFaplException {
		EllipticCurve ec = new EllipticCurve(_thread);
		
		if (node._node1.getPos() == 0)
			ec.setRandom(true, false);
		else
			ec.setRandom(true, true);
		_interpreter.pushStack(ec);

		return null;
	}
	
	
	
	
	
	@Override
	public Object visit(ASTIdTerm node, Object argument) throws FFaplException {
		//node._node1.accept(this, argument);
		Polynomial ply;
		BigInteger exp;
		if(node._node2.ispresent()){
			node._node2.accept(this, FFaplTypeCrossTable.FFAPLINTEGER);
			exp = (BigInteger) _interpreter.popStack();
			if(exp.compareTo(BigInteger.ZERO) < 0){
				//negative exponent
				Object[] arguments = {"x^" + exp, exp};
				FFaplAlgebraicException fae = new FFaplAlgebraicException(arguments, IAlgebraicError.EXPONENT_LESS_ZERO);
				fae.addToken(node._node1.getToken());
				throw fae; 
			}
			ply = new Polynomial(BigInteger.ONE, exp, _thread);
		}else{
			//x ^ 1
			ply = new Polynomial(1,1, _thread);
		}
		_interpreter.pushStack(ply);
		return null;
	}

	@Override
	public Object visit(ASTIfStatement node, Object argument)
			throws FFaplException {
		JBoolean condition;
		//node._node1.accept(this, argument);
		//Condition
		
		//System.out.println(_interpreter);
		node._node2.accept(this, FFaplTypeCrossTable.FFAPLBOOLEAN);
		//System.out.println(_interpreter);
		condition = (JBoolean) _interpreter.popStack();
		//System.out.println(_interpreter);
		if(condition.getValue()){
			return node._node3.accept(this, argument);
		}else{
			return node._node4.accept(this, argument);
		}		
	}

	@Override
	public Object visit(ASTLiteral node, Object argument) throws FFaplException {
		FFaplNodeType tp;
		
		
		tp = (FFaplNodeType) node._node1.getNode();
		
		switch (tp.getType().typeID()){
		case FFaplTypeCrossTable.FFAPLBOOLEAN://Boolean
			_interpreter.pushStack(new JBoolean(((FFaplBoolean)tp.getType()).getValue()));
			break;
		case FFaplTypeCrossTable.FFAPLINTEGER://Integer
			_interpreter.pushStack(new BInteger(((FFaplInteger)tp.getType()).getValue(), _thread));
			break;
		case FFaplTypeCrossTable.FFAPLSTRING://String
			_interpreter.pushStack(new JString(((FFaplString)tp.getType()).getValue()));
			break;
		case FFaplTypeCrossTable.FFAPLEC://EllipticCurve
			//_interpreter.pushStack(new EllipticCurve(_thread));
			node._node1.accept(this,argument);
			break;
		default:
			node._node1.accept(this, argument);
		}
		return null;
	}

	@Override
	public Object visit(ASTMulExpr node, Object argument) throws FFaplException {
		INode n;
		FFaplNodeSequence s;
		IToken tk = null;
		ASTMulOp mulOp;
		int i = 0;
		//ISymbol symbol;
		IJavaType val;
		Polynomial tmp;
		try{
			
			
			if(node._node2.ispresent()){
				
				for(Iterator<INode> itr = node._node2.iterator(); itr.hasNext();){
					
					n = itr.next();
					s = (FFaplNodeSequence) n;
					mulOp = (ASTMulOp)s.elementAt(0);
					tk = ((FFaplNodeToken) mulOp._node1.getNode()).getToken();
					switch(mulOp._node1.getPos()){
					
					case 0: //*
						if(i == 0){
							node._node1.accept(this, argument);
						}
						n.accept(this, argument);
					    _interpreter.mul();
					    break;
					case 1: // /
						if(i == 0){
							node._node1.accept(this, argument);
						}
						n.accept(this, argument);
						_interpreter.div();
						break;
					case 2: // mod
						
						
						//if(argument instanceof ISymbol){
							//symbol = (ISymbol) argument;
							//if(symbol.getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER){
								n.accept(this, null);
								val = (IJavaType) _interpreter.popStack();
                                                                if (val instanceof BigInteger)
                                                                {
                                                                        if (((BigInteger) val).signum() < 1)
                                                                        {
                                                                                Object[] arguments ={};
                                                                                throw new FFaplAlgebraicException(arguments,
                                                                                IAlgebraicError.NEGATIVE_MODUL);
                                                                        }
                                                                }
								if(i == 0 && val.typeID() == IJavaType.INTEGER){
										node._node1.accept(this, new PolynomialRC((BigInteger) val, _thread));
										tmp = ((PolynomialRC) _interpreter.popStack()).getPolynomial();
										if(tmp.degree().compareTo(BigInteger.ZERO) == 0){
											_interpreter.pushStack(tmp.leadingCoefficient());
										}else{
											_interpreter.pushStack(tmp);
										}
								}else{ 
									if(i == 0){
										node._node1.accept(this, argument);	
									}
									_interpreter.pushStack(val);
									_interpreter.mod();
								}
								
							//}	
						//}else if(argument == null){
							
						/*}else{
							if(i == 0){
								node._node1.accept(this, argument);
							}
							n.accept(this, null);
							_interpreter.mod();
						}*/
						
						
					}
					
					i++;
				}
			}else{
				node._node1.accept(this, argument);
			}
			
			/*
			node._node1.accept(this, argument);
			
			if(node._node2.ispresent()){
				for(Iterator<INode> itr = node._node2.iterator(); itr.hasNext();){
					
					n = itr.next();
					s = (FFaplNodeSequence) n;
					mulOp = (ASTMulOp)s.elementAt(0);
					tk = ((FFaplNodeToken) mulOp._node1.getNode()).getToken();
					switch(mulOp._node1.getPos()){
					
					case 0: //*
						n.accept(this, argument);
					    _interpreter.mul();
					    break;
					case 1: // /
						n.accept(this, argument);
						_interpreter.div();
						break;
					case 2: // mod
						n.accept(this, null);
						_interpreter.mod();
					}	
				}
			}*/
		}catch(FFaplAlgebraicException e){
			if(tk != null){
				e.addToken(tk);
			}
			throw e;
		}
		return null;
	}

	@Override
	public Object visit(ASTMulOp node, Object argument) throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTPolynomial node, Object argument)
			throws FFaplException {
		FFaplNodeSequence s;
		INode n;
		//node._node1.accept(this, argument);
		node._node2.accept(this, FFaplTypeCrossTable.FFAPLPOLYNOMIAL);//first term

		if(node._node3.ispresent()){
			for(Iterator<INode> itr = node._node3.iterator(); itr.hasNext();){
				n = itr.next();
				n.accept(this, argument);
				s = (FFaplNodeSequence) n;
				switch(((ASTAddOp)s.elementAt(0))._node1.getPos()){
				case 0: //+
				    _interpreter.add();
				    break;
				case 1: // -
					_interpreter.sub();
					break;
				}	
			}
		}
		
		
		//node._node4.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTPowExpr node, Object argument) throws FFaplException {
		IToken tk = null;
		INode n;
		try{
			node._node1.accept(this, argument);
			
			if(node._node2.ispresent()){
				for(Iterator<INode> itr = node._node2.iterator(); itr.hasNext();){
					n = itr.next();
					tk = ((FFaplNodeToken)((FFaplNodeSequence)n).elementAt(0)).getToken();  
					n.accept(this, FFaplTypeCrossTable.FFAPLINTEGER);
					
					_interpreter.pow();
				}
			}
		}catch(FFaplAlgebraicException e){
			if(tk != null){
				e.addToken(tk);
			}
			throw e;
		}
		return null;
	}

	@Override
	public Object visit(ASTPrimaryExpr node, Object argument)
			throws FFaplException {
		FFaplNodeToken t1;
		//FFaplNodeOpt t2;
		ISymbol s1;
		
		//IJavaType jt;
		_symbolTable.setScope(node.getSymbolScope());
		
		//System.out.println(_interpreter);
		switch(node._node1.getPos()){
		case 0: //Literal 
			node._node1.accept(this, argument);
			break;
		case 1: // '(' Expr ')' 
			node._node1.accept(this, argument);
			break;
		case 2: // ProcFuncCall 
			node._node1.accept(this, null);
			_interpreter.loadReturn();
			break;
		
		case 3: // ident [ Selector ] 
			
			t1 = (FFaplNodeToken) ((FFaplNodeSequence)node._node1.getNode()).elementAt(0);
			//t2 = (FFaplNodeOpt) ((FFaplNodeSequence)node._node1.getNode()).elementAt(1);
			s1 = new FFaplSymbol(t1.getToken().toString(), t1.getToken(), ISymbol.VARIABLE);
			s1 = _symbolTable.lookup(s1);
			//if(s1.)
			//System.out.println(s1.getType() +"is" );
			if(s1.getType().typeID() == FFaplTypeCrossTable.FFAPLRECORD){
				Object[] arguments = {s1, false}; //symbol, store
				node._node1.accept(this, arguments);
				
			}else{
				_interpreter.loadValue(s1.getOffset(), s1.isGlobal());
				//for array
				node._node1.accept(this, true);
			}
			break;
		case 4: // ArrayLen.
			node._node1.accept(this, argument);
			break;
		case 5: // ArgumentList
			node._node1.accept(this, argument);
			return null;
			//break;
		default:
			Object[] arguments = {"PrimaryExpr"};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
		}
		
		if(argument != null){
			
			if(argument instanceof ISymbol){
				s1 = (ISymbol) argument;
				if(s1.getType().typeID() != FFaplTypeCrossTable.FFAPLBOOLEAN &&
						s1.getType().typeID() != FFaplTypeCrossTable.FFAPLARRAY){
					_interpreter.loadValue(s1.getOffset(), s1.isGlobal());
					if(s1.getType().typeID() != FFaplTypeCrossTable.FFAPLPRIME){
						_interpreter.castElementOnStackTo(s1.getType().typeID());
					}else{
						//Prime
						_interpreter.castElementOnStackTo(IJavaType.INTEGER);
					}
				}else{
					//if end Type is boolean nothing is to do
				}
			}else if(argument instanceof Integer){
				switch (((Integer) argument).byteValue()){
				
				case FFaplTypeCrossTable.FFAPLINTEGER:
					_interpreter.pushStack(null);
					_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLINTEGER);	
					break;
				case FFaplTypeCrossTable.FFAPLPOLYNOMIAL:
					_interpreter.pushStack(null);
					_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLPOLYNOMIAL);
				case FFaplTypeCrossTable.FFAPLEC:
					_interpreter.pushStack(null);
					_interpreter.castElementOnStackTo(FFaplTypeCrossTable.FFAPLEC);
				}
			}else if(argument instanceof IJavaType){
				_interpreter.pushStack(argument);
				_interpreter.castElementOnStackTo(
						IJavaType.FFapl_Type_Compatibility[((IJavaType)argument).typeID()]);
				
			}
		}
		//System.out.println(_interpreter);
		
		return null;
	}

	@Override
	public Object visit(ASTPrimitiveType node, Object argument)
			throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTProc node, Object argument) throws FFaplException {
		
		//node._node1.accept(this, argument);
		//node._node2.accept(this, argument);
		//node._node3.accept(this, argument);
		//System.out.println(_interpreter);
		_interpreter.enterProcFunc();
		node._node4.accept(this, argument);
		//node._node5.accept(this, argument);
		node._node6.accept(this, argument);
		//System.out.println(_interpreter);
		_interpreter.exitProcFunc();
		//System.out.println(_interpreter);
		return null;
	}

	@Override
	public Object visit(ASTProcFuncCall node, Object argument)
			throws FFaplException {
		
		ISymbol s1;
		FFaplProcFuncSymbol pfs;
		FFaplPreProcFuncSymbol ppfs;
		//node._node1.accept(this, argument);
		//node._node2.accept(this, argument);
		
		//node._node4.accept(this, argument);
		s1 = node._procfuncSymbol;
		
		try{
			node._node3.accept(this, null);//Arguments
			
			if(s1 instanceof FFaplProcFuncSymbol){
				pfs = (FFaplProcFuncSymbol) s1;
				pfs.getTreeNode().accept(this, argument);
			}else if(s1 instanceof FFaplPreProcFuncSymbol){
				ppfs = (FFaplPreProcFuncSymbol) s1;
				ppfs.execute(_interpreter);
			}else{
				Object[] arguments = {"Interpreter ASTProcFuncCall"};
				throw new FFaplAlgebraicException(arguments, IAlgebraicError.INTERNAL);
			}
		}catch(FFaplException fe){
			fe.addToken(node._node1.getToken());
			throw fe;
		}
		
		return null;
	}

	@Override
	public Object visit(ASTProgram node, Object argument) throws FFaplException {
		//node._node1.accept(this, argument);
		//node._node2.accept(this, argument);
		//node._node3.accept(this, argument);
		node._node4.accept(this, argument);
		//node._node5.accept(this, argument);
		node._node6.accept(this, argument);
		node._node7.accept(this, argument);
		node._node8.accept(this, argument);
		node._node9.accept(this, argument);
		//System.out.print(this._interpreter);
		return null;
	}

	@Override
	public Object visit(ASTPsRandomGenerator node, Object argument)
			throws FFaplException {
		IPseudoRandomGenerator rg;
		BigInteger seed, max;
		
		//node._node1.accept(this, argument);
		node._node3.accept(this, argument);//seed
		node._node5.accept(this, argument);//max
		max = (BigInteger) _interpreter.popStack();
		seed = (BigInteger) _interpreter.popStack();
		rg = new PRNG_Standard(seed, max, _thread);
		_interpreter.pushStack(rg);
		
		return null;
	}

	@Override
	public Object visit(ASTRandom node, Object argument) throws FFaplException {
		FFaplNodeSequence s;
		ITrueRandomGenerator rg;
		BigInteger min, max;
		//BigInteger seed = BigInteger.valueOf(System.currentTimeMillis());
		//node._node1.accept(this, argument);
		node._node2.accept(this, FFaplTypeCrossTable.FFAPLINTEGER);
		if(node._node2.ispresent()){
			s = (FFaplNodeSequence) node._node2.getNode();
			if(((FFaplNodeOpt)s.elementAt(2)).ispresent()){
				//Random (Expr : Expr)
				max = (BigInteger) _interpreter.popStack();
				min = (BigInteger) _interpreter.popStack();
				rg = new RNG_Placebo(min, max, _thread);
			}else{ 
				//Random (Expr)
				max = (BigInteger) _interpreter.popStack();
				rg = new RNG_Placebo(max, _thread);
			}
		}else{
			//Random
			rg = new RNG_Placebo(_thread);
		}
		_interpreter.pushStack(rg.next());
		return null;
	}

	@Override
	public Object visit(ASTRandomGenerator node, Object argument)
			throws FFaplException {
		ITrueRandomGenerator rg;
		BigInteger min, max;
		
		//node._node1.accept(this, argument);
		node._node3.accept(this, argument);
		if(node._node4.ispresent()){
			node._node4.accept(this, argument);
			max = (BigInteger) _interpreter.popStack();
			min = (BigInteger) _interpreter.popStack();
			rg = new RNG_Placebo(min, max, _thread);
		}else{
			max = (BigInteger) _interpreter.popStack();
			rg = new RNG_Placebo(max, _thread);
		}
		_interpreter.pushStack(rg);
		
		return null;
	}

	@Override
	public Object visit(ASTRecord node, Object argument) throws FFaplException {
		node._node1.accept(this, argument);
		node._node2.accept(this, argument);
		node._node3.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTRelExpr node, Object argument) throws FFaplException {
		INode n;
		FFaplNodeSequence s;
		IToken tk = null;
		ASTRelOp relop;
		try{
			node._node1.accept(this, argument);
			//System.out.println(_interpreter);
			if(node._node2.ispresent()){
				n = node._node2.getNode();
				s = (FFaplNodeSequence) n;
				relop = (ASTRelOp)s.elementAt(0);
				tk = ((FFaplNodeToken)relop._node1.getNode()).getToken();
				
				node._node2.accept(this, argument);
				
				switch(relop._node1.getPos()){
				case 0: //'<='
				    _interpreter.isLessEqual();
				    break;
				case 1: // '>='
					_interpreter.isGreaterEqual();
					break;
				case 2: // '>'
					_interpreter.isGreater();
					break;
				case 3: // '<'
					_interpreter.isLess();
					break;
				}	
			}
		}catch(FFaplAlgebraicException e){
			if(tk != null){
				e.addToken(tk);
			}
			throw e;
		}
		//System.out.println(_interpreter);
		
		return null;
	}

	@Override
	public Object visit(ASTRelOp node, Object argument) throws FFaplException {
		node._node1.accept(this, argument);
		
		return null;
	}

	@Override
	public Object visit(ASTReturnStatement node, Object argument)
			throws FFaplException {
		node._node1.accept(this, argument);
		node._node2.accept(this, argument);
		node._node3.accept(this, argument);
		
		return null;
	}

	
	
	

	public Object visit(ASTSameAs node, Object argument) throws FFaplException
	{
		ISymbol s1 = new FFaplSymbol(node._node1.getToken().toString(),node._node1.getToken(), ISymbol.VARIABLE);
		ISymbol found = _symbolTable.lookup(s1);
		
		_interpreter.loadValue(found.getOffset(), found.isGlobal());

		return null;
	}
	
	
	
	
	@Override
	public Object visit(ASTSelector node, Object argument)
			throws FFaplException {
		
		FFaplNodeToken t1;
		Object[] arguments;
		ISymbol s1;
		FFaplNodeSequence sq1;
		Boolean tmp;
		switch (node._node1.getPos()){
		case 0: //Array
			tmp = (Boolean) argument;//load array until depth
			sq1 = (FFaplNodeSequence) node._node1.getNode();
			sq1.elementAt(1).accept(this, argument);//[ Expr ]
			if(((FFaplNodeOpt)sq1.elementAt(3)).ispresent() || tmp){
				_interpreter.loadArrayElement();
			}
			sq1.elementAt(3).accept(this, argument);
			
			break;
		case 1: //RecordElement
			arguments = (Object[]) argument;
			t1 = (FFaplNodeToken)((FFaplNodeSequence) node._node1.getNode()).elementAt(1);
			s1 = new FFaplSymbol(t1.getToken().toString(), t1.getToken(), ISymbol.VARIABLE);
			s1 = _symbolTable.lookuplocal((ISymbol)arguments[0], s1);
				
			if(((FFaplNodeOpt)(
					(FFaplNodeSequence)
							node._node1.getNode()).elementAt(2)).ispresent()){
				//there is another selector
				if(s1.getType().typeID() == FFaplTypeCrossTable.FFAPLARRAY){
					//array
					if((Boolean) arguments[1]){
						//store					
						_interpreter.loadValue(s1.getOffset(), false);
						node._node1.accept(this, false);
						
						((INode)arguments[2]).accept(this, s1);
						//cast needs reference object on stack, null is allowed for
						//algebraic and primitive Type
						//TODO integrate cast if needed in Version 1.0 no cast needed
						//_interpreter.pushStack(null);
					    //_interpreter.castElementOnStackTo(((FFaplArray)found.getType()).baseType().typeID());
						//System.out.println(_interpreter);
						_interpreter.storeArrayElement();
						
					}else{
					    //load
						_interpreter.loadValue(s1.getOffset(), s1.isGlobal());
						node._node1.accept(this, true);
					}
		
				}else{
					//Record
					arguments[0] = s1;
					node._node1.accept(this, arguments);
				}
				
			}else{
				if((Boolean) arguments[1]){
					//store
					((INode)arguments[2]).accept(this, s1);
					_interpreter.storeValue(s1.getOffset(), s1.isGlobal());
				}else{
				    //load
					_interpreter.loadValue(s1.getOffset(), s1.isGlobal());
				}
			}
		}
		
		return null;
	}

	@Override
	public Object visit(ASTStatement node, Object argument)
			throws FFaplException {
		return node._node1.accept(this, argument);
	}
	
	@Override
	public Object visit(ASTBlockStatement node, Object argument)
			throws FFaplException {
		return node._node1.accept(this, argument);
	}

	@Override
	public Object visit(ASTStatementList node, Object argument)
			throws FFaplException {
		return node._node1.accept(this, argument);
	}

	@Override
	public Object visit(ASTTerm node, Object argument) throws FFaplException {
		FFaplNodeSequence s;
		
		node._node1.accept(this, FFaplTypeCrossTable.FFAPLPOLYNOMIAL);
		
		switch (node._node1.getPos()){
		case 0:
		    //Expr [ IdTerm ]
			s = (FFaplNodeSequence) node._node1.getNode();
			if(!((FFaplNodeOpt)s.elementAt(1)).ispresent()){
				_interpreter.pushStack(new Polynomial(1,0, _thread));
			}
			_interpreter.mul();
			break;
		case 1:
			s = (FFaplNodeSequence) node._node1.getNode();
			if(((FFaplNodeOpt)s.elementAt(0)).ispresent()){
				_interpreter.neg();
			}
			//nothing todo
			break;
		}
		return null;
	}

	@Override
	public Object visit(ASTDeclType node, Object argument)
			throws FFaplException {
		node._node1.accept(this, argument);
		
		return null;
	}

	@Override
	public Object visit(ASTUnaryExpr node, Object argument)
			throws FFaplException {
		//node._node1.accept(this, argument);
		FFaplNodeChoice nc;
		node._node2.accept(this, argument);
		if(node._node1.ispresent()){
			nc = (FFaplNodeChoice)node._node1.getNode();
			switch(nc.getPos()){
			case 0://addop
				switch (((ASTAddOp)nc.getNode())._node1.getPos()){
				case 0://+ nothing to do
					break;
				case 1://- negative
					_interpreter.neg();
				}
				break;
			case 1://notop
				_interpreter.not();
			}
		}
		
		return null;
	}

	@Override
	public Object visit(ASTWhileStatement node, Object argument)
			throws FFaplException {
		//node._node1.accept(this, argument);
		try{
			JBoolean val;
			node._node2.accept(this, argument);
			val = (JBoolean) _interpreter.popStack();
			Object obj ;
			while(val.getValue()){
				isRunning();
				obj = node._node3.accept(this, argument);
				node._node2.accept(this, argument);
				val = (JBoolean) _interpreter.popStack();
				if(obj instanceof LoopState){
					if(LoopState.BREAK == obj){
						break;
					}
				}
			}
		}catch(FFaplException e){
				e.addToken(node._node1.getToken());
			throw e;
		}
		
		return null;
	}

	@Override
	public Object visit(FFaplNodeChoice node, Object argument)
			throws FFaplException {
		return node.getNode().accept(this, argument);
	}

	@Override
	public Object visit(FFaplNodeList node, Object argument)
			throws FFaplException {
		Object obj = null;
		for (final Iterator<INode> e = node.iterator(); e.hasNext();) {
			obj = e.next().accept(this, argument); 
			if(obj instanceof LoopState){
				if(LoopState.BREAK == obj){
					break;
				}
			}
		}
		return obj;
	}

	@Override
	public Object visit(FFaplNodeListOpt node, Object argument)
			throws FFaplException {
		Object obj = null;
		if(node.ispresent()){
			for (final Iterator<INode> e = node.iterator(); e.hasNext();) {
				e.next().accept(this, argument); 
				if(obj instanceof LoopState){
					if(LoopState.BREAK == obj){
						break;
					}
				}
			}
		}
		return obj;
	}

	@Override
	public Object visit(FFaplNodeOpt node, Object argument)
			throws FFaplException {
		if(node.ispresent()){
			return node.getNode().accept(this, argument);
		}
		return null;
	}

	@Override
	public Object visit(FFaplNodeSequence node, Object argument)
			throws FFaplException {
		Object obj = null;
		for (final Iterator<INode> e = node.iterator(); e.hasNext();) {
			  obj = e.next().accept(this, argument);
			  if(obj instanceof LoopState){
					if(LoopState.BREAK == obj){
						break;
					}
				}
		}
		return obj;
	}

	@Override
	public Object visit(FFaplNodeToken node, Object argument)
			throws FFaplException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(FFaplNodeType node, Object argument)
			throws FFaplException {
		return node.getNode().accept(this, argument);
	}

	@Override
	public Object visit(ASTRandomGeneratorType node, Object argument)
			throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}

	@Override
	public Object visit(ASTExprRandomGType node, Object argument)
			throws FFaplException {
		node._node1.accept(this, argument);
		return null;
	}
	
	
	/**
	  * throws an interrupt exception if not running
	  *@throws FFaplException
	  */
	  private void isRunning() throws FFaplException
	  {
		if(_thread != null){
			if(_thread.isInterrupted()){
				throw new FFaplException(null, ICompilerError.INTERRUPT);
			}
			
		}
	  }

	@Override
	public Object visit(ASTBreak node, Object argument) throws FFaplException {
		return LoopState.BREAK;
	}

	@Override
	public Vector<IAttribute> visit(ASTECBaseField node, Object symbol)
			throws FFaplException {
		
		/*
		//Fix Type of Declaration
		FFaplNodeChoice test1 = (FFaplNodeChoice) node.getParent(); 
		ASTDeclType test2 = (ASTDeclType) test1.getParent();
		ASTDecl test3 = (ASTDecl) test2.getParent();
		
		//Fix Type
		FFaplNodeToken t1 = test3._node1;
		ISymbol sym, fnd;
		sym = new FFaplSymbol(t1.getToken().toString(), t1.getToken(), ISymbol.VARIABLE);
		fnd = _symbolTable.lookup(sym);
		 */
		
		
		
		//Find EC
		EllipticCurve ec;
		ISymbol s1 = new FFaplSymbol(node._node2.getToken().toString(),node._node2.getToken(), ISymbol.VARIABLE);
		ISymbol found = _symbolTable.lookup(s1);
		
		if (found == null)
		{
			Object[] arguments = {"'" + node._node2.getToken() + "'"};
			throw new FFaplException(arguments,ICompilerError.IDENT_NOT_DECL,node._node2.getToken());
		}
		
		_interpreter.loadValue(found.getOffset(), found.isGlobal());
		
		ec = (EllipticCurve)_interpreter.popStack();
		if (ec.isGf() && node._node3.getPos() == 0) 
		{
			_interpreter.pushStack(ec.getGF().clone());
		}
		else if (!ec.isGf() && node._node3.getPos() == 1)
		{
			_interpreter.pushStack(ec.getRC().clone());
		}
		else
		{
			FFaplAlgebraicException e = new FFaplAlgebraicException(null, IAlgebraicError.EC_BASEFIELD_ERROR);
			e.addToken(node._node1.getToken());
			throw e;
		}


		return null;
	}

	@Override
	public Object visit(ASTECAssignment node, Object argument)
			throws FFaplException {

		_symbolTable.setScope(node.getSymbolScope());


		EllipticCurve ec;
		

		ISymbol s1 = new FFaplSymbol(node._node1.getToken().toString(),node._node1.getToken(), ISymbol.VARIABLE);
		ISymbol found1 = _symbolTable.lookup(s1); //x
		
		ISymbol s2 = new FFaplSymbol(node._node2.getToken().toString(),node._node2.getToken(), ISymbol.VARIABLE);
		ISymbol found2 = _symbolTable.lookup(s2); //y

		ISymbol s3 = new FFaplSymbol(node._node3.getToken().toString(),node._node3.getToken(), ISymbol.VARIABLE);
		ISymbol found3 = _symbolTable.lookup(s3);
		
		
		_interpreter.loadValue(found3.getOffset(), found3.isGlobal());
		
		ec = (EllipticCurve)_interpreter.popStack();
		
		if (ec.isGf())
		{
			GaloisField x = ec.getGF().clone(),y = ec.getGF().clone();
			x.setValue(ec.getX_gf());
			y.setValue(ec.getY_gf());
			
			_interpreter.pushStack(x);
			_interpreter.storeValue(found1.getOffset(), false);
			
			_interpreter.pushStack(y);
			_interpreter.storeValue(found2.getOffset(), false);
		}
		else
		{
			ResidueClass x = ec.getRC().clone(),y = ec.getRC().clone();
			x.setValue(ec.getX_rc());
			y.setValue(ec.getY_rc());
			
			_interpreter.pushStack(x);
			_interpreter.storeValue(found1.getOffset(), false);
			
			_interpreter.pushStack(y);
			_interpreter.storeValue(found2.getOffset(), false);
		}
		
		return null;
	}

	

}
