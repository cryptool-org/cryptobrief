package ffapl.types;

public class PrimitiveType extends Type {

	 
	
	public PrimitiveType(){
		_typeID =  FFaplTypeCrossTable.FFAPLPRIMITIVETYPE;
	}
	
	
	@Override
	public boolean isCompatibleTo(Type type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Type clone() {
		return null;
	}

}
