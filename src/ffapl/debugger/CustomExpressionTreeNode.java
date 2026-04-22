package ffapl.debugger;

import sunset.gui.debugger.IFFaplDebugger;

import javax.swing.tree.DefaultMutableTreeNode;

public class CustomExpressionTreeNode extends DefaultMutableTreeNode {
    private final CustomExpression expression;
    private final IFFaplDebugger debugger;

    public CustomExpressionTreeNode(CustomExpression expression, IFFaplDebugger debugger) {
        this.expression = expression;
        this.debugger = debugger;
    }

    public void changeExpression(String expr) {
        this.expression.setExpression(expr);
    }

    public CustomExpression getExpression() {
        return expression;
    }

    public String getValue() {
        if (!expression.getExpression().isBlank()) {
            return expression.getExpression() + "= " + debugger.evaluateExpression(expression.getExpression());
        }
        return "Enter New Custom Expression";
    }

    @Override
    public String toString() {
        return getValue();
    }
}
