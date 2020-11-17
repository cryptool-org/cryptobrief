/**
 * 
 */
package sunset.gui.listener;

import java.awt.Component;
import java.awt.Container;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sunset.gui.FFaplJFrame;

import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.interfaces.IProperties;
import sunset.gui.panel.JPanelCode;
import sunset.gui.tabbedpane.JTabbedPaneCode;


/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class ChangeListenerSelectedTab implements ChangeListener {

	private Vector<Component> _undoComp;
	private Vector<Component> _redoComp;
	private Vector<Component> _saveComp;
	private Vector<Component> _saveAllComp;
	private Vector<Component> _closeTabComp;
	private Vector<Component> _closeAllTabComp;
	private Container _consoleContainer;
        private JTextField _inputTextField;
	private JFrame _owner;
	private JPanel _lineNumber;
	
	/**
	 * 
	 * @param undoComp
	 * @param redoComp
	 * @param saveComp
	 * @param saveAllComp
	 */
	public ChangeListenerSelectedTab(JFrame owner, Container consoleContainer, JTextField inputTextField, Vector<Component> undoComp, Vector<Component> redoComp, 
									 Vector<Component> saveComp, Vector<Component> saveAllComp,
									 Vector<Component> closeTabComp, Vector<Component> closeAllTabComp,
									 JPanel lineNumber) {
		_undoComp = undoComp;
		_redoComp = redoComp;
		_saveComp = saveComp;
		_saveAllComp = saveAllComp;
		_consoleContainer = consoleContainer;
                _inputTextField = inputTextField;
		_owner = owner;
		_closeTabComp = closeTabComp;
		_closeAllTabComp = closeAllTabComp;
		_lineNumber = lineNumber;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {
		FFaplCodeTextPane codeTextPane;
		JPanelCode codePanel;
		JTabbedPaneCode tabbedPane = (JTabbedPaneCode)arg0.getSource();
		codeTextPane = (FFaplCodeTextPane)tabbedPane.currentCodePane();
		codePanel = (JPanelCode)tabbedPane.currentCodePanel();
		Component comp;
		if(codeTextPane != null){
			_owner.setTitle(MessageFormat.format(IProperties.APPTITLE,"- " + codePanel.getTitle() + " -"));
			codeTextPane.getInputMap().put(KeyStroke.getKeyStroke("ctrl H"), "none");
			setEnabled(_undoComp, codeTextPane.getManager().canUndo());
			setEnabled(_redoComp, codeTextPane.getManager().canRedo());
			setEnabled(_saveComp, !codeTextPane.isSaved());
			setEnabled(_saveAllComp, false);
			setEnabled(_closeAllTabComp, true);
			setEnabled(_closeTabComp, true);
			_lineNumber.setVisible(true);
			for (int i = 0; i < tabbedPane.getTabCount(); i++){
				comp = tabbedPane.getComponentAt(i);
				if(comp instanceof JPanelCode){
					//System.out.println(((FFaplCodeTextPane)((JPanelCode)comp).getCodePane()).isSaved());
					if(!((FFaplCodeTextPane)((JPanelCode)comp).getCodePane()).isSaved()){
						setEnabled(_saveAllComp, true);
						break;
					}
				}
			}
			
			if(_consoleContainer instanceof JScrollPane){
				((JScrollPane) _consoleContainer).setViewportView(((JPanelCode)tabbedPane.currentCodePanel()).getConsole());
			}else{
				Component[] comps = _consoleContainer.getComponents();
				for(int i = 0; i < comps.length; i++) {
					if (comps[i].getName().equals(JPanelCode.STR_CONSOLE)){
						_consoleContainer.remove(i);
						break;
					}
				}
				_consoleContainer.add(((JPanelCode)tabbedPane.currentCodePanel()).getConsole());
			}
                        
                        _inputTextField = codePanel.getInputField();
                        if(ActionListenerInputField.getMap(tabbedPane.getSelectedIndex()) == null){
                                FFaplJFrame.getInputField().setEnabled(false);
                        }else{
                                FFaplJFrame.getInputField().setEnabled(true);
                        }
                        _inputTextField.repaint();
                        
			codeTextPane.requestFocusInWindow();
			codePanel.setLineColumn();
			//codeTextPane.setCaretPosition(codeTextPane.getCaretPosition());
			//codeTextPane.getCaret().setDot(codeTextPane.getCaret().getDot() + 1);
		}else{
			_owner.setTitle(MessageFormat.format(IProperties.APPTITLE,"-"));
			setEnabled(_closeAllTabComp, false);
			setEnabled(_closeTabComp, false);
			setEnabled(_undoComp, false);
			setEnabled(_redoComp, false);
			setEnabled(_saveComp, false);
			setEnabled(_saveAllComp, false);
			_lineNumber.setVisible(false);
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
