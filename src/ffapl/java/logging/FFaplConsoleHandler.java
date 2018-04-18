/**
 * 
 */
package ffapl.java.logging;

import ffapl.utils.Observable;
import ffapl.utils.Observer;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

import ffapl.java.enums.LoggerMode;
import ffapl.java.interfaces.ILevel;
import sunset.gui.editor.FFaplCodeTextPane;

/**
 * Console Handler, prints all to the console or shell if
 * console jTextPane is not set
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplConsoleHandler implements Observer {

	
	private JTextPane _console;
	private FFaplCodeTextPane _code;
	private boolean _default;
	private LoggerMode _mode;
	
	/**
	 * 
	 */
	public FFaplConsoleHandler() {
		_default = true;
		_mode = LoggerMode.ALL;
	}
	
	/**
	 * 
	 * @param code
	 * @param console
	 */
	public FFaplConsoleHandler(FFaplCodeTextPane code, JTextPane console, LoggerMode mode){
		_default = false;
		_console = console;
		_code = code;
		_mode = mode;
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable looger, Object arguments) {
		SimpleAttributeSet attributes;
		FFaplLogMessage logMessage;
		logMessage = ((FFaplLogMessage) arguments);

		 if(logMessage.kind() <= _mode.getMode()){
			if(_default){
				System.out.println(logMessage.message());
			}else{
				
				attributes = new SimpleAttributeSet();
				_code.getStyledDocument().setCharacterAttributes(0, _code.getDocument().getLength(), attributes, true);
				try {
					_console.getDocument().insertString(
							_console.getDocument().getLength(), 
							logMessage.message(),
							attributes);
					if(logMessage.kind() != ILevel.RESULT){
						underline(logMessage, _code);
						_code.repaint();
					}
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * underlines the line were the error or warning occurred
	 * @param logMessage
	 * @param code
	 */
	private void underline(FFaplLogMessage logMessage, FFaplCodeTextPane code){
		int line = logMessage.line() - 1;
		int column = logMessage.column() - 1;
		if(line >= 0){
			if(logMessage.kind() == ILevel.ERROR){
				code.setErrorLineColumn(line, column);
			}else if(logMessage.kind() == ILevel.WARNING){
				code.setWarningLineColumn(line, column);
			}
		}
	}
	
	/**
	 * Sets the console of the logger
	 * @param console
	 
	public void setConsole(JTextPane console){
		if(console != null){
			_console = console;
		}
	}
*/	

}
