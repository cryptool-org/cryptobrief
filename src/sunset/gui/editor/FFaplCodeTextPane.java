/**
 * 
 */
package sunset.gui.editor;

import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.undo.UndoManager;

import sunset.gui.enums.LineState;
import sunset.gui.lib.FFaplUndoManager;
import sunset.gui.view.FFaplStatusLine;
import sunset.gui.view.LineHandler;
import ffapl.java.enums.LoggerMode;
import ffapl.java.util.LoggerUtil;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class FFaplCodeTextPane extends JTextPane {

	private boolean _inputsaved;
	private LineHandler _statusLineHandler;
	private UndoManager _manager;
	private FFaplView _activeView;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4964823807536669978L;

	public FFaplCodeTextPane(){ 
		// Set editor kit
		_statusLineHandler = new LineHandler();
	    this.setEditorKitForContentType("text/ffapl", new FFaplEditorKit(this));
	    this.setContentType("text/ffapl");
	    _inputsaved = true;
	    _manager = new FFaplUndoManager();
	    this.getDocument().addUndoableEditListener(_manager);
	}
	
	/**
	 * Sets true or false if input saved or not
	 * @param val
	 */
	public void inputSaved(boolean val){
		_inputsaved = val;
	}
	
	/**
	 * Returns true or false if input saved
	 * @return
	 */
	public boolean isSaved(){
		return _inputsaved;
	}
	
	/**
	 * returns the UndoManager of the TextPane
	 * @return
	 */
	public UndoManager getManager(){
		return _manager;
	}
	
	/**
	 * Sets the error line and column of the JTextPane
	 * @param errorLine
	 * @param errorColumn
	 */
	public void setErrorLineColumn(int errorLine, int errorColumn){
		if(LoggerUtil.getLoggerMode().getMode() >= LoggerMode.ERROR.getMode()){
			_statusLineHandler.addLineState(new FFaplStatusLine(errorLine, errorColumn, LineState.ERROR));
		}
	}
	
	public void setWarningLineColumn(int line, int column) {
		if(LoggerUtil.getLoggerMode().getMode() >= LoggerMode.WARNING.getMode()){
			_statusLineHandler.addLineState(new FFaplStatusLine(line, column, LineState.WARNING));
		}
	}
	
	public LineHandler getLineHandler(){
		return _statusLineHandler;
	}
        
        
	
	@Override
	protected void processKeyEvent(KeyEvent e){
		Vector<Integer> markedLines;
		Element root;
		int dot = getCaret().getDot();
		int mark = getCaret().getMark();
		int offset, counter;
		Document document;
		boolean anotherTab;
		String txt;
		
		if(e.getID() == KeyEvent.KEY_PRESSED){
			if(!(e.getKeyCode() == KeyEvent.VK_TAB) ||
					 dot == mark){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					document = getDocument();
					root = document.getRootElements()[0];
					//can only be one line because of dot == mark
					markedLines = getMarkedLines(mark, dot);
					
					super.processKeyEvent(e);
					
					txt = getCleanLine(markedLines.firstElement());
					anotherTab = txt.endsWith("{");
					anotherTab = anotherTab || txt.endsWith("(");
					anotherTab = anotherTab || txt.endsWith("/*");
					anotherTab = anotherTab || txt.endsWith("/**");
					
					offset = root.getElement(markedLines.firstElement()).getStartOffset();
					counter = 0;
					
					try {
						while(document.getText(offset + counter, 1).equals("\t")){
							counter ++;
						}
						//offset from next line
						offset = root.getElement(markedLines.firstElement() + 1).getStartOffset();
						
						while(counter > 0){
							document.insertString(offset, "\t", null);
							counter --;
						}
						//if { is encountered at previous line
						if(anotherTab){
							document.insertString(offset, "\t", null);
						}
						
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}else{
					super.processKeyEvent(e);
				}
			}else{
				//Marked and pressed Tab
				markedLines = getMarkedLines(mark, dot);
				if(markedLines.size() > 1){
					//more than one line marked 
					document = getDocument();
					root = document.getRootElements()[0];
					for(Iterator<Integer> itr = markedLines.iterator(); itr.hasNext(); ){
						offset = root.getElement(itr.next()).getStartOffset();
						try {
							document.insertString(offset, "\t", null);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				}else{
					super.processKeyEvent(e);
				}
				
			}
			if(String.valueOf(e.getKeyChar()).equals("*") 
					|| String.valueOf(e.getKeyChar()).equals("/")
					|| String.valueOf(e.getKeyChar()).equals("v")){
				this.repaint();
			}
		}else{
			super.processKeyEvent(e);
		}
	}
	
	/**
	 * @param line
	 * @return the clean string of the line, i.e. \r \t \n are removed
	 */
	private String getCleanLine(Integer line){
		String txt = "";
		Document document = getDocument();
		Element root = document.getRootElements()[0];
		int offset = root.getElement(line).getStartOffset();
		int length = root.getElement(line).getEndOffset() - root.getElement(line).getStartOffset();		
		try {
			txt = document.getText(offset, length);
			txt = txt.replace("\r", "");
			txt = txt.replace("\n", "");
			txt = txt.replace("\t", "");
			txt = txt.trim();
			
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return txt;
	}

	/**
	 * @param mark
	 * @param dot
	 * @return the marked lines of the Document
	 */
	private Vector<Integer> getMarkedLines(int mark, int dot){
		Vector<Integer> result = new Vector<Integer>();
		int start = Math.min(mark, dot);
		int stop = Math.max(mark, dot);
		Element node;
		Element root = getDocument().getRootElements()[0];
		
		for (int i = 0; i < root.getElementCount(); i++){
			node = root.getElement(i);
			if((start <= node.getStartOffset() && node.getStartOffset() <= stop) ||
			   (start > node.getStartOffset() && node.getStartOffset() <= stop && node.getEndOffset() > start)	){
				result.add(i);
			}
		}
		return result;
		
	}

	public void setActiveView(FFaplView view) {
		_activeView = view;		
	}
	
	public FFaplView getActiveView() {
		return _activeView;		
	}

}
