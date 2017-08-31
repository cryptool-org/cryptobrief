package ffapl.lib.interfaces;

import ffapl.exception.FFaplException;

public interface IAttributeOperation {

	/**
	 * Unary Operation
	 * @param op
	 * @param a1
	 * @return
	 * @throws FFaplException
	 *         <ILLEGAL_OP1_TYPE> Illegal operand type for unary operator.
	 * @throws FFaplException
	 *         <INTERNAL> if internal error occurs
	 */
	public IAttribute op1 (IToken op, IAttribute a1) throws FFaplException;
	
	/**
	 * Binary Operation
	 * @param a1
	 * @param op
	 * @param a2
	 * @return
	 * @throws FFaplException
	 *         <ILLEGAL_OP2_TYPE> Illegal operand type for binary operator.
	 * @throws FFaplException
	 *         <INTERNAL> if internal error occurs         
	 */
	public IAttribute op2 (IAttribute a1, IToken op, IAttribute a2) throws FFaplException;
	
	/**
	 * Binary Operation power
	 * @param a1
	 * @param op
	 * @param a2
	 * @return
	 * @throws FFaplException
	 *         <ILLEGAL_OP2_TYPE> Illegal operand type for binary operator.
	 * @throws FFaplException
	 *         <INTERNAL> if internal error occurs         
	 */
	public IAttribute pow (IAttribute a1, IToken op, IAttribute a2) throws FFaplException;
	
	/**
	 * Binary Boolean Operation
	 * @param a1
	 * @param op
	 * @param a2
	 * @return
	 * @throws FFaplException
	 *         <ILLEGAL_OP2_TYPE> Illegal operand type for binary operator.
	 * @throws FFaplException
	 *         <INTERNAL> if internal error occurs         
	 */
	public IAttribute op2boolean (IAttribute a1, IToken op, IAttribute a2) throws FFaplException;
	
	/**
	 * Relation Operation
	 * @param a1
	 * @param op
	 * @param a2
	 * @return
	 * @throws FFaplException
	 *         <ILLEGAL_RELOP_TYPE> Illegal operand type for relational operator.
	 * @throws FFaplException
	 *         <INTERNAL> if internal error occurs         
	 */
	public IAttribute relop (IAttribute a1, IToken op, IAttribute a2) throws FFaplException;

	/**
	 * equality Operation
	 * @param a1
	 * @param op
	 * @param a2
	 * @return
	 * @throws FFaplException
	 *         <ILLEGAL_EQUALOP_TYPE> Illegal operand type for equality operator.
	 * @throws FFaplException
	 *         <INTERNAL> if internal error occurs         
	 */
	public IAttribute equalop (IAttribute a1, IToken op, IAttribute a2) throws FFaplException;

	/**
	 * Assignment a1:=a2
	 * @param a1
	 * @param a2
	 * @throws FFaplException
	 *         <TYPE_MISMATCH_ASSIGN> type mismatch in assignment.
	 * @throws FFaplException
	 *         <INTERNAL> if internal error occurs 
	 */
	public void assignment (IAttribute a1, IToken t1, IAttribute a2) throws FFaplException;

	
	/**
	 * ArrayLen # a1
	 * @param a1
	 * @param op
	 * @return
	 * @throws FFaplException
	 *         <ARRAY_LEN_NOT_ARRAY> Expression after '#' is not an array type.
	 * @throws FFaplException
	 *         <INTERNAL> if internal error occurs
	 */
	public IAttribute arraylen(IToken op, IAttribute a1) throws FFaplException;
	
	
	
	
}
