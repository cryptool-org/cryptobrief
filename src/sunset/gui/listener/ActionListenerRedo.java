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
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.lib.FFaplUndoManager;
import sunset.gui.panel.JPanelCode;
import sunset.gui.panel.JPanelTabTitle;
import sunset.gui.tabbedpane.JTabbedPaneCode;


/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class ActionListenerRedo extends AbstractAction implements ActionListener {

	private JTabbedPaneCode _tabbedPane;
	private Vector<Component> _undoComp;
	private Vector<Component> _redoComp;
	private Vector<Component> _saveComp;
	private Vector<Component> _saveAllComp;
	

	/**
	 * 
	 * @param tabbedPane
	 * @param undoComp
	 * @param redoComp
	 * @param saveComp
	 * @param saveAllComp
	 */
	public ActionListenerRedo(JTabbedPaneCode tabbedPane, 
			                  Vector<Component> undoComp, Vector<Component> redoComp,
			                  Vector<Component> saveComp, Vector<Component> saveAllComp) {
		_tabbedPane = tabbedPane;
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
		Component comp;
		JPanelCode panel = ((JPanelCode)_tabbedPane.currentCodePanel());
		FFaplCodeTextPane textPane = (FFaplCodeTextPane) panel.getCodePane();
		int idx;
		
		UndoManager manager =  ((FFaplCodeTextPane)_tabbedPane.currentCodePane()).getManager();
		if(manager.canRedo()){
			try {
		        manager.redo(); 
		        if(((FFaplUndoManager)manager).isSaved()){
		        	textPane.inputSaved(true);
		        	idx = _tabbedPane.indexOfComponent(panel);
		    		if(idx > -1){
		    			comp = _tabbedPane.getTabComponentAt(idx);
		    			if(comp instanceof JPanelTabTitle){
		    				((JPanelTabTitle) comp).inputSaved(true);
		    			}
		    		}
		        }
		      } catch (CannotRedoException d) {
		        Toolkit.getDefaultToolkit().beep();
		      }
		}
	      
	      setEnabled(_undoComp, manager.canUndo());
	      setEnabled(_redoComp, manager.canRedo());
	      
	      setEnabled(_saveComp, !textPane.isSaved());
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
