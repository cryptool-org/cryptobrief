package ffapl.types;

public class FFaplPolynomialResidue extends ComplexAlgebraicType {


	
	public FFaplPolynomialResidue(){
		_typeID = FFaplTypeCrossTable.FFAPLPOLYNOMIALRESIDUE;
	}
	
	public Type clone() {
		return new FFaplPolynomialResidue();
	}	
	
	@Override 
	public boolean isCompatibleTo(Type type){
		try {
			throw new Exception();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
