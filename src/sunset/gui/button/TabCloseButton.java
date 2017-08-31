/**
 * 
 */
package sunset.gui.button;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import sunset.gui.panel.JPanelCode;
import sunset.gui.util.SunsetBundle;

/**
 * Represents the TAb Close Button
 * @author Alexander Ortner
 * @version 1.0
 */
@SuppressWarnings("serial")
public class TabCloseButton extends JButton implements ActionListener{

	private JTabbedPane _pane;
	private Component _owner;
	/**
	 * 
	 */
	public TabCloseButton(JTabbedPane pane, Component owner) {
		_pane = pane;
		_owner = owner;
		initButton();
	}
	
	/**
	 * Inits the button
	 */
	private void initButton(){
		int size = 13;
		String tooltip;
        setPreferredSize(new Dimension(size, size));
        
        tooltip = SunsetBundle.getInstance().getProperty("button_closetab");
        if(tooltip!=null){
        	setToolTipText(tooltip);
        }else{
        	setToolTipText("close this tab");
        }
        
        //Make the button looks the same for all Laf's
        setUI(new BasicButtonUI());
        //Make it transparent
        setContentAreaFilled(false);
        //No need to be focusable
        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder());
        setBorderPainted(false);
        addActionListener(this);
        setRolloverEnabled(true);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		int i = _pane.indexOfTabComponent(_owner);
		boolean isClosed;
		if (i != -1) {
			isClosed = ((JPanelCode)_pane.getComponentAt(i)).close();
            if(isClosed){
            	_pane.remove(i);
            }
        }
	}

}
