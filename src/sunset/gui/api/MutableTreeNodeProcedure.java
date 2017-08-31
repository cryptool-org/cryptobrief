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
public class MutableTreeNodeProcedure extends DefaultMutableTreeNode implements IMutableTreeNodeAPI{

	private APIProcedure _procedure;
	
	public MutableTreeNodeProcedure(APIProcedure procedure) {
		super(procedure.getRepresentation());
		_procedure = procedure;
	}
	
	@Override
	public String getInfo(){
		return _procedure.toString();
	}

	

	@Override
	public String getHTMLInfo() {
		// TODO Auto-generated method stub
		return _procedure.htmlInfo();
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported(flavor)) {
		      return _procedure.getUsageCode();
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
