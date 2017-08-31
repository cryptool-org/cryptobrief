/**
 * 
 */
package ffapl.types;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ContainerType extends Type {

	/**
	 * 
	 */
	public ContainerType() {
		_typeID =  FFaplTypeCrossTable.FFAPLCONTAINERTYPE;
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
