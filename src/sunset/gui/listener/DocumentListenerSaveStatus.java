/**
 * 
 */
package sunset.gui.listener;


import java.awt.Component;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JTabbedPane;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.panel.JPanelCode;
import sunset.gui.panel.JPanelTabTitle;



/**
 * Draw line numbers into a JTextPane
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class DocumentListenerSaveStatus implements DocumentListener {

	private FFaplCodeTextPane _textPane;
	private JPanelCode _panel;
	private JTabbedPane _pane;
	private Vector<Component> _saveComp;
	private Vector<Component> _saveAllComp;
	
	/**
	 * 
	 */
	public DocumentListenerSaveStatus(JPanelCode panel, JTabbedPane pane, Vector<Component> saveComp, Vector<Component> saveAllComp) {
		_panel = panel;
		_pane = pane;
		_textPane = (FFaplCodeTextPane) _panel.getCodePane();
		_saveComp = saveComp;
		_saveAllComp = saveAllComp;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent arg) {
		//System.out.println("state changed");
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent arg) {
		int idx;
		Component comp;
		_textPane.inputSaved(false);
		idx = _pane.indexOfComponent(_panel);
		if(idx > -1){
			comp = _pane.getTabComponentAt(idx);
			if(comp instanceof JPanelTabTitle){
				((JPanelTabTitle) comp).inputSaved(false);
			}
		}
		setEnabled(_saveComp, true);
		setEnabled(_saveAllComp, true);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent arg) {
		int idx;
		Component comp;
		_textPane.inputSaved(false);
		idx = _pane.indexOfComponent(_panel);
		if(idx > -1){
			comp = _pane.getTabComponentAt(idx);
			if(comp instanceof JPanelTabTitle){
				((JPanelTabTitle) comp).inputSaved(false);
			}
		}
		setEnabled(_saveComp, true);
		setEnabled(_saveAllComp, true);
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
