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
public class ActionListenerCloseTab implements ActionListener {

	JTabbedPaneCode _pane;
	
	/**
	 * 
	 * @param _pane
	 */
	public ActionListenerCloseTab(JTabbedPaneCode pane) {
		_pane = pane;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int i = _pane.getSelectedIndex();
		boolean isClosed;
                
                if (i != -1) {
                        isClosed = ((JPanelCode)_pane.getComponentAt(i)).close();
                        if(isClosed){
                                        _pane.remove(i);
                        }
                }
	}

}
