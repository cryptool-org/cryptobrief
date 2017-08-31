/**
 * 
 */
package sunset.gui.tabbedpane;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sunset.gui.editor.FFaplCodeTextPane;

import sunset.gui.panel.JPanelCode;
import sunset.gui.panel.JPanelTabTitle;



/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class JTabbedPaneCode extends JTabbedPane 
implements ChangeListener{

	
	
	/**
	 * 
	 */
	public JTabbedPaneCode() {
		this.addChangeListener(this);
	}

	/**
	 * Returns the current Code PaneText
	 * @return
	 */
	public JTextPane currentCodePane(){
		JPanel panel;
		JTextPane textPane = null;
		panel = currentCodePanel();
		if(panel != null){
			if (panel instanceof JPanelCode){
				textPane = ((JPanelCode)panel).getCodePane();
			}			
		}		
		return textPane;
	}
	
	/**
	 * Return the current Code Panel
	 * @return
	 */
	public JPanel currentCodePanel(){
		Component comp;
		JPanel panel = null;
		comp = this.getSelectedComponent();
		if(comp != null){
			if (comp instanceof JPanelCode){
				panel = ((JPanelCode)comp);
			}			
		}		
		return panel;
	}
        /**
         * Sets the font for all current TabbedPane code panels
         * @param newFont 
         */
        public void setTabbedPaneCodeFont(Font newFont){
            int count = this.getTabCount();
            Component comp;
            
            for(int i = 0; i < count; i++){
		comp = this.getComponentAt(i);
		if(comp != null){
			if (comp instanceof JPanelCode){
				((JPanelCode)comp).setCodeFont(newFont);
			}			
		}		
            }
        }

	@Override
	public void stateChanged(ChangeEvent arg0) {
		int idx = this.getSelectedIndex();
		int count = this.getTabCount();
		Component comp;
		for(int i = 0; i < count; i++){
			comp = this.getTabComponentAt(i);
			if (comp instanceof JPanelTabTitle){
				if(i == idx){
					((JPanelTabTitle)comp).setButtonVisible(true);
				}else{
					((JPanelTabTitle)comp).setButtonVisible(false);
				}
			}
		}
		
	}
	
	
	
	/**
	 * reorder the tab from dragIndex to targetIndex
	 * @param dragIndex
	 * @param targetIndex
	 
	public void reorderTab(int dragIndex, int targetIndex){
		
		Component cmp;
		Component tab;
		String str;
		Icon icon;
		String tip;
		
		if(targetIndex < 0){
	    	return;
	    }

	    cmp = getComponentAt(dragIndex);
	    tab = getTabComponentAt(dragIndex);
	    str = getTitleAt(dragIndex);
	    icon = getIconAt(dragIndex);
	    tip = getToolTipTextAt(dragIndex);
	    remove(dragIndex);
	    
	    insertTab(str, icon, cmp, tip, targetIndex);
	    setTabComponentAt(targetIndex, tab);
	    setSelectedIndex(targetIndex);
	    
	}
	*/
	
	
	
}
