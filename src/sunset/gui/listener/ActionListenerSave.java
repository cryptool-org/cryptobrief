/**
 * 
 */
package sunset.gui.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractAction;

import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.panel.JPanelCode;
import sunset.gui.tabbedpane.JTabbedPaneCode;


/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class ActionListenerSave extends AbstractAction implements ActionListener {

	private JTabbedPaneCode _tabbedPane;
	private Vector<Component> _saveComp;
	private Vector<Component> _saveAllComp;

	/**
	 * 
	 * @param tabbedPane
	 * @param saveComp
	 * @param saveAllComp
	 */
	public ActionListenerSave(JTabbedPaneCode tabbedPane, Vector<Component> saveComp, Vector<Component> saveAllComp) {
		_tabbedPane = tabbedPane;
		_saveComp = saveComp;
		_saveAllComp = saveAllComp;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Component comp;
		JPanelCode codePane = ((JPanelCode)_tabbedPane.currentCodePanel());
		FFaplCodeTextPane codeTextPane = (FFaplCodeTextPane) codePane.getCodePane();
		codePane.save();
		
		setEnabled(_saveComp, !codeTextPane.isSaved());
		setEnabled(_saveAllComp, false);
		for (int i = 0; i < _tabbedPane.getTabCount(); i++){
			comp = _tabbedPane.getComponentAt(i);
			if(comp instanceof JPanelCode){
				//System.out.println(((FFaplCodeTextPane)((JPanelCode)comp).getCodePane()).isSaved());
				if(!((FFaplCodeTextPane)((JPanelCode)comp).getCodePane()).isSaved()){
					setEnabled(_saveAllComp, true);
					break;
				}
			}
		}
	}
	
	/**
	 * Enables components according val
	 * @param comp
	 * @param val
	 */
	private void setEnabled(Vector<Component> comp, boolean val){
		for(Iterator<Component> itr = comp.iterator(); itr.hasNext(); ){
			itr.next().setEnabled(val);
		}
	}

}
