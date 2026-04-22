package sunset.gui.debugger;

import ffapl.debugger.StackFrame;

import javax.swing.*;
import java.awt.*;

public class StackFrameCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof StackFrame) {
            StackFrame stackFrame = ((StackFrame) value);
            setText("Line " + stackFrame.getLine() + ":" + stackFrame.getColumn() + " " + stackFrame.getName());
        }
        return this;
    }


}
