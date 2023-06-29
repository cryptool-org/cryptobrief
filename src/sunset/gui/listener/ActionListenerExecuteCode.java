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
import sunset.gui.interfaces.IProperties;
import sunset.gui.lib.ExecuteThread;
import sunset.gui.logic.GUIPropertiesLogic;
import sunset.gui.panel.JPanelCode;
import sunset.gui.tabbedpane.JTabbedPaneCode;
import sunset.gui.util.IsomorphismCalculationUtil;
import sunset.gui.util.LoggerUtil;
import ffapl.FFaplInterpreter;
import ffapl.java.logging.FFaplLogger;
import ffapl.java.util.FFaplRuntimeProperties;
import sunset.gui.logging.JTextPaneConsoleHandler;
import sunset.gui.panel.JPanelTabTitle;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerExecuteCode implements ActionListener {

	private JTabbedPaneCode _tabbedPane_code;
	private Thread _running;
	private Vector<Component> _startComp;
	private Vector<Component> _stopComp;

	private JTextPaneConsoleHandler consoleHandler;
	/**
	 * @param tabbedPane_code
	 * @param startComp
	 * @param stopComp
	 */
	public ActionListenerExecuteCode(JTabbedPaneCode tabbedPane_code, Vector<Component> startComp, Vector<Component> stopComp) {
		_tabbedPane_code = tabbedPane_code;
		_startComp = startComp;
		_stopComp = stopComp;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		FFaplCodeTextPane textPane;
		FFaplLogger logger;
		FFaplRuntimeProperties properties;
		Component button;
		Thread executeThread;
		JTextPane textPane_console;
		button = (Component) ev.getSource();
		textPane = (FFaplCodeTextPane) _tabbedPane_code.currentCodePane();
		textPane_console = ((JPanelCode)_tabbedPane_code.currentCodePanel()).getConsole();
		// reset Errors
		textPane.getLineHandler().resetHandler();
		textPane.repaint();

		if (_running != null) {
			if (!_running.isAlive()) {
				_running = null;
			} //else {
				//System.out.println("Error in Thread");
			//}
		}

		if (button.getName().equals(IFFaplLang.BUTTON_RUN)) {
			logger = new FFaplLogger("FFaplLog");
			consoleHandler = new JTextPaneConsoleHandler(textPane, textPane_console, LoggerUtil.getLoggerMode());
			logger.addObserver(consoleHandler);

			properties = new FFaplRuntimeProperties(
					IsomorphismCalculationUtil.getRootFindingStrategyType(),
					IsomorphismCalculationUtil.getTimeLimitInSeconds());

			// EXECUTE
			if (textPane != null && _running == null) {
				textPane_console.setText("");
				Reader reader = new StringReader(textPane.getText());

				try {
					_running = new FFaplInterpreter(logger, properties, reader);
					executeThread = new ExecuteThread(_running, _startComp, _stopComp);
					executeThread.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
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
		// STOP
		if (_running != null) {
			_running.interrupt();
			try {
				_running.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			_running = null;
		}
		return true;
	}

	/**
	 * @return true if Execution Thread is running, false otherwise
	 */
	public boolean isRunning() {
		if (_running != null) {
			if (_running.isAlive()) {
				return true;
			}
		}
		return false;
	}
}
