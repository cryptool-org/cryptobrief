package sunset.gui.debugger;

import ffapl.debugger.StackFrame;
import sunset.gui.FFaplJFrame;
import sunset.gui.editor.FFaplCodeTextPane;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CallStackMouseListener implements MouseListener {

    private final JList<StackFrame> _owner;

    public CallStackMouseListener(JList<StackFrame> _owner) {
        this._owner = _owner;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2){
            StackFrame selectedValue = _owner.getSelectedValue();
            ((FFaplCodeTextPane) FFaplJFrame.getCurrentCodePanel().getCodePane()).setCaretPosition(selectedValue.getLine(), selectedValue.getColumn());
            if (selectedValue.getScopeSymbol() != null){
                // DebuggerUtil.setTreeScope(selectedValue.getScopeSymbol());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
