package ffapl.types;

public class FFaplArray extends Type {
	
	// the dimensions of the type
	protected int _dim;
	
	// the baseType
	protected Type _baseType;
	
	protected int _length;
	
	
	public FFaplArray(Type baseType, int dim){
		_dim = dim;
		_baseType = baseType;
		_typeID = FFaplTypeCrossTable.FFAPLARRAY;
		_length = 0;
	}
	
	@Override 
	public boolean isCompatibleTo(Type type){
		return this.equals(type);
	}
	
	@Override
	public Type clone() {
		return new FFaplArray(_baseType.clone(), _dim);
	}
	
	/**
	 * Returns the dimension of the array
	 * @return
	 */
	public int getDim(){
		return _dim;
	}
	
	
	
	/**
	 * 
	 * @return length of the top most dim
	 */
	public int getLength(){
		return _length;
	}
	
	/**
	 * returns the base Type of the Array
	 * @return
	 */
	public Type baseType(){
		return _baseType;
	}
	
	/**
	 * sets the length of the top most dimension
	 * @param length
	 */
	public void setLength(int length){
		_length = length;
	}
	
	@Override
	public boolean equals(Type type){
		if(type instanceof FFaplArray){
			return ((FFaplArray)(type)).getDim() == this.getDim() &&
			        _baseType.equals(((FFaplArray)(type)).baseType());
		}else{
			return false;
		}	
	}
	
	/**
	 * the array type B derived from A by omitting the last k dimensions
	 * @param k
	 * @return
	 */
	public Type subarray(int k){
		FFaplArray result = null;
		if(_dim - k > 0){
			result = new FFaplArray(_baseType, _dim - k);
			return result;
		}else if(_dim - k == 0){
			return this.baseType();
		}else{
			return null; 
		}
	}
	
	@Override
	public String toString(){
		
		String out = _baseType.toString();
		for(int i = 0; i < _dim ; i++){
				out = out + "[]";
		}
		return out;
	}

}
