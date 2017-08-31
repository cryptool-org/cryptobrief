package ffapl.types;

public class ComplexAlgebraicType extends Type {

	
	
	public ComplexAlgebraicType(){
		//_typeID = "Complex Type";
	}
	@Override
	public boolean isCompatibleTo(Type type) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Type clone() {
		return new ComplexAlgebraicType();
	}
	
}
