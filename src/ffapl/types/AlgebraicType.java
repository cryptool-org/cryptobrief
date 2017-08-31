/**
 * 
 */
package ffapl.types;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class AlgebraicType extends Type {

	/**
	 * 
	 */
	public AlgebraicType() {
		_typeID =  FFaplTypeCrossTable.FFAPLALGEBRAICTYPE;
	}

	/* (non-Javadoc)
	 * @see ffapl.types.Type#isCompatibleTo(ffapl.types.Type)
	 */
	@Override
	public boolean isCompatibleTo(Type type) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see ffapl.types.Type#clone()
	 */
	@Override
	public Type clone() {
		return null;
	}

}
