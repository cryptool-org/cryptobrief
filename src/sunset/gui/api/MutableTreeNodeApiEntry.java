/**
 * 
 */
package sunset.gui.api;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

import sunset.gui.api.jaxb.ApiEntry;
import sunset.gui.api.jaxb.Snippet;
import sunset.gui.api.util.ApiUtil;

/**
 * Represents a function Node in the API Tree
 * 
 * @author Alexander Ortner
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MutableTreeNodeApiEntry extends DefaultMutableTreeNode implements
		IMutableTreeNodeAPI {

	private ApiEntry entry;
	public ApiEntry getEntry() {
		return entry;
	}

	private boolean custom;

	public MutableTreeNodeApiEntry(ApiEntry entry) {
		this(entry, false);
	}
	
	public MutableTreeNodeApiEntry(ApiEntry entry, boolean isCustom) {
		super(ApiUtil.getRepresentation(entry));
		this.custom = isCustom;
		this.entry = entry;
	}

	@Override
	public String getInfo() {
		return entry.getName();
	}

	@Override
	public String getHTMLInfo() {
		return ApiUtil.getHTMLInfo(entry);
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (isDataFlavorSupported(flavor)) {
			return ApiUtil.getTransferData(entry);
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

	/**
	 * @return the custom
	 */
	public boolean isCustom() {
		return custom;
	}

}
