/**
 * 
 */
package sunset.gui.listener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sunset.gui.editor.FFaplCodeTextPane;


/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class DocumentListenerErrorLine implements DocumentListener {

	private FFaplCodeTextPane _owner;
	
	/**
	 * @param owner
	 */
	public DocumentListenerErrorLine(FFaplCodeTextPane owner) {
		_owner = owner;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		//nothing todo
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		//_owner.setErrorLineColumn(-1, -1);
		_owner.getLineHandler().resetHandler();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		//_owner.setErrorLineColumn(-1, -1);
		_owner.getLineHandler().resetHandler();
	}

}
