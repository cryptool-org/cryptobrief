package sunset.gui.debugger;

import ffapl.debugger.*;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class DebuggerTreeCellRenderer extends DefaultTreeCellRenderer {
    private final static Icon SYMBOL_ICON = new DebugIcon(new Color(60, 120, 216), "v");
    private final static Icon SCOPE_ICON = new DebugIcon(new Color(100, 100, 100), "S");
    private final static Icon CUSTOM_EXPRESSION_ICON = new DebugIcon(new Color(220, 130, 40), "w");


    @Override
    public Component getTreeCellRendererComponent(
            JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        // following line is to fix layouting issues....
        getPreferredSize().height = 16;

        if (value instanceof SymbolTreeNode) {
            setIcon(SYMBOL_ICON);
        } else if (value instanceof ScopeTreeNode) {
            setIcon(SCOPE_ICON);;
        } else if (value instanceof CustomExpressionTreeNode) {
            setIcon(CUSTOM_EXPRESSION_ICON);
        }
        return this;
    }

}