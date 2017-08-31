package ffapl.exception;


import java.text.MessageFormat;
import java.util.Vector;

import ffapl.lib.interfaces.IToken;
import ffapl.utils.FFaplProperties;

/**
 * Represents a parser exception
 * @author Alexander Ortner
 * @version 1.0
 *
 */

public class FFaplWarning {
	
		protected Vector<IToken> _tokens;
		//protected IToken _currentToken;
		protected int _warningNr;
		protected String _warningMessage;
		
		/**
		 * 
		 * @param val
		 * @param warningNr
		 * @param token
		 */
		public FFaplWarning(Object[] val, int warningNr, IToken token){
			_warningNr = warningNr;
			_tokens = new Vector<IToken>();
			if(token != null){
				_tokens.add(token);
			}
			generateMessage(val, FFaplProperties.getInstance().getProperty(warningNr));
		}
		
		public int warningColumn() {
			if(_tokens.size() > 0){
				return _tokens.firstElement().column();
			}else{
				return -1;
			}
		}
	 

		public int warningLine() {
			if(_tokens.size() > 0){
				return _tokens.firstElement().line();
			}else{
				return -1;
			}
		}


		public int warningNumber() {
			return this._warningNr;
		}


		public String getWarningMessage() {
			return _warningMessage;
		}
		

		public String warningType(){
			return "Compilerwarning";
		}
		
		/**
		 * Generates the warningMessage
		 * @param val
		 * @param message
		 */
		protected void generateMessage(Object[] val, String message){
			if(message != null){
				message = MessageFormat.format(message,val);
				message = FFaplProperties.getInstance().getProperty("COMMON.WARNING") + ": " + message 
						  + " (" + FFaplProperties.getInstance().getProperty("COMMON.ROW") + " " + warningLine()+ ", "
						  + FFaplProperties.getInstance().getProperty("COMMON.COLUMN") + " " + warningColumn() + ")"  
						  + System.getProperty("line.separator");
		    }else{
		    	message = "no warning message found for warningID: " + _warningNr;
		    }
			_warningMessage = message;
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
		    final StringBuilder warning = new StringBuilder();
		    warning.append(aThrowable.toString());
		    final String NEW_LINE = System.getProperty("line.separator");
		    warning.append(NEW_LINE);

		    //add each element of the stack trace
		    for (StackTraceElement element : aThrowable.getStackTrace() ){
		    	warning.append( element );
		    	warning.append( NEW_LINE );
		    }
		    return warning.toString();
		  }

}
