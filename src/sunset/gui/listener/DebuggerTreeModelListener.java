package sunset.gui.listener;

import ffapl.debugger.CustomExpressionTreeNode;
import ffapl.debugger.SymbolTreeNode;
import sunset.gui.debugger.IFFaplDebugger;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class DebuggerTreeModelListener implements TreeModelListener {
    private final IFFaplDebugger debugger;

    public DebuggerTreeModelListener(IFFaplDebugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public void treeNodesChanged(TreeModelEvent e) {
        Object[] children = e.getChildren();
        if (children != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) children[0];
            if (node instanceof SymbolTreeNode){
                SymbolTreeNode symbol_node = (SymbolTreeNode) node;
                var expression = symbol_node.getUserObject();
                if (expression instanceof String){
                    String string_expression = (String) expression;
                    if (!string_expression.isBlank()){
                        debugger.evaluateAssignment(string_expression, symbol_node.getSymbol());
                    }
                }
            } else if (node instanceof CustomExpressionTreeNode){
                CustomExpressionTreeNode custom_node = (CustomExpressionTreeNode) node;
                custom_node.changeExpression(((String) custom_node.getUserObject()));
            }
        }
    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {

    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {

    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {

    }
}
