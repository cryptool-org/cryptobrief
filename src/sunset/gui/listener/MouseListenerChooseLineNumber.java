/**
 * 
 */
package sunset.gui.listener;



import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.Element;

import sunset.gui.FFaplJFrame;
import sunset.gui.tabbedpane.JTabbedPaneCode;
import sunset.gui.util.SunsetBundle;




/**
 * Opens a Dialog
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class MouseListenerChooseLineNumber implements MouseListener {

	private FFaplJFrame _frame;
	private JTabbedPaneCode _tabbedPane;

	/**
	 * 
	 * @param frame
	 */
	public MouseListenerChooseLineNumber(FFaplJFrame frame, JTabbedPaneCode tabbedPane) {
		_frame = frame;
		_tabbedPane = tabbedPane;
		//_dialog = new JDialogLineNumber(_frame, "");
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		String str, gotoLine, txt;
		int lineNumber;
		int max;
		JTextPane  pane;
		Element root;
		
		if(arg0.getClickCount() > 1){
			pane = _tabbedPane.currentCodePane();
			if(pane != null){
				root = pane.getDocument().getRootElements()[0];
				max = root.getElementCount();
				gotoLine =SunsetBundle.getInstance().getProperty("inputDialog_gotoline");
				txt =SunsetBundle.getInstance().getProperty("inputDialog_enterline");
				
				if(gotoLine == null){
					gotoLine = "Go to Line";
				}
				if(txt == null){
					txt = "Enter line number";
				}
				
				txt = txt + " (" + 1 + ".." + max + "):";
				
				str = JOptionPane.showInputDialog(_frame, txt, 
						gotoLine, JOptionPane.PLAIN_MESSAGE);
				try{
					lineNumber = Integer.parseInt(str);
					pane.getCaret().setDot(root.getElement(
							Math.min(Math.max(1, lineNumber), max) - 1).getStartOffset());
					pane.requestFocusInWindow();
				}catch(Exception e){
					
				}
			}
			
		}
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// nothing
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// nothing
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// nothing
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// nothing
	}

}
