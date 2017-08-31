/**
 * 
 */
package sunset.gui.api;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * 
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public interface IMutableTreeNodeAPI extends Transferable{

	public static DataFlavor TREE_PATH_FLAVOR = new DataFlavor(String.class,
    "API Code");
	public static DataFlavor[] FLAVORS = { IMutableTreeNodeAPI.TREE_PATH_FLAVOR };
	
	/**
	 * @return the info to the Node
	 */
	public String getInfo();
	
	/**
	 * @return the info formatted in HTML to the Node
	 */
	public String getHTMLInfo();
}
