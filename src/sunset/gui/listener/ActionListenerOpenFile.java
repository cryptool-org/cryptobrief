/**
 * 
 */
package sunset.gui.listener;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import sunset.gui.interfaces.IProperties;
import sunset.gui.lib.FFaplFileFilter;
import sunset.gui.logic.GUIPropertiesLogic;
import sunset.gui.panel.JPanelCode;
import sunset.gui.panel.JPanelTabTitle;
import sunset.gui.tabbedpane.JTabbedPaneCode;




/**
 * Opens a Dialog
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerOpenFile implements ActionListener {

	private JFrame _frame;
	private JTabbedPaneCode _tabbedPane;
	private Vector<Component> _undoComp;
	private Vector<Component> _redoComp;
	private Vector<Component> _saveComp;
	private Vector<Component> _saveAllComp;
	private JLabel _lineColumnPosition;

	/**
	 * 
	 * @param frame
	 * @param tabbedPane
	 * @param undoComp
	 * @param redoComp
	 * @param saveComp
	 * @param saveAllComp
	 */
	public ActionListenerOpenFile(JFrame frame, JTabbedPaneCode tabbedPane, Vector<Component> undoComp, Vector<Component> redoComp,
			Vector<Component> saveComp, Vector<Component> saveAllComp, 
			JLabel lineColumnPosition) {
		_tabbedPane = tabbedPane;
		_undoComp = undoComp;
		_redoComp = redoComp;
		_saveComp = saveComp;
		_saveAllComp = saveAllComp;
		_frame = frame;
		_lineColumnPosition = lineColumnPosition;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		int returnVal;
		int idx;
		String title, dir;
		JPanelCode panel;
		File file;
		JFileChooser fileChooser = new JFileChooser();
		boolean loaded;
			
		//set Filter
		fileChooser.addChoosableFileFilter(new FFaplFileFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);
		//set current directory
		dir = GUIPropertiesLogic.getInstance().getProperty(IProperties.LASTDIROPEN);
		if(dir != null){
			fileChooser.setCurrentDirectory(new File(dir));
		}
		
		returnVal = fileChooser.showOpenDialog(_frame);
		if (returnVal == JFileChooser.APPROVE_OPTION){
			file = fileChooser.getSelectedFile();
				GUIPropertiesLogic.getInstance().setProperty(IProperties.LASTDIROPEN, file.getAbsolutePath());
				panel = new JPanelCode(_frame, _tabbedPane, _undoComp, _redoComp, _saveComp, _saveAllComp, _lineColumnPosition);
				loaded = panel.loadFile(file);
				if(loaded){
					title = file.getName();
					
					_tabbedPane.addTab(title, panel);
					idx = _tabbedPane.getTabCount() - 1;
					_tabbedPane.setTabComponentAt(idx, new JPanelTabTitle(_tabbedPane, _tabbedPane.getTitleAt(idx)));
					panel.setTabTitle((JPanelTabTitle) _tabbedPane.getTabComponentAt(idx));
					_tabbedPane.setSelectedIndex(idx);
					

				}
			}
		}		
	

}
