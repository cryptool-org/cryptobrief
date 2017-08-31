/**
 * 
 */
package ffapl.lib;

import ffapl.FFaplInterpreter;
import ffapl.FFaplInterpreterConstants;
import ffapl.exception.FFaplException;
import ffapl.exception.FFaplWarning;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.logging.FFaplLogger;
import ffapl.lib.interfaces.IAttribute;
import ffapl.lib.interfaces.IAttributeOperation;
import ffapl.lib.interfaces.ICompilerError;
import ffapl.lib.interfaces.IToken;
import ffapl.types.FFaplArray;
import ffapl.types.FFaplBoolean;
import ffapl.types.FFaplInteger;
import ffapl.types.FFaplTypeConversation;
import ffapl.types.FFaplTypeCrossTable;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplAttributeOperation implements IAttributeOperation {

	FFaplTypeConversation _typeconv;
	FFaplLogger _logger;
	/**
	 * @param _logger 
	 * 
	 */
	public FFaplAttributeOperation(FFaplLogger logger) {
		_typeconv = new FFaplTypeConversation();
		_logger = logger;
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttributeOperation#op1(ffapl.lib.interfaces.IToken, ffapl.lib.interfaces.IAttribute)
	 */
	@Override
	public IAttribute op1(IToken op, IAttribute a1) throws FFaplException {
		
		if(a1 == null || op == null){
			Object[] arguments = {"AttributeOperation op1"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		
				
		if(!op.toString().equals(FFaplInterpreter.tokenImage[FFaplInterpreter.NOT].substring(1, 1 + op.toString().length()))){
			if(!FFaplTypeCrossTable.OP1_compatibility[a1.getType().typeID()]){
				Object[] arguments = {"'" + op.toString() + "'","'" + a1.getType() + "'", 
						FFaplTypeConversation.getExpected(FFaplTypeCrossTable.OP1_compatibility)};//for error
				throw new FFaplException(arguments, ICompilerError.ILLEGAL_OP1_TYPE, op);
			}
		}else{
			if(a1.getType().typeID() != FFaplTypeCrossTable.FFAPLBOOLEAN){
				Object[] arguments = {"'" + op.toString() + "'","'" + a1.getType() + "'", 
						FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLBOOLEAN]};//for error
				throw new FFaplException(arguments, ICompilerError.ILLEGAL_OP1_TYPE, op);
			}
		}
		
		
		return a1;
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttributeOperation#op2(ffapl.lib.interfaces.IAttribute, ffapl.lib.interfaces.IToken, ffapl.lib.interfaces.IAttribute)
	 */
	@Override
	public IAttribute op2(IAttribute a1, IToken op, IAttribute a2)
			throws FFaplException {
		
		if(a1 == null || op == null || a2 == null){
			Object[] arguments = {"AttributeOperation op2"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		//System.out.println(a1.getType().typeID()+ "..." +a2.getType().typeID());
		if(!FFaplTypeCrossTable.OP2_compatibility[a1.getType().typeID()][a2.getType().typeID()]){
			Object[] arguments = {"'" + a1.getType() + "; " + a2.getType() + "'", "'" + op.toString() + "'", 
								  a1.getType() + " " + op.toString() + " " + a2.getType()};//for error
			throw new FFaplException(arguments, ICompilerError.ILLEGAL_OP2_TYPE, op);
		}else if((a2.getType().typeID() == FFaplTypeCrossTable.FFAPLRESIDUECLASS || 
				a2.getType().typeID() == FFaplTypeCrossTable.FFAPLGF ||
				//a2.getType().typeID() == FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE ||
				a1.getType().typeID() == FFaplTypeCrossTable.FFAPLRESIDUECLASS || 
				a1.getType().typeID() == FFaplTypeCrossTable.FFAPLGF //||
				//a1.getType().typeID() == FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE
				)	&& 
				(op.toString().equals(FFaplTypeConversation.getTokenString(FFaplInterpreterConstants.MODULO)) )){
			//for String only + allowed
			Object[] arguments = {"'" + a1.getType() + "; " + a2.getType() + "'", "'" + op.toString() + "'", 
					  a1.getType() + " " + op.toString() + " " + a2.getType()};//for error
					  throw new FFaplException(arguments, ICompilerError.ILLEGAL_OP2_TYPE, op);
		}
		/*
		if(a1.getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGERRESIDUE && a1.getType().typeID() == a2.getType().typeID()){
			//IntegerResidue with different UUID are per definition of FFapl 1.0 not compatible
			if(((FFaplIntegerResidue)a1.getType()).getUUID().compareTo(((FFaplIntegerResidue)a2.getType()).getUUID()) != 0 ){
				throw new FFaplException("illegal operand type for binary operator '" +
						  op.toString() + "'" + 
						  " ---> " + a1.getType() + " " + op.toString() + " " +
						  a2.getType() , op, ICompilerError.ILLEGAL_OP2_TYPE);
			}
		}/*else if(a1.getType().typeID() == FFaplTypeCrossTable.FFAPLGF && a1.getType().typeID() == a2.getType().typeID()){
				//System.out.println("hier");
				//Galois Fields with different UUID are per definition of FFapl 1.0 not compatible
				if(((FFaplGaloisField)a1.getType()).getUUID().compareTo(((FFaplGaloisField)a2.getType()).getUUID()) != 0 ){
					throw new FFaplException("illegal operand type for binary operator '" +
							  op.toString() + "'" + 
							  " ---> " + a1.getType() + " " + op.toString() + " " +
							  a2.getType() , op, ICompilerError.ILLEGAL_OP2_TYPE);
				}
		}*/
		
		if((a2.getType().typeID() == FFaplTypeCrossTable.FFAPLSTRING || 
				a1.getType().typeID() == FFaplTypeCrossTable.FFAPLSTRING )	&& 
				( !op.toString().equals(FFaplTypeConversation.getTokenString(FFaplInterpreterConstants.PLUS)) )){
			//for String only + allowed
			Object[] arguments = {"'" + a1.getType() + "; " + a2.getType() + "'", "'" + op.toString() + "'", 
					  a1.getType() + " " + op.toString() + " " + a2.getType()};//for error
					  throw new FFaplException(arguments, ICompilerError.ILLEGAL_OP2_TYPE, op);
		}
		
		
		if (((a1.getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER || a1.getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME) && a2.getType().typeID() == FFaplTypeCrossTable.FFAPLEC)
				|| ((a2.getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER || a2.getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME) && a1.getType().typeID() == FFaplTypeCrossTable.FFAPLEC))
		{
			if (op.toString().equals(FFaplTypeConversation.getTokenString(FFaplInterpreterConstants.PLUS)) || op.toString().equals(FFaplTypeConversation.getTokenString(FFaplInterpreterConstants.MINUS)))
			{
				Object[] arguments = {"'" + a1.getType() + "; " + a2.getType() + "'", "'" + op.toString() + "'", 
						  a1.getType() + " " + op.toString() + " " + a2.getType()};//for error
						  throw new FFaplException(arguments, ICompilerError.ILLEGAL_OP2_TYPE, op);
			}
		}
		
		if((a2.getType().typeID() == FFaplTypeCrossTable.FFAPLEC && 
				a1.getType().typeID() == FFaplTypeCrossTable.FFAPLEC )	&& 
				( !op.toString().equals(FFaplTypeConversation.getTokenString(FFaplInterpreterConstants.PLUS)) &&  !op.toString().equals(FFaplTypeConversation.getTokenString(FFaplInterpreterConstants.MINUS)))){
			Object[] arguments = {"'" + a1.getType() + "; " + a2.getType() + "'", "'" + op.toString() + "'", 
					  a1.getType() + " " + op.toString() + " " + a2.getType()};//for error
					  throw new FFaplException(arguments, ICompilerError.ILLEGAL_OP2_TYPE, op);
		}
		

		
		
	        //System.out.println(a1.getType() + " " + op.toString() + " " +
					  //a2.getType() + " = " + _typeconv.max(a1.getType(), a2.getType())) ;
			return new FFaplAttribute(IAttribute.REGISTER, FFaplTypeConversation.max(a1.getType(), a2.getType()));
	}
	
	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttributeOperation#pow(ffapl.lib.interfaces.IAttribute, ffapl.lib.interfaces.IToken, ffapl.lib.interfaces.IAttribute)
	 */
	@Override
	public IAttribute pow(IAttribute a1, IToken op, IAttribute a2)
			throws FFaplException {
		
		if(a1 == null || op == null || a2 == null){
			Object[] arguments = {"AttributeOperation pow"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		
		if(!FFaplTypeCrossTable.POW_compatibility[a1.getType().typeID()][a2.getType().typeID()]){
			Object[] arguments = {"'" + a1.getType() + "; " + a2.getType() + "'", "'" + op.toString() + "'", 
					  a1.getType() + " " + op.toString() + " " + a2.getType()};//for error
			throw new FFaplException(arguments, ICompilerError.ILLEGAL_OP2_TYPE, op);
		}
		if(a2.getType().typeID() == FFaplTypeCrossTable.FFAPLRESIDUECLASS){
			Object[] arguments = {"'" + a2.getType() + "'","'" + new FFaplInteger() + "'"};//for warning
			
			_logger.log(new FFaplWarning(arguments,IAlgebraicError.WARNING_IMPLICIT_CAST, op));
		}
			return new FFaplAttribute(IAttribute.REGISTER, a1.getType());
	}
	
	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttributeOperation#op2boolean(ffapl.lib.interfaces.IAttribute, ffapl.lib.interfaces.IToken, ffapl.lib.interfaces.IAttribute)
	 */
	@Override
	public IAttribute op2boolean(IAttribute a1, IToken op, IAttribute a2)
			throws FFaplException {
		
		if(a1 == null || op == null || a2 == null){
			Object[] arguments = {"AttributeOperation op2boolean"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		
		if(a1.getType().typeID() == a2.getType().typeID() &&
				a1.getType().typeID() == FFaplTypeCrossTable.FFAPLBOOLEAN ){
			return new FFaplAttribute(IAttribute.REGISTER, new FFaplBoolean());
		}
		else if ((a1.getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER || a1.getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME) &&
				(a2.getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER || a2.getType().typeID() == FFaplTypeCrossTable.FFAPLPRIME) )
		{
			//if you use logical operators for integers 
			return new FFaplAttribute(IAttribute.REGISTER, new FFaplInteger());
		}
			Object[] arguments = {"'" + a1.getType() + "; " + a2.getType() + "'", "'" + op.toString() + "'", 
					  a1.getType() + " " + op.toString() + " " + a2.getType()};//for error
			throw new FFaplException(arguments, ICompilerError.ILLEGAL_OP2_TYPE, op);
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttributeOperation#relop(ffapl.lib.interfaces.IAttribute, ffapl.lib.interfaces.IToken, ffapl.lib.interfaces.IAttribute)
	 */
	@Override
	public IAttribute relop(IAttribute a1, IToken op, IAttribute a2)
			throws FFaplException {
		if(a1 == null || op == null || a2 == null){
			Object[] arguments = {"AttributeOperation relop"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		if(!FFaplTypeCrossTable.RELOP_compatibility[a1.getType().typeID()][a2.getType().typeID()]){
			Object[] arguments = {"'" + a1.getType() + "; " + a2.getType() + "'", "'" + op.toString() + "'", 
					  a1.getType() + " " + op.toString() + " " + a2.getType()};//for error
			throw new FFaplException(arguments, ICompilerError.ILLEGAL_OP2_TYPE, op);
		}
				
		return new FFaplAttribute(IAttribute.REGISTER, new FFaplBoolean());
	}

	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttributeOperation#equalop(ffapl.lib.interfaces.IAttribute, ffapl.lib.interfaces.IToken, ffapl.lib.interfaces.IAttribute)
	 */
	@Override
	public IAttribute equalop(IAttribute a1, IToken op, IAttribute a2)
			throws FFaplException {
		if(a1 == null || op == null || a2 == null){
			Object[] arguments = {"AttributeOperation equalop"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		if(!FFaplTypeCrossTable.EQUALOP_compatibility[a1.getType().typeID()][a2.getType().typeID()]){
			Object[] arguments = {"'" + a1.getType() + "; " + a2.getType() + "'", "'" + op.toString() + "'", 
					  a1.getType() + " " + op.toString() + " " + a2.getType()};//for error
			throw new FFaplException(arguments, ICompilerError.ILLEGAL_OP2_TYPE, op);
			
		}
		
		
		
		return new FFaplAttribute(IAttribute.REGISTER, new FFaplBoolean());
	}
//assign
	/* (non-Javadoc)
	 * @see ffapl.lib.interfaces.IAttributeOperation#assignment(ffapl.lib.interfaces.IAttribute, ffapl.lib.interfaces.IAttribute)
	 */
	@Override
	public void assignment(IAttribute a1, IToken t1, IAttribute a2)
			throws FFaplException {
		FFaplArray ar1, ar2;
		
		if(a1 == null || a2 == null){
			Object[] arguments = {"AttributeOperation assignment"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		
		//System.out.println(a1.getType() + " := " + a2.getType());
		
		if(!FFaplTypeCrossTable.ASSIGN_compatibility[a1.getType().typeID()][a2.getType().typeID()]){
			Object[] arguments = {a1.getType() + " " + t1.toString() + " " + a2.getType()};//for error
			throw new FFaplException(arguments, ICompilerError.TYPE_MISMATCH_ASSIGN, t1);
		}
		/*
		if(a1.getType().typeID() == FFaplTypeCrossTable.FFAPLGF && 
				a1.getType().typeID() == a2.getType().typeID()){
			//Galois Fields with different UUID are per definition of FFapl 1.0 not compatible
			if(((FFaplGaloisField)a1.getType()).getUUID().compareTo(((FFaplGaloisField)a2.getType()).getUUID()) != 0 ){
				throw new FFaplException("type missmatch in assignment '" +
						  t1.toString() + "'" + 
						  " ---> " + a1.getType() + " " +  t1.toString() + " " +
						  a2.getType() , t1, ICompilerError.TYPE_MISMATCH_ASSIGN);
			}
		}else if(a1.getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGERRESIDUE && 
				a1.getType().typeID() == a2.getType().typeID()){
			//Residual Class with different UUID are per definition of FFapl 1.0 not compatible
			if(((FFaplIntegerResidue)a1.getType()).getUUID().compareTo(((FFaplIntegerResidue)a2.getType()).getUUID()) != 0 ){
				throw new FFaplException("type missmatch in assignment '" +
						  t1.toString() + "'" + 
						  " ---> " + a1.getType() + " " +  t1.toString() + " " +
						  a2.getType() , t1, ICompilerError.TYPE_MISMATCH_ASSIGN);
			}
		}else */ if(a1.getType().typeID() == FFaplTypeCrossTable.FFAPLARRAY){
			ar1 = (FFaplArray) a1.getType();
			ar2 = (FFaplArray) a2.getType();
			
			if(ar1.getDim() != ar2.getDim() || ar1.baseType().typeID() != ar2.baseType().typeID()){
				//dimensions not equal or Type not equal
				Object[] arguments = {a1.getType() + " " + t1.toString() + " " + a2.getType()};//for error
				throw new FFaplException(arguments, ICompilerError.TYPE_MISMATCH_ASSIGN, t1);
			}
		}
	 
	}

	@Override
	public IAttribute arraylen(IToken op, IAttribute a1) throws FFaplException {
		if(a1 == null){
			Object[] arguments = {"AttributeOperation arraylen"};
			throw new FFaplException(arguments, ICompilerError.INTERNAL);
		}
		
		if(a1.getType().typeID() != FFaplTypeCrossTable.FFAPLARRAY){
			Object[] arguments = {"'" + op.toString() + "'", op.toString() + " " + a1.getType()};//for error
			throw new FFaplException(arguments, ICompilerError.ARRAY_LEN_NOT_ARRAY, op);
		}
		return new FFaplAttribute(IAttribute.REGISTER, new FFaplInteger());
	}

}
