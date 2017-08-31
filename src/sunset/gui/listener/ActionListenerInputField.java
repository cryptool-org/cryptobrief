/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sunset.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JTextField;
import sunset.gui.tabbedpane.JTabbedPaneCode;

/**
 * Manages the 
 * @author vbugl
 */
public class ActionListenerInputField implements ActionListener{
    
    private static LinkedList<String> _input = new LinkedList<>();
    private static Hashtable<Integer, Thread> map = new Hashtable<>();
    private static JTabbedPaneCode _tabbedPane_code;
    
    public ActionListenerInputField(JTabbedPaneCode tabbedPane_code){
        _tabbedPane_code = tabbedPane_code;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        synchronized (_input) {
            _input.add(((JTextField)e.getSource()).getText().toString());
            _input.notifyAll();
        }
        ((JTextField)e.getSource()).setText("");
        ((JTextField)e.getSource()).setEnabled(false);
    }
    public static LinkedList<String> getInput(){
        return _input;
    }
    
    public static void addMap(int i, Thread t){
        map.put(i, t);
    }
    
    public static Thread getMap(int i){
        return map.get(i);
    }
    
    public static Thread deleteMap(int i){
        return map.remove(i);
    }
    
    public static JTabbedPaneCode getTabbedPane(){
        return _tabbedPane_code;
    }
}
