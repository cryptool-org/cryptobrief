package ffapl.exception;

import java.text.MessageFormat;

import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.lib.interfaces.ICompilerError;
import ffapl.utils.FFaplProperties;

/** This class provides static methods for generating compiler messages
 * in a given specified format. The error message format specification is (EBNF):
 * 
 * @author Alexander Ortner
 */
public class CompilerMessage {
	private static final String prefix = "FFapl compilation: [";
	
	/** Print a compiler status OK message to the standard error stream.
	 * @param programName    the source program name.
	 */
	public static void printOK(String programName)
	{
		System.err.println(prefix + programName + "] OK"); 
	}
	
	/**
	 * Print a FFapl error message to the standard error stream.
	 * @param err         the Exception or Error object representing the cause
	 *                    and source location of the error.
	 * @param programName    the source program name.
	 */
	public static void printError(ICompilerError err, String programName)
	{
		
		System.out.println(getError(err,programName));
		if (err.errorNumber() == ICompilerError.INTERNAL && err instanceof Throwable)
			((Throwable) err).printStackTrace();
	}
	
	/**
	 * Return a FFapl error message.
	 * @param err
	 * @param programName
	 * @return the FFapl error message
	 */
	public static String getError(ICompilerError err, String programName){
		StringBuffer buf = new StringBuffer();
		String msg = "";
		if(err instanceof FFaplAlgebraicException){
			msg =  FFaplProperties.getInstance().getProperty("0000");
		}else{
			msg =  FFaplProperties.getInstance().getProperty("000");
		}
		if(msg == null){
			buf.append(prefix);
			buf.append(programName);
			buf.append("] " + err.errorType() + " ");
			buf.append(String.valueOf(err.errorNumber()));
			buf.append(" (line ");
			buf.append(String.valueOf(err.errorLine()));
			buf.append(", column ");
			buf.append(String.valueOf(err.errorColumn()));
			buf.append(")\n");
			
		}else{
			Object[] arguments = {"[" + programName + "]", err.errorType(), String.valueOf(err.errorNumber()), 
					String.valueOf(err.errorLine()), String.valueOf(err.errorColumn())};
			msg = MessageFormat.format(msg,arguments) + "\n";
			buf.append(msg);
		}
		buf.append(err.getErrorMessage());
		return buf.toString();
	}
}

