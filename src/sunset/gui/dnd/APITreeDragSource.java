/**
 * 
 */
package sunset.gui.dnd;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import sunset.gui.api.*;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class APITreeDragSource implements DragGestureListener,
		DragSourceListener {

	private JTree _sourceTree;
	
	private IMutableTreeNodeAPI _transferable;
	
	private DragSource _source;
	
	//private DragGestureRecognizer _recognizer;


	public APITreeDragSource(JTree tree) {
		 _sourceTree = tree;
		 _source = new DragSource();
		 _source.createDefaultDragGestureRecognizer(_sourceTree, DnDConstants.ACTION_COPY, this);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
	 */
	@Override
	public void dragDropEnd(DragSourceDropEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
	 */
	@Override
	public void dragEnter(DragSourceDragEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragExit(DragSourceEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
	 */
	@Override
	public void dragOver(DragSourceDragEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
	 */
	@Override
	public void dropActionChanged(DragSourceDragEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		TreePath path = _sourceTree.getSelectionPath();
	    if ((path == null) || (path.getPathCount() <= 1)) {
	      // We can't move the root node or an empty selection
	      return;
	    }
	    if(path.getLastPathComponent() instanceof IMutableTreeNodeAPI){
		    _transferable = (IMutableTreeNodeAPI) path.getLastPathComponent();
		    _source.startDrag(dge, DragSource.DefaultCopyDrop, _transferable, this);
	    }
	}

}
