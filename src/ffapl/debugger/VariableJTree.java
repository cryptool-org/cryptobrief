package ffapl.debugger;

import sunset.gui.debugger.IFFaplDebugger;

import javax.swing.*;
import javax.swing.tree.TreeNode;

public class VariableJTree extends JTree {
    private IFFaplDebugger _debugger;

    public VariableJTree(IFFaplDebugger _debugger) {
        this._debugger = _debugger;
    }

    public VariableJTree(TreeNode root, IFFaplDebugger _debugger) {
        super(root);
        this._debugger = _debugger;
    }

    public IFFaplDebugger get_debugger() {
        return _debugger;
    }

    public void set_debugger(IFFaplDebugger _debugger) {
        this._debugger = _debugger;
    }
}
