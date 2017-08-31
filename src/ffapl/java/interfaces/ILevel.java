/**
 * 
 */
package ffapl.java.interfaces;

/**
 * Level of FFapl Logging
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public interface ILevel {

	public final static int RESULT 	= 0;
	
	public final static int ERROR = 1;
	
	public final static int WARNING	= 2;

	
	public final static String[] Level_name = {
		"result",
		"error",
		"warning"
	};
}
