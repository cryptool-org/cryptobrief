/**
 * 
 */
package sunset.gui.api;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * Represents a function Node in the API Tree
 * @author Alexander Ortner
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MutableTreeNodeFunction extends DefaultMutableTreeNode implements IMutableTreeNodeAPI {

	private APIFunction _function;
	
	public MutableTreeNodeFunction(APIFunction function) {
		super(function.getRepresentation());
		_function = function;
	}
	
	@Override
	public String getInfo(){
		return _function.toString();
	}

	@Override
	public String getHTMLInfo() {
		return _function.htmlInfo();
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported(flavor)) {
		      return _function.getUsageCode();
		    } else {
		      throw new UnsupportedFlavorException(flavor);
		    }
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return IMutableTreeNodeAPI.FLAVORS;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return (flavor.getRepresentationClass() == String.class);
	}

	
	

}
