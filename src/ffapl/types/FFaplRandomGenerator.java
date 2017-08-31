package ffapl.types;

/**
 * Represents a Randomgenerator type for the Compiler
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplRandomGenerator extends RandomGeneratorType {

	/**
	 * Constructor
	 */
	public FFaplRandomGenerator(){
		 _typeID =  FFaplTypeCrossTable.FFAPLRANDOMG;
	}
	
	@Override 
	public boolean isCompatibleTo(Type type){
		return type instanceof FFaplInteger || 
		type instanceof FFaplResidueClass ||
		type instanceof FFaplPolynomial;
	}
	
	public Type clone(){
		return new  FFaplRandomGenerator();
	}	
}
