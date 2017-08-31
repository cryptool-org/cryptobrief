/**
 * 
 */
package sunset.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sunset.gui.panel.JPanelCode;
import sunset.gui.tabbedpane.JTabbedPaneCode;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ActionListenerCloseAllTab implements ActionListener {

	JTabbedPaneCode _pane;
	
	/**
	 * 
	 * @param _pane
	 */
	public ActionListenerCloseAllTab(JTabbedPaneCode pane) {
		_pane = pane;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		boolean isClosed = true;
		
		while(isClosed && _pane.getTabCount() > 0){
					isClosed = ((JPanelCode)_pane.getComponentAt(0)).close();
					if(isClosed){
						_pane.remove(0);
					}
	     }

	}

}
