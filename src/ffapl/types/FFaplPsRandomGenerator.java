package ffapl.types;

/**
 * Represents a Pseudorandomgenerator type for the Compiler
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplPsRandomGenerator extends RandomGeneratorType {

	/**
	 * Constructor
	 */
	public FFaplPsRandomGenerator(){
		_typeID = FFaplTypeCrossTable.FFAPLPSRANDOMG;
	}
	
	@Override 
	public boolean isCompatibleTo(Type type){
		return type instanceof FFaplInteger || 
		type instanceof FFaplResidueClass ||
		type instanceof FFaplPolynomial;
	}
	
	public Type clone(){
		return new  FFaplPsRandomGenerator();
	}	
	
}
