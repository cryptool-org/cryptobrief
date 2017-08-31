/**
 * 
 */
package sunset.gui.listener;


import java.awt.Component;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sunset.gui.editor.FFaplCodeTextPane;

/**
 * Draw line numbers into a JTextPane
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class DocumentListenerUndoManager implements DocumentListener {

	private Vector<Component> _undoComp;
	private Vector<Component> _redoComp;
	private FFaplCodeTextPane _pane;
	//int counter = 0;

	/**
	 * @param undoComp
	 * @param redoComp
	 */
	public DocumentListenerUndoManager(FFaplCodeTextPane pane, Vector<Component> undoComp, Vector<Component> redoComp) {
		_undoComp = undoComp;
		_redoComp = redoComp;
		_pane = pane;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent arg) {
		//System.out.println("update" + counter++);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent arg) {
		_pane.getActiveView().updateSyntaxHighlighting();
		setEnabled(_undoComp, true);
	    setEnabled(_redoComp, false);
	    //System.out.println("insert" + counter++ + " " + arg.getType());
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent arg) {   
		_pane.getActiveView().updateSyntaxHighlighting();
	    setEnabled(_undoComp, true);
	    setEnabled(_redoComp, false);
	    _pane.repaint();
	    //System.out.println("remove" + counter++);
	}
	
	
	/**
	 * Enables components according val
	 * @param comp
	 * @param val
	 */
	private void setEnabled(Vector<Component> comp, boolean val){
		for(Iterator<Component> itr = comp.iterator(); itr.hasNext(); ){
			itr.next().setEnabled(val);
		}
	}

}
