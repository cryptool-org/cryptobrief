package ffapl.types;

import java.util.Iterator;
import java.util.Vector;

import ffapl.FFaplInterpreterConstants;
import ffapl.exception.FFaplException;
import ffapl.lib.interfaces.IAttribute;
import ffapl.lib.interfaces.ICompilerError;

public class FFaplTypeConversation implements FFaplTypeCrossTable {
	
	/**
	 * least upper bound of types t1 and t2 in the widening hierarchy
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static Type max(Type t1, Type t2) throws FFaplException{
		
		
		if(t1.typeID() ==  FFAPLSTRING){
			return t1.clone();
		}else if(t2.typeID() == FFAPLSTRING){
			return t2.clone();
		}else if(t1.typeID() == FFAPLINTEGER){
			if(t2.typeID() == FFAPLINTEGER ||
					t2.typeID() == FFAPLPRIME || 
					t2.typeID() == FFAPLRANDOM ||
					t2.typeID() == FFAPLPSRANDOMG ||
					t2.typeID() == FFAPLRANDOMG){
			  return new FFaplInteger();
			}else if(t2.typeID() == FFAPLPOLYNOMIAL){
				return t2.clone();
			}else if(t2.typeID() == FFAPLGF){
				return t2.clone();
			}else if(t2.typeID() == FFAPLPOLYNOMIALRESIDUE){
				return t2.clone();
			}else if(t2.typeID() == FFAPLRESIDUECLASS){
				return t2.clone();
			}else if(t2.typeID() == FFAPLEC){
				return t2.clone();
			}
		}else if(t1.typeID() == FFAPLRESIDUECLASS){
			if(t2.typeID() == FFAPLINTEGER ||
					t2.typeID() == FFAPLPRIME || 
					t2.typeID() == FFAPLRANDOM ||
					t2.typeID() == FFAPLPSRANDOMG ||
					t2.typeID() == FFAPLRANDOMG ||
					t2.typeID() == FFAPLRESIDUECLASS){
				return t1.clone();
			}else if(t2.typeID() == FFAPLPOLYNOMIALRESIDUE){
				return t2.clone();
			}else if(t2.typeID() == FFAPLGF){
				return t2.clone();
			}
		}else if (t1.typeID() == FFAPLPOLYNOMIAL){
			if(t2.typeID() == FFAPLINTEGER ||
					t2.typeID() == FFAPLPRIME || 
					t2.typeID() == FFAPLRANDOM ||
					t2.typeID() == FFAPLPSRANDOMG ||
					t2.typeID() == FFAPLRANDOMG ||
					t2.typeID() == FFAPLPOLYNOMIAL){
				return t1.clone();
			}else if(t2.typeID() == FFAPLGF){
				return t2.clone();
			}else if(t2.typeID() == FFAPLPOLYNOMIALRESIDUE){
				return t2.clone();
			}
		}else if(t1.typeID() == FFAPLPRIME){
			if(t2.typeID() == FFAPLINTEGER ||
					t2.typeID() == FFAPLPRIME || 
					t2.typeID() == FFAPLRANDOM ||
					t2.typeID() == FFAPLPSRANDOMG ||
					t2.typeID() == FFAPLRANDOMG){
				return new FFaplInteger();
			}else if(t2.typeID() == FFAPLPOLYNOMIAL){
				return t2.clone();
			}else if(t2.typeID() == FFAPLGF){
				return t2.clone();
			}else if(t2.typeID() == FFAPLEC){
				return t2.clone();
			}else if(t2.typeID() == FFAPLPOLYNOMIALRESIDUE){
				return t2.clone();
			}else if(t2.typeID() == FFAPLRESIDUECLASS){
				return t2.clone();
			}
		}else if(t1.typeID() == FFAPLRANDOM || t1.typeID() == FFAPLPSRANDOMG
				 || t1.typeID() == FFAPLRANDOMG){
			if(t2.typeID() == FFAPLINTEGER ||
					t2.typeID() == FFAPLPRIME || 
					t2.typeID() == FFAPLRANDOM ||
					t2.typeID() == FFAPLPSRANDOMG ||
					t2.typeID() == FFAPLRANDOMG){
				return new FFaplInteger();
			}else if(t2.typeID() == FFAPLPOLYNOMIAL){
				return t2.clone();
			}else if(t2.typeID() == FFAPLGF){
				return t2.clone();
			}else if(t2.typeID() == FFAPLPOLYNOMIALRESIDUE){
				return t2.clone();
			}else if(t2.typeID() == FFAPLRESIDUECLASS){
				return t2.clone();
			}
		}else if(t1.typeID() == FFAPLGF){
			//TODO bearbeiten für Galois Galois
			return t1.clone();
		}else if(t1.typeID() == FFAPLEC){
			return t1.clone();
		}else if(t1.typeID() ==  FFAPLPOLYNOMIALRESIDUE){
			if(t2.typeID() == FFAPLINTEGER ||
					t2.typeID() == FFAPLPRIME || 
					t2.typeID() == FFAPLRANDOM ||
					t2.typeID() == FFAPLPSRANDOMG ||
					t2.typeID() == FFAPLRANDOMG ||
					t2.typeID() == FFAPLPOLYNOMIALRESIDUE ||
					t2.typeID() == FFAPLPOLYNOMIAL){
				return t1.clone();
			}else if(t2.typeID() == FFAPLGF){
				return t2.clone();
			}
		}else if(t1.typeID() == FFAPLARRAY){
			if(t2.typeID() == FFAPLARRAY){
				if(((FFaplArray)t1).getDim() == ((FFaplArray)t2).getDim()){
					return new FFaplArray(max(((FFaplArray)t1).subarray(1),
											  ((FFaplArray)t2).subarray(1)),
											  ((FFaplArray)t1).getDim());
				}	
			}
		}else if(t1.typeID() == FFAPLBOOLEAN &&
				 t2.typeID() == FFAPLBOOLEAN){
			return t1.clone();
		}
		
		Object[] arguments = {t1, t2};
		throw new FFaplException(arguments, ICompilerError.IMPLICIT_CAST_ERROR);
	}
	
	/**
	 * Return expected Types
	 * @param typeID
	 * @param array
	 * @return the expected types
	 */
	public static Vector<String> getExpected(int typeID, boolean[][] array){
		Vector<String> result = new Vector<String>();
		for(int j = 0; j < array[typeID].length; j++){
			if(array[typeID][j]){
				result.add(FFaplTypeCrossTable.TYPE_Name[j]);
			}
		}
		return result;
	}

