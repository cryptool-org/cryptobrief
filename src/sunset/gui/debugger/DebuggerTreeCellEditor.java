package sunset.gui.debugger;

import ffapl.debugger.CustomExpressionTreeNode;
import ffapl.debugger.ScopeTreeNode;
import ffapl.debugger.SymbolTreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.Arrays;
import java.util.EventObject;

public class DebuggerTreeCellEditor extends DefaultTreeCellEditor {
    public DebuggerTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
        super(tree, renderer);
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {

        Component component = super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);

        if (realEditor instanceof DefaultCellEditor) {
            Component editorComponent = ((DefaultCellEditor) realEditor).getComponent();

            if (editorComponent instanceof JTextField) {
                JTextField textField = (JTextField) editorComponent;

                String text = textField.getText();
                textField.setText("");

                if (text != null && !text.isBlank()) {
                    // first real use of functional programming
                    String displayText = Arrays.stream(text.split("="))
                            .skip(1)
                            .reduce("", (x, y) -> x + y);
                    textField.setText(displayText.strip());
                }

            }
        }

        return component;
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        Object node = tree.getLastSelectedPathComponent();
        if (node instanceof SymbolTreeNode || node instanceof CustomExpressionTreeNode) {
            return super.isCellEditable(event);
        }
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        // when clicking off of the tree while editing, editing will be canceled
        cancelCellEditing();
        return false;
    }
}
