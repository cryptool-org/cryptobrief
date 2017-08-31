/**
 * 
 */
package sunset.gui.listener;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

/**
 * Draw line numbers into a JTextPane
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class DocumentListenerLineNumber implements DocumentListener {

	private JTextPane _textPane;
	private int _currentLine;

	/**
	 * 
	 */
	public DocumentListenerLineNumber(JTextPane textPane) {
		_textPane = textPane;
		_currentLine = 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent arg) {
	
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent arg) {
		SimpleAttributeSet attributes;
		int lines;
		String txt;
		
		lines = arg.getDocument().getDefaultRootElement().getElementCount();
		
		if( _currentLine < lines){
			while(_currentLine < lines){
				_currentLine ++;
				if(_currentLine !=  1){
					txt = "\n" + _currentLine;
				}else{
					txt = _currentLine + "";
				}
				attributes = new SimpleAttributeSet();
				try {
					_textPane.getDocument().insertString(
							_textPane.getDocument().getLength(), txt, attributes);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent arg) {
		int lines;
		String txt;
		lines = arg.getDocument().getDefaultRootElement().getElementCount();
		
		if(_currentLine > lines){
			while(_currentLine > lines){
				if(_currentLine !=  1){
					txt = "\n" + _currentLine;
				}else{
					txt = _currentLine + "";
				}
				try {
					_textPane.getDocument().remove(
							_textPane.getDocument().getLength() - txt.length(), txt.length());
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				_currentLine --;
			}
		}

	}

}
