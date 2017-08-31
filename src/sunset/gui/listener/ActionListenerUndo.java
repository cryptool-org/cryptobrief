/**
 * 
 */
package sunset.gui.listener;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.lib.*;
import sunset.gui.panel.JPanelCode;
import sunset.gui.panel.JPanelTabTitle;
import sunset.gui.tabbedpane.JTabbedPaneCode;


/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class ActionListenerUndo extends AbstractAction implements ActionListener {

	private JTabbedPaneCode _pane;
	private Vector<Component> _undoComp;
	private Vector<Component> _redoComp;
	private Vector<Component> _saveComp;
	private Vector<Component> _saveAllComp;

	/**
	 * 
	 * @param pane
	 * @param undoComp
	 * @param redoComp
	 * @param saveComp
	 * @param saveAllComp
	 */
	public ActionListenerUndo(JTabbedPaneCode pane, 
							  Vector<Component> undoComp, Vector<Component> redoComp,
							  Vector<Component> saveComp, Vector<Component> saveAllComp) {
		_pane = pane;
		_undoComp = undoComp;
		_redoComp = redoComp;
		_saveComp = saveComp;
		_saveAllComp = saveAllComp;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		FFaplCodeTextPane textPane = ((FFaplCodeTextPane)_pane.currentCodePane());
		JPanelCode panel = ((JPanelCode)_pane.currentCodePanel());
		Component comp;
		UndoManager manager =  textPane.getManager();
		int idx;
		if(manager.canUndo()){
			try {
		        manager.undo(); 
		        if(((FFaplUndoManager)manager).isSaved()){
		        	textPane.inputSaved(true);
		        	idx = _pane.indexOfComponent(panel);
		    		if(idx > -1){
		    			comp = _pane.getTabComponentAt(idx);
		    			if(comp instanceof JPanelTabTitle){
		    				((JPanelTabTitle) comp).inputSaved(true);
		    			}
		    		}
		        }
		      } catch (CannotUndoException d) {
		        Toolkit.getDefaultToolkit().beep();
		      }
		}
	      
	      setEnabled(_undoComp, manager.canUndo());
	      setEnabled(_redoComp, manager.canRedo());
	      
	      setEnabled(_saveComp, !textPane.isSaved());
			setEnabled(_saveAllComp, false);
			for (int i = 0; i < _pane.getTabCount(); i++){
				comp = _pane.getComponentAt(i);
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
