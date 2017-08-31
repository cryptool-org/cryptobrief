/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sunset.gui.listener;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;
import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.interfaces.IFFaplLang;
import sunset.gui.panel.JPanelCode;
import sunset.gui.tabbedpane.JTabbedPaneCode;

/**
 *
 * @author vbugl
 */
public class ActionListenerZoom implements ActionListener{
    
    private JTabbedPaneCode _tabbedPane_code;
    private Vector<Component> _zoomInComp;
    private Vector<Component> _zoomOutComp;
    
    public ActionListenerZoom(JTabbedPaneCode tabbedPane_code, Vector<Component> zoomInComp, Vector<Component> zoomOutComp) {
        _tabbedPane_code = tabbedPane_code;
        _zoomInComp = zoomInComp;
        _zoomOutComp = zoomOutComp;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Component button;
        float size;
        Font newFont;
        
        button = (Component) e.getSource();
        size = JPanelCode.codeFont.getSize();
        
        if(button.getName().equals(IFFaplLang.BUTTON_ZOOMIN)){
            reableButtons(size += 2f);           
        }else if(button.getName().equals(IFFaplLang.BUTTON_ZOOMOUT)){
            reableButtons(size -= 2f);
        }
        
        newFont = JPanelCode.codeFont.deriveFont((float)size);
        
        _tabbedPane_code.setFont(newFont);
        _tabbedPane_code.setTabbedPaneCodeFont(newFont);
        
    }
    
    private void setEnabled(Vector<Component> comp, boolean val){
		for(Iterator<Component> itr = comp.iterator(); itr.hasNext(); ){
			itr.next().setEnabled(val);
		}
    }
    
    private void reableButtons(float size){
        if(size <= 12){
            setEnabled(_zoomOutComp, false);
        }else if(size >= 40){
            setEnabled(_zoomInComp, false);
        }else{
            setEnabled(_zoomOutComp, true);
            setEnabled(_zoomInComp, true);
        }
    }
    
}
