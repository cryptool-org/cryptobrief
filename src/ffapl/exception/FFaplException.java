package ffapl.exception;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import ffapl.lib.interfaces.ICompilerError;
import ffapl.lib.interfaces.IToken;
import ffapl.utils.FFaplProperties;

/**
 * Represents a parser exception
 * @author Alexander Ortner
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
public class FFaplException extends Exception implements ICompilerError {
	
		protected List<IToken> _tokens;
		//protected IToken _currentToken;
		protected int _errorNr;
		protected String _errorMessage;
		
		public FFaplException(){
			_tokens = new ArrayList<IToken>();
			_errorMessage = "";
		}
		
		/**
		 * 
		 * @param message
		 */
		public FFaplException(String message){
			_tokens = new ArrayList<IToken>();
			_errorMessage = message;
		}
		
		/**
		 * 
		 * @param val
		 * @param errorNr
		 */
		public FFaplException(Object[] val, int errorNr){
			_tokens = new ArrayList<IToken>();
			this._errorNr = errorNr;
			generateMessage(val, FFaplProperties.getInstance().getProperty(errorNr));
		}

		/*
		public FFaplException(String message, IToken token, int errorNr){
			this(message);
			this._currentToken = token;
			this._errorNr = errorNr;
		}*/
		
		/**
		 * 
		 * @param val
		 * @param errorNr
		 * @param token
		 */
		public FFaplException(Object[] val, int errorNr, IToken token){
			this(val, errorNr);
			if(token != null){
				_tokens.add(token);
			}
		}
		
		/*
		public FFaplException(String message, ffapl.Token token, int errorNr){
			this(message, new FFaplToken(token), errorNr);
		}*/
		
		//public FFaplException(String string, int typeIllegalUse) {
			// TODO Auto-generated constructor stub
		//}

		@Override
		public int errorColumn() {
			if(_tokens.size() > 0){
				return _tokens.get(0).column();
			}else{
				return -1;
			}
		}
	 
		@Override
		public int errorLine() {
			if(_tokens.size() > 0){
				return _tokens.get(0).line();
			}else{
				return -1;
			}
		}

		@Override
		public int errorNumber() {
			return this._errorNr;
		}

		@Override
		public String getErrorMessage() {
			return _errorMessage;
		}
		
		@Override
		public String errorType(){
			return "CompilerError";
		}
		
		/**
		 * Generates the ErrorMessage
		 * @param val
		 * @param message
		 */
		protected void generateMessage(Object[] val, String message){
			if(message != null){
				message = MessageFormat.format(message,val);
		    }else{
		    	message = "no error message found for ErrorID: " + _errorNr;
		    }
			_errorMessage = message;
		}
		
		/**
		 * add the Token for the Exception
		 * @param token
		 */
		public void addToken(IToken token){
			if(token != null){
				_tokens.add(token);
			}
		}
		
		 /**
		  * returns the stack trace as string
		  * @param aThrowable
		  * @return
		  */
		  public static String getCustomStackTrace(Throwable aThrowable) {
		    //add the class name and any message passed to constructor
		    final StringBuilder error = new StringBuilder();
		    error.append(aThrowable.toString());
		    final String NEW_LINE = System.getProperty("line.separator");
		    error.append(NEW_LINE);

		    //add each element of the stack trace
		    for (StackTraceElement element : aThrowable.getStackTrace() ){
		    	error.append( element );
		    	error.append( NEW_LINE );
		    }
		    return error.toString();
		  }

}
