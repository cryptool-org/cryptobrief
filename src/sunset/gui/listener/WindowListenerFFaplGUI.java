/**
 * 
 */
package sunset.gui.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

import sunset.gui.FFaplJFrame;
import sunset.gui.panel.JPanelCode;
import sunset.gui.util.SunsetBundle;


/**
 * Window Listener for FFapl Main GUI
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class WindowListenerFFaplGUI implements WindowListener {

	private ActionListenerExecuteCode _execution;
	
	/**
	 * 
	 * @param execution
	 */
	public WindowListenerFFaplGUI(ActionListenerExecuteCode execution) {
		_execution = execution;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent arg0) {
		JPanelCode currentCodePanel = FFaplJFrame.getCurrentCodePanel(); 
		
	    if (currentCodePanel != null) {
	    	currentCodePanel.getCodePane().requestFocusInWindow();
	    }
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		boolean isClosed, close;
		FFaplJFrame jFrame;
		String message, title;
		int answer;
		
		jFrame = (FFaplJFrame) arg0.getSource();
		close = false;
		if(_execution.isRunning()){
			
			message = SunsetBundle.getInstance().getProperty("question_abortexecution");
			title = SunsetBundle.getInstance().getProperty("warning_title");
			if(message == null){
				message = "Currently a FFapl-Program is executed.\nDo you want to abort the execution?";
			}
			if(title == null){
				title = "Warning!";
			}
			answer = JOptionPane.showConfirmDialog(jFrame, message, title, JOptionPane.YES_NO_OPTION);
			if(answer == JOptionPane.YES_OPTION){
				_execution.stopRunningThread();
				close = true;
				//close tabs after execution stop
			}
			
		}else{
			close = true;
		}
		
		if(close){
			jFrame.storeGUIProperties();
			isClosed = jFrame.closeCodeTab();
			if(isClosed){//everything OK
				jFrame.dispose();
			}else{
				
			}
		}
		
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent arg0) {

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent arg0) {

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent arg0) {

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent arg0) {

	}

}
