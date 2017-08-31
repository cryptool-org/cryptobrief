package ffapl.types;

public class FFaplPolynomial extends AlgebraicType {


	
	public FFaplPolynomial(){
		_typeID = FFaplTypeCrossTable.FFAPLPOLYNOMIAL;
	}
	
	public Type clone() {
		return new FFaplPolynomial();
	}	
	
	@Override 
	public boolean isCompatibleTo(Type type){
		return type instanceof FFaplPolynomial ||
				type instanceof FFaplGaloisField ||
				type instanceof FFaplInteger ||
				type instanceof FFaplPrime;
	}
}
