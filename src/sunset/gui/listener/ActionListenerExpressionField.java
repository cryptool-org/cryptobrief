package sunset.gui.listener;

import sunset.gui.FFaplJFrame;
import sunset.gui.debugger.IFFaplDebugger;
import sunset.gui.editor.FFaplCodeTextPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerExpressionField implements ActionListener {
    private final JTextField expression_field;

    public ActionListenerExpressionField(JTextField expression_field) {
        this.expression_field = expression_field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IFFaplDebugger debugger = ((FFaplCodeTextPane) FFaplJFrame.getCurrentCodePanel().getCodePane()).getDebugger();
        if (debugger != null){
            boolean success = debugger.evaluateStatement(expression_field.getText(), null);
            if (success) {
                expression_field.setText("");
                FFaplJFrame.getDebuggerPanel().repaintTree();
            }
        }
    }
}
