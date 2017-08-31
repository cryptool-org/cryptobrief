/**
 * 
 */
package ffapl.java.classes;

import java.util.Vector;

import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.java.interfaces.IJavaType;


/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class Array implements IJavaType{

	protected int _dim;
	protected Object[] _array;
	protected int _baseType;
	protected IJavaType _jType;
	protected Thread _thread;
	
	
	public Array(int type, int dim, Thread thread) {
		_baseType = type;
		_dim = dim;
		_array = null;
		_jType = null;
		_thread = thread;
	}
	
	public Array(int type, int dim, Object[] array, IJavaType jType, Thread thread){
		this(type, dim, jType, thread);
		_array = array;
	}
	
	public Array(int type, int dim, IJavaType jType, Thread thread){
		this(type, dim, thread);
		_jType = jType;
	}
	
	
	/**
	 * returns the Type id of the array according FFAPL Types
	 * @return
	 */
	public int getBaseType(){
		return _baseType;
	}
	
	/**
	 * Returns the jType if ComplexAlgebraicType or null
	 * @return
	 */
	public IJavaType getjType(){
		return _jType;
	}
	
	/**
	 * Return length of array
	 * @return
	 * @throws FFaplAlgebraicException 
	 */
	public int length() throws FFaplAlgebraicException{
		if(_array != null){
			return _array.length;
		}else{
			Object[] arguments = {classInfo()};
			throw new FFaplAlgebraicException(arguments, IAlgebraicError.VALUE_IS_NULL);
		}
	}
	
	/**
	 * Returns the dimension of the array
	 * @return
	 */
	public int dim(){
		return _dim;
	}
	
	/**
	 * Checks if two Arrays are compatible
	 * @param arr
	 * @return
	 */
	public boolean isCompatibleTo(Array arr){
		if(arr.getBaseType() == _baseType && arr.dim() == _dim){
			if(_jType != null){
				return _jType.equals(arr.getjType());
			}
			return true;
		}else{
			return false;
		}
	}
	
	public void setValue(Vector<Integer> pos, IJavaType obj) throws FFaplAlgebraicException{
		Object result = _array;
		try{
			if(pos.size() <= _dim){
				
				for(int i = 0; i < pos.size() - 1; i++){
					result = ((Object[])result)[pos.elementAt(i)];
				}
				if(obj.typeID() == IJavaType.ARRAY){
					((Object[])result)[pos.lastElement()] = ((Array)obj).getArray();
				}else{
					//if(_baseType == FFaplTypeCrossTable.FFAPLPRIME){
					//	((Object[])result)[pos.lastElement()] = new Prime((BInteger)obj, _thread);
					//}else{
						((Object[])result)[pos.lastElement()] = obj;
					//}
				}
				//if(pos.size() != _dim){
					//subarray
				//	result = new Array(_type, _dim - pos.size(), (Object[]) result);
				//}
			}else{
				Object[] messages ={"Subarray"};
				throw new FFaplAlgebraicException(messages, IAlgebraicError.INTERNAL);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			throw new FFaplAlgebraicException(null, IAlgebraicError.ARR_INDEX_OUT_OF_BOUNDS);
		}
	}
	
	/**
	 * set the array value
	 * @param intValue
	 * @param c
	 * @throws FFaplAlgebraicException
	 */
	public void setValue(int intValue, IJavaType c) throws FFaplAlgebraicException {
		Vector<Integer> pos = new Vector<Integer>();
		pos.add(intValue);
		setValue(pos, c);
	}	
	
	/**
	 * Returns the value or subarray of the Array at the specified position
	 * @param pos
	 * @return
	 * @throws FFaplAlgebraicException
	 */
	public IJavaType getValue(Vector<Integer> pos) throws FFaplAlgebraicException{
		IJavaType result = null;
		Object tmp = _array;
		try{
			if(pos.size() <= _dim){
				
				for(int i = 0; i < pos.size(); i++){
					tmp = ((Object[])tmp)[pos.elementAt(i)];
				}
				//System.out.println(result instanceof Array);
				if(pos.size() != _dim){
					//subarray
					if(tmp instanceof Array){
						
						tmp = ((Array)tmp).getArray();
					}
					result = new Array(_baseType, _dim - pos.size(), (Object[]) tmp, _jType, _thread);
				}else{
					result = (IJavaType) tmp;
				}
			}else{
				Object[] messages ={"Subarray"};
				throw new FFaplAlgebraicException(messages, IAlgebraicError.INTERNAL);
			}	
			return result;
		}catch(ArrayIndexOutOfBoundsException e){
			throw new FFaplAlgebraicException(null, IAlgebraicError.ARR_INDEX_OUT_OF_BOUNDS);
		}
	}
	
	/**
	 * return the array value
	 * @param intValue
	 * @return
	 * @throws FFaplAlgebraicException 
	 */
	public IJavaType getValue(int intValue) throws FFaplAlgebraicException {
		Vector<Integer> pos = new Vector<Integer>();
		pos.add(intValue);
		return getValue(pos);
	}

	
	/**
	 * Returns the internal Array
	 * @return
	 */
	public Object getArray(){
		return _array;
	}

	@Override
	public int typeID() {
		return IJavaType.ARRAY;
	}

	@Override
	public String classInfo() {
		return "Array";
	}
	
	@Override
	public Array clone(){
		IJavaType jType = null;
		Object[] array = null;
		
		if(_jType != null){
			jType = _jType.clone();
		}
		
		if(_array != null){
			array = _array.clone();
		}
		
		return new Array(_baseType, _dim, array, jType, _thread);
	}
	
	@Override
	public String toString(){
		return printArray(_array);
	}
	
	/**
	 * Returns true if the array is not initialized
	 * @return
	 */
	public boolean isNull(){
		return _array == null;
	}
	
	/**
	 * create string from Array
	 * @param array
	 * @return
	 */
	private String printArray(Object[] array){
		String result = "";
		Object val;
		if(array != null){
			result = "{";
			for(int i = 0; i < array.length; i++){
				val = array[i];
				if (val instanceof Object[]){
					result = result + (printArray((Object[])val));
				}else if(val!= null){
					result = result + (val.toString());
				}else{
					result = result + "NULL";
				}
				if(i < array.length - 1){
					result = result + ", ";
				}	
			}
			result = result + "}";
		}else{
			result = null;
		}
		return result ;
	}
	
	@Override
	public boolean equalType(Object type) {
		Array arr;
		
		if(type instanceof Array){
			arr = (Array) type;
			
			if(arr.getBaseType() == _baseType && arr.dim() == _dim){
				
				if(_jType != null &&  arr.getjType()!= null){ 
					return _jType.equalType(arr.getjType());
				}else{
					return true;
				}
			}
		}
		return false;
	}
	
	
	

}
