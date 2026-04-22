package sunset.gui.listener;

import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.interfaces.IFFaplLang;
import sunset.gui.tabbedpane.JTabbedPaneCode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ActionListenerBreakpoint implements ActionListener {

    private JTabbedPaneCode _tabbedPane_code;
    private Vector<Component> _addBreakPointComp;
    private Vector<Component> _removeBreakPointComp;

    public ActionListenerBreakpoint(JTabbedPaneCode _tabbedPane_code, Vector<Component> _addBreakPointComp, Vector<Component> _removeBreakPointComp) {
        this._tabbedPane_code = _tabbedPane_code;
        this._addBreakPointComp = _addBreakPointComp;
        this._removeBreakPointComp = _removeBreakPointComp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component button = (Component) e.getSource();
        FFaplCodeTextPane textPane = ((FFaplCodeTextPane) _tabbedPane_code.currentCodePane());
        int line = getLineNumberOfCaret(textPane);

        if (button.getName().equals(IFFaplLang.BUTTON_ADD_BREAKPOINT)) {
            //DebuggerUtil.setBreakpoint(line + 1, textPane);
            textPane.getDebugger().setBreakpoint(line + 1);
        } else if (button.getName().equals(IFFaplLang.BUTTON_REMOVE_BREAKPOINT)) {
            //DebuggerUtil.removeBreakpoint(line + 1, textPane);
            textPane.getDebugger().removeBreakpoint(line + 1);
        }


    }

    private static int getLineNumberOfCaret(FFaplCodeTextPane textPane) {
        return textPane.getDocument().getDefaultRootElement().getElementIndex(textPane.getCaretPosition());
    }
}
