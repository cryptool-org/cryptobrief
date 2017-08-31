/**
 * 
 */
package sunset.gui.listener;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JLabel;

import sunset.gui.interfaces.IFFaplLang;
import sunset.gui.interfaces.IProperties;
import sunset.gui.panel.JPanelCode;
import sunset.gui.panel.JPanelTabTitle;
import sunset.gui.tabbedpane.JTabbedPaneCode;
import sunset.gui.util.SunsetBundle;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerNewFile implements ActionListener {

	private JTabbedPaneCode _tabbedPane;
	private Window _window;
	private Vector<Component> _undoComp;
	private Vector<Component> _redoComp;
	private Vector<Component> _saveComp;
	private Vector<Component> _saveAllComp;
	private JLabel _lineColumnPosition;
	
	/**
	 * 
	 * @param window
	 * @param tabbedPane
	 * @param undoComp
	 * @param redoComp
	 * @param saveComp
	 * @param saveAllComp
	 */
	public ActionListenerNewFile(Window window, JTabbedPaneCode tabbedPane, Vector<Component> undoComp, Vector<Component> redoComp,
			Vector<Component> saveComp, Vector<Component> saveAllComp,
			JLabel lineColumnPosition) {
		_tabbedPane = tabbedPane;
		_window = window;
		_undoComp = undoComp;
		_redoComp = redoComp;
		_saveComp = saveComp;
		_saveAllComp = saveAllComp;
		_lineColumnPosition = lineColumnPosition;
	}

	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int idx;
		String filename = SunsetBundle.getInstance().getProperty(IFFaplLang.NEW_FILE);
		if(filename == null){
			filename = "New";
		}
		JPanelCode panel = new JPanelCode(_window, _tabbedPane, _undoComp, _redoComp, _saveComp, _saveAllComp, _lineColumnPosition);
		String title = filename + IProperties.FILEEXTENTION;
		_tabbedPane.addTab(title, panel);
		idx = _tabbedPane.getTabCount() - 1;
		_tabbedPane.setTabComponentAt(idx, new JPanelTabTitle(_tabbedPane, _tabbedPane.getTitleAt(idx)));
		_tabbedPane.getTabComponentAt(idx).setFocusable(false);
		
		panel.setTabTitle((JPanelTabTitle) _tabbedPane.getTabComponentAt(idx));
		_tabbedPane.setSelectedIndex(idx);
		_tabbedPane.currentCodePane().getCaret().setDot(0);
		_tabbedPane.currentCodePane().requestFocusInWindow();

	}

	

}
