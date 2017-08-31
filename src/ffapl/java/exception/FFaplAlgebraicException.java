package ffapl.java.exception;

import java.util.Vector;

import ffapl.exception.FFaplException;
import ffapl.java.interfaces.IAlgebraicError;
import ffapl.lib.interfaces.IToken;
import ffapl.utils.FFaplProperties;

/**
 * Represents a Algebraic Exception
 * @author Alexander Ortner
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class FFaplAlgebraicException extends FFaplException implements IAlgebraicError {

	/**
	 * 
	 * @param val
	 * @param errorNr
	 */
	public FFaplAlgebraicException(Object[] val, int errorNr){
		_errorNr = errorNr;
		_tokens = new Vector<IToken>(0,1);
		super.generateMessage(val, FFaplProperties.getInstance().getProperty(errorNr));
		
	}
			
	@Override
	public String errorType(){
		return "Algebraic Error";
	}

}
