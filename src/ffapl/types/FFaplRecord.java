package ffapl.types;

/**
 * Represents a Record type for the Compiler
 * @author Alexander Ortner
 * @version 1.0
 */
public class FFaplRecord extends ContainerType {
	
	/**
	 * Constructor
	 */
	 public FFaplRecord(){
		 _typeID =  FFaplTypeCrossTable.FFAPLRECORD;
	 }
	 
	 @Override 
	 public boolean isCompatibleTo(Type type){
			return type instanceof FFaplRecord;
	 }
	 
	 public Type clone(){
			return new  FFaplRecord();
		}	
}
