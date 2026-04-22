package sunset.gui.listener;

import ffapl.debugger.StepKind;
import sunset.gui.debugger.IFFaplDebugger;
import sunset.gui.editor.FFaplCodeTextPane;
import sunset.gui.tabbedpane.JTabbedPaneCode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ActionListenerDebugger implements ActionListener {

    private JTabbedPaneCode _tabbedPane_code;
    private Vector<Component> _stepOverComp;
    private Vector<Component> _stepIntoComp;
    private Vector<Component> _resumeComp;

    public ActionListenerDebugger(JTabbedPaneCode _tabbedPane_code, Vector<Component> _stepOverComp, Vector<Component> _stepIntoComp, Vector<Component> _resumeComp) {
        this._tabbedPane_code = _tabbedPane_code;
        this._stepOverComp = _stepOverComp;
        this._stepIntoComp = _stepIntoComp;
        this._resumeComp = _resumeComp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component source;

        source = (Component) e.getSource();
        IFFaplDebugger debugger = ((FFaplCodeTextPane) _tabbedPane_code.currentCodePane()).getDebugger();
        if (debugger != null) {
            if (_stepOverComp.contains(source)) {
                debugger.setStep(StepKind.STEP_OVER);
            } else if (_stepIntoComp.contains(source)) {
                debugger.setStep(StepKind.STEP_INTO);
            } else if (_resumeComp.contains(source)) {
                debugger.setStep(StepKind.STEP_RESUME);
            }
        }
    }


}