	/**
	 * Return expected Types
	 * @param array
	 * @return the expected types
	 */
	public static Vector<String> getExpected(boolean[] array) {
		Vector<String> result = new Vector<String>();
		for(int j = 0; j < array.length; j++){
			if(array[j]){
				result.add(FFaplTypeCrossTable.TYPE_Name[j]);
			}
		}
		return result;
	}
	
	/**
	 * @param id
	 * @return the token String of the specified id
	 */
	public static String getTokenString(int id){
		String result;
		result = FFaplInterpreterConstants.tokenImage[id];
		result = result.replace("\"", "");
		return result;		
	}

	/**
	 * cast first element of attribv to max element
	 * @param attribv
	 * @param symbolType
	 * @return casted attribv
	 * @throws FFaplException
	 */
	public static Vector<IAttribute> castToMax(Vector<IAttribute> attribv, Type symbolType) throws FFaplException {
		IAttribute a;
		if(attribv != null){
			a = attribv.firstElement();
			a.setType(max(a.getType(), symbolType));
		}
		return attribv;
	}

	/**
	 * tries to cast types of attribv to symbolType
	 * @param attribv
	 * @param symbolType
	 * @return returns the casted attributes
	 * @throws FFaplException
	 */
	public static Vector<IAttribute> castTo(Vector<IAttribute> attribv,
			Type symbolType) throws FFaplException {
		IAttribute a;
		Type type;
		int len = 0;
		if(attribv != null){
			for(Iterator<IAttribute> itr = attribv.iterator(); itr.hasNext();){
				a = itr.next();
				if(a.getType().typeID() == FFaplTypeCrossTable.FFAPLARRAY){
					len = ((FFaplArray)a.getType()).getLength();//store length
					
					if(	symbolType.typeID() == FFaplTypeCrossTable.FFAPLARRAY ){
						if(((FFaplArray)symbolType).baseType().typeID() == ((FFaplArray)a.getType()).baseType().typeID() && 
								((FFaplArray)symbolType).baseType().typeID() ==	FFaplTypeCrossTable.FFAPLPRIME){
							//prime Array
							type = new FFaplArray(new FFaplPrime(), ((FFaplArray)a.getType()).getDim());
							((FFaplArray)type).setLength(len);
							a.setType(type);
							return attribv;
						}
					}
				}
				
				if(symbolType.typeID() == FFaplTypeCrossTable.FFAPLPRIME &&
						a.getType().typeID() == FFaplTypeCrossTable.FFAPLINTEGER){
					type = new FFaplPrime();
				}else {
				
					type = max(a.getType(), symbolType);
					//System.out.println(type);
					if(type.typeID() != symbolType.typeID()){
						Object[] arguments = {a, symbolType};
						throw new FFaplException(arguments, ICompilerError.IMPLICIT_CAST_ERROR);
					}
				}
				if(type.typeID() == FFaplTypeCrossTable.FFAPLARRAY){
					((FFaplArray)type).setLength(len);//backup length
				}
				a.setType(type);
			}
		}
		return attribv;
	}
}
