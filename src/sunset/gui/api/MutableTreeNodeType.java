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
public class MutableTreeNodeType extends DefaultMutableTreeNode implements IMutableTreeNodeAPI {

	private APIType _type;
	
	public MutableTreeNodeType(APIType type) {
		super(type.getName());
		_type = type;
	}
	
	@Override
	public String getInfo(){
		return _type.toString();
	}

	@Override
	public String getHTMLInfo() {
		return _type.htmlInfo();
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported(flavor)) {
		      return _type.getName();
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
