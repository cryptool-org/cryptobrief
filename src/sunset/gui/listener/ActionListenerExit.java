/**
 * 
 */
package sunset.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import sunset.gui.FFaplJFrame;
import sunset.gui.util.SunsetBundle;


/**
 * Action Listener for exit Sunset 
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerExit implements ActionListener {

	private FFaplJFrame _frame;
	private ActionListenerExecuteCode _execution;
	
	/**
	 * 
	 * @param frame
	 * @param execution
	 */
	public ActionListenerExit(FFaplJFrame frame, ActionListenerExecuteCode execution){
		_frame = frame;
		_execution = execution;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		boolean isClosed, close;
		String message, title;
		int answer;
	
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
			answer = JOptionPane.showConfirmDialog(_frame, message, title, JOptionPane.YES_NO_OPTION);
			if(answer == JOptionPane.YES_OPTION){
				_execution.stopRunningThread();
				close = true;
				//close tabs after execution stop
			}
			
		}else{
			close = true;
		}
		
		if(close){
			_frame.storeGUIProperties();
			isClosed = _frame.closeCodeTab();
			if(isClosed){//everything OK
				_frame.dispose();
			}else{
				
			}
		}
	}

	

}
