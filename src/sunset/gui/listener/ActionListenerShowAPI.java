/**
 * 
 */
package sunset.gui.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSplitPane;

import sunset.gui.interfaces.IProperties;
import sunset.gui.logic.GUIPropertiesLogic;




/**
 * Opens a Dialog
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerShowAPI implements ActionListener {

	private Component _jPanel_Api;
	private JSplitPane _splitPane;

	/**
	 * 
	 * @param jPanel_Api
	 */
	public ActionListenerShowAPI(JSplitPane splitPane, Component jPanel_Api) {
		_jPanel_Api = jPanel_Api;
		_splitPane = splitPane;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		JCheckBoxMenuItem item;
		item = (JCheckBoxMenuItem) arg0.getSource();
		_jPanel_Api.setVisible(item.isSelected());
		GUIPropertiesLogic.getInstance().setBooleanProperty(IProperties.SHOW_API, item.isSelected());
		_splitPane.setDividerLocation(_splitPane.getWidth() - 200);
		//_frame.pack();
		//_frame.repaint();
	}

}
