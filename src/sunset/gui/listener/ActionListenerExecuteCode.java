/**
 * 
 */
package sunset.gui.listener;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Reader;
import java.io.StringReader;
import java.util.Vector;

import javax.swing.JTextPane;

import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.interfaces.IFFaplLang;
import sunset.gui.lib.ExecuteThread;
import sunset.gui.panel.JPanelCode;
import sunset.gui.tabbedpane.JTabbedPaneCode;
import sunset.gui.util.InputReaderUtil;
import sunset.gui.util.IsomorphismCalculationUtil;
import sunset.gui.util.LoggerUtil;
import ffapl.FFaplInterpreter;
import ffapl.java.logging.FFaplLogger;
import ffapl.java.util.FFaplReader;
import ffapl.java.util.FFaplRuntimeProperties;
import sunset.gui.logging.JTextPaneConsoleHandler;

import static sunset.gui.lib.ExecuteThread.setEnabled;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerExecuteCode implements ActionListener {

	private JTabbedPaneCode _tabbedPane_code;
	private Vector<Component> _startComp;
	private Vector<Component> _stopComp;
	private Vector<Component> _stepOverComp;
	private Vector<Component> _stepIntoComp;
	private Vector<Component> _resumeComp;


	private JTextPaneConsoleHandler consoleHandler;
	/**
	 * @param tabbedPane_code
	 * @param startComp
	 * @param stopComp
	 */
	public ActionListenerExecuteCode(JTabbedPaneCode tabbedPane_code, Vector<Component> startComp, Vector<Component> stopComp, Vector<Component> stepOverComp, Vector<Component> stepIntoComp, Vector<Component> resumeComp) {
		_tabbedPane_code = tabbedPane_code;
		_startComp = startComp;
		_stopComp = stopComp;
		_stepOverComp = stepOverComp;
		_stepIntoComp = stepIntoComp;
		_resumeComp = resumeComp;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		FFaplCodeTextPane textPane;
		FFaplLogger logger;
		FFaplReader inputReader;
		FFaplRuntimeProperties properties;
		Component button;
		Thread executeThread;
		JTextPane textPane_console;
		button = (Component) ev.getSource();
		textPane = (FFaplCodeTextPane) _tabbedPane_code.currentCodePane();
		FFaplInterpreter running = textPane.getInterpreter();
		textPane_console = ((JPanelCode)_tabbedPane_code.currentCodePanel()).getConsole();
		// reset Errors
		textPane.getLineHandler().resetWarningLines();
		textPane.repaint();

		if (running != null) {
			if (!running.isAlive()) {
				running = null;
			} //else {
				//System.out.println("Error in Thread");
			//}
		}

		if (button.getName().equals(IFFaplLang.BUTTON_RUN) || button.getName().equals(IFFaplLang.BUTTON_RUN_DEBUG)) {
			logger = new FFaplLogger("FFaplLog");
			consoleHandler = new JTextPaneConsoleHandler(textPane, textPane_console, LoggerUtil.getLoggerMode());
			logger.addObserver(consoleHandler);

			inputReader = new InputReaderUtil();

			properties = new FFaplRuntimeProperties(
					IsomorphismCalculationUtil.getRootFindingStrategyType(),
					IsomorphismCalculationUtil.getTimeLimitInSeconds());

			// EXECUTE
			if (textPane != null && running == null) {
				textPane_console.setText("");
				Reader reader = new StringReader(textPane.getText());

				try {
					running = new FFaplInterpreter(logger, properties, inputReader, reader);
					textPane.setInterpreter(running);
					if (button.getName().equals(IFFaplLang.BUTTON_RUN_DEBUG)){
						running.set_debug(true);
						running.set_debugger(textPane.getDebugger());
					}
					executeThread = new ExecuteThread(running, _startComp, _stopComp, _stepOverComp, _stepIntoComp, _resumeComp, textPane.getDebugger());
					executeThread.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (button.getName().equals(IFFaplLang.BUTTON_TERMINATE)) {
			// STOP
			stopRunningThread();
		}
	}

	/**
	 * stops running Thread
	 * @return returns true if success, false if error occurred
	 */
	public boolean stopRunningThread() {
		FFaplCodeTextPane textPane = (FFaplCodeTextPane) _tabbedPane_code.currentCodePane();
		FFaplInterpreter running = textPane.getInterpreter();
		// STOP
		if (running != null) {
			running.interrupt();
			try {
				running.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			textPane.setInterpreter(null);
		}
		return true;
	}

	/**
	 * @return true if Execution Thread is running, false otherwise
	 */
	public boolean isRunning() {
		FFaplInterpreter running = ((FFaplCodeTextPane) _tabbedPane_code.currentCodePane()).getInterpreter();
		return running != null && running.isAlive();
	}
}
